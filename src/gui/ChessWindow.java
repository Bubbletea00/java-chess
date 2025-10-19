package gui;

import util.SpriteManager;

import model.Board;
import model.Pieces;

import javax.swing.*;
import java.awt.*;


public class ChessWindow {
    int windowWidth = 1200;
    int windowHeight = 800;

    Color COLOR_DARK_SQUARE = new Color(181, 136, 99);
    Color COLOR_LIGHT_SQUARE = new Color(240, 217, 181);
    Color COLOR_BACKGROUND = new Color(40,40,40);

    JFrame frame = new JFrame("Chess");
    JPanel board = new JPanel();
    JPanel rightPanel = new JPanel();

    JButton[][] boardButtons = new JButton[8][8];

    SpriteManager spriteManager = new SpriteManager();

    public ChessWindow() {
        initWindow();
    }

    private void initWindow() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.getContentPane().setBackground(COLOR_BACKGROUND);
        frame.setResizable(false);


        board.setLayout(new GridLayout(8, 8));
        board.setPreferredSize(new Dimension(800, 800));
        createBoardSquares();
        frame.add(board, BorderLayout.WEST);



        rightPanel.setPreferredSize(new Dimension(400, 800));
        rightPanel.setBackground(COLOR_BACKGROUND);
        rightPanel.setLayout(new BorderLayout());

        JLabel placeholder = new JLabel("Game History Area", SwingConstants.CENTER);
        placeholder.setForeground(Color.LIGHT_GRAY);
        rightPanel.add(placeholder, BorderLayout.CENTER);

        frame.add(rightPanel, BorderLayout.CENTER);


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createBoardSquares() {
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


                //TODO Ich hasse drag and drop

//                square.setTransferHandler(new TransferHandler("icon"));
//                square.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        JButton button = (JButton) e.getSource();
//                        if (button.getIcon() != null){
//                            TransferHandler handler = button.getTransferHandler();
//                            handler.exportAsDrag(button, e, TransferHandler.MOVE);
//                        }
//                    }
//                });

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

                String key = piece.name().toLowerCase();
                ImageIcon icon =  new ImageIcon(spriteManager.get(key)) ;

                square.setIcon(icon);

            }
        }


    }
}
