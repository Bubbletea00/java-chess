package model;

import java.awt.*;
import java.util.List;

public abstract class SlidingMover extends PieceMover {
    private final int[][] directions;

    protected SlidingMover(int[][] directions) {
        this.directions = directions;
    }

    @Override
    List<Point> generateMoves(ChessGame game, Point from, Pieces piece) {
        return game.getSlidingMoves(from, piece, directions);
    }
}

