package model;

import java.io.Serializable;

public class GameRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String playerName;
    private int rows;
    private int cols;
    private int mines;
    private long timeTaken;
    private boolean won;
    private long timestamp;
    private int score;

    public GameRecord(String playerName, int rows, int cols, int mines,
                      long timeTaken, boolean won) {
        this.playerName = playerName;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.timeTaken = timeTaken;
        this.won = won;
        this.timestamp = System.currentTimeMillis();
        this.score = calculateScore();
    }

    private int calculateScore() {
        if (!won) {
            return 0;
        }
        int boardSize = rows * cols;
        double mineRatio = (double) mines / boardSize;
        int baseScore = (int) (mineRatio * 10000);
        int timeBonus = Math.max(0, 3000 - (int) timeTaken);
        return baseScore + timeBonus;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMines() {
        return mines;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public boolean isWon() {
        return won;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getScore() {
        return score;
    }

    public String getDifficulty() {
        int total = rows * cols;
        double ratio = (double) mines / total;
        if (ratio < 0.13) {
            return "Easy";
        } else if (ratio < 0.18) {
            return "Medium";
        } else {
            return "Hard";
        }
    }

    public String toSaveString() {
        return playerName + "|" + rows + "|" + cols + "|" + mines + "|"
                + timeTaken + "|" + won + "|" + timestamp + "|" + score;
    }

    public static GameRecord fromSaveString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 8) {
            return null;
        }
        GameRecord record = new GameRecord(
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Long.parseLong(parts[4]),
                Boolean.parseBoolean(parts[5])
        );
        record.timestamp = Long.parseLong(parts[6]);
        record.score = Integer.parseInt(parts[7]);
        return record;
    }
}
