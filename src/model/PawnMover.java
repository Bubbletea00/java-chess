package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class PawnMover extends PieceMover {
    @Override
    List<Point> generateMoves(ChessGame game, Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();
        int rank = from.x;
        int file = from.y;

        int direction = piece.isWhite() ? -1 : 1;
        int startRank = piece.isWhite() ? 6 : 1;

        int newRank = rank + direction;
        if (game.isValidSquare(newRank, file) && game.getBoard().getPieceAt(newRank, file).isEmpty()) {
            moves.add(new Point(newRank, file));

            if (rank == startRank) {
                int doubleRank = rank + 2 * direction;
                if (game.getBoard().getPieceAt(doubleRank, file).isEmpty()) {
                    moves.add(new Point(doubleRank, file));
                }
            }
        }

        for (int fileOffset : new int[]{-1, 1}) {
            int captureFile = file + fileOffset;
            if (game.isValidSquare(newRank, captureFile)) {
                Pieces target = game.getBoard().getPieceAt(newRank, captureFile);
                if (!target.isEmpty() && game.isOpponentPiece(piece, target)) {
                    moves.add(new Point(newRank, captureFile));
                }
            }
        }

        if (game.getBoard().isEnPassant()) {
            int epRank = game.getBoard().getEnPassantRank();
            int epFile = game.getBoard().getEnPassantFile();
            if (epRank == newRank && Math.abs(epFile - file) == 1) {
                moves.add(new Point(epRank, epFile));
            }
        }

        return moves;
    }

    @Override
    List<Point> generateAttacks(ChessGame game, Point from, Pieces piece) {
        List<Point> attacks = new ArrayList<>();
        int direction = piece.isWhite() ? -1 : 1;
        int newRank = from.x + direction;

        for (int fileOffset : new int[]{-1, 1}) {
            int captureFile = from.y + fileOffset;
            if (game.isValidSquare(newRank, captureFile)) {
                attacks.add(new Point(newRank, captureFile));
            }
        }

        return attacks;
    }
}

