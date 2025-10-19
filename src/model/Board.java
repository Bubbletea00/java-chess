package model;

import java.util.Arrays;

public class Board {
    private Pieces[][] board = new Pieces[8][8];

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
