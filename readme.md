# ♟️ Welcome to Chess

This is a fully functional **command-line chess game** written in Java. It supports all standard chess rules — including check, checkmate, castling, en passant, pawn promotion, and resign/draw conditions.

The project is organized using object-oriented programming principles, with a modular structure that separates the logic for the **board**, **pieces**, **players**, and **game flow**.

---

## 🧠 Project Overview

This project was developed as a learning exercise to understand **OOP design**, **game state management**, and **move validation logic**.  
Each piece (King, Queen, Bishop, Knight, Rook, Pawn) is implemented as a class that extends a common `Piece` superclass, allowing polymorphic move validation.

The `Board` class tracks the positions of all pieces, while the `Chess` and `PlayChess` classes handle gameplay control, move parsing, and user interaction.

---

## ⚙️ How to Compile

Use the following command to compile all `.java` files while keeping your source folder clean.  
This will place all compiled `.class` files into a `bin` directory:

```bash
javac -d bin chess/*.java
```

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