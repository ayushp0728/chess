package chess.pieces;

import chess.Board;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite, "Pawn");
    }

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int dir = isWhite ? -1 : 1;
        Piece target = board.getPieceAt(toRow, toCol);

        // Forward moves
        if (toCol == fromCol) {
            if (toRow == fromRow + dir && target == null) return true;
            if ((isWhite && fromRow == 6 || !isWhite && fromRow == 1) &&
                toRow == fromRow + 2 * dir &&
                board.getPieceAt(fromRow + dir, fromCol) == null &&
                target == null) return true;
        }

        // Capture
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + dir &&
            target != null && target.isWhite() != isWhite) return true;

        return false;
    }

    public boolean move(Board board, int fromRow, int fromCol, int toRow, int toCol, char promotion) {
        if (!canMove(board, fromRow, fromCol, toRow, toCol)) return false;

        board.setPieceAt(toRow, toCol, this);
        board.setPieceAt(fromRow, fromCol, null);

        // Promotion
        if ((isWhite && toRow == 0) || (!isWhite && toRow == 7)) {
            Piece promoted;
            switch (promotion) {
                case 'N': promoted = new Knight(isWhite); break;
                case 'B': promoted = new Bishop(isWhite); break;
                case 'R': promoted = new Rook(isWhite); break;
                default: promoted = new Queen(isWhite); break;
            }
            board.setPieceAt(toRow, toCol, promoted);
        }

        return true;
    }
}
