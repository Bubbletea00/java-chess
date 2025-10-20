package app;


import model.Pieces;
import util.SpriteManager;

import javax.swing.*;
import javax.swing.event.MenuDragMouseListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImagingOpException;

public class Playground extends JPanel implements MouseListener, MouseMotionListener {
    public Playground() {
        initUI();
    }

    private void initUI() {
        SpriteManager sm = new SpriteManager();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        setSize(400, 400);

        ImageIcon icon1 = sm.getIcon(Pieces.WHITE_PAWN);

        JLabel label1 = new JLabel(icon1);

        add(label1);

        repaint();
    }



    private Rectangle dragRect = new Rectangle(40,40,40,40);

    private boolean dragging = false;


    @Override
    public void paintComponent(Graphics g) {

            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(dragRect.x, dragRect.y, dragRect.width, dragRect.height);

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
        if (dragRect.contains(e.getPoint())) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
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
            dragRect.setLocation(e.getX()-20, e.getY()-20);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
