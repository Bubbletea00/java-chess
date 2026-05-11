package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class KingMover extends PieceMover {
    private static final int[][] OFFSETS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    @Override
    List<Point> generateMoves(ChessGame game, Point from, Pieces piece) {
        List<Point> moves = generateAttacks(game, from, piece);
        moves.addAll(game.getCastlingMoves(piece));
        return moves;
    }

    @Override
    List<Point> generateAttacks(ChessGame game, Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();

        for (int[] offset : OFFSETS) {
            int newRank = from.x + offset[0];
            int newFile = from.y + offset[1];
            if (game.isValidSquare(newRank, newFile) && game.canMoveTo(piece, newRank, newFile)) {
                moves.add(new Point(newRank, newFile));
            }
        }

        return moves;
    }
}

