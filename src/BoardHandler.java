package handler;

import model.Board;
import model.Cell;
import java.util.Random;


public class BoardHandler {

    private static final int[][] DIRECTIONS = {
        {-1, -1}, {-1, 0}, {-1, 1},
        { 0, -1},          { 0, 1},
        { 1, -1}, { 1, 0}, { 1, 1}
    };

    public static Board createBoard(int rows, int cols, int mines) {
        rows = Math.max(5, rows);
        cols = Math.max(5, cols);
        mines = Math.max(1, Math.min(mines, rows * cols - 9));
        return new Board(rows, cols, mines);
    }

    public static void placeMines(Board board, int safeRow, int safeCol) {
        int rows = board.getRows(), cols = board.getCols();
        int[] pos = new int[rows * cols];
        int n = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (Math.abs(r - safeRow) <= 1 && Math.abs(c - safeCol) <= 1) continue;
                pos[n++] = r * cols + c;
            }
        }

        Random rd = new Random();
        int mines = Math.min(board.getTotalMines(), n);
        for (int i = 0; i < mines; i++) {
            int j = i + rd.nextInt(n - i);       // Fisher-Yates shuffle
            int tmp = pos[i]; pos[i] = pos[j]; pos[j] = tmp;
            board.getCell(pos[i] / cols, pos[i] % cols).setMine(true);
        }

        calculateAdjacentMines(board);
    }

    public static void calculateAdjacentMines(Board board) {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Cell cell = board.getCell(r, c);
                if (cell.hasMine()) continue;

                int cnt = 0;
                for (int[] d : DIRECTIONS) {
                    int nr = r + d[0], nc = c + d[1];
                    if (board.isValidPosition(nr, nc) && board.getCell(nr, nc).hasMine()) cnt++;
                }
                cell.setAdjacentMines(cnt);
            }
        }
    }

    public static int[][] getDirections() {
        return DIRECTIONS;
    }

    public static int countFlags(Board board) {
        int cnt = 0;
        for (int r = 0; r < board.getRows(); r++)
            for (int c = 0; c < board.getCols(); c++)
                if (board.getCell(r, c).isFlagged()) cnt++;
        return cnt;
    }

    public static int countRevealed(Board board) {
        int cnt = 0;
        for (int r = 0; r < board.getRows(); r++)
            for (int c = 0; c < board.getCols(); c++)
                if (board.getCell(r, c).isRevealed()) cnt++;
        return cnt;
    }
}
