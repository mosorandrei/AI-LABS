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
    private int alpha;

    public List<GamePiece> getToBeGuessed() {
        return toBeGuessed;
    }

    private int beta;

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
      // a function to get all the children of a node
    public List<List<GamePiece>> computeAllChildren(List<GamePiece> list){
        List<List<GamePiece>> childs = new LinkedList<>();
        for(int color = 0 ; color<this.m ; color++) {
            List<GamePiece> temp = new LinkedList<>(list);
            temp.add(new GamePiece(color));
            childs.add(temp);
        }
        return childs;
    }
    public int minimax(List<GamePiece> l,int alpha,int beta,boolean maximizingPlayer) {
        if(l.size()==this.k) {
            if(this.k==compareSequences(l))
            {System.out.println(l);}
            return compareSequences(l);
        }

        if(maximizingPlayer) {
            int maxEval=Integer.MIN_VALUE;
            for(List<GamePiece> child : computeAllChildren(l)) {
                int eval=minimax(child,alpha,beta,true);
                if(maxEval<=eval) maxEval=eval;
                if(alpha<=eval) alpha=eval;
                if(beta<=alpha) break;
            }
            return maxEval;
        }
        else {
            int minEval=Integer.MAX_VALUE;
            for(List<GamePiece> child : computeAllChildren(l)) {
                int eval=minimax(child,alpha,beta,false);
                if(minEval>=eval) minEval=eval;
                if(beta>=eval) beta=eval;
                if(beta<=alpha) break;
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
    }

    private int compareSequences() {
        int guessed = 0;
        for (int i = 0; i < toBeGuessed.size(); i++) {
            if (toBeGuessed.get(i).equals(actuallyGuessed.get(i))) guessed++;
        }
        return guessed;
    }

    public int compareSequences(List<GamePiece> l) {
        int guessed = 0;
        for (int i = 0; i < toBeGuessed.size(); i++) {
            if (l.get(i).equals(toBeGuessed.get(i))) guessed++;
        }
        return guessed;
    }

    public GamePiece[] getAllPossibleGamePieces() {
        return allPossibleGamePieces;
    }
}
