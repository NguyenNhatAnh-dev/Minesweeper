package com.minesweeper;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class BoardAlgorithm {
    private final Board board;

    public BoardAlgorithm(Board board) {
        this.board = board;
    }

    public void calculateBombAndSquare() {
        board.calculateAdjacentBombs();
    }

    public void openLargeArea(int row, int col) {
        if (!board.isInBounds(row, col)) {
            return;
        }

        Queue<Cell> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.add(board.getCell(row, col));

        while (!queue.isEmpty()) {
            Cell cell = queue.remove();
            String key = cell.getRow() + ":" + cell.getCol();
            if (!visited.add(key)) {
                continue;
            }
            if (cell.isBomb() || cell.isFlagged()) {
                continue;
            }

            cell.setRevealed(true);
            if (cell.getAdjacentBombs() != 0) {
                continue;
            }

            for (Cell neighbor : board.getNeighbors(cell.getRow(), cell.getCol())) {
                if (!neighbor.isRevealed() && !neighbor.isFlagged()) {
                    queue.add(neighbor);
                }
            }
        }
    }
}
