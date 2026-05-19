package handler;

import model.Board;
import model.Cell;
import model.GameState;
import dsa.Queue;
import dsa.Stack;

public class GameLogicHandler {

    private Stack<Board> undoStack;

    public GameLogicHandler() {
        this.undoStack = new Stack<>(100);
    }

    public void saveStateForUndo(GameState gameState) {
        Board snapshot = gameState.getBoard().deepCopy();
        undoStack.push(snapshot);
    }

    public Board undo() {
        if (undoStack.isEmpty()) {
            return null;
        }
        return undoStack.pop();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public void clearUndoHistory() {
        undoStack.clear();
    }

    public int revealCell(GameState gameState, int row, int col) {
        Board board = gameState.getBoard();
        Cell cell = board.getCell(row, col);

        if (cell == null || cell.isRevealed() || cell.isFlagged()) {
            return 0;
        }

        if (gameState.isFirstClick()) {
            BoardHandler.placeMines(board, row, col);
            gameState.setFirstClick(false);
        }

        if (cell.hasMine()) {
            cell.setRevealed(true);
            return -1;
        }

        if (cell.getAdjacentMines() > 0) {
            cell.setRevealed(true);
            gameState.setCellsRevealed(gameState.getCellsRevealed() + 1);
            return 1;
        }

        return floodFillBFS(gameState, row, col);
    }

    private int floodFillBFS(GameState gameState, int startRow, int startCol) {
        Board board = gameState.getBoard();
        int revealed = 0;

        Queue<int[]> queue = new Queue<>(board.getRows() * board.getCols());
        queue.enqueue(new int[]{startRow, startCol});
        board.getCell(startRow, startCol).setRevealed(true);
        revealed++;

        int[][] directions = BoardHandler.getDirections();

        while (!queue.isEmpty()) {
            int[] current = queue.dequeue();
            int r = current[0];
            int c = current[1];

            Cell currentCell = board.getCell(r, c);

            if (currentCell.getAdjacentMines() == 0) {
                for (int[] dir : directions) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (!board.isValidPosition(nr, nc)) {
                        continue;
                    }

                    Cell neighbor = board.getCell(nr, nc);

                    if (neighbor.isRevealed() || neighbor.isFlagged() || neighbor.hasMine()) {
                        continue;
                    }

                    neighbor.setRevealed(true);
                    revealed++;

                    if (neighbor.getAdjacentMines() == 0) {
                        queue.enqueue(new int[]{nr, nc});
                    }
                }
            }
        }

        gameState.setCellsRevealed(gameState.getCellsRevealed() + revealed);
        return revealed;
    }

    public boolean toggleFlag(GameState gameState, int row, int col) {
        Board board = gameState.getBoard();
        Cell cell = board.getCell(row, col);

        if (cell == null || cell.isRevealed()) {
            return false;
        }

        if (cell.isFlagged()) {
            cell.setFlagged(false);
            gameState.setFlagsPlaced(gameState.getFlagsPlaced() - 1);
        } else {
            cell.setFlagged(true);
            gameState.setFlagsPlaced(gameState.getFlagsPlaced() + 1);
        }

        return true;
    }
}
