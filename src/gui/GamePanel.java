package gui;

import model.Board;
import model.Pieces;
import util.SpriteManager;
import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    private boolean dragging = false;

    SquareButton[][] squareButtons = new SquareButton[8][8];

    public GamePanel() {
        this.setBackground(Theme.BACKGROUND);
        this.setPreferredSize(new Dimension(800, 800));

        this.setLayout(new GridLayout(8, 8));
        drawBoard();
        assignAllIcons(new Board());
    }

    private void drawBoard() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                squareButtons[rank][file] = new SquareButton(rank, file);
                this.add(squareButtons[rank][file]);
            }
        }
    }

    private void assignAllIcons(Board board){
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



    public Sprite getSprite(java.awt.Point point) {
        return null; //todo implement
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
