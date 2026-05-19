package handler;

import model.Board;
import model.Cell;
import model.GameState;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SaveHandler {

    private static final String SAVE_FILE = "minesweeper_save.txt";

    public static boolean saveGame(GameState gameState) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE));
            Board board = gameState.getBoard();

            writer.write(board.getRows() + "," + board.getCols() + "," + board.getTotalMines());
            writer.newLine();
            writer.write(gameState.getGameStatus() + "," + gameState.getFlagsPlaced()
                    + "," + gameState.getCellsRevealed());
            writer.newLine();
            writer.write(gameState.getElapsedTime() + "," + (gameState.isFirstClick() ? 1 : 0));
            writer.newLine();

            for (int r = 0; r < board.getRows(); r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < board.getCols(); c++) {
                    if (c > 0) {
                        sb.append(";");
                    }
                    Cell cell = board.getCell(r, c);
                    sb.append(cell.hasMine() ? 1 : 0).append(",");
                    sb.append(cell.isRevealed() ? 1 : 0).append(",");
                    sb.append(cell.isFlagged() ? 1 : 0).append(",");
                    sb.append(cell.getAdjacentMines());
                }
                writer.write(sb.toString());
                writer.newLine();
            }

            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static GameState loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return null;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String[] boardInfo = reader.readLine().split(",");
            int rows = Integer.parseInt(boardInfo[0]);
            int cols = Integer.parseInt(boardInfo[1]);
            int mines = Integer.parseInt(boardInfo[2]);

            String[] stateInfo = reader.readLine().split(",");
            int gameStatus = Integer.parseInt(stateInfo[0]);
            int flagsPlaced = Integer.parseInt(stateInfo[1]);
            int cellsRevealed = Integer.parseInt(stateInfo[2]);

            String[] timeInfo = reader.readLine().split(",");
            long elapsedTime = Long.parseLong(timeInfo[0]);
            boolean firstClick = Integer.parseInt(timeInfo[1]) == 1;

            Board board = new Board(rows, cols, mines);
            GameState gameState = new GameState(board);
            gameState.setGameStatus(gameStatus);
            gameState.setFlagsPlaced(flagsPlaced);
            gameState.setCellsRevealed(cellsRevealed);
            gameState.setElapsedTime(elapsedTime);
            gameState.setFirstClick(firstClick);
            gameState.setStartTime(System.currentTimeMillis() - (elapsedTime * 1000));

            for (int r = 0; r < rows; r++) {
                String line = reader.readLine();
                String[] cells = line.split(";");
                for (int c = 0; c < cols; c++) {
                    String[] cellData = cells[c].split(",");
                    Cell cell = board.getCell(r, c);
                    cell.setMine(Integer.parseInt(cellData[0]) == 1);
                    cell.setRevealed(Integer.parseInt(cellData[1]) == 1);
                    cell.setFlagged(Integer.parseInt(cellData[2]) == 1);
                    cell.setAdjacentMines(Integer.parseInt(cellData[3]));
                }
            }

            reader.close();
            return gameState;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasSavedGame() {
        return new File(SAVE_FILE).exists();
    }

    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
