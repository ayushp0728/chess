package chess.pieces;

import chess.Board;

public class Queen extends Piece {
    public Queen(boolean isWhite) { super(isWhite, "Queen"); }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int rDiff = toRow - fromRow;
        int cDiff = toCol - fromCol;


        

        // Horizontal or vertical
        if (fromRow == toRow || fromCol == toCol) {
            int rStep = Integer.compare(toRow, fromRow);
            int cStep = Integer.compare(toCol, fromCol);

            int r = fromRow + rStep;
            int c = fromCol + cStep;
            while (r != toRow || c != toCol) {
                if (board.getPieceAt(r, c) != null) return false;
                r += rStep; c += cStep;
            }

            Piece target = board.getPieceAt(toRow, toCol);
            return target == null || target.isWhite() != isWhite;
        }

        // Diagonal
        if (Math.abs(rDiff) == Math.abs(cDiff)) {
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

        return false;
    }
}
