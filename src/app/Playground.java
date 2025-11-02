package app;


import gui.Sprite;
import model.Pieces;
import util.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Playground extends JPanel implements MouseListener, MouseMotionListener {
    public Playground() {
        initUI();
    }

    private Sprite sprite;

    private void initUI() {
        SpriteManager sm = new SpriteManager();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        setSize(400, 400);

//        ImageIcon icon1 = sm.getIcon(Pieces.WHITE_PAWN);
//        JLabel label1 = new JLabel(icon1);
//        add(label1);

        sprite = sm.getSprite(Pieces.WHITE_PAWN);

        repaint();
    }



    private int spriteX = 60;
    private int spriteY = 60;
    private final int spriteWidth = 60;
    private final int spriteHeight = 60;

    private boolean dragging = false;


    @Override
    public void paintComponent(Graphics g) {

            super.paintComponent(g);
            if (sprite != null) {
                g.drawImage(sprite, spriteX, spriteY, spriteWidth, spriteHeight, null);
            }

    }

    public static void main(String[] args) {

        var f = new JFrame("Playground");
        f.add(new Playground());
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(400, 400));
        f.setResizable(false);
        f.setVisible(true);
        f.pack();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= spriteX && e.getX() <= spriteX + spriteWidth &&
            e.getY() >= spriteY && e.getY() <= spriteY + spriteHeight) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
        snapToGrid(e.getPoint().x, e.getPoint().y);
        
    }

    private void snapToGrid(int posX, int posY) {
        int gridX = posX / 40;
        int gridY = posY / 40;

        System.out.println("[" + gridX + "," + gridY + "]");
        spriteX = gridX * 40;
        spriteY = gridY * 40;
        repaint();

    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            spriteX = e.getX() - 20;
            spriteY = e.getY() - 20;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
