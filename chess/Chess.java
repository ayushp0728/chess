//Chess.java
package chess;

import java.util.ArrayList;

public class Chess {
    
    private static Board board;
    private static Player currentPlayer = Player.WHITE;

    enum Player { WHITE, BLACK }

    public static void start() {
        board = new Board();
        board.initialize();
        currentPlayer = Player.WHITE;
    }

    public static ReturnPlay play(String moveStr) {
        ReturnPlay result = new ReturnPlay();

        //handle resign
        if (moveStr.equals("resign")){
            if (currentPlayer == Player.WHITE){
                result.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
            }
            else{
                result.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
            }
            return result;
            
        }

        Move move = new Move(moveStr);

        //handle draw
        if ("draw?".equals(move.special)){
            result.message = ReturnPlay.Message.DRAW;
            return result;
        }
        
        //if not draw or resign, handle move
    
        boolean success = board.makeMove(move, currentPlayer);
        result.piecesOnBoard = board.getPieces();
        if(board.gameOver){
            if(currentPlayer==Player.WHITE) result.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
            else result.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
            return result;
        }
        if (!success) {
            result.message = ReturnPlay.Message.ILLEGAL_MOVE;
        } 
        else {
            result.message = null;
            currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;

        }

        return result;
    }

    public static ArrayList<ReturnPiece> getBoardState() {
        return board.getPieces();
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

}