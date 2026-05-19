package handler;

import model.Board;
import model.Cell;
import model.GameState;

public class GameStateHandler {

    public static boolean checkWin(GameState gameState) {
        Board board = gameState.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        int totalCells = rows * cols;
        int totalMines = board.getTotalMines();
        int safeCells = totalCells - totalMines;

        if (gameState.getCellsRevealed() == safeCells) {
            gameState.setGameStatus(GameState.STATE_WON);
            gameState.setElapsedTime(
                    (System.currentTimeMillis() - gameState.getStartTime()) / 1000
            );
            autoFlagRemainingMines(board);
            return true;
        }

        return false;
    }

    public static void triggerLose(GameState gameState) {
        gameState.setGameStatus(GameState.STATE_LOST);
        gameState.setElapsedTime(
                (System.currentTimeMillis() - gameState.getStartTime()) / 1000
        );
        revealAllMines(gameState.getBoard());
    }

    public static void revealAllMines(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.hasMine()) {
                    cell.setRevealed(true);
                }
            }
        }
    }

    private static void autoFlagRemainingMines(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                if (cell.hasMine() && !cell.isFlagged()) {
                    cell.setFlagged(true);
                }
            }
        }
    }

    public static long getElapsedSeconds(GameState gameState) {
        if (gameState.isGameOver()) {
            return gameState.getElapsedTime();
        }
        return (System.currentTimeMillis() - gameState.getStartTime()) / 1000;
    }

    public static boolean isGamePlaying(GameState gameState) {
        return gameState.getGameStatus() == GameState.STATE_PLAYING;
    }
}
