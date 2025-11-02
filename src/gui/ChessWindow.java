package gui;

import util.Theme;

import javax.swing.*;
import java.awt.*;


public class ChessWindow extends JFrame{
    int windowWidth = 1200;
    int windowHeight = 830;

    private final GamePanel gamePanel;



    JFrame frame;
    JPanel board;
    JPanel rightPanel;

    public ChessWindow() {

        gamePanel = new GamePanel();

        frame = new JFrame("Chess");
        board = new JPanel();
        rightPanel = new JPanel();



        setupFrame();
    }

    private void setupFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        getContentPane().setBackground(Theme.BACKGROUND);
        setResizable(false);

        initRightPanel();

        add(gamePanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void initRightPanel() {
        rightPanel.setPreferredSize(new Dimension(400, 800));
        rightPanel.setBackground(Theme.BACKGROUND);
        rightPanel.setLayout(new BorderLayout());

        JLabel placeholder = new JLabel("Game History Area", SwingConstants.CENTER);
        placeholder.setForeground(Theme.TEXT_COLOR);

        rightPanel.add(placeholder, BorderLayout.CENTER);
    }

}
