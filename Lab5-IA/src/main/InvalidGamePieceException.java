package main;

public class InvalidGamePieceException extends RuntimeException {
    public InvalidGamePieceException(int invalidIndex) {
        super("You attempted to insert a GamePiece with invalid index: " + invalidIndex + "! Game terminated! Try again!...");
    }
}
