package chess;

public class Move {
    char fromFile, toFile;
    int fromRank, toRank;

    public Move(String moveStr) {
        String[] parts = moveStr.trim().split(" ");
        if (parts.length == 2) {
            fromFile = parts[0].charAt(0);
            fromRank = Character.getNumericValue(parts[0].charAt(1));
            toFile = parts[1].charAt(0);
            toRank = Character.getNumericValue(parts[1].charAt(1));
        }
    }
}
