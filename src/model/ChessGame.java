package model;

import java.awt.Point;
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
     * Attempt to make a move. Returns true if move is legal and executed.
     * @param from Point where x=rank, y=file
     * @param to Point where x=rank, y=file
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
            
            // Remove the captured pawn (which is on the same file but different rank)
            int direction = (piece == Pieces.WHITE_PAWN) ? -1 : 1;
            int capturedPawnRank = toRank - direction;
            board.setPieceAt(capturedPawnRank, toFile, Pieces.EMPTY);
            System.out.println("En passant capture executed at [" + capturedPawnRank + "," + toFile + "]");
        }

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

            // Move forward two squares from starting position
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
            
            // The en passant target square should be diagonally forward from current pawn
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

        return moves;
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
