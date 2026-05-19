package ui;

import handler.BoardHandler;
import handler.GameLogicHandler;
import handler.GameStateHandler;
import handler.RecordHandler;
import handler.SaveHandler;
import model.Board;
import model.Cell;
import model.GameRecord;
import model.GameState;
import dsa.LinkedList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {

    private static final int CELL_SIZE = 36;
    private static final Color COLOR_UNREVEALED = new Color(170, 215, 81);
    private static final Color COLOR_UNREVEALED_ALT = new Color(162, 209, 73);
    private static final Color COLOR_REVEALED = new Color(229, 194, 159);
    private static final Color COLOR_REVEALED_ALT = new Color(215, 184, 153);
    private static final Color COLOR_MINE = new Color(255, 80, 80);
    private static final Color COLOR_FLAG = new Color(255, 50, 50);
    private static final Color COLOR_HEADER = new Color(74, 117, 44);
    private static final Color COLOR_BG = new Color(40, 40, 40);

    private static final Color[] NUMBER_COLORS = {
        null,
        new Color(25, 118, 210),
        new Color(56, 142, 60),
        new Color(211, 47, 47),
        new Color(123, 31, 162),
        new Color(255, 143, 0),
        new Color(0, 151, 167),
        new Color(66, 66, 66),
        new Color(158, 158, 158)
    };

    private GameState gameState;
    private GameLogicHandler logicHandler;
    private RecordHandler recordHandler;
    private BoardPanel boardPanel;
    private JLabel mineCountLabel;
    private JLabel timerLabel;
    private JLabel statusLabel;
    private Timer gameTimer;
    private String playerName;

    private int currentRows = 9;
    private int currentCols = 9;
    private int currentMines = 10;

    public GameFrame() {
        this.logicHandler = new GameLogicHandler();
        this.recordHandler = new RecordHandler();
        this.playerName = "Player";

        setTitle("Minesweeper DSA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(COLOR_BG);
        getContentPane().setBackground(COLOR_BG);

        initializeMenu();
        initializeComponents();
        startNewGame(currentRows, currentCols, currentMines);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COLOR_HEADER);

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setForeground(Color.WHITE);

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> startNewGame(currentRows, currentCols, currentMines));

        JMenuItem easyItem = new JMenuItem("Easy (9x9, 10 mines)");
        easyItem.addActionListener(e -> startNewGame(9, 9, 10));

        JMenuItem mediumItem = new JMenuItem("Medium (16x16, 40 mines)");
        mediumItem.addActionListener(e -> startNewGame(16, 16, 40));

        JMenuItem hardItem = new JMenuItem("Hard (16x30, 99 mines)");
        hardItem.addActionListener(e -> startNewGame(16, 30, 99));

        JMenuItem customItem = new JMenuItem("Custom...");
        customItem.addActionListener(e -> showCustomDialog());

        JMenuItem undoItem = new JMenuItem("Undo (Ctrl+Z)");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoItem.addActionListener(e -> performUndo());

        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.addActionListener(e -> saveGame());

        JMenuItem loadItem = new JMenuItem("Load Game");
        loadItem.addActionListener(e -> loadGame());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(easyItem);
        gameMenu.add(mediumItem);
        gameMenu.add(hardItem);
        gameMenu.add(customItem);
        gameMenu.addSeparator();
        gameMenu.add(undoItem);
        gameMenu.addSeparator();
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        JMenu recordMenu = new JMenu("Records");
        recordMenu.setForeground(Color.WHITE);

        JMenuItem leaderboardItem = new JMenuItem("Leaderboard");
        leaderboardItem.addActionListener(e -> showLeaderboard());

        JMenuItem statsItem = new JMenuItem("Statistics");
        statsItem.addActionListener(e -> showStatistics());

        JMenuItem clearRecordsItem = new JMenuItem("Clear Records");
        clearRecordsItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Clear all game records?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                recordHandler.clearRecords();
            }
        });

        recordMenu.add(leaderboardItem);
        recordMenu.add(statsItem);
        recordMenu.addSeparator();
        recordMenu.add(clearRecordsItem);

        JMenu nameMenu = new JMenu("Player");
        nameMenu.setForeground(Color.WHITE);

        JMenuItem setNameItem = new JMenuItem("Set Name...");
        setNameItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter your name:", playerName);
            if (name != null && !name.trim().isEmpty()) {
                playerName = name.trim();
            }
        });
        nameMenu.add(setNameItem);

        menuBar.add(gameMenu);
        menuBar.add(recordMenu);
        menuBar.add(nameMenu);
        setJMenuBar(menuBar);
    }

    private void initializeComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_HEADER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        mineCountLabel = new JLabel("Mines: 10");
        mineCountLabel.setForeground(Color.WHITE);
        mineCountLabel.setFont(new Font("Monospaced", Font.BOLD, 18));

        statusLabel = new JLabel("PLAYING", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 18));

        timerLabel = new JLabel("Time: 0");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(mineCountLabel, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.CENTER);
        headerPanel.add(timerLabel, BorderLayout.EAST);

        boardPanel = new BoardPanel();

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        gameTimer = new Timer(1000, e -> updateTimer());
    }

    private void startNewGame(int rows, int cols, int mines) {
        currentRows = rows;
        currentCols = cols;
        currentMines = mines;

        Board board = BoardHandler.createBoard(rows, cols, mines);
        gameState = new GameState(board);
        logicHandler.clearUndoHistory();

        mineCountLabel.setText("Mines: " + mines);
        statusLabel.setText("PLAYING");
        timerLabel.setText("Time: 0");

        int panelWidth = cols * CELL_SIZE;
        int panelHeight = rows * CELL_SIZE;
        boardPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));

        if (gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gameTimer.start();

        pack();
        setLocationRelativeTo(null);
        boardPanel.repaint();
    }

    private void updateTimer() {
        if (gameState != null && !gameState.isGameOver()) {
            long seconds = GameStateHandler.getElapsedSeconds(gameState);
            timerLabel.setText("Time: " + seconds);
        }
    }

    private void performUndo() {
        if (gameState.isGameOver()) {
            return;
        }
        Board previous = logicHandler.undo();
        if (previous != null) {
            gameState.setBoard(previous);
            gameState.setFlagsPlaced(BoardHandler.countFlags(previous));
            gameState.setCellsRevealed(BoardHandler.countRevealed(previous));
            mineCountLabel.setText("Mines: " + gameState.getRemainingMines());
            boardPanel.repaint();
        }
    }

    private void handleLeftClick(int row, int col) {
        if (gameState.isGameOver()) {
            return;
        }

        logicHandler.saveStateForUndo(gameState);

        int result = logicHandler.revealCell(gameState, row, col);

        if (result == -1) {
            GameStateHandler.triggerLose(gameState);
            gameTimer.stop();
            statusLabel.setText("GAME OVER");
            boardPanel.repaint();
            recordHandler.addRecord(new GameRecord(
                    playerName, currentRows, currentCols, currentMines,
                    gameState.getElapsedTime(), false
            ));
            JOptionPane.showMessageDialog(this,
                    "You hit a mine! Game Over.", "Game Over", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (result > 0 && GameStateHandler.checkWin(gameState)) {
            gameTimer.stop();
            statusLabel.setText("YOU WIN!");
            boardPanel.repaint();
            recordHandler.addRecord(new GameRecord(
                    playerName, currentRows, currentCols, currentMines,
                    gameState.getElapsedTime(), true
            ));
            JOptionPane.showMessageDialog(this,
                    "Congratulations! You won in " + gameState.getElapsedTime() + " seconds!",
                    "Victory", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boardPanel.repaint();
    }

    private void handleRightClick(int row, int col) {
        if (gameState.isGameOver()) {
            return;
        }

        logicHandler.saveStateForUndo(gameState);

        if (logicHandler.toggleFlag(gameState, row, col)) {
            mineCountLabel.setText("Mines: " + gameState.getRemainingMines());
            boardPanel.repaint();
        }
    }

    private void saveGame() {
        if (SaveHandler.saveGame(gameState)) {
            JOptionPane.showMessageDialog(this,
                    "Game saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save game.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        GameState loaded = SaveHandler.loadGame();
        if (loaded != null) {
            gameState = loaded;
            currentRows = loaded.getBoard().getRows();
            currentCols = loaded.getBoard().getCols();
            currentMines = loaded.getBoard().getTotalMines();
            logicHandler.clearUndoHistory();

            mineCountLabel.setText("Mines: " + gameState.getRemainingMines());
            timerLabel.setText("Time: " + gameState.getElapsedTime());

            if (gameState.isGameOver()) {
                gameTimer.stop();
                if (gameState.getGameStatus() == GameState.STATE_WON) {
                    statusLabel.setText("YOU WIN!");
                } else {
                    statusLabel.setText("GAME OVER");
                }
            } else {
                statusLabel.setText("PLAYING");
                gameTimer.start();
            }

            int panelWidth = currentCols * CELL_SIZE;
            int panelHeight = currentRows * CELL_SIZE;
            boardPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
            pack();
            setLocationRelativeTo(null);
            boardPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No saved game found.", "Load", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showCustomDialog() {
        JTextField rowsField = new JTextField("9", 5);
        JTextField colsField = new JTextField("9", 5);
        JTextField minesField = new JTextField("10", 5);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Rows (5-30):"));
        panel.add(rowsField);
        panel.add(new JLabel("Columns (5-30):"));
        panel.add(colsField);
        panel.add(new JLabel("Mines:"));
        panel.add(minesField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Custom Game", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int r = Integer.parseInt(rowsField.getText());
                int c = Integer.parseInt(colsField.getText());
                int m = Integer.parseInt(minesField.getText());
                r = Math.max(5, Math.min(30, r));
                c = Math.max(5, Math.min(30, c));
                m = Math.max(1, Math.min(r * c - 9, m));
                startNewGame(r, c, m);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid input. Please enter numbers.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showLeaderboard() {
        LinkedList<GameRecord> sorted = recordHandler.getSortedByScore();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s %-12s %-8s %-8s %-6s%n", "Rank", "Player", "Score", "Time(s)", "Diff"));
        sb.append("-------------------------------------------\n");
        int limit = Math.min(sorted.size(), 10);
        for (int i = 0; i < limit; i++) {
            GameRecord record = sorted.get(i);
            sb.append(String.format("%-4d %-12s %-8d %-8d %-6s%n",
                    i + 1, record.getPlayerName(), record.getScore(),
                    record.getTimeTaken(), record.getDifficulty()));
        }
        if (sorted.isEmpty()) {
            sb.append("No records yet. Play some games!\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(420, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Leaderboard (Top 10)", JOptionPane.PLAIN_MESSAGE);
    }

    private void showStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Games: ").append(recordHandler.getTotalGames()).append("\n");
        sb.append("Total Wins: ").append(recordHandler.getTotalWins()).append("\n");
        sb.append(String.format("Win Rate: %.1f%%%n", recordHandler.getWinRate()));
        JOptionPane.showMessageDialog(this, sb.toString(),
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private class BoardPanel extends JPanel {

        BoardPanel() {
            setBackground(COLOR_BG);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (gameState == null) {
                        return;
                    }
                    int col = e.getX() / CELL_SIZE;
                    int row = e.getY() / CELL_SIZE;
                    Board board = gameState.getBoard();
                    if (!board.isValidPosition(row, col)) {
                        return;
                    }
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        handleLeftClick(row, col);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        handleRightClick(row, col);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gameState == null) {
                return;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Board board = gameState.getBoard();
            int rows = board.getRows();
            int cols = board.getCols();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    drawCell(g2d, board.getCell(r, c), r, c);
                }
            }
        }

        private void drawCell(Graphics2D g, Cell cell, int row, int col) {
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;
            boolean isAlt = (row + col) % 2 == 0;

            if (cell.isRevealed()) {
                if (cell.hasMine()) {
                    g.setColor(COLOR_MINE);
                } else {
                    g.setColor(isAlt ? COLOR_REVEALED : COLOR_REVEALED_ALT);
                }
            } else {
                g.setColor(isAlt ? COLOR_UNREVEALED : COLOR_UNREVEALED_ALT);
            }
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g.setColor(new Color(0, 0, 0, 20));
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            if (cell.isRevealed()) {
                if (cell.hasMine()) {
                    drawMine(g, x, y);
                } else if (cell.getAdjacentMines() > 0) {
                    drawNumber(g, x, y, cell.getAdjacentMines());
                }
            } else if (cell.isFlagged()) {
                drawFlag(g, x, y);
            }
        }

        private void drawMine(Graphics2D g, int x, int y) {
            int cx = x + CELL_SIZE / 2;
            int cy = y + CELL_SIZE / 2;
            int size = CELL_SIZE / 3;

            g.setColor(Color.BLACK);
            g.fillOval(cx - size / 2, cy - size / 2, size, size);

            g.setStroke(new BasicStroke(2));
            g.drawLine(cx - size / 2 - 2, cy, cx + size / 2 + 2, cy);
            g.drawLine(cx, cy - size / 2 - 2, cx, cy + size / 2 + 2);
            g.drawLine(cx - size / 3, cy - size / 3, cx + size / 3, cy + size / 3);
            g.drawLine(cx - size / 3, cy + size / 3, cx + size / 3, cy - size / 3);
            g.setStroke(new BasicStroke(1));
        }

        private void drawNumber(Graphics2D g, int x, int y, int number) {
            g.setColor(NUMBER_COLORS[number]);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            String text = String.valueOf(number);
            int tx = x + (CELL_SIZE - fm.stringWidth(text)) / 2;
            int ty = y + (CELL_SIZE + fm.getAscent() - fm.getDescent()) / 2;
            g.drawString(text, tx, ty);
        }

        private void drawFlag(Graphics2D g, int x, int y) {
            int cx = x + CELL_SIZE / 2;
            int cy = y + CELL_SIZE / 2;

            g.setColor(new Color(80, 80, 80));
            g.setStroke(new BasicStroke(2));
            g.drawLine(cx, cy - 10, cx, cy + 8);
            g.setStroke(new BasicStroke(1));

            g.setColor(COLOR_FLAG);
            int[] xPoints = {cx, cx + 10, cx};
            int[] yPoints = {cy - 10, cy - 5, cy};
            g.fillPolygon(xPoints, yPoints, 3);

            g.setColor(new Color(80, 80, 80));
            g.fillRect(cx - 5, cy + 7, 10, 3);
        }
    }
}
