package gui;

import model.Board;
import model.Pieces;
import util.SpriteManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    Color COLOR_DARK_SQUARE = new Color(181, 136, 99);
    Color COLOR_LIGHT_SQUARE = new Color(240, 217, 181);


    SquareButton[][] squareButtons = new SquareButton[8][8];

    public GamePanel() {
        this.setBackground(Color.darkGray);
        this.setPreferredSize(new Dimension(800, 800));

        this.setLayout(new GridLayout(8, 8));  // Changed to 8x8 for chess squareButtons
        drawBoard();
        assignAllSprites(new Board());
    }

    private void drawBoard() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                squareButtons[file][rank] = new SquareButton(file, rank);
                this.add(squareButtons[file][rank]);
            }
        }
    }

    private void assignAllSprites(Board board){
        SpriteManager spriteManager = new SpriteManager();

        Pieces[][] layout = board.getBoard();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                squareButtons[file][rank].setPiece(layout[rank][file]);
//                System.out.println("[" + file + "," + rank + "]" + layout[rank][file]);
                squareButtons[file][rank].setIcon(spriteManager.getIcon(layout[rank][file]));
            }
        }
    }
}
