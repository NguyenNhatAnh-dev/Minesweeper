import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameBoard extends JPanel {
    private BufferedImage backgroundImage;
    private final int ROWS = 8;
    private final int COLS = 8;

    // --- PHẦN ĐIỀU CHỈNH CẤU TRÌNH ---
    private final double SCALE = 0.65;      // Tỉ lệ toàn bộ board
    private final int CELL_SIZE = 61;       // Kích thước chuẩn của 1 ô ảnh
    private final int GAP = 20;              // <--- KHOẢNG CÁCH GIỮA CÁC Ô (Chỉnh ở đây)
    
    private final int START_X = 83;         // Tọa độ X của ô đầu tiên
    private final int START_Y = 220;        // Tọa độ Y của ô đầu tiên
    // ---------------------------------

    private Map<String, BufferedImage> cellLayer = new HashMap<>();

    public GameBoard() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/boardgame_integrated.png"));
            int w = (int) (backgroundImage.getWidth() * SCALE);
            int h = (int) (backgroundImage.getHeight() * SCALE);
            setPreferredSize(new Dimension(w, h));
        } catch (IOException e) {
            System.err.println("Resource error: " + e.getMessage());
        }
    }

    public void setCell(char row, int col, BufferedImage img) {
        int r = Character.toLowerCase(row) - 'a';
        int c = col - 1;
        if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
            cellLayer.put(r + "_" + c, img);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.scale(SCALE, SCALE);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, null);
        }

        drawLabels(g2d);

        // Vẽ các icon (num1, num2...) với khoảng cách GAP
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                BufferedImage img = cellLayer.get(r + "_" + c);
                if (img != null) {
                    // Công thức có tính đến GAP
                    int drawX = START_X + (c * (CELL_SIZE + GAP));
                    int drawY = START_Y + (r * (CELL_SIZE + GAP));
                    
                    g2d.drawImage(img, drawX, drawY, CELL_SIZE, CELL_SIZE, null);
                }
            }
        }
    }

    private void drawLabels(Graphics2D g2d) {
        g2d.setColor(new Color(0, 255, 255)); 
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));

        for (int i = 0; i < 8; i++) {
            // Tọa độ tính toán dựa trên (CELL_SIZE + GAP) để luôn khớp với ô
            int offsetPos = i * (CELL_SIZE + GAP);

            // Vẽ chữ cái a-h (Cột bên trái)
            // +40 là để đẩy chữ xuống giữa ô theo chiều dọc
            g2d.drawString(String.valueOf((char)('a' + i)), START_X - 35, START_Y + offsetPos + 40);
            
            // Vẽ số 1-8 (Hàng bên trên)
            // +25 là để đẩy chữ vào giữa ô theo chiều ngang
            g2d.drawString(String.valueOf(i + 1), START_X + offsetPos + 25, START_Y - 20);
        }
    }
}