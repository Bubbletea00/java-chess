package model;

import java.util.Arrays;

public class Board {
    private Pieces[][] board = new Pieces[8][8];
    private boolean shortCastleWhite = false;
    private boolean longCastleWhite = false;
    private boolean shortCastleBlack = false;
    private boolean longCastleBlack = false;
    private boolean enPassant = false;
    private boolean isWhiteTurn = true;
    private int enPassantRank = 0;
    private int enPassantFile = 0;

    //getters and setters
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        isWhiteTurn = whiteTurn;
    }
    public boolean isShortCastleWhite() {
        return shortCastleWhite;
    }

    public void setShortCastleWhite(boolean shortCastleWhite) {
        this.shortCastleWhite = shortCastleWhite;
    }

    public boolean isLongCastleWhite() {
        return longCastleWhite;
    }

    public void setLongCastleWhite(boolean longCastleWhite) {
        this.longCastleWhite = longCastleWhite;
    }

    public boolean isShortCastleBlack() {
        return shortCastleBlack;
    }

    public void setShortCastleBlack(boolean shortCastleBlack) {
        this.shortCastleBlack = shortCastleBlack;
    }

    public boolean isLongCastleBlack() {
        return longCastleBlack;
    }

    public void setLongCastleBlack(boolean longCastleBlack) {
        this.longCastleBlack = longCastleBlack;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public int getEnPassantRank() {
        return enPassantRank;
    }

    public void setEnPassantRank(int enPassantRank) {
        this.enPassantRank = enPassantRank;
    }

    public int getEnPassantFile() {
        return enPassantFile;
    }

    public void setEnPassantFile(int enPassantFile) {
        this.enPassantFile = enPassantFile;
    }

    public Board(){
        setDefaultBoard();
    }

    public Pieces[][] getBoard(){
        return board;
    }

    public void setBoard(Pieces[][] board) {
        this.board = board;
    }

    public void setDefaultBoard(){
        clearBoard(board);

        board[0][0] = Pieces.BLACK_ROOK;
        board[0][1] = Pieces.BLACK_KNIGHT;
        board[0][2] = Pieces.BLACK_BISHOP;
        board[0][3] = Pieces.BLACK_QUEEN;
        board[0][4] = Pieces.BLACK_KING;
        board[0][5] = Pieces.BLACK_BISHOP;
        board[0][6] = Pieces.BLACK_KNIGHT;
        board[0][7] = Pieces.BLACK_ROOK;

        for (int i = 0; i < 8; i++) {
            board[1][i] = Pieces.BLACK_PAWN;
            board[6][i] = Pieces.WHITE_PAWN;
        }


        board[7][0] = Pieces.WHITE_ROOK;
        board[7][1] = Pieces.WHITE_KNIGHT;
        board[7][2] = Pieces.WHITE_BISHOP;
        board[7][3] = Pieces.WHITE_QUEEN;
        board[7][4] = Pieces.WHITE_KING;
        board[7][5] = Pieces.WHITE_BISHOP;
        board[7][6] = Pieces.WHITE_KNIGHT;
        board[7][7] = Pieces.WHITE_ROOK;
    }

    public void clearBoard(Pieces[][] board) {
        for (Pieces[] row : board) {
            Arrays.fill(row, Pieces.EMPTY);
        }
    }


    //TODO implement ChessCoordinate class
    public Pieces getPieceAt(int rank, int file){
        return board[rank][file];
    }

    public void movePiece(int fromRank, int fromFile, int toRank, int toFile) {
        board[toRank][toFile] = board[fromRank][fromFile];
        board[fromRank][fromFile] = Pieces.EMPTY;
    }


    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            stringBuilder.append('\n');
            for (int j = 0; j < 8; j++) {
                stringBuilder.append("[ ");
                stringBuilder.append(board[i][j]);
                stringBuilder.append(" ]");
            }
        }
        return stringBuilder.toString();
    }

}
