package model;

import java.awt.*;
import java.util.List;

public abstract class PieceMover {
    abstract List<Point> generateMoves(ChessGame game, Point from, Pieces piece);

    List<Point> generateAttacks(ChessGame game, Point from, Pieces piece) {
        return generateMoves(game, from, piece);
    }
}

