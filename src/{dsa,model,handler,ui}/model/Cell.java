package model;

import java.io.Serializable;

public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    private int row;
    private int col;
    private boolean hasMine;
    private boolean revealed;
    private boolean flagged;
    private int adjacentMines;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.hasMine = false;
        this.revealed = false;
        this.flagged = false;
        this.adjacentMines = 0;
    }

    public Cell(Cell other) {
        this.row = other.row;
        this.col = other.col;
        this.hasMine = other.hasMine;
        this.revealed = other.revealed;
        this.flagged = other.flagged;
        this.adjacentMines = other.adjacentMines;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public void setMine(boolean mine) {
        this.hasMine = mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public String toKey() {
        return row + "," + col;
    }
}
