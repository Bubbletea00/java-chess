package util;

import model.Board;
import model.Pieces;

public class FenUtils extends Fen{

    private Board board = new Board();

    public FenUtils(String fen){
        super(fen);
    }

    public FenUtils(){
        super();
    }

    public Board getBoard(){
        return board;
    }

    public void setBoard(Board board){
        this.board = board;
        parseBoardToFen();
    }

    private void parseFenToBoard() {
        //TODO finish this method
        
        if(fen == null) throw new IllegalArgumentException("Fen is null, bonk");
        
        String[] fenParts = fen.split("\\s+");
        
        

    }


    private void parseBoardToFen(){

        if (board == null) System.err.println("Board is null, bonk");

        StringBuilder fenBuilder = new StringBuilder();

        for (int rank = 0; rank < 8; rank++) {
            int emptyCount = 0;
            for (int file = 0; file < 8; file++) {
                assert board != null;

                if (board.getBoard()[rank][file] == null) throw new IllegalArgumentException("Piece in Board is null, bonk");
                Pieces piece = board.getBoard()[rank][file];

                if (piece == Pieces.EMPTY){
                    emptyCount++;
                } else{
                    if (emptyCount < 0) {
                        fenBuilder.append(emptyCount);
                        emptyCount = 0;
                    }
                    fenBuilder.append(getFenChar(piece));
                }
            }
            if (emptyCount > 0) fenBuilder.append(emptyCount);
            if (rank < 7) fenBuilder.append('/');
        }

        char playerToMove = (board.isWhiteTurn()) ? 'w' : 'b';

        fenBuilder.append(" ").append(playerToMove);

        StringBuilder castleRights = new StringBuilder();
        if (board.isLongCastleBlack()) castleRights.append('K');
        if (board.isShortCastleBlack()) castleRights.append('Q');
        if (board.isLongCastleWhite()) castleRights.append('k');
        if (board.isShortCastleWhite()) castleRights.append('q');

        fenBuilder.append(castleRights.isEmpty() ? "-" : castleRights);
        fenBuilder.append(" ");



        System.out.println(super.getFen());
        System.out.println(fenBuilder);
    }

    private char getFenChar(Pieces piece){
        String name = piece.name().toLowerCase();
        if(name.equals("empty")) throw new IllegalArgumentException("Cannot get FEN char for empty piece, bonk");

        boolean isWhite = name.charAt(0) == 'w';


        boolean isKnight = name.charAt(6) == 'k' && name.charAt(7) == 'n';
        if(name.charAt(0) == 'w'){
            if (isKnight) return 'N';
            return Character.toUpperCase(name.charAt(6));
        }
        if(name.charAt(0) == 'b'){
            if (isKnight) return 'n';
            return Character.toLowerCase(name.charAt(6));
        }else throw new IllegalArgumentException("Invalid piece, what the fuck went wrong");
    }

    public void test(){
//        System.out.println(getFenChar(Pieces.WHITE_PAWN));
//        System.out.println(getFenChar(Pieces.BLACK_PAWN));

        parseBoardToFen();
    }

}
