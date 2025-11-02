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
     */
    public List<Point> getLegalMoves(Point from) {
        List<Point> legalMoves = new ArrayList<>();
        Pieces piece = board.getPieceAt(from.x, from.y);
        
        if (piece == Pieces.EMPTY) {
            return legalMoves;
        }

        // Check if it's the correct player's turn
        if (!isCorrectPlayersPiece(piece)) {
            return legalMoves;
        }

        // Generate legal moves based on piece type
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
     */
    public boolean makeMove(Point from, Point to) {
        List<Point> legalMoves = getLegalMoves(from);
        
        if (!legalMoves.contains(to)) {
            return false; // Illegal move
        }

        // Execute the move
        Pieces piece = board.getPieceAt(from.x, from.y);
        board.setPieceAt(to.x, to.y, piece);
        board.setPieceAt(from.x, from.y, Pieces.EMPTY);

        // Update states
        if (board.isEnPassant()) {
            board.setEnPassant(false);
            board.setEnPassantFile(-1);
            board.setEnPassantRank(-1);
        }

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
                if (from.x == 0) board.setLongCastleWhite(false);
                if (from.x == 7) board.setShortCastleBlack(false);
                break;
            case WHITE_ROOK:
                if (from.x == 0) board.setLongCastleBlack(false);
                if (from.x == 7) board.setShortCastleWhite(false);
                break;
            case WHITE_PAWN:
            case BLACK_PAWN:
                for (int rankOffset : new int[]{-2, 2}) {
                    if (from.y + rankOffset == to.y) {
                        for (int fileOffset : new int[]{-1, 1}) {
                            if (isValidSquare(from.x + fileOffset, to.y)) {
                                if (isOpponentPiece(piece, board.getPieceAt(from.x + fileOffset, to.y))) {
                                    board.setEnPassant(true);
                                    board.setEnPassantFile(from.x);
                                    board.setEnPassantRank(to.y + (rankOffset == -2 ? 1 : -1));

                                    System.out.println("en passant [file,rank] = [" + board.getEnPassantFile() + "," + board.getEnPassantRank() + "]");
                                    //todo fix the en passant ...
                                }
                            }
                        }
                    }
                }
//                if (from.x - to.x == 2 || from.x - to.x == -2) {
//                    if (isOpponentPiece(piece, board.getPieceAt(to.x + 1, to.y))) {
//                        board.setEnPassant(true);
//                        board.setEnPassantFile(to.x + 1);
//                        board.setEnPassantRank(to.y);
//                        System.out.println("en passant [file,rank] = [" + board.getEnPassantFile() + "," + board.getEnPassantRank() + "]");
//                    }
//                    if (isOpponentPiece(piece, board.getPieceAt(to.x - 1, to.y))) {
//                        board.setEnPassant(true);
//                        board.setEnPassantFile(to.x - 1);
//                        board.setEnPassantRank(to.y);
//                        System.out.println("en passant [file,rank] = [" + board.getEnPassantFile() + "," + board.getEnPassantRank() + "]");
//
//                    }
//                }
                break;
            case null, default:
                break;
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
        boolean isWhite = piece == Pieces.WHITE_PAWN;
        int direction = isWhite ? -1 : 1; // White moves up (-1), Black moves down (+1)
        int startRank = isWhite ? 6 : 1;

        // Move forward one square
        int newRank = from.x + direction;
        if (isValidSquare(newRank, from.y) && board.getPieceAt(newRank, from.y) == Pieces.EMPTY) {
            moves.add(new Point(newRank, from.y));

            // Move forward two squares from starting position
            if (from.x == startRank) {
                int doubleRank = from.x + 2 * direction;
                if (board.getPieceAt(doubleRank, from.y) == Pieces.EMPTY) {
                    moves.add(new Point(doubleRank, from.y));
                }
            }
        }

        // Capture diagonally
        for (int fileOffset : new int[]{-1, 1}) {
            int captureFile = from.y + fileOffset;
            if (isValidSquare(newRank, captureFile)) {
                Pieces target = board.getPieceAt(newRank, captureFile);
                if (target != Pieces.EMPTY && isOpponentPiece(piece, target)) {
                    moves.add(new Point(newRank, captureFile));
                }
            }
        }


        //en passant
        if (board.isEnPassant()) {
            if(isValidSquare(board.getEnPassantRank(),board.getEnPassantFile())){
                moves.add(new Point(board.getEnPassantRank(), board.getEnPassantFile()));
            }
        }



        return moves;
    }

    private List<Point> getKnightMoves(Point from, Pieces piece) {
        List<Point> moves = new ArrayList<>();
        int[][] offsets = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, 
                          {1, -2}, {1, 2}, {2, -1}, {2, 1}};

        for (int[] offset : offsets) {
            int newRank = from.x + offset[0];
            int newFile = from.y + offset[1];
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

    private boolean isValidSquare(int rank, int file) {
        return rank >= 0 && rank < 8 && file >= 0 && file < 8;
    }
}
