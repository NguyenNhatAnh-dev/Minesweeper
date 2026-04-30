import javax.swing.Timer;

public class GameSession {
    private int moves = 0;
    private int secondsElapsed = 0;
    private boolean isGameOver = false;
    private boolean isWon = false;
    private final int MAX_TIME_SECONDS = 45 * 60; // 45 phút = 2700 giây
    private Timer timer;

    public GameSession() {
        timer = new Timer(1000, e -> {
            if (!isGameOver) {
                secondsElapsed++;
            }
        });
        timer.start();
    }

    public void addMove() { moves++; }

    public int calculateScore() {
        // Điểm thời gian: Tối đa 4000, giảm dần theo thời gian. Sau 45p còn 0.
        int timeScore = 0;
        if (secondsElapsed < MAX_TIME_SECONDS) {
            timeScore = (int) (4000 * (1.0 - (double) secondsElapsed / MAX_TIME_SECONDS));
        }

        // Điểm lượt đi: Tối đa 6000, mỗi lượt đi trừ 50 điểm
        int moveScore = Math.max(0, 6000 - (moves * 50));

        return timeScore + moveScore;
    }

    public String getTimeString() {
        int mins = secondsElapsed / 60;
        int secs = secondsElapsed % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public int getMoves() { return moves; }
    public boolean isGameOver() { return isGameOver; }
    public void setGameOver(boolean gameOver) { 
        this.isGameOver = gameOver; 
        if (gameOver) timer.stop();
    }
    public boolean isWon() { return isWon; }
    public void setWon(boolean won) { this.isWon = won; }
}