package gui;

import model.Board;
import model.ChessGame;
import model.Pieces;
import util.SpriteManager;
import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    private boolean dragging = false;
    private Sprite draggedSprite = null;
    private Point dragPosition = null;
    private Point dragSourceSquare = null;
    private List<Point> legalMoves = null;

    private ChessGame game;
    private SpriteManager spriteManager;
    
    private static final int BOARD_SIZE = 8;
    private static final int PANEL_SIZE = 800;
    private static final int SQUARE_SIZE = PANEL_SIZE / BOARD_SIZE;
    
    private static final Color LEGAL_MOVE_HIGHLIGHT = new Color(124, 252, 0, 100);

    public GamePanel() {
        this.setBackground(Theme.BACKGROUND);
        this.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        
        this.spriteManager = new SpriteManager(80);
        this.game = new ChessGame();
        
        this.setLayout(null);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Sprite getSprite(java.awt.Point point) {
        Pieces piece = game.getBoard().getPieceAt(point.x, point.y);
        return piece != Pieces.EMPTY ? spriteManager.getSprite(piece) : null;
    }
    
    private void drawCenteredSprite(Graphics2D g2d, Sprite sprite, int squareX, int squareY, int squareSize) {
        if (sprite == null) return;
        
        int spriteWidth = sprite.getWidth();
        int spriteHeight = sprite.getHeight();
        
        int x = squareX + (squareSize - spriteWidth) / 2;
        int y = squareY + (squareSize - spriteHeight) / 2;
        
        g2d.drawImage(sprite, x, y, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        Board board = game.getBoard();
        
        // Draw the chessboard squares
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                boolean isLight = (rank + file) % 2 != 0;
                g2d.setColor(isLight ? Theme.LIGHT_SQUARE : Theme.DARK_SQUARE);
                g2d.fillRect(file * SQUARE_SIZE, rank * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        // Highlight legal move squares
        if (dragging && legalMoves != null) {
            g2d.setColor(LEGAL_MOVE_HIGHLIGHT);
            for (Point move : legalMoves) {
                int x = move.y * SQUARE_SIZE;
                int y = move.x * SQUARE_SIZE;
                g2d.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
        
        // Draw all sprites on their squares (except the one being dragged)
        for (int rank = 0; rank < BOARD_SIZE; rank++) {
            for (int file = 0; file < BOARD_SIZE; file++) {
                Point square = new Point(rank, file);
                
                // Skip drawing the sprite at its original position if it's being dragged
                if (dragging && square.equals(dragSourceSquare)) {
                    continue;
                }
                
                Pieces piece = board.getPieceAt(rank, file);
                if (piece != Pieces.EMPTY) {
                    Sprite sprite = spriteManager.getSprite(piece);
                    if (sprite != null) {
                        int squareX = file * SQUARE_SIZE;
                        int squareY = rank * SQUARE_SIZE;
                        drawCenteredSprite(g2d, sprite, squareX, squareY, SQUARE_SIZE);
                    }
                }
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
            Pieces piece = game.getBoard().getPieceAt(rank, file);
            
            if (piece != Pieces.EMPTY) {
                // Get legal moves for this piece
                legalMoves = game.getLegalMoves(square);
                
                // Only allow dragging if there are legal moves
                if (!legalMoves.isEmpty()) {
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
                
                // Try to make the move - only succeeds if legal
                boolean moveSuccess = game.makeMove(dragSourceSquare, targetSquare);
                
                if (!moveSuccess) {
                    // Illegal move - piece snaps back (do nothing)
                    System.out.println("Illegal move!");
                }
            }
            
            dragging = false;
            draggedSprite = null;
            dragPosition = null;
            dragSourceSquare = null;
            legalMoves = null;
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
