package main;


import java.util.*;

public class Game {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private final List<GamePiece> toBeGuessed = new LinkedList<>();
    private final Map<GamePiece, Integer> chooseCount = new HashMap<>(); // Here we will keep count of how many times a certain GamePiece has been chosen. If it extends m, we need to choose another
    boolean gameInitialized = false; // Variable that keeps count of the fact that the game is initialized or not
    private int currentMoves = 0;
    private List<GamePiece> actuallyGuessed;
    private int totalMovesAvailable;
    private int m; // number of balls
    private int k; // number of guess sequence
    private GamePiece[] allPossibleGamePieces;
    private int optimalHeuristicResult;
    private int totalPruningInstances = 0;
    private int miniMaxGuesses = 0;

    public Game() {
        System.out.println("New Game Instance created!");
    }

    public List<GamePiece> getToBeGuessed() {
        return toBeGuessed;
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
        System.out.println("OptimalHeuristicResult is " + optimalHeuristicResult);
    }

    public void playGame() {
        System.out.println("\nWelcome to MasterMind!");
        System.out.println("The parameters of the game are: ");
        System.out.println("Number of possible colors (M) : " + m);
        System.out.println("Number of GamePiece for every sequence (K) : " + k);
        System.out.println("Number of total guesses of sequences available (N^2) : " + totalMovesAvailable);
        System.out.println("GOOD LUCK!");
        System.out.println("\n===============================================\n");
        while (currentMoves < totalMovesAvailable) {
            actuallyGuessed = new LinkedList<>();
            for (int i = 0; i < k; i++) {
                System.out.println("[Attempt #" + currentMoves + "] Please insert the GamePiece #" + i + "!...");
                int inputIndex = scanner.nextInt();
                if (inputIndex >= m || inputIndex < 0)
                    throw new InvalidGamePieceException(inputIndex);
                actuallyGuessed.add(new GamePiece(inputIndex));
                System.out.println("Added GamePiece with index " + inputIndex + "!...");
            }
            int correctChoices = compareSequences();
            if (correctChoices == k) {
                System.out.println("Congratulation! You won the MasterMind Game in " + (currentMoves + 1) + " move(s)!...");
                return;
            } else {
                currentMoves++;
                System.out.println("You got " + correctChoices + " correct choice(s) out of " + k + "! Try again! You have " + (totalMovesAvailable - currentMoves) + " move(s) available...");
            }
        }
        System.out.println("You are out of moves! CPU wins!");
    }

    public int miniMax(int depth, List<GamePiece> computedList, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == k) {
            miniMaxGuesses++;
            if (compareSequencesHeuristic(computedList) == optimalHeuristicResult) {
                System.out.println("Solution found using the MiniMax method, with Alpha-Beta pruning! Solution found in " + miniMaxGuesses + " step(s)!...");
                System.out.println("Total number of pruning optimizations with the Alpha-Beta method: " + totalPruningInstances);
                System.out.println("Solution is: " + computedList);
            }
            return compareSequencesHeuristic(computedList);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (GamePiece gamePiece : allPossibleGamePieces) {
                List<GamePiece> computedListUpdated = new LinkedList<>(computedList);
                computedListUpdated.add(gamePiece);
                int eval = miniMax(depth + 1, computedListUpdated, alpha, beta, false);
                if (maxEval <= eval) maxEval = eval;
                if (alpha <= maxEval) alpha = maxEval;
                if (beta <= alpha) {
                    totalPruningInstances++;
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (GamePiece gamePiece : allPossibleGamePieces) {
                List<GamePiece> computedListUpdated = new LinkedList<>(computedList);
                computedListUpdated.add(gamePiece);
                int eval = miniMax(depth + 1, computedListUpdated, alpha, beta, true);
                if (eval <= minEval) minEval = eval;
                if (minEval <= beta) beta = minEval;
                if (beta <= alpha) {
                    totalPruningInstances++;
                    break;
                }
            }
            return minEval;
        }
    }

    private boolean verifyFinal() {
        boolean isFinal = true;
        if (currentMoves == totalMovesAvailable)
            return false;
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
        optimalHeuristicResult = compareSequencesHeuristic(toBeGuessed);
    }

    private int compareSequences() {
        int guessed = 0;
        for (int i = 0; i < toBeGuessed.size(); i++) {
            if (toBeGuessed.get(i).equals(actuallyGuessed.get(i))) guessed++;
        }
        return guessed;
    }

    public int compareSequencesHeuristic(List<GamePiece> l) {
        int heuristicVal = 0;
        for (int i = 0; i < toBeGuessed.size(); i++) {
            if (l.get(i).equals(toBeGuessed.get(i)))
                heuristicVal += Math.pow(k,(i+1));
        }
        return heuristicVal;
    }

}