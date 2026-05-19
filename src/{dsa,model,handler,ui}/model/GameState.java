package model;

import java.io.Serializable;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATE_PLAYING = 0;
    public static final int STATE_WON = 1;
    public static final int STATE_LOST = 2;

    private Board board;
    private int gameStatus;
    private int flagsPlaced;
    private int cellsRevealed;
    private long startTime;
    private long elapsedTime;
    private boolean firstClick;

    public GameState(Board board) {
        this.board = board;
        this.gameStatus = STATE_PLAYING;
        this.flagsPlaced = 0;
        this.cellsRevealed = 0;
        this.startTime = System.currentTimeMillis();
        this.elapsedTime = 0;
        this.firstClick = true;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getFlagsPlaced() {
        return flagsPlaced;
    }

    public void setFlagsPlaced(int flagsPlaced) {
        this.flagsPlaced = flagsPlaced;
    }

    public int getCellsRevealed() {
        return cellsRevealed;
    }

    public void setCellsRevealed(int cellsRevealed) {
        this.cellsRevealed = cellsRevealed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public boolean isFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean firstClick) {
        this.firstClick = firstClick;
    }

    public int getRemainingMines() {
        return board.getTotalMines() - flagsPlaced;
    }

    public boolean isGameOver() {
        return gameStatus != STATE_PLAYING;
    }
}
