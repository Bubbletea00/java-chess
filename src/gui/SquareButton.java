package gui;

import model.Pieces;

import java.awt.*;

public class SquareButton extends javax.swing.JButton{
    private final boolean isLightSquare;
    private Pieces piece;
    private final int rank;
    private final int file;

    Color COLOR_DARK_SQUARE = new Color(181, 136, 99);
    Color COLOR_LIGHT_SQUARE = new Color(240, 217, 181);


    public SquareButton(int rank, int file){
        this.isLightSquare = (rank + file) % 2 != 0;
        this.piece = Pieces.EMPTY;
        this.rank = rank;
        this.file = file;

        Color color = isLightSquare ? COLOR_LIGHT_SQUARE : COLOR_DARK_SQUARE;
        super.setBackground(color);
        super.setOpaque(true);
        super.setBorderPainted(false);
        super.setFocusPainted(false);
    }

    public boolean isLightSquare(){
        return isLightSquare;
    }

    public Pieces getPiece(){
        return piece;
    }

    public void setPiece(Pieces piece){
        this.piece = piece;
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }
}
