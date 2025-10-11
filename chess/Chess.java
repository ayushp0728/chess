package chess;

import java.util.ArrayList;

public class Chess {

        enum Player { white, black }
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		System.out.println("Move entered: " + move);
        
        ReturnPlay res = new ReturnPlay();
        res.piecesOnBoard = new ArrayList<>();

        // add two kings so the board shows something
        ReturnPiece whiteKing = new ReturnPiece();
        whiteKing.pieceType = ReturnPiece.PieceType.WK;
        whiteKing.pieceFile = ReturnPiece.PieceFile.e;
        whiteKing.pieceRank = 1;

        ReturnPiece blackKing = new ReturnPiece();
        blackKing.pieceType = ReturnPiece.PieceType.BK;
        blackKing.pieceFile = ReturnPiece.PieceFile.e;
        blackKing.pieceRank = 8;

        res.piecesOnBoard.add(whiteKing);
        res.piecesOnBoard.add(blackKing);

        res.message = null;

        // toggle player (optional for testing)
        return res;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
        System.out.println("Starting a new game...");
	}
}