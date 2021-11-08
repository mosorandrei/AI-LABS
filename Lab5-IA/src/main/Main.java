package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.initGame(8, 8, 4);
        //System.out.println("\nAll possible pieces generated are: \n" + Arrays.toString(game.getAllPossibleGamePieces()));
        System.out.println("\nGenerated sequence to be guessed by player is: \n" + game.getToBeGuessed());
        //game.playGame();
        List<GamePiece> finalPick = new LinkedList<>();
        game.minimax(finalPick,Integer.MIN_VALUE,Integer.MAX_VALUE,true);
    }
}
