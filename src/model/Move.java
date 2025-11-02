package model;

import java.awt.Point;

public class Move {
    private final Point from;
    private final Point to;
    private final Pieces piece;
    private final Pieces capturedPiece;


    public Move(Point from, Point to, Pieces piece, Pieces capturedPiece){
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
    }

    public Point getFrom(){
        return from;
    }
    public Point getTo(){
        return to;
    }
    public Pieces getPiece(){
        return piece;
    }
    public Pieces getCapturedPiece(){return capturedPiece;}


    //TODO Implement equals method and hashCode in order to speed up legal moves checking
}
