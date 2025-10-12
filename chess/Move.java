package chess;

public class Move {
    char fromFile, toFile;
    int fromRank, toRank;
    char promotion;
    String special;

    public Move(String moveStr) {
        String[] parts = moveStr.trim().split(" ");
        if (parts.length == 2) {
            fromFile = parts[0].charAt(0);
            fromRank = Character.getNumericValue(parts[0].charAt(1));
            toFile = parts[1].charAt(0);
            toRank = Character.getNumericValue(parts[1].charAt(1));
        }
        else if (parts.length == 3) {
            fromFile = parts[0].charAt(0);
            fromRank = Character.getNumericValue(parts[0].charAt(1));
            toFile = parts[1].charAt(0);
            toRank = Character.getNumericValue(parts[1].charAt(1));
            if(parts[2].equals("draw?")){
                special = "draw?";
            }
            else if(parts[2].equals("N") || parts[2].equals("B") || parts[2].equals("R")){
                promotion = parts[2].charAt(0);
            }
            else{
                special = parts[2];
            }
        }
    }
}
