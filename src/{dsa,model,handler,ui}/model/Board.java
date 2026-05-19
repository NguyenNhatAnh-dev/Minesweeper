package model;

import java.io.Serializable;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    private Cell[][] grid;
    private int rows;
    private int cols;
    private int totalMines;

    public Board(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.grid = new Cell[rows][cols];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return grid[row][col];
        }
        return null;
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalMines() {
        return totalMines;
    }

    public Board deepCopy() {
        Board copy = new Board(rows, cols, totalMines);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                copy.grid[r][c] = new Cell(grid[r][c]);
            }
        }
        return copy;
    }
}
