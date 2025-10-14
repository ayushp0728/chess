package chess.pieces;

import chess.Board;

public class Knight extends Piece {
    public Knight(boolean isWhite) { super(isWhite, "Knight"); }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int rDiff = Math.abs(toRow - fromRow);
        int cDiff = Math.abs(toCol - fromCol);

        if (!((rDiff == 2 && cDiff == 1) || (rDiff == 1 && cDiff == 2))) return false;

        Piece target = board.getPieceAt(toRow, toCol);
        return target == null || target.isWhite() != isWhite;
    }
}
