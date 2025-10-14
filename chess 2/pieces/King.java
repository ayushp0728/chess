package chess.pieces;

import chess.Board;

public class King extends Piece {
    public King(boolean isWhite) { super(isWhite, "King"); }

    public boolean hasMoved = false;

    @Override
    public boolean canMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        // --- Normal king move: 1 square any direction, but NOT into check ---
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece target = board.getPieceAt(toRow, toCol);
            // can't capture your own piece
            if (target != null && target.isWhite() == this.isWhite()) return false;
            // don't step into an attacked square
            if (board.isSquareUnderAttack(toRow, toCol, !this.isWhite())) return false;
            return true;
        }

        // --- Castling detection ---
        if (rowDiff == 0 && colDiff == 2 && !hasMoved) {
            int rookCol = (toCol > fromCol) ? 7 : 0; // kingside or queenside
            Piece rook = board.getPieceAt(fromRow, rookCol);

            if (rook == null || !(rook instanceof Rook) || rook.isWhite() != this.isWhite()) return false;
            Rook castlingRook = (Rook) rook;
            if (castlingRook.hasMoved) return false;

            // squares between king and rook must be empty
            int step = (toCol > fromCol) ? 1 : -1;
            for (int c = fromCol + step; c != rookCol; c += step) {
                if (board.getPieceAt(fromRow, c) != null) return false;
            }

            // king may not castle out of/through/into check
            if (board.isSquareUnderAttack(fromRow, fromCol, !this.isWhite())) return false;
            if (board.isSquareUnderAttack(fromRow, fromCol + step, !this.isWhite())) return false;
            if (board.isSquareUnderAttack(fromRow, toCol, !this.isWhite())) return false;

            return true;
        }

        return false;
    }


    @Override
    public boolean move(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        boolean isCastling = (fromRow == toRow && Math.abs(toCol - fromCol) == 2);
        boolean moved = super.move(board, fromRow, fromCol, toRow, toCol);

        if (moved) {
            hasMoved = true;

            // Handle rook movement during castling
            if (isCastling) {
                int rookFromCol = (toCol > fromCol) ? 7 : 0;
                int rookToCol = (toCol > fromCol) ? 5 : 3;
                Piece rook = board.getPieceAt(fromRow, rookFromCol);
                if (rook instanceof Rook) {
                    board.setPieceAt(fromRow, rookFromCol, null);
                    board.setPieceAt(fromRow, rookToCol, rook);
                    ((Rook) rook).hasMoved = true;
                }
            }
        }

        return moved;
    }
}
