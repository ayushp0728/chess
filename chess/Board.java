package chess;

import java.util.ArrayList;
import chess.pieces.*;

public class Board {
    private Piece[][] grid = new Piece[8][8];

    public void initialize() {
        // Clear the board
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                grid[r][c] = null;

        // Pawns
        for (int c = 0; c < 8; c++) {
            grid[6][c] = new Pawn(true);
            grid[1][c] = new Pawn(false);
        }

        // Rooks
        grid[7][0] = new Rook(true); grid[7][7] = new Rook(true);
        grid[0][0] = new Rook(false); grid[0][7] = new Rook(false);

        // Knights
        grid[7][1] = new Knight(true); grid[7][6] = new Knight(true);
        grid[0][1] = new Knight(false); grid[0][6] = new Knight(false);

        // Bishops
        grid[7][2] = new Bishop(true); grid[7][5] = new Bishop(true);
        grid[0][2] = new Bishop(false); grid[0][5] = new Bishop(false);

        // Queens
        grid[7][3] = new Queen(true); grid[0][3] = new Queen(false);

        // Kings
        grid[7][4] = new King(true); grid[0][4] = new King(false);
    }

    public boolean makeMove(Move move, Chess.Player currentPlayer) {
        if (move.special != null) return false; // draw already handled

        int fromCol = Character.toLowerCase(move.fromFile) - 'a';
        int toCol = Character.toLowerCase(move.toFile) - 'a';
        int fromRow = 8 - move.fromRank;
        int toRow = 8 - move.toRank;

        // Bounds check
        if (fromRow < 0 || fromRow > 7 || toRow < 0 || toRow > 7 ||
            fromCol < 0 || fromCol > 7 || toCol < 0 || toCol > 7)
            return false;

        Piece piece = getPieceAt(fromRow, fromCol);
        if (piece == null) return false;

        // Ensure correct player
        if ((piece.isWhite() && currentPlayer != Chess.Player.WHITE) ||
            (!piece.isWhite() && currentPlayer != Chess.Player.BLACK))
            return false;

        // Move piece, pass promotion choice if pawn
        boolean moved;
        if (piece instanceof Pawn) {
            moved = ((Pawn) piece).move(this, fromRow, fromCol, toRow, toCol, move.promotion);
        } else {
            moved = piece.move(this, fromRow, fromCol, toRow, toCol);
        }

        return moved;
    }

    public Piece getPieceAt(int row, int col) {
        return grid[row][col];
    }

    public void setPieceAt(int row, int col, Piece piece) {
        grid[row][col] = piece;
    }

    public ArrayList<ReturnPiece> getPieces() {
        ArrayList<ReturnPiece> list = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null) {
                    ReturnPiece rp = new ReturnPiece();
                    rp.pieceType = mapPieceToReturnPiece(p);
                    rp.pieceFile = ReturnPiece.PieceFile.values()[c];
                    rp.pieceRank = 8 - r;
                    list.add(rp);
                }
            }
        }
        return list;
    }

    private ReturnPiece.PieceType mapPieceToReturnPiece(Piece piece) {
        if (piece == null) return null;

        boolean isWhite = piece.isWhite();
        switch (piece.getType()) {
            case "Pawn":   return isWhite ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP;
            case "Rook":   return isWhite ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
            case "Knight": return isWhite ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
            case "Bishop": return isWhite ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
            case "Queen":  return isWhite ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
            case "King":   return isWhite ? ReturnPiece.PieceType.WK : ReturnPiece.PieceType.BK;
            default:       return null;
        }
    }

}
