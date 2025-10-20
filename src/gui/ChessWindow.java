package gui;

import util.SpriteManager;

import model.Board;
import model.Pieces;

import javax.swing.*;
import java.awt.*;


public class ChessWindow extends JFrame{
    int windowWidth = 1200;
    int windowHeight = 800;

    private final GamePanel gamePanel;

    Color COLOR_DARK_SQUARE = new Color(181, 136, 99);
    Color COLOR_LIGHT_SQUARE = new Color(240, 217, 181);
    Color COLOR_BACKGROUND = new Color(40,40,40);

    JFrame frame;
    JPanel board;
    JPanel rightPanel;

    JButton[][] boardButtons = new JButton[8][8];

    SpriteManager spriteManager = new SpriteManager();

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
        getContentPane().setBackground(COLOR_BACKGROUND);
        setResizable(false);

        initRightPanel();

        add(gamePanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

/*
    private void initWindow() {


        board.setLayout(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(800, 800));
        createBoardSquares();
        frame.add(board, BorderLayout.WEST);

        initRightPanel();

        frame.add(rightPanel, BorderLayout.CENTER);


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
*/

    private void initRightPanel() {
        rightPanel.setPreferredSize(new Dimension(400, 800));
        rightPanel.setBackground(COLOR_BACKGROUND);
        rightPanel.setLayout(new BorderLayout());

        JLabel placeholder = new JLabel("Game History Area", SwingConstants.CENTER);
        placeholder.setForeground(Color.LIGHT_GRAY);

        rightPanel.add(placeholder, BorderLayout.CENTER);
    }

/*    private void createBoardSquares() {
        //TODO file and rank numbers and letters
        for (int file = 0; file < 8; file++) {
            for (int row = 0; row < 8; row++) {
                JButton square = new JButton();
                square.setOpaque(true);
//                square.setContentAreaFilled(false);
                square.setBorderPainted(false);
//                square.setEnabled(false);
                square.setFocusPainted(false);

                if ((file + row) % 2 == 0)
                    square.setBackground(COLOR_LIGHT_SQUARE); // light
                else
                    square.setBackground(COLOR_DARK_SQUARE);   // dark



                boardButtons[file][row] = square;
                board.add(square);
            }
        }
        assignAllSprites(new Board());
    }

    private void assignAllSprites(Board board){
        Pieces[][] layout = board.getBoard();

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Pieces piece = layout[rank][file];
                JButton square = boardButtons[rank][file];

                if (piece == null || piece == Pieces.EMPTY) {
                    square.setIcon(null);
                    continue;
                }

                ImageIcon icon =  new ImageIcon(spriteManager.get(piece)) ;

                square.setIcon(icon);

            }
        }
    }*/
}
