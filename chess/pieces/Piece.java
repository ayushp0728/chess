package chess.pieces;

import chess.*;

public abstract class Piece {
    //fields
    public boolean isWhite;
    public String type;

    //constructor
    public Piece(boolean isWhite, String type) {
        this.isWhite = isWhite;
        this.type = type;
    }

    //getter methods
    public boolean isWhite(){ 
        return isWhite; 
    }
    public String getType() {
        return type;
    }

    //abstract method, gets implemented for each subclass (each piece)
    public abstract boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol);

    //move method, actually moves the piece from one position to another
    public boolean move(Board board, int fromRow, int fromCol, int toRow, int toCol) {
    if(fromRow==toRow&&fromCol==toCol) return false;

    if (canMove(board, fromRow, fromCol, toRow, toCol)) {
            board.setPieceAt(toRow, toCol, this);
            board.setPieceAt(fromRow, fromCol, null);
            return true;
        }
        return false;
    }


    
}


