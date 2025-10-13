package chess.pieces;

import chess.Board;

public class King extends Piece {
    public King(boolean isWhite) { super(isWhite, "King"); }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int rDiff = toRow - fromRow;
        int cDiff = toCol - fromCol;

        if (rDiff == 0 && cDiff == 0) return false;
        if (!(Math.abs(rDiff) <= 1 && Math.abs(cDiff) <= 1)) return false;

        Piece target = board.getPieceAt(toRow, toCol);
        return target == null || target.isWhite() != isWhite;
    }
}
