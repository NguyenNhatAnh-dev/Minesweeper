package handler;

import model.Board;
import model.Cell;
import model.GameState;
import java.io.*;


public class SaveHandler {

    private static final String SAVE_FILE = "minesweeper_save.txt";
    private static final String MAGIC = "MSDSA1";

    public static boolean saveGame(GameState state) {
        if (state == null) return false;
        try (BufferedWriter w = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            Board b = state.getBoard();
            long elapsed = GameStateHandler.getElapsedSeconds(state);

            w.write(MAGIC); w.newLine();
            w.write(b.getRows() + " " + b.getCols() + " " + b.getTotalMines()); w.newLine();
            w.write(state.getGameStatus() + " " + state.getFlagsPlaced() + " "
                    + state.getCellsRevealed() + " " + elapsed + " " + (state.isFirstClick() ? 1 : 0));
            w.newLine();

            for (int r = 0; r < b.getRows(); r++) {
                StringBuilder line = new StringBuilder();
                for (int c = 0; c < b.getCols(); c++) {
                    if (c > 0) line.append(' ');
                    line.append(encode(b.getCell(r, c)));
                }
                w.write(line.toString());
                w.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static GameState loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            if (!MAGIC.equals(r.readLine())) return null;

            String[] bi = r.readLine().trim().split("\\s+");
            int rows = Integer.parseInt(bi[0]);
            int cols = Integer.parseInt(bi[1]);
            int mines = Integer.parseInt(bi[2]);

            String[] si = r.readLine().trim().split("\\s+");
            int status = Integer.parseInt(si[0]);
            int flags = Integer.parseInt(si[1]);
            int revealed = Integer.parseInt(si[2]);
            long elapsed = Long.parseLong(si[3]);
            boolean firstClick = Integer.parseInt(si[4]) == 1;

            Board board = new Board(rows, cols, mines);
            for (int i = 0; i < rows; i++) {
                String[] codes = r.readLine().trim().split("\\s+");
                for (int j = 0; j < cols; j++) decode(board.getCell(i, j), Integer.parseInt(codes[j]));
            }

            GameState state = new GameState(board);
            state.setGameStatus(status);
            state.setFlagsPlaced(flags);
            state.setCellsRevealed(revealed);
            state.setElapsedTime(elapsed);
            state.setFirstClick(firstClick);
            state.setStartTime(System.currentTimeMillis() - elapsed * 1000);
            return state;
        } catch (Exception e) {
            return null;
        }
    }

    private static int encode(Cell c) {
        int x = c.getAdjacentMines() << 3;
        if (c.hasMine()) x |= 1;
        if (c.isRevealed()) x |= 2;
        if (c.isFlagged()) x |= 4;
        return x;
    }

    private static void decode(Cell c, int x) {
        c.setMine((x & 1) != 0);
        c.setRevealed((x & 2) != 0);
        c.setFlagged((x & 4) != 0);
        c.setAdjacentMines(x >> 3);
    }

    public static boolean hasSavedGame() {
        return new File(SAVE_FILE).exists();
    }

    public static void deleteSave() {
        new File(SAVE_FILE).delete();
    }
}
