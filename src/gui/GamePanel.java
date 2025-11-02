package gui;

import model.Board;
import model.Pieces;
import util.SpriteManager;
import util.Theme;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {


    SquareButton[][] squareButtons = new SquareButton[8][8];

    public GamePanel() {
        this.setBackground(Theme.BACKGROUND);
        this.setPreferredSize(new Dimension(800, 800));

        this.setLayout(new GridLayout(8, 8));
        drawBoard();
        assignAllSprites(new Board());
    }

    private void drawBoard() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                squareButtons[rank][file] = new SquareButton(rank, file);
                this.add(squareButtons[rank][file]);
            }
        }
    }

    private void assignAllSprites(Board board){
        SpriteManager spriteManager = new SpriteManager();

        Pieces[][] layout = board.getBoard();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                squareButtons[rank][file].setPiece(layout[rank][file]);
//                System.out.println("[" + rank + "," + file + "]" + layout[rank][file]);
                squareButtons[rank][file].setIcon(spriteManager.getIcon(layout[rank][file]));
            }
        }
    }
}
