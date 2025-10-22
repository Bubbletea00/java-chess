package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fen {
    protected String fen;

    public Fen(){
        this.fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    }

    public Fen(String fen){
        checkFenValidity(fen);
        this.fen = fen;
    }

    private boolean getFenValidity(String fen){
        if(fen == null) return false;
        Pattern pattern = Pattern.compile("^(?:[pnbrqkPNBRQK1-8]+/){7}[pnbrqkPNBRQK1-8]+\\s[bw]\\s(-|K?Q?k?q?)\\s(-|[a-h][36])\\s\\d+\\s\\d+$");
        Matcher matcher = pattern.matcher(fen);
        return matcher.matches();
    }

    private void checkFenValidity(String fen) throws IllegalArgumentException{
        if(!getFenValidity(fen)){
            throw new IllegalArgumentException("Invalid FEN, bonk");
        }
    }









    public String getFen(){
        return fen;
    }

    public void setFen(String fen){
        try {checkFenValidity(fen);}
        catch (IllegalArgumentException e){
            System.err.println("Invalid FEN, bonk");
            return;
        }

        this.fen = fen;
    }



    @Override
    public String toString(){
        return fen;
    }
}
