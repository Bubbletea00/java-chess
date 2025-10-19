package gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
        int windowWidth = 1200;
        int windowHeight = 800;
        JButton[][] board = new JButton[8][8];

        public GamePanel() {
            this.setPreferredSize(new Dimension(windowWidth,windowHeight));
            this.setBackground(Color.darkGray);
//            this.setDoubleBuffered(true);

            this.setLayout(new GridLayout(3,3));
            drawBoard();
        }

        private void drawBoard(){
            for (int file = 0; file < 8; file++) {
                for (int rank = 0; rank < 8; rank++) {
                    boolean isLightSquare = (rank + file) % 2 != 0;

                    JButton square = board[file][rank];

                    this.add(square);

                    board[file][rank].setBackground(Color.darkGray);
                    board[file][rank].setFocusable(false);
                }
            }
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    
//                    
//                    JButton tile = board[i][j];
//
//                    this.add(tile);
//
//
//
//                    tile.setBackground(Color.darkGray);
//                    tile.setFocusable(false);
//
//                }
//            }
        }
}
