package com.minesweeper;

public class GameStateHandler {
    private final Board board;
    private final BoardAlgorithm boardAlgorithm;
    private GameStatus status = GameStatus.READY;

    public GameStateHandler(Board board) {
        this.board = board;
        this.boardAlgorithm = new BoardAlgorithm(board);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void placeBomb(int row, int col) {
        board.getCell(row, col).setBomb(true);
    }

    public void initializeBoard() {
        boardAlgorithm.calculateBombAndSquare();
        status = GameStatus.PLAYING;
    }

    public GameStatus clickCell(int row, int col) {
        if (status == GameStatus.WON || status == GameStatus.LOST) {
            return status;
        }

        if (status == GameStatus.READY) {
            status = GameStatus.PLAYING;
        }

        Cell cell = board.getCell(row, col);
        if (cell.isFlagged() || cell.isRevealed()) {
            return status;
        }

        if (cell.isBomb()) {
            return lose();
        }

        boardAlgorithm.openLargeArea(row, col);
        return checkWinCondition();
    }

    public GameStatus toggleFlag(int row, int col) {
        if (status == GameStatus.WON || status == GameStatus.LOST) {
            return status;
        }

        Cell cell = board.getCell(row, col);
        if (cell.isRevealed()) {
            return status;
        }

        cell.setFlagged(!cell.isFlagged());
        return checkWinCondition();
    }

    public GameStatus checkWinCondition() {
        if (board.countBombs() > 0 && board.allBombsFlagged()) {
            status = GameStatus.WON;
            return status;
        }

        if (board.countRevealedSafeCells() == board.countSafeCells()) {
            status = GameStatus.WON;
            return status;
        }

        return status;
    }

    public GameStatus lose() {
        board.revealAllBombs();
        status = GameStatus.LOST;
        return status;
    }
}
