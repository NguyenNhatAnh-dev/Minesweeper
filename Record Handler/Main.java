import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
    JFrame frame = new JFrame("Deep Sea Mines");
    GameBoard board = new GameBoard();
    frame.add(board);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    try {
        // Load your icon
        BufferedImage n1 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num1.png"));
        BufferedImage n2 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num2.png"));
        BufferedImage n3 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num3.png"));
        BufferedImage n4 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num4.png"));
        BufferedImage n5 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num5.png"));
        BufferedImage n6 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num6.png"));
        BufferedImage n7 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num7.png"));
        BufferedImage n8 = ImageIO.read(board.getClass().getResourceAsStream("/resources/num8.png"));
        BufferedImage boom = ImageIO.read(board.getClass().getResourceAsStream("/resources/Boom.png"));
        
        // TEST CORNERS: If these 4 are right, the whole board is right.
        board.setCell('a', 1, n1); // Top Left
        board.setCell('a', 8, n5); // Top Right
        board.setCell('h', 1, n8); // Bottom Left
        board.setCell('h', 8, boom); // Bottom Right
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
