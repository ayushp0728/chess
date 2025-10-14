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
        if (move.special != null && !move.special.equals("draw?")) return false; // draw already handled

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
        boolean moved = (piece instanceof Pawn)
            ? ((Pawn) piece).move(this, fromRow, fromCol, toRow, toCol, move.promotion)
            : piece.move(this, fromRow, fromCol, toRow, toCol);
        if (!moved) return false;

        boolean opponentIsWhite = !whiteToMove;
        boolean oppInCheck = isInCheck(opponentIsWhite);
        boolean oppHasMove = hasAnyLegalMove(opponentIsWhite);

        if (oppInCheck && !oppHasMove) {
            gameOver = true; // checkmate
        } else if (!oppInCheck && !oppHasMove) {
            gameOver = true; // stalemate
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
        int kr = -1, kc = -1;
        outer:
        for (int r=0; r<8; r++) {
            for (int c=0; c<8; c++) {
                Piece p = grid[r][c];
                if (p instanceof King && p.isWhite()==whiteKing) { kr=r; kc=c; break outer; }
            }
        }
        if (kr == -1) return false; // shouldn't happen in normal play
        return isSquareUnderAttack(kr, kc, !whiteKing);
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
        // Knights
        int[][] KN = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        for (int[] d : KN) {
            int r = row + d[0], c = col + d[1];
            if (r>=0 && r<8 && c>=0 && c<8) {
                Piece p = getPieceAt(r,c);
                if (p != null && p.isWhite()==byWhite && p instanceof Knight) return true;
            }
        }

        // King (adjacent 8; ignore castling)
        for (int dr=-1; dr<=1; dr++) for (int dc=-1; dc<=1; dc++) {
            if (dr==0 && dc==0) continue;
            int r=row+dr, c=col+dc;
            if (r>=0 && r<8 && c>=0 && c<8) {
                Piece p = getPieceAt(r,c);
                if (p!=null && p.isWhite()==byWhite && p instanceof King) return true;
            }
        }

        // Pawns (diagonal only)
        int dir = byWhite ? -1 : 1;           // white attacks "up" (to lower row index)
        int pr = row - dir;                    // pawn would sit here to attack (row-wise)
        if (pr>=0 && pr<8) {
            for (int dc=-1; dc<=1; dc+=2) {
                int pc = col + dc;
                if (pc>=0 && pc<8) {
                    Piece p = getPieceAt(pr, pc);
                    if (p!=null && p.isWhite()==byWhite && p instanceof Pawn) return true;
                }
            }
        }

        // Rook/Queen orthogonal rays
        int[][] ORTH = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : ORTH) {
            int r=row+d[0], c=col+d[1];
            while (r>=0 && r<8 && c>=0 && c<8) {
                Piece p = getPieceAt(r,c);
                if (p!=null) {
                    if (p.isWhite()==byWhite && (p instanceof Rook || p instanceof Queen)) return true;
                    break; // blocked
                }
                r+=d[0]; c+=d[1];
            }
        }

        // Bishop/Queen diagonal rays
        int[][] DIAG = {{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] d : DIAG) {
            int r=row+d[0], c=col+d[1];
            while (r>=0 && r<8 && c>=0 && c<8) {
                Piece p = getPieceAt(r,c);
                if (p!=null) {
                    if (p.isWhite()==byWhite && (p instanceof Bishop || p instanceof Queen)) return true;
                    break; // blocked
                }
                r+=d[0]; c+=d[1];
            }
        }

        return false;
    }


    public boolean isGameOver() {
        return gameOver;
    }

    private boolean hasAnyLegalMove(boolean whiteSide) {
        for (int r=0; r<8; r++) for (int c=0; c<8; c++) {
            Piece p = grid[r][c];
            if (p == null || p.isWhite() != whiteSide) continue;

            for (int r2=0; r2<8; r2++) for (int c2=0; c2<8; c2++) {
                if (r==r2 && c==c2) continue;
                if (!p.canMove(this, r, c, r2, c2)) continue;

                Piece cap = grid[r2][c2];
                grid[r2][c2] = p; grid[r][c] = null;

                boolean leavesInCheck = isInCheck(whiteSide);

                grid[r][c] = p; grid[r2][c2] = cap;

                if (!leavesInCheck) return true;
            }
        }
        return false;
    }
}

