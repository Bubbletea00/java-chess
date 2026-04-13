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

        if (piece == Pieces.EMPTY) {
            return legalMoves;
        }

        if (!isCorrectPlayersPiece(piece)) {
            return legalMoves;
        }


        switch (piece) {
            case WHITE_PAWN:
            case BLACK_PAWN:
                legalMoves.addAll(getPawnMoves(from, piece));
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                legalMoves.addAll(getKnightMoves(from, piece));
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                legalMoves.addAll(getBishopMoves(from, piece));
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                legalMoves.addAll(getRookMoves(from, piece));
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                legalMoves.addAll(getQueenMoves(from, piece));
                break;
            case WHITE_KING:
            case BLACK_KING:
                legalMoves.addAll(getKingMoves(from, piece));
                break;
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

        // Check if this is a castling move
//        if (piece == Pieces.WHITE_KING || piece == Pieces.BLACK_KING) {
//            if (Math.abs(toFile - fromFile) == 2 || Math.abs(toFile - fromFile)==) {
//                //todo
//            }
//        }

        // Execute the move
        board.setPieceAt(toRank, toFile, piece);
        board.setPieceAt(fromRank, fromFile, Pieces.EMPTY);

        // Reset en passant flag
        board.setEnPassant(false);
        board.setEnPassantFile(-1);
        board.setEnPassantRank(-1);

        // Update castling rights
        switch (piece) {
            case BLACK_KING:
                board.setLongCastleBlack(false);
                board.setShortCastleBlack(false);
                break;
            case WHITE_KING:
                board.setLongCastleWhite(false);
                board.setShortCastleWhite(false);
                break;
            case BLACK_ROOK:
                if (fromFile == 0) board.setLongCastleBlack(false);
                if (fromFile == 7) board.setShortCastleBlack(false);
                break;
            case WHITE_ROOK:
                if (fromFile == 0) board.setLongCastleWhite(false);
                if (fromFile == 7) board.setShortCastleWhite(false);
                break;
        }

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
        boolean isWhitePiece = piece.name().startsWith("WHITE");
        return isWhitePiece == whiteToMove;
    }

    private List<Point> getPawnMoves(Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();
        int rank = from.x;
        int file = from.y;

        boolean isWhite = piece == Pieces.WHITE_PAWN;
        int direction = isWhite ? -1 : 1; // White moves up (rank decreases), Black moves down (rank increases)
        int startRank = isWhite ? 6 : 1;

        // Move forward one square
        int newRank = rank + direction;
        if (isValidSquare(newRank, file) && board.getPieceAt(newRank, file) == Pieces.EMPTY) {
            moves.add(new Point(newRank, file));

            // Move forward two squares from the starting position
            if (rank == startRank) {
                int doubleRank = rank + 2 * direction;
                if (board.getPieceAt(doubleRank, file) == Pieces.EMPTY) {
                    moves.add(new Point(doubleRank, file));
                }
            }
        }

        // Capture diagonally
        for (int fileOffset : new int[]{-1, 1}) {
            int captureFile = file + fileOffset;
            if (isValidSquare(newRank, captureFile)) {
                Pieces target = board.getPieceAt(newRank, captureFile);
                if (target != Pieces.EMPTY && isOpponentPiece(piece, target)) {
                    moves.add(new Point(newRank, captureFile));
                }
            }
        }

        // En passant capture
        if (board.isEnPassant()) {
            int epRank = board.getEnPassantRank();
            int epFile = board.getEnPassantFile();

            // The en passant target square should be diagonally forward from the current pawn
            if (epRank == newRank && Math.abs(epFile - file) == 1) {
                moves.add(new Point(epRank, epFile));
            }
        }

        return moves;
    }

    private List<Point> getKnightMoves(Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();
        int rank = from.x;
        int file = from.y;

        int[][] offsets = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] offset : offsets) {
            int newRank = rank + offset[0];
            int newFile = file + offset[1];
            if (isValidSquare(newRank, newFile) && canMoveTo(piece, newRank, newFile)) {
                moves.add(new Point(newRank, newFile));
            }
        }

        return moves;
    }

    private List<Point> getBishopMoves(Point from, Pieces piece) {
        return getSlidingMoves(from, piece, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
    }

    private List<Point> getRookMoves(Point from, Pieces piece) {
        return getSlidingMoves(from, piece, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
    }

    private List<Point> getQueenMoves(Point from, Pieces piece) {
        return getSlidingMoves(from, piece, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
    }

    private List<Point> getKingMoves(Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();
        int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] offset : offsets) {
            int newRank = from.x + offset[0];
            int newFile = from.y + offset[1];
            if (isValidSquare(newRank, newFile) && canMoveTo(piece, newRank, newFile)) {
                moves.add(new Point(newRank, newFile));
            }
        }
        //System.out.println("Castle rights: " + board.isLongCastleWhite() + ", " + board.isLongCastleBlack() + ", " + board.isShortCastleWhite() + ", " + board.isShortCastleBlack());
        if (isCastleLegal(piece)) moves.addAll(getCastlingMoves(piece));

        return moves;
    }

    private List<Point> getCastlingMoves(Pieces piece) {


        List<Point> moves = new ArrayList<>();

        switch (piece) {
            case WHITE_KING:
                if (board.isLongCastleWhite()) {
                    moves.add(new Point(7, 0));
                    moves.add(new Point(7, 2));
                }
                if (board.isShortCastleWhite()) {
                    moves.add(new Point(7, 7));
                    moves.add(new Point(7, 6));
                }
                break;
            case BLACK_KING:
                if (board.isLongCastleBlack()) {
                    moves.add(new Point(0, 0));
                    moves.add(new Point(0, 2));
                }
                if (board.isShortCastleBlack()) {
                    moves.add(new Point(0, 7));
                    moves.add(new Point(0, 6));
                }
                break;
        }

        return moves;
    }

    private boolean isCastleLegal(Pieces king) {
        boolean isWhiteKing = king.isWhite();
        boolean isShortCastle = isWhiteKing ? board.isShortCastleWhite() : board.isShortCastleBlack();

        int kingRank = isWhiteKing ? 7 : 0;
        int kingFile = 4;

        List<Point> dangerSquares = new ArrayList<>();
        List<Point> emptySquares = new ArrayList<>();
        dangerSquares.add(new Point(kingRank, kingFile));

        if (isShortCastle) {
            dangerSquares.add(new Point(kingRank, kingFile + 1));
            dangerSquares.add(new Point(kingRank, kingFile + 2));

            emptySquares.add(new Point(kingRank, kingFile + 1));
            emptySquares.add(new Point(kingRank, kingFile + 2));
        } else {
            dangerSquares.add(new Point(kingRank, kingFile - 1));
            dangerSquares.add(new Point(kingRank, kingFile - 2));

            emptySquares.add(new Point(kingRank, kingFile - 1));
            emptySquares.add(new Point(kingRank, kingFile - 2));
            emptySquares.add(new Point(kingRank, kingFile - 3));
        }

        return areNonDangerSquares(dangerSquares) && areEmptySquares(emptySquares);
    }

    private boolean isEmptySquare(Point square) {
        return board.getPieceAt(square.x, square.y) == Pieces.EMPTY;
    }

    private boolean areEmptySquares(List<Point> squares) {
        for (Point square : squares) {
            if (!isEmptySquare(square)) return false;
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

                if (piece == Pieces.EMPTY) continue;
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
        return switch (piece) {
            case WHITE_PAWN, BLACK_PAWN -> getPawnAttacks(from, piece); // Special method for pawn attacks only
            case WHITE_KNIGHT, BLACK_KNIGHT -> getKnightMoves(from, piece);
            case WHITE_BISHOP, BLACK_BISHOP -> getBishopMoves(from, piece);
            case WHITE_ROOK, BLACK_ROOK -> getRookMoves(from, piece);
            case WHITE_QUEEN, BLACK_QUEEN -> getQueenMoves(from, piece);
            case WHITE_KING, BLACK_KING -> getKingAttacks(from, piece); // Without castling
            default -> new ArrayList<>();
        };
    }

    /**
     * Get only pawn attack squares (not forward moves)
     */
    private List<Point> getPawnAttacks(Point from, Pieces piece) {
        List<Point> attacks = new ArrayList<>();
        int rank = from.x;
        int file = from.y;

        boolean isWhite = piece == Pieces.WHITE_PAWN;
        int direction = isWhite ? -1 : 1;
        int newRank = rank + direction;

        // Pawns can only attack diagonally
        for (int fileOffset : new int[]{-1, 1}) {
            int captureFile = file + fileOffset;
            if (isValidSquare(newRank, captureFile)) {
                attacks.add(new Point(newRank, captureFile));
            }
        }

        return attacks;
    }

    /**
     * Get king attacks without castling
     */
    private List<Point> getKingAttacks(Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();
        int rank = from.x;
        int file = from.y;

        int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] offset : offsets) {
            int newRank = rank + offset[0];
            int newFile = file + offset[1];
            if (isValidSquare(newRank, newFile) && canMoveTo(piece, newRank, newFile)) {
                moves.add(new Point(newRank, newFile));
            }
        }

        return moves; // No castling in attack detection
    }

    private boolean areNonDangerSquares(List<Point> squares) {
        for (Point square : squares) {
            if (!isNonDangerSquare(square)) return false;
        }
        return true;
    }


    private List<Point> getSlidingMoves(Point from, Pieces piece, int[][] directions) {
        List<Point> moves = new ArrayList<>();

        for (int[] dir : directions) {
            int rank = from.x;
            int file = from.y;

            while (true) {
                rank += dir[0];
                file += dir[1];

                if (!isValidSquare(rank, file)) break;

                Pieces target = board.getPieceAt(rank, file);
                if (target == Pieces.EMPTY) {
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

    private boolean canMoveTo(Pieces piece, int rank, int file) {
        Pieces target = board.getPieceAt(rank, file);
        return target == Pieces.EMPTY || isOpponentPiece(piece, target);
    }

    private boolean isOpponentPiece(Pieces piece, Pieces target) {
        boolean pieceIsWhite = piece.name().startsWith("WHITE");
        boolean targetIsWhite = target.name().startsWith("WHITE");
        if (target == Pieces.EMPTY || piece == Pieces.EMPTY) return false;
        return pieceIsWhite != targetIsWhite;
    }

    private boolean isOpponentPawn(Pieces piece, Pieces target) {
        return isOpponentPiece(piece, target) && (target == Pieces.WHITE_PAWN || target == Pieces.BLACK_PAWN);
    }

    private boolean isValidSquare(int rank, int file) {
        return rank >= 0 && rank < 8 && file >= 0 && file < 8;
    }
}
