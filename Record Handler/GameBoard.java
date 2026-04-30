import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GameBoard extends JPanel {
    private final int ROWS = 8, COLS = 8, CELL_SIZE = 61, GAP = 20, START_X = 83, START_Y = 220;
    private final double SCALE = 0.65;
    private BufferedImage bg, bomb, flag, win, fail, nums[] = new BufferedImage[9];
    private Cell[][] grid = new Cell[ROWS][COLS];
    private GameSession session = new GameSession();

    public GameBoard() {
        for (int r = 0; r < ROWS; r++) for (int c = 0; c < COLS; c++) grid[r][c] = new Cell();
        loadRes();
        if (bg != null) setPreferredSize(new Dimension((int)(bg.getWidth()*SCALE), (int)(bg.getHeight()*SCALE)));
        new Timer(500, e -> repaint()).start();
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (session.isGameOver()) return;
                int c = (int)((e.getX()/SCALE - START_X) / (CELL_SIZE + GAP)), r = (int)((e.getY()/SCALE - START_Y) / (CELL_SIZE + GAP));
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS) handle(r, c, SwingUtilities.isLeftMouseButton(e));
            }
        });
    }

    private void handle(int r, int c, boolean isL) {
        Cell cell = grid[r][c];
        if (isL && !cell.isFlagged() && !cell.isRevealed()) {
            cell.setRevealed(true);
            if (cell.getValue() != null && cell.getValue() == -1) session.setGameOver(true);
        } else if (!isL && !cell.isRevealed()) {
            cell.setFlagged(!cell.isFlagged());
            checkWin();
        }
        session.addMove(); repaint();
    }

    private void checkWin() {
        int m = 0, f = 0, ok = 0;
        for (Cell[] row : grid) for (Cell cl : row) {
            boolean isM = cl.getValue() != null && cl.getValue() == -1;
            if (isM) m++;
            if (cl.isFlagged()) { f++; if (isM) ok++; }
        }
        if (f == m && ok == m) { session.setGameOver(true); session.setWon(true); }
    }

    private void loadRes() {
        try {
            bg = img("boardgame_integrated.png"); bomb = img("Boom.png"); flag = img("Flat.png");
            win = img("won.png"); fail = img("fail.png");
            for (int i = 1; i <= 8; i++) nums[i] = img("num" + i + ".png");
        } catch (Exception e) {}
    }

    private BufferedImage img(String n) {
        try { return ImageIO.read(getClass().getResource("/resources/" + n)); } catch (Exception e) { return null; }
    }

    public void setCellValue(char r, int c, Integer v) { grid[Character.toLowerCase(r)-'a'][c-1].setValue(v); }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g); Graphics2D g2 = (Graphics2D) g; g2.scale(SCALE, SCALE);
        if (bg != null) g2.drawImage(bg, 0, 0, null);
        drawHUD(g2);
        for (int r = 0; r < ROWS; r++) for (int c = 0; c < COLS; c++) drawCell(g2, r, c);
        if (session.isGameOver()) {
            BufferedImage res = session.isWon() ? win : fail;
            if (res != null) g2.drawImage(res, (bg.getWidth()-res.getWidth())/2, (bg.getHeight()-res.getHeight())/2, null);
        }
    }

    private void drawCell(Graphics2D g2, int r, int c) {
        int x = START_X + c*(CELL_SIZE+GAP), y = START_Y + r*(CELL_SIZE+GAP); Cell cl = grid[r][c];
        if (cl.isRevealed()) {
            Integer v = cl.getValue();
            if (v == null) { g2.setColor(new Color(255,255,255,50)); g2.fillRect(x,y,CELL_SIZE,CELL_SIZE); }
            else if (v == -1) g2.drawImage(bomb, x, y, CELL_SIZE, CELL_SIZE, null);
            else g2.drawImage(nums[v], x, y, CELL_SIZE, CELL_SIZE, null);
        } else if (cl.isFlagged()) g2.drawImage(flag, x, y, CELL_SIZE, CELL_SIZE, null);
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(Color.CYAN); g2.setFont(new Font("Monospaced", 1, 18));
        for (int i = 0; i < 8; i++) {
            g2.drawString(""+(char)('a'+i), START_X-35, START_Y+i*(CELL_SIZE+GAP)+40);
            g2.drawString(""+(i+1), START_X+i*(CELL_SIZE+GAP)+25, START_Y-20);
        }
        g2.setFont(new Font("Monospaced", 1, 22));
        g2.drawString("TIME: "+session.getTimeString() + "  MOVES: "+session.getMoves(), START_X, START_Y-70);
        g2.drawString("SCORE: "+session.calculateScore(), START_X, START_Y-40);
    }
}