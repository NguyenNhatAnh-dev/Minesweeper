import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Deep Sea Mines");
        GameBoard board = new GameBoard();

        // THIẾT LẬP DỮ LIỆU TEST
        board.setCellValue('a', 1, 1);
        board.setCellValue('b', 2, -1); // Bom ở b2
        board.setCellValue('h', 8, -1); // Bom ở h8
        
        // Bạn có thể thêm các ô số xung quanh bom ở đây
        // Hoặc viết thêm hàm generateMap tự động như mình đã gợi ý ở trên.

        frame.add(board);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}