package model;

public enum Pieces {
    EMPTY(false, PieceType.EMPTY),

    WHITE_PAWN(true, PieceType.PAWN),
    WHITE_ROOK(true, PieceType.ROOK),
    WHITE_BISHOP(true, PieceType.BISHOP),
    WHITE_KING(true, PieceType.KING),
    WHITE_QUEEN(true, PieceType.QUEEN),
    WHITE_KNIGHT(true, PieceType.KNIGHT),

    BLACK_PAWN(false, PieceType.PAWN),
    BLACK_ROOK(false, PieceType.ROOK),
    BLACK_BISHOP(false, PieceType.BISHOP),
    BLACK_KING(false, PieceType.KING),
    BLACK_QUEEN(false, PieceType.QUEEN),
    BLACK_KNIGHT(false, PieceType.KNIGHT);

    private final boolean white;
    private final PieceType type;

    Pieces(boolean white, PieceType type) {
        this.white = white;
        this.type = type;
    }

    public boolean isWhite() {
        return !isEmpty() && white;
    }

    public boolean isBlack() {
        return !isEmpty() && !white;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public PieceType getType() {
        return type;
    }

    public enum PieceType {
        EMPTY,
        PAWN,
        ROOK,
        BISHOP,
        KNIGHT,
        QUEEN,
        KING
    }
}
