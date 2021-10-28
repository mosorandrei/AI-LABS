package main;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	 Game game = new Game();
     game.initGame(8,8,4);
        System.out.println("\nAll possible pieces generated are: \n" + Arrays.toString(game.getAllPossibleGamePieces()));
        System.out.println("\nGenerated sequence to be guessed by player is: \n" + game.getToBeGuessed());
        System.out.println("\nFunction to verify if guess is on point (for hard-coded guessed sequence): \n" + game.verifyFinal());
        //simulating the choices;
        List<GamePiece> choices=new LinkedList<>();
        choices.add(new GamePiece(1));
        choices.add(new GamePiece(2));
        choices.add(new GamePiece(3));
        choices.add(new GamePiece(4));
        System.out.println("\nHow many Game Pieces have we guessed ?\n"+game.compareSequences(choices));

    }
}
