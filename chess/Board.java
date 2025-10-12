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

        //check for invalid 3rd arg (draw already accounted for in Chess.play)
        if(move.special != null){
            return false;
        }


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
        
        // Pawn Promotion move handling
        if (piece == ReturnPiece.PieceType.WP &&
            (toRow == 0) &&
            ((toCol == fromCol && grid[toRow][toCol] == null) ||
            (Math.abs(toCol - fromCol) == 1 && grid[toRow][toCol] != null &&
            grid[toRow][toCol].name().charAt(0) == 'B'))){

                if(move.promotion == 'N'){
                    piece = ReturnPiece.PieceType.WN;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
                else if (move.promotion == 'R'){
                    piece = ReturnPiece.PieceType.WR;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
                else if (move.promotion == 'B'){
                    piece = ReturnPiece.PieceType.WB;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
                else{
                    piece = ReturnPiece.PieceType.WQ;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
        }
        if (piece == ReturnPiece.PieceType.BP &&
            (toRow == 7) &&
            ((toCol == fromCol && grid[toRow][toCol] == null) ||
            (Math.abs(toCol - fromCol) == 1 && grid[toRow][toCol] != null &&
            grid[toRow][toCol].name().charAt(0) == 'W'))){
                if(move.promotion == 'N'){
                    piece = ReturnPiece.PieceType.BN;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
                else if (move.promotion == 'R'){
                    piece = ReturnPiece.PieceType.BR;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
                else if (move.promotion == 'B'){
                    piece = ReturnPiece.PieceType.BB;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
                else{
                    piece = ReturnPiece.PieceType.BQ;
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
        }

    
        // Basic pawn moves
        if (piece == ReturnPiece.PieceType.WP &&
            toCol == fromCol && toRow == fromRow - 1 && grid[toRow][toCol] == null) {
            grid[toRow][toCol] = piece;
            grid[fromRow][fromCol] = null;
            return true;
        }
        else if (piece == ReturnPiece.PieceType.WP &&
            toCol == fromCol && fromRow == 6 && toRow == fromRow - 2 && grid[fromRow - 1][fromCol] == null && grid[toRow][toCol] == null) {
            grid[toRow][toCol] = piece;
            grid[fromRow][fromCol] = null;
            return true;
        }
        else if (
            piece == ReturnPiece.PieceType.WP &&
            (toCol == fromCol + 1 || toCol == fromCol - 1) &&
            toRow == fromRow - 1 &&
            grid[toRow][toCol] != null &&
            grid[toRow][toCol].name().charAt(0) == 'B'
        ) {
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
        else if (piece == ReturnPiece.PieceType.BP &&
            toCol == fromCol && fromRow == 1 && toRow == fromRow + 2 && grid[fromRow + 1][fromCol] == null && grid[toRow][toCol] == null) {
            grid[toRow][toCol] = piece;
            grid[fromRow][fromCol] = null;
            return true;
        }
        else if (
            piece == ReturnPiece.PieceType.BP &&
            (toCol == fromCol + 1 || toCol == fromCol - 1) &&
            toRow == fromRow + 1 &&
            grid[toRow][toCol] != null &&
            grid[toRow][toCol].name().charAt(0) == 'W'
        ) {
            grid[toRow][toCol] = piece;
            grid[fromRow][fromCol] = null;
            return true;
        }

        //Rook moves
        if (piece == ReturnPiece.PieceType.WR || piece == ReturnPiece.PieceType.BR) {
            if (fromRow == toRow) {
                int step;

                if (toCol > fromCol) {
                    step = 1;
                } else {
                    step = -1;
                }

                for (int c = fromCol + step; c != toCol; c += step) {
                    if (grid[fromRow][c] != null) return false;
                }
                if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
            } else if (fromCol == toCol) {
                int step;
                if(toRow > fromRow){
                    step = 1;
                }
                else{
                    step = -1;
                }

                for (int r = fromRow + step; r != toRow; r += step) {
                    if (grid[r][fromCol] != null) return false;
                }
                if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
            }
        }

        // Bishop moves
        if (piece == ReturnPiece.PieceType.WB || piece == ReturnPiece.PieceType.BB) {
            int rn = toRow - fromRow;
            int cn = toCol - fromCol;


            if (Math.abs(rn) != Math.abs(cn)) {
                return false;
            }


            int rstep = (toRow > fromRow) ? 1 : -1;
            int cstep = (toCol > fromCol) ? 1 : -1;


            int r = fromRow + rstep;
            int c = fromCol + cstep;
            while (r != toRow) {
                if (grid[r][c] != null) return false;
                r += rstep;
                c += cstep;
            }



            if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                grid[toRow][toCol] = piece;
                grid[fromRow][fromCol] = null;
                return true;
            }
        }

        //Knight moves
        if (piece == ReturnPiece.PieceType.WN || piece == ReturnPiece.PieceType.BN) {
            int rn = toRow - fromRow;
            int cn = toCol - fromCol;


            if (!(Math.abs(rn) == 1 && Math.abs(cn) == 2 || Math.abs(rn) == 2 && Math.abs(cn) == 1)) {
                return false;
            }

            if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                grid[toRow][toCol] = piece;
                grid[fromRow][fromCol] = null;
                return true;
            }
        }

        //King moves
        if (piece == ReturnPiece.PieceType.WK || piece == ReturnPiece.PieceType.BK) {
            int rn = toRow - fromRow;
            int cn = toCol - fromCol;

            if (rn == 0 && cn == 0) return false;

            if (!((Math.abs(rn) == 0 || Math.abs(rn) == 1) && 
                (Math.abs(cn) == 0 || Math.abs(cn) == 1))) {
                return false;
            }


            if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                grid[toRow][toCol] = piece;
                grid[fromRow][fromCol] = null;
                return true;
            }
        }

        //Queen moves
        if (piece == ReturnPiece.PieceType.WQ || piece == ReturnPiece.PieceType.BQ) {
            int rn = toRow - fromRow;
            int cn = toCol - fromCol;

            if (fromRow == toRow) {
                int step;

                if (toCol > fromCol) {
                    step = 1;
                } else {
                    step = -1;
                }

                for (int c = fromCol + step; c != toCol; c += step) {
                    if (grid[fromRow][c] != null) return false;
                }
                if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
            } else if (fromCol == toCol) {
                int step;
                if(toRow > fromRow){
                    step = 1;
                }
                else{
                    step = -1;
                }

                for (int r = fromRow + step; r != toRow; r += step) {
                    if (grid[r][fromCol] != null) return false;
                }
                if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
            }
            else{
                if (Math.abs(rn) != Math.abs(cn)) {
                    return false;
                }
                int rstep = (toRow > fromRow) ? 1 : -1;
                int cstep = (toCol > fromCol) ? 1 : -1;


                int r = fromRow + rstep;
                int c = fromCol + cstep;
                while (r != toRow) {
                    if (grid[r][c] != null) return false;
                    r += rstep;
                    c += cstep;
                }



                if (grid[toRow][toCol] == null || (grid[toRow][toCol].name().charAt(0) == 'W') != isWhite) {
                    grid[toRow][toCol] = piece;
                    grid[fromRow][fromCol] = null;
                    return true;
                }
            }
     
        }

        return false;
      
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
