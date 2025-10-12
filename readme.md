# welcome to chess

### use this command to compile classes without cluttering chess folder

javac -d bin chess/*.java

### use this command to run playchess

java -cp bin chess.PlayChess


## Vedanti changes
- in Board class, implemented all basic moves for all piece types
- made changes to structure of PlayChess.main() so that output prints which player's move is next
- implemented pawn promotion by:
    - handling line input in format "g7 g8 N" (knight promotion, and similarly bishop B and rook R promotion) and the default format (default queen promotion) of "g7 g8"
        - the former handling done in Move.java
        - the latter handled in the move itself in pawn move section in Board.makeMove()
    - implementing the move itself within the pawn move section in Board.makeMove()
- implemented resign by:
    - in Chess.play, check for resign in the string arg input, and if so, resigning player loses, other wins, return the ReturnPlay object
- implemented draw by:
    - handling the 3rd arg input in Move.java
    - if "draw?" passed as the 3rd arg, handling the draw itself in Chess.play