package chess;

import java.util.ArrayList;

public class Board {
    private ReturnPiece.PieceType[][] grid = new ReturnPiece.PieceType[8][8];

    public void initialize() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                grid[r][c] = null;
            }
        }

        // Place pawns
        for (int c = 0; c < 8; c++) {
            grid[6][c] = ReturnPiece.PieceType.WP;
            grid[1][c] = ReturnPiece.PieceType.BP;
        }

        // Rooks
        grid[7][0] = ReturnPiece.PieceType.WR; grid[7][7] = ReturnPiece.PieceType.WR;
        grid[0][0] = ReturnPiece.PieceType.BR; grid[0][7] = ReturnPiece.PieceType.BR;

        // Knights
        grid[7][1] = ReturnPiece.PieceType.WN; grid[7][6] = ReturnPiece.PieceType.WN;
        grid[0][1] = ReturnPiece.PieceType.BN; grid[0][6] = ReturnPiece.PieceType.BN;

        // Bishops
        grid[7][2] = ReturnPiece.PieceType.WB; grid[7][5] = ReturnPiece.PieceType.WB;
        grid[0][2] = ReturnPiece.PieceType.BB; grid[0][5] = ReturnPiece.PieceType.BB;

        // Queens
        grid[7][3] = ReturnPiece.PieceType.WQ;
        grid[0][3] = ReturnPiece.PieceType.BQ;

        // Kings
        grid[7][4] = ReturnPiece.PieceType.WK;
        grid[0][4] = ReturnPiece.PieceType.BK;
    }

    public boolean makeMove(Move move, Chess.Player currentPlayer) {
        int fromCol = Character.toLowerCase(move.fromFile) - 'a'; // ensure lowercase
        int toCol = Character.toLowerCase(move.toFile) - 'a';

        int fromRow = 8 - move.fromRank;
        int toRow = 8 - move.toRank;

        // ✅ Bounds check
        if (fromRow < 0 || fromRow > 7 || toRow < 0 || toRow > 7 ||
            fromCol < 0 || fromCol > 7 || toCol < 0 || toCol > 7) {
            System.out.println("Move out of bounds: " + move);
            return false;
        }

        ReturnPiece.PieceType piece = grid[fromRow][fromCol];
        if (piece == null) return false;

        boolean isWhite = piece.name().charAt(0) == 'W';
        if ((isWhite && currentPlayer != Chess.Player.WHITE) ||
            (!isWhite && currentPlayer != Chess.Player.BLACK))
            return false;

        // Basic pawn moves
        if (piece == ReturnPiece.PieceType.WP &&
            toCol == fromCol && toRow == fromRow - 1 && grid[toRow][toCol] == null) {
            grid[toRow][toCol] = piece;
            grid[fromRow][fromCol] = null;
            return true;
        }

        if (piece == ReturnPiece.PieceType.BP &&
            toCol == fromCol && toRow == fromRow + 1 && grid[toRow][toCol] == null) {
            grid[toRow][toCol] = piece;
            grid[fromRow][fromCol] = null;
            return true;
        }

        return false;
        //comment
    }

    public ArrayList<ReturnPiece> getPieces() {
        ArrayList<ReturnPiece> list = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (grid[r][c] != null) {
                    ReturnPiece rp = new ReturnPiece();
                    rp.pieceType = grid[r][c];
                    rp.pieceFile = ReturnPiece.PieceFile.values()[c];
                    rp.pieceRank = 8 - r;
                    list.add(rp);
                }
            }
        }
        return list;
    }
}
