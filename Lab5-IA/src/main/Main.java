package main;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	 Game game = new Game();
     game.initGame(8,8,4);
        System.out.println("\nAll possible pieces generated are: \n" + Arrays.toString(game.getAllPossibleGamePieces()));
        System.out.println("\nGenerated sequence to be guessed by player is: \n" + game.getToBeGuessed());
        System.out.println("\nFunction to verify if guess is on point (for hard-coded guessed sequence): \n" + game.verifyFinal());
    }
}
