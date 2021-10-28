package main;

import java.util.*;

public class Game {
    private static final Random random = new Random();
    private final List<GamePiece> toBeGuessed = new LinkedList<>();
    private final Map<GamePiece, Integer> chooseCount = new HashMap<>(); // Here we will keep count of how many times a certain GamePiece has been chosen. If it extends m, we need to choose another
    boolean gameInitialized = false; // Variable that keeps count of the fact that the game is initialized or not
    private List<GamePiece> actuallyGuessed;
    private int totalMovesAvailable;
    private int m; // number of balls
    private int k; // number of guess sequence
    private GamePiece[] allPossibleGamePieces;

    public Game() {
        System.out.println("New Game Instance created!");
    }

    public void initGame(int n, int m, int k) {
        if (gameInitialized) {
            System.out.println("The Game is already initialized!");
            return;
        }
        // number of colors
        this.m = m;
        this.k = k;
        totalMovesAvailable = n * 2;
        generateAllPossibleGamePieces(n);
        generateRandomSequence();
        System.out.println("The Game has been initialized! Parameters: N = " + n + ", M = " + m + " and K = " + k);
    }

    public boolean verifyFinal() {
        boolean isFinal = true;
        System.out.println("\n[TO BE IMPLEMENTED] This function is hard-coded for now");
        actuallyGuessed = new LinkedList<>(toBeGuessed);
        for (int i = 0; i < toBeGuessed.size(); i++) {
            if (toBeGuessed.get(i) == actuallyGuessed.get(i))
                System.out.println("[POSITION #" + i + "] GOOD");
            else {
                System.out.println("[POSITION #" + i + "] BAD");
                isFinal = false;
            }
        }
        return isFinal;
    }

    private void generateAllPossibleGamePieces(int n) {
        allPossibleGamePieces = new GamePiece[n]; // Even though we have n*m possible GamePieces available, they each repeat themselves m times. Thus, we only generate n pieces
        for (int i = 0; i < n; i++) {
            allPossibleGamePieces[i] = new GamePiece(i);
            chooseCount.put(allPossibleGamePieces[i], 0);
        }
    }

    private void generateRandomSequence() {
        while (toBeGuessed.size() < k) {
            GamePiece chosen = allPossibleGamePieces[random.nextInt(allPossibleGamePieces.length)];
            int chosenPieceCount = chooseCount.get(chosen);
            if (chosenPieceCount != m) {
                toBeGuessed.add(chosen);
                chooseCount.put(chosen, chosenPieceCount + 1);
            }
        }
    }

    public int compareSequences(List <GamePiece> choice) {
        int guessed = 0;
        for(int i = 0 ; i < toBeGuessed.size() ; i++) {
            if(toBeGuessed.get(i).equals(choice.get(i))) guessed++;
        }
        return guessed;
    }

    public GamePiece[] getAllPossibleGamePieces() {
        return allPossibleGamePieces;
    }

    public List<GamePiece> getToBeGuessed() {
        return toBeGuessed;
    }

    public int getTotalMovesAvailable() {
        return totalMovesAvailable;
    }
}
