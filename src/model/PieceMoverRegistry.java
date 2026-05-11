package model;

import java.util.EnumMap;
import java.util.Map;

public final class PieceMoverRegistry {
    private static final Map<Pieces.PieceType, PieceMover> MOVERS = new EnumMap<>(Pieces.PieceType.class);

    static {
        MOVERS.put(Pieces.PieceType.PAWN, new PawnMover());
        MOVERS.put(Pieces.PieceType.KNIGHT, new KnightMover());
        MOVERS.put(Pieces.PieceType.BISHOP, new BishopMover());
        MOVERS.put(Pieces.PieceType.ROOK, new RookMover());
        MOVERS.put(Pieces.PieceType.QUEEN, new QueenMover());
        MOVERS.put(Pieces.PieceType.KING, new KingMover());
    }

    private PieceMoverRegistry() {
    }

    public static PieceMover getMover(Pieces piece) {
        return MOVERS.get(piece.getType());
    }
}

