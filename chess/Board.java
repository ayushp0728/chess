package chess;

import java.util.ArrayList;
import chess.pieces.*;

public class Board {
    private Piece[][] grid = new Piece[8][8];

    public boolean gameOver = false; 

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
        int toCol   = Character.toLowerCase(move.toFile)   - 'a';
        int fromRow = 8 - move.fromRank;
        int toRow   = 8 - move.toRank;

        // Bounds
        if (fromRow < 0 || fromRow > 7 || toRow < 0 || toRow > 7 ||
            fromCol < 0 || fromCol > 7 || toCol < 0 || toCol > 7) return false;

        Piece piece = getPieceAt(fromRow, fromCol);
        if (piece == null) return false;

        // Side to move
        boolean whiteToMove = (currentPlayer == Chess.Player.WHITE);
        if (piece.isWhite() != whiteToMove) return false;

        // --- simulate to ensure you don't leave your own king in check ---
        Piece captured = getPieceAt(toRow, toCol);
        setPieceAt(toRow, toCol, piece);
        setPieceAt(fromRow, fromCol, null);

        boolean ownKingInCheck = isInCheck(whiteToMove);

        // undo simulation
        setPieceAt(fromRow, fromCol, piece);
        setPieceAt(toRow, toCol, captured);

        if (ownKingInCheck) return false; // illegal (leaves yourself in check)

        // --- commit the move for real ---
        boolean moved;
        if (piece instanceof Pawn) {
            moved = ((Pawn) piece).move(this, fromRow, fromCol, toRow, toCol, move.promotion);
        } else {
            moved = piece.move(this, fromRow, fromCol, toRow, toCol);
        }
        if (!moved) return false;

        // --- after the move: did the opponent get checkmated? ---
        boolean opponentIsWhite = !whiteToMove; // next to move’s king color
        if (isCheckmate(opponentIsWhite)) {
            gameOver = true;
        }
        return true;
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
    public boolean isInCheck(boolean whiteKing) {
        int kingRow = -1, kingCol = -1;

        // Find the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p instanceof chess.pieces.King && p.isWhite() == whiteKing) {
                    kingRow = r;
                    kingCol = c;
                }
            }
        }

        if (kingRow == -1) return false; // should never happen

        // See if any enemy piece can attack it
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.isWhite() != whiteKing) {
                    if (p.canMove(this, r, c, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(boolean whiteKing) {
        if (!isInCheck(whiteKing)) return false;

        // Try every legal move for the checked side
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.isWhite() == whiteKing) {
                    for (int r2 = 0; r2 < 8; r2++) {
                        for (int c2 = 0; c2 < 8; c2++) {
                            if (r == r2 && c == c2) continue;
                            if (!p.canMove(this, r, c, r2, c2)) continue;

                            // simulate the move
                            Piece cap = grid[r2][c2];
                            grid[r2][c2] = p; grid[r][c] = null;

                            boolean stillCheck = isInCheck(whiteKing);

                            // undo
                            grid[r][c] = p; grid[r2][c2] = cap;

                            if (!stillCheck) return false; // at least one escape
                        }
                    }
                }
            }
        }
        return true; // in check and no legal escapes
    }

    public boolean isSquareUnderAttack(int row, int col, boolean byWhite) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = getPieceAt(r, c);
                if (p == null || p.isWhite() != byWhite) continue;

                // Pawn attacks: diagonals only
                if (p instanceof Pawn) {
                    int dir = byWhite ? -1 : 1; // white pawns go up (toward lower row index)
                    int attackRow = r + dir;
                    if (attackRow == row && (c + 1 == col || c - 1 == col)) return true;
                    continue; // don't use canMove() for pawns here
                }

                // Other pieces: canMove is OK as an attack test
                if (p.canMove(this, r, c, row, col)) return true;
            }
        }
        return false;
    }
    public boolean isGameOver() {
        return gameOver;
    }
}