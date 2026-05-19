package handler;

import model.Board;
import model.Cell;
import dsa.HashTable;
import java.util.Random;

public class BoardHandler {

    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        {0, -1},           {0, 1},
        {1, -1},  {1, 0},  {1, 1}
    };

    public static Board createBoard(int rows, int cols, int mines) {
        Board board = new Board(rows, cols, mines);
        return board;
    }

    public static void placeMines(Board board, int safeRow, int safeCol) {
        Random random = new Random();
        int rows = board.getRows();
        int cols = board.getCols();
        int minesPlaced = 0;
        int totalMines = board.getTotalMines();

        HashTable<String, Boolean> safeZone = new HashTable<>(20);
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = safeRow + dr;
                int nc = safeCol + dc;
                if (board.isValidPosition(nr, nc)) {
                    safeZone.put(nr + "," + nc, true);
                }
            }
        }

        while (minesPlaced < totalMines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            String key = r + "," + c;

            if (safeZone.containsKey(key)) {
                continue;
            }

            Cell cell = board.getCell(r, c);
            if (!cell.hasMine()) {
                cell.setMine(true);
                minesPlaced++;
            }
        }

        calculateAdjacentMines(board);
    }

    public static void calculateAdjacentMines(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board.getCell(r, c).hasMine()) {
                    continue;
                }
                int count = 0;
                for (int[] dir : DIRECTIONS) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (board.isValidPosition(nr, nc) && board.getCell(nr, nc).hasMine()) {
                        count++;
                    }
                }
                board.getCell(r, c).setAdjacentMines(count);
            }
        }
    }

    public static int[][] getDirections() {
        return DIRECTIONS;
    }

    public static int countFlags(Board board) {
        int count = 0;
        int rows = board.getRows();
        int cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board.getCell(r, c).isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int countRevealed(Board board) {
        int count = 0;
        int rows = board.getRows();
        int cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board.getCell(r, c).isRevealed()) {
                    count++;
                }
            }
        }
        return count;
    }
}
