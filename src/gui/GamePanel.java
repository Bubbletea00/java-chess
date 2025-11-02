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
    private SpriteManager spriteManager;
    
    private static final int BOARD_SIZE = 8;
    private static final int PANEL_SIZE = 800;
    private static final int SQUARE_SIZE = PANEL_SIZE / BOARD_SIZE; // 100 pixels

    public GamePanel() {
        this.setBackground(Theme.BACKGROUND);
        this.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        
        // Create SpriteManager with correct square size
        this.spriteManager = new SpriteManager(80);
        
        // Remove layout manager - we'll draw everything manually
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
    
    private void drawCenteredSprite(Graphics2D g2d, Sprite sprite, int squareX, int squareY, int squareSize) {
        if (sprite == null) return;
        
        int spriteWidth = sprite.getWidth();
        int spriteHeight = sprite.getHeight();
        
        // Calculate centered position
        int x = squareX + (squareSize - spriteWidth) / 2;
        int y = squareY + (squareSize - spriteHeight) / 2;
        
        g2d.drawImage(sprite, x, y, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw the chessboard squares
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                boolean isLight = (rank + file) % 2 != 0;
                g2d.setColor(isLight ? Theme.LIGHT_SQUARE : Theme.DARK_SQUARE);
                g2d.fillRect(file * SQUARE_SIZE, rank * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        // Draw all sprites on their squares (except the one being dragged)
        for (Map.Entry<Point, Pieces> entry : piecePositions.entrySet()) {
            Point square = entry.getKey();
            Pieces piece = entry.getValue();
            
            // Skip drawing the sprite at its original position if it's being dragged
            if (dragging && dragSourceSquare != null && square.equals(dragSourceSquare)) {
                continue;
            }
            
            Sprite sprite = spriteManager.getSprite(piece);
            if (sprite != null) {
                int squareX = square.y * SQUARE_SIZE;
                int squareY = square.x * SQUARE_SIZE;
                drawCenteredSprite(g2d, sprite, squareX, squareY, SQUARE_SIZE);
            }
        }
        
        // Draw the dragged sprite centered at mouse position
        if (dragging && draggedSprite != null && dragPosition != null) {
            int x = dragPosition.x - draggedSprite.getWidth() / 2;
            int y = dragPosition.y - draggedSprite.getHeight() / 2;
            g2d.drawImage(draggedSprite, x, y, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int file = e.getX() / SQUARE_SIZE;
        int rank = e.getY() / SQUARE_SIZE;
        
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
            int targetFile = e.getX() / SQUARE_SIZE;
            int targetRank = e.getY() / SQUARE_SIZE;
            
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
