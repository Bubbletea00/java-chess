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
import java.util.HashMap;
import java.util.Map;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    private boolean dragging = false;
    private Sprite draggedSprite = null;
    private Point dragPosition = null;
    private Point dragSourceSquare = null;

    private Map<Point, Pieces> piecePositions = new HashMap<>();
    private final SpriteManager spriteManager;
    
    private static final int BOARD_SIZE = 8;

    public GamePanel() {
        this.setBackground(Theme.BACKGROUND);
        this.setPreferredSize(new Dimension(800, 800));
        this.spriteManager = new SpriteManager();

        this.setLayout(null);
        
        assignAllIcons(new Board());
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void assignAllIcons(Board board){
        Pieces[][] layout = board.getBoard();
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                if (layout[rank][file] != Pieces.EMPTY) {
                    piecePositions.put(new Point(rank, file), layout[rank][file]);
                }
            }
        }
    }

    public Sprite getSprite(java.awt.Point point) {
        Pieces piece = piecePositions.get(point);
        return piece != null ? spriteManager.getSprite(piece) : null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int squareSize = getWidth() / BOARD_SIZE;
        
        // Draw the chessboard squares
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                boolean isLight = (rank + file) % 2 != 0;
                g2d.setColor(isLight ? Theme.LIGHT_SQUARE : Theme.DARK_SQUARE);
                g2d.fillRect(file * squareSize, rank * squareSize, squareSize, squareSize);
            }
        }

        for (Map.Entry<Point, Pieces> entry : piecePositions.entrySet()) {
            Point square = entry.getKey();
            Pieces piece = entry.getValue();
            
            // Skip drawing the sprite at its original position if it's being dragged
            if (dragging && square.equals(dragSourceSquare)) {
                continue;
            }
            
            Sprite sprite = spriteManager.getSprite(piece);
            if (sprite != null) {
                int x = square.y * squareSize;
                int y = square.x * squareSize;
                g2d.drawImage(sprite, x, y, squareSize, squareSize, null);
            }
        }

        if (dragging && draggedSprite != null && dragPosition != null) {
            int x = dragPosition.x - squareSize / 2;
            int y = dragPosition.y - squareSize / 2;
            g2d.drawImage(draggedSprite, x, y, squareSize, squareSize, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int squareSize = getWidth() / BOARD_SIZE;
        int file = e.getX() / squareSize;
        int rank = e.getY() / squareSize;
        
        if (rank >= 0 && rank < BOARD_SIZE && file >= 0 && file < BOARD_SIZE) {
            Point square = new Point(rank, file);
            Pieces piece = piecePositions.get(square);
            
            if (piece != null) {
                Sprite sprite = spriteManager.getSprite(piece);
                if (sprite != null) {
                    dragging = true;
                    draggedSprite = sprite;
                    dragSourceSquare = square;
                    dragPosition = e.getPoint();
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (dragging) {
            int squareSize = getWidth() / BOARD_SIZE;
            int targetFile = e.getX() / squareSize;
            int targetRank = e.getY() / squareSize;
            
            // Check if drop is within board bounds
            if (targetRank >= 0 && targetRank < BOARD_SIZE && 
                targetFile >= 0 && targetFile < BOARD_SIZE) {
                Point targetSquare = new Point(targetRank, targetFile);
                
                // Get the piece being moved
                Pieces piece = piecePositions.get(dragSourceSquare);
                
                // Remove sprite from source position
                piecePositions.remove(dragSourceSquare);
                
                // Place sprite at target position (will replace any piece there)
                piecePositions.put(targetSquare, piece);
            }
            // If dropped outside, piece stays at original position
            
            dragging = false;
            draggedSprite = null;
            dragPosition = null;
            dragSourceSquare = null;
            repaint();
        }
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
            dragPosition = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
