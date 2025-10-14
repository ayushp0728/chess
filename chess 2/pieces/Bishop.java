package chess.pieces;

import chess.Board;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) { super(isWhite, "Bishop"); }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int rDiff = toRow - fromRow;
        int cDiff = toCol - fromCol;

        if (Math.abs(rDiff) != Math.abs(cDiff)) return false;

        int rStep = (rDiff > 0) ? 1 : -1;
        int cStep = (cDiff > 0) ? 1 : -1;

        int r = fromRow + rStep;
        int c = fromCol + cStep;
        while (r != toRow) {
            if (board.getPieceAt(r, c) != null) return false;
            r += rStep; c += cStep;
        }

        Piece target = board.getPieceAt(toRow, toCol);
        return target == null || target.isWhite() != isWhite;
    }
}
