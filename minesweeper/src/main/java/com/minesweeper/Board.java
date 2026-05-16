package com.minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;

    public Board(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Board size must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        if (!isInBounds(row, col)) {
            throw new IndexOutOfBoundsException("Cell out of range");
        }
        return cells[row][col];
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public List<Cell> getNeighbors(int row, int col) {
        List<Cell> neighbors = new ArrayList<>(8);
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                if (deltaRow == 0 && deltaCol == 0) {
                    continue;
                }
                int neighborRow = row + deltaRow;
                int neighborCol = col + deltaCol;
                if (isInBounds(neighborRow, neighborCol)) {
                    neighbors.add(cells[neighborRow][neighborCol]);
                }
            }
        }
        return neighbors;
    }

    public int countBombs() {
        int bombs = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isBomb()) {
                    bombs++;
                }
            }
        }
        return bombs;
    }

    public int countSafeCells() {
        return rows * cols - countBombs();
    }

    public int countRevealedSafeCells() {
        int revealedSafeCells = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.isBomb() && cell.isRevealed()) {
                    revealedSafeCells++;
                }
            }
        }
        return revealedSafeCells;
    }

    public boolean allBombsFlagged() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isBomb() != cell.isFlagged()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void calculateAdjacentBombs() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isBomb()) {
                    cell.setAdjacentBombs(-1);
                    continue;
                }
                int count = 0;
                for (Cell neighbor : getNeighbors(cell.getRow(), cell.getCol())) {
                    if (neighbor.isBomb()) {
                        count++;
                    }
                }
                cell.setAdjacentBombs(count);
            }
        }
    }

    public void revealAllBombs() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.isBomb()) {
                    cell.setRevealed(true);
                }
            }
        }
    }
}
