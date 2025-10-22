package util;

import model.Board;
import model.Pieces;

public class FenUtils extends Fen{

    private Board board;

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


    private void parseBoardToFen(){

    }

    private char getFenChar(Pieces piece){
        String name = piece.name().toLowerCase();
        if(name.equals("empty")) throw new IllegalArgumentException("Cannot get FEN char for empty piece, bonk");

        if(name.charAt(0) == 'w'){
            return Character.toUpperCase(name.charAt(6));
        }
        if(name.charAt(0) == 'b'){
            return name.charAt(6);
        }else throw new IllegalArgumentException("Invalid piece, what the fuck went wrong");
    }

    public void test(){
        System.out.println(getFenChar(Pieces.WHITE_PAWN));
        System.out.println(getFenChar(Pieces.BLACK_PAWN));
    }

}
