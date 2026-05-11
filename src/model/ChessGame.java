package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private Board board;
    private boolean whiteToMove;

    public ChessGame() {
        this.board = new Board();
        whiteToMove = board.isWhiteTurn();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    /**
     * Get all legal moves for a piece at the given position
     *
     * @param from Point where x=rank, y=file
     */
    public List<Point> getLegalMoves(Point from) {
        List<Point> legalMoves = new ArrayList<>();
        int rank = from.x;
        int file = from.y;
        Pieces piece = board.getPieceAt(rank, file);

        if (piece.isEmpty()) {
            return legalMoves;
        }

        if (!isCorrectPlayersPiece(piece)) {
            return legalMoves;
        }

        PieceMover mover = getPieceMover(piece);
        if (mover != null) {
            legalMoves.addAll(mover.generateMoves(this, from, piece));
        }

        return legalMoves;
    }

    /**
     * Attempt to make a move. Returns true if the move is legal and executed.
     *
     * @param from Point where x=rank, y=file
     * @param to   Point where x=rank, y=file
     */
    public boolean makeMove(Point from, Point to) {
        List<Point> legalMoves = getLegalMoves(from);

        if (!legalMoves.contains(to)) {
            return false;
        }

        int fromRank = from.x;
        int fromFile = from.y;
        int toRank = to.x;
        int toFile = to.y;

        Pieces piece = board.getPieceAt(fromRank, fromFile);
        Pieces capturedPiece = board.getPieceAt(toRank, toFile);
        boolean isCastlingMove = isCastlingMove(piece, from, to);

        // Check if this is an en passant capture
        if ((piece == Pieces.WHITE_PAWN || piece == Pieces.BLACK_PAWN) &&
                board.isEnPassant() &&
                toRank == board.getEnPassantRank() &&
                toFile == board.getEnPassantFile()) {

            // Remove the captured pawn (which is in the same file but different rank)
            int direction = (piece == Pieces.WHITE_PAWN) ? -1 : 1;
            int capturedPawnRank = toRank - direction;
            board.setPieceAt(capturedPawnRank, toFile, Pieces.EMPTY);
            System.out.println("En passant capture executed at [" + capturedPawnRank + "," + toFile + "]");
        }

        // Execute the move
        board.setPieceAt(toRank, toFile, piece);
        board.setPieceAt(fromRank, fromFile, Pieces.EMPTY);

        // Move rook after king move when castling.
        if (isCastlingMove) {
            moveRookForCastling(piece, toFile);
        }

        // Reset en passant flag
        board.setEnPassant(false);
        board.setEnPassantFile(-1);
        board.setEnPassantRank(-1);

        updateCastlingRightsAfterMove(piece, fromRank, fromFile, toRank, toFile, capturedPiece);

        // Check for pawn double move to set en passant
        if (piece == Pieces.WHITE_PAWN || piece == Pieces.BLACK_PAWN) {
            int direction = (piece == Pieces.WHITE_PAWN) ? -1 : 1;

            if (Math.abs(toRank - fromRank) == 2) {

                for (int fileOffset : new int[]{-1, 1}) {
                    int adjacentFile = toFile + fileOffset;
                    if (adjacentFile >= 0 && adjacentFile < 8) {
                        Pieces adjacentPiece = board.getPieceAt(toRank, adjacentFile);
                        if (isOpponentPawn(piece, adjacentPiece)) {
                            int enPassantRank = fromRank + direction;
                            board.setEnPassant(true);
                            board.setEnPassantRank(enPassantRank);
                            board.setEnPassantFile(fromFile);
                            System.out.println("En passant available at [rank,file] = [" + enPassantRank + "," + fromFile + "]");
                            break;
                        }
                    }
                }
            }
        }

        // Switch turns
        whiteToMove = !whiteToMove;
        board.setWhiteTurn(whiteToMove);

        return true;
    }

    private boolean isCorrectPlayersPiece(Pieces piece) {
        return !piece.isEmpty() && piece.isWhite() == whiteToMove;
    }

    private PieceMover getPieceMover(Pieces piece) {
        return PieceMoverRegistry.getMover(piece);
    }

    private boolean isCastlingMove(Pieces piece, Point from, Point to) {
        return piece.getType() == Pieces.PieceType.KING
                && from.x == to.x
                && Math.abs(to.y - from.y) == 2;
    }

    private void moveRookForCastling(Pieces king, int kingToFile) {
        int rank = king.isWhite() ? 7 : 0;

        if (kingToFile == 6) {
            Pieces rook = board.getPieceAt(rank, 7);
            board.setPieceAt(rank, 5, rook);
            board.setPieceAt(rank, 7, Pieces.EMPTY);
            return;
        }

        if (kingToFile == 2) {
            Pieces rook = board.getPieceAt(rank, 0);
            board.setPieceAt(rank, 3, rook);
            board.setPieceAt(rank, 0, Pieces.EMPTY);
        }
    }

    private void updateCastlingRightsAfterMove(Pieces piece, int fromRank, int fromFile, int toRank, int toFile, Pieces capturedPiece) {
        if (piece == Pieces.WHITE_KING) {
            board.setLongCastleWhite(false);
            board.setShortCastleWhite(false);
        } else if (piece == Pieces.BLACK_KING) {
            board.setLongCastleBlack(false);
            board.setShortCastleBlack(false);
        }

        if (piece == Pieces.WHITE_ROOK && fromRank == 7) {
            if (fromFile == 0) board.setLongCastleWhite(false);
            if (fromFile == 7) board.setShortCastleWhite(false);
        } else if (piece == Pieces.BLACK_ROOK && fromRank == 0) {
            if (fromFile == 0) board.setLongCastleBlack(false);
            if (fromFile == 7) board.setShortCastleBlack(false);
        }

        // Capturing a corner rook removes castling rights for that side.
        if (capturedPiece == Pieces.WHITE_ROOK && toRank == 7) {
            if (toFile == 0) board.setLongCastleWhite(false);
            if (toFile == 7) board.setShortCastleWhite(false);
        } else if (capturedPiece == Pieces.BLACK_ROOK && toRank == 0) {
            if (toFile == 0) board.setLongCastleBlack(false);
            if (toFile == 7) board.setShortCastleBlack(false);
        }
    }

    List<Point> getCastlingMoves(Pieces king) {
        List<Point> moves = new ArrayList<>();
        int rank = king.isWhite() ? 7 : 0;

        if (canCastle(king, true)) {
            moves.add(new Point(rank, 6));
        }
        if (canCastle(king, false)) {
            moves.add(new Point(rank, 2));
        }

        return moves;
    }

    private boolean canCastle(Pieces king, boolean shortCastle) {
        if (king.getType() != Pieces.PieceType.KING) {
            return false;
        }

        boolean rights = king.isWhite()
                ? (shortCastle ? board.isShortCastleWhite() : board.isLongCastleWhite())
                : (shortCastle ? board.isShortCastleBlack() : board.isLongCastleBlack());
        if (!rights) {
            return false;
        }

        int rank = king.isWhite() ? 7 : 0;
        Pieces rook = board.getPieceAt(rank, shortCastle ? 7 : 0);
        Pieces expectedRook = king.isWhite() ? Pieces.WHITE_ROOK : Pieces.BLACK_ROOK;
        if (rook != expectedRook) {
            return false;
        }

        int[] emptyFiles = shortCastle ? new int[]{5, 6} : new int[]{1, 2, 3};
        int[] dangerFiles = shortCastle ? new int[]{4, 5, 6} : new int[]{4, 3, 2};

        return areEmptySquares(rank, emptyFiles) && areNonDangerSquares(rank, dangerFiles);
    }

    private boolean areEmptySquares(int rank, int[] files) {
        for (int file : files) {
            if (!board.getPieceAt(rank, file).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean areNonDangerSquares(int rank, int[] files) {
        for (int file : files) {
            if (!isNonDangerSquare(new Point(rank, file))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a square is not under attack by opponent pieces
     *
     * @param square Point where x=rank, y=file
     * @return true if the square is safe (not attacked by opponent)
     */
    private boolean isNonDangerSquare(Point square) {
        // Temporarily switch perspective to check opponent attacks
        boolean originalTurn = whiteToMove;
        whiteToMove = !whiteToMove;

        // Check all opponent pieces to see if any can attack this square
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Pieces piece = board.getPieceAt(rank, file);

                if (piece.isEmpty()) continue;
                if (!isCorrectPlayersPiece(piece)) continue; // Skip our own pieces

                Point from = new Point(rank, file);
                List<Point> moves = getPseudoLegalMoves(from, piece);

                if (moves.contains(square)) {
                    whiteToMove = originalTurn; // Restore original turn
                    return false; // Square is under attack
                }
            }
        }

        whiteToMove = originalTurn; // Restore original turn
        return true; // Square is safe
    }

    /**
     * Get pseudo-legal moves (doesn't check for checks/pins, just raw piece movement)
     * Used internally for attack detection
     */
    private List<Point> getPseudoLegalMoves(Point from, Pieces piece) {
        PieceMover mover = getPieceMover(piece);
        return mover == null ? new ArrayList<>() : mover.generateAttacks(this, from, piece);
    }


    List<Point> getSlidingMoves(Point from, Pieces piece, int[][] directions) {
        List<Point> moves = new ArrayList<>();

        for (int[] dir : directions) {
            int rank = from.x;
            int file = from.y;

            while (true) {
                rank += dir[0];
                file += dir[1];

                if (!isValidSquare(rank, file)) break;

                Pieces target = board.getPieceAt(rank, file);
                if (target.isEmpty()) {
                    moves.add(new Point(rank, file));
                } else {
                    if (isOpponentPiece(piece, target)) {
                        moves.add(new Point(rank, file));
                    }
                    break; // Can't move through pieces
                }
            }
        }

        return moves;
    }

    boolean canMoveTo(Pieces piece, int rank, int file) {
        Pieces target = board.getPieceAt(rank, file);
        return target.isEmpty() || isOpponentPiece(piece, target);
    }

    boolean isOpponentPiece(Pieces piece, Pieces target) {
        if (target.isEmpty() || piece.isEmpty()) return false;
        return piece.isWhite() != target.isWhite();
    }

    private boolean isOpponentPawn(Pieces piece, Pieces target) {
        return isOpponentPiece(piece, target) && (target == Pieces.WHITE_PAWN || target == Pieces.BLACK_PAWN);
    }

    boolean isValidSquare(int rank, int file) {
        return rank >= 0 && rank < 8 && file >= 0 && file < 8;
    }
}
