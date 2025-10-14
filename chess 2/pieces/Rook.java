package chess.pieces;

import chess.Board;

public class Rook extends Piece {
    public Rook(boolean isWhite) { super(isWhite, "Rook"); }

    public boolean hasMoved = false;

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) return false;

        if(fromRow==toRow&&fromCol==toCol) return false;

        int step = 0;
        if (fromRow == toRow) {
            step = (toCol > fromCol) ? 1 : -1;
            for (int c = fromCol + step; c != toCol; c += step)
                if (board.getPieceAt(fromRow, c) != null) return false;
        } else {
            step = (toRow > fromRow) ? 1 : -1;
            for (int r = fromRow + step; r != toRow; r += step)
                if (board.getPieceAt(r, fromCol) != null) return false;
        }

        Piece target = board.getPieceAt(toRow, toCol);
        return target == null || target.isWhite() != isWhite;

        

        
    }
}
