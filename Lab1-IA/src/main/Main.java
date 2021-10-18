package main;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please insert the number of couples!");
        int n = scanner.nextInt();
        if (n < 1 || n > 3) {
            System.out.println("The problem is only possible for n being either 1, 2 or 3!");
            return;
        }
        boolean running = true;
        while (running) {
            System.out.println("========================================");
            System.out.println("Please select the index of your wanted algorithm!\n0 - Exit\n1 - BackTracking\n2 - BFS\n3 - A*\n4 - HillClimbing");
            int index = scanner.nextInt();
            switch (index) {
                case 0:
                    System.out.println("========================================");
                    System.out.println("Terminating program!...");
                    running = false;
                    break;
                case 1:
                    System.out.println("========================================");
                    System.out.println("Generating result for BackTracking!...");
                    System.out.println(BackTracking(new LinkedList<>(), initState(n)));
                    break;
                case 2:
                    System.out.println("========================================");
                    System.out.println("Generating result for BFS!...");
                    BFS(initState(n));
                    break;
                case 3:
                    System.out.println("========================================");
                    System.out.println("Generating result for A*!...");
                    AStar(initState(n));
                    break;
                case 4:
                    System.out.println("========================================");
                    System.out.println("Generating result for HillClimbing!...");
                    HillClimb(initState(3));
                    break;
                default:
                    System.out.println("Invalid index! Please try again!");
            }
        }
    }

    // BASIC FUNCTIONS

    // INITIALIZING STATE

    public static State initState(int couplesNum) {
        List<Male> initialMales = new LinkedList<>();
        List<Female> initialFemales = new LinkedList<>();
        for (int i = 0; i < couplesNum; i++) {
            initialMales.add(new Male(i));
            initialFemales.add(new Female(i));
        }
        return new State(initialMales, initialFemales, true, new LinkedList<>(), new LinkedList<>());
    }

    // VERIFYING IF STATE IS FINAL

    public static boolean isStateFinal(State state) {
        return state.getLeftMales().isEmpty() && state.getLeftFemales().isEmpty() && !state.isBoatLeft();
    }

    // COMPUTING STATE TRANSITIONS

    public static State transition(State state, Person p1, Person p2) {
        List<Male> newLeftMales = new ArrayList<>(state.getLeftMales());
        List<Male> newRightMales = new ArrayList<>(state.getRightMales());
        List<Female> newLeftFemales = new ArrayList<>(state.getLeftFemales());
        List<Female> newRightFemales = new ArrayList<>(state.getRightFemales());
        if (p1 == null && p2 == null) {
            System.out.println("No persons moved! Returning state as it is!");
            return state;
        }
        if (p1 != null) {
            personTransition(state, p1, newLeftMales, newRightMales, newLeftFemales, newRightFemales);
        }
        if (p2 != null) {
            personTransition(state, p2, newLeftMales, newRightMales, newLeftFemales, newRightFemales);
        }
        return new State(newLeftMales, newLeftFemales, !state.isBoatLeft(), newRightMales, newRightFemales);
    }

    // COMPUTING STATE TRANSITIONS FOR ONE PERSON

    private static void personTransition(State state, Person p, List<Male> LeftMales, List<Male> RightMales, List<Female> LeftFemales, List<Female> RightFemales) {
        if (p.isMale()) {
            if (state.isBoatLeft()) {
                LeftMales.remove((Male) p);
                RightMales.add((Male) p);
            } else {
                RightMales.remove((Male) p);
                LeftMales.add((Male) p);
            }
        } else {
            if (state.isBoatLeft()) {
                LeftFemales.remove((Female) p);
                RightFemales.add((Female) p);
            } else {
                RightFemales.remove((Female) p);
                LeftFemales.add((Female) p);
            }
        }
    }

    // VALIDATE STATE

    public static boolean validate(State state) {
        if (isStateFinal(state)) return true;
        boolean areMenLeft = !state.getLeftMales().isEmpty();
        for (Female f : state.getLeftFemales()) {
            if (areMenLeft) {
                if (!state.getLeftMales().contains(new Male(f.getIndex())))
                    return false;
            }
        }
        boolean areMenRight = !state.getRightMales().isEmpty();
        for (Female f : state.getRightFemales()) {
            if (areMenRight) {
                if (!state.getRightMales().contains(new Male(f.getIndex())))
                    return false;
            }
        }
        return true;
    }

    // ALGORITHMS

    // BACKTRACKING

    public static List<State> BackTracking(List<State> visited, State currentState) {
        if (isStateFinal(currentState)) {
            visited.add(currentState);
            System.out.println("\n ===== \n BACKTRACKING SOLUTION FOUND - " + visited.size() + " STEPS! \n ===== \n" + visited);
            return new LinkedList<>(visited);
        }
        if (visited.contains(currentState))
            return new LinkedList<>();
        visited.add(currentState);
        for (State s : computeAllTransitions(currentState)) {
            if (!visited.contains(s)) {
                BackTracking(visited, s);
                visited.remove(s);
            }
        }
        return new LinkedList<>();
    }

    // BFS

    public static void BFS(State initState) {
        Map<State, State> predecessors = new HashMap<>();
        List<State> visitedStates = new LinkedList<>();
        List<State> queue = new LinkedList<>();
        visitedStates.add(initState);
        queue.add(initState);
        while (!queue.isEmpty()) {
            State currentState = queue.get(0);
            queue.remove(currentState);
            if (isStateFinal(currentState)) {
                System.out.println("\n ===== \n BFS SOLUTION FOUND \n ===== \n");
                reconstructPath(predecessors, currentState, initState);
            }
            for (State s : computeAllTransitions(currentState)) {
                if (!visitedStates.contains(s)) {
                    visitedStates.add(s);
                    queue.add(s);
                    predecessors.put(s, currentState);
                }
            }
        }
    }

    // A*

    public static void AStar(State beginning) {
        Comparator<State> stateComparator = Comparator.comparingInt(Main::AStarHeuristic); // Comparator for priority queue, we need to keep the nodes with the lowest perceived heuristic score at the beginning
        PriorityQueue<State> priorityQueue = new PriorityQueue<>(stateComparator);
        Map<State, State> predecessors = new HashMap<>(); // Here we store each found node's predecessor
        Map<State, Integer> cheapestKnownPath = new HashMap<>(); // Here we store the cost of the cheapest path from the start state to every other state
        cheapestKnownPath.put(beginning, 0);
        Map<State, Integer> estimatedShortestPath = new HashMap<>(); // Here we store our best computation of the shortest path from the initial state to the final state for a path that goes through every other state
        estimatedShortestPath.put(beginning, AStarHeuristic(beginning));
        priorityQueue.add(beginning);
        while (!priorityQueue.isEmpty()) {
            State currentState = priorityQueue.peek();
            if (isStateFinal(currentState)) {
                System.out.println("\n ===== \n A* SOLUTION FOUND \n ===== \n");
                reconstructPath(predecessors, currentState, beginning);
            }
            priorityQueue.remove(currentState);
            for (State state : computeAllTransitions(currentState)) {

                // Since we don't know the states beforehand as we do with the nodes of a graph, these decision control instructions are necessary in order to assure we have an entry for each encountered state
                if (!cheapestKnownPath.containsKey(state))
                    cheapestKnownPath.put(state, Integer.MAX_VALUE);
                if (!estimatedShortestPath.containsKey(state))
                    estimatedShortestPath.put(state, Integer.MAX_VALUE);

                int distanceStartNeighbor = cheapestKnownPath.get(currentState) + computePerceivedDifference(currentState, state);
                if (distanceStartNeighbor < cheapestKnownPath.get(state)) {
                    predecessors.put(state, currentState);
                    cheapestKnownPath.put(state, distanceStartNeighbor);
                    estimatedShortestPath.put(state, cheapestKnownPath.get(state) + AStarHeuristic(state));
                    if (!priorityQueue.contains(state))
                        priorityQueue.add(state);
                }
            }
        }
    }

    // FUNCTION TO PRINT GENERATED PATH

    private static void reconstructPath(Map<State, State> predecessors, State currentState, State beginningState) {
        List<State> solution = new LinkedList<>();
        State toBeAddedState = currentState;
        while (!predecessors.get(toBeAddedState).equals(beginningState)) {
            solution.add(toBeAddedState);
            toBeAddedState = predecessors.get(toBeAddedState);
        }
        solution.add(toBeAddedState);
        solution.add(beginningState);
        Collections.reverse(solution);
        System.out.println(solution);
    }

    // HEURISTIC FUNCTION FOR A*

    private static int AStarHeuristic(State s) {
        return s.getLeftMales().size() + s.getLeftFemales().size();
    }

    // FUNCTION TO COMPUTE DIFFERENCE BETWEEN CONSECUTIVE STATES

    private static int computePerceivedDifference(State s1, State s2) {
        return AStarHeuristic(s1) - AStarHeuristic(s2);
    }

    // HILLCLIMB

    public static void HillClimb(State beginning) {
        if (beginning.getLeftMales().size() + beginning.getLeftMales().size() == 6) {
            AbstractMap.SimpleEntry<State, State> entry = HillClimbDescending(beginning, false);
            State halfState = entry.getValue();
            State toBeExcluded = entry.getKey();
            State nextState = null;
            int maxH = HillClimbHeuristic(halfState);
            for (State s : computeAllTransitions(halfState)) {
                if (maxH <= HillClimbHeuristic(s) && !s.equals(toBeExcluded)) {
                    maxH = HillClimbHeuristic(s);
                    nextState = s;
                }
            }
            HillClimbDescending(nextState, true);
        } else HillClimbDescending(beginning, false);
    }

    // HILLCLIMB DESCENDING

    private static AbstractMap.SimpleEntry<State, State> HillClimbDescending(State state, boolean isHalfway) {
        while (!isStateFinal(state)) {
            State bestState = null;
            int minH = HillClimbHeuristic(state);
            List<State> neighbours = computeAllTransitions(state);
            if (isHalfway)
                Collections.reverse(neighbours);
            for (State s : neighbours) {
                if (minH >= HillClimbHeuristic(s)) {
                    minH = HillClimbHeuristic(s);
                    bestState = s;
                }
            }
            System.out.println(state);
            if (isHalfState(Objects.requireNonNull(bestState))) {
                System.out.println(bestState);
                return new AbstractMap.SimpleEntry<>(state, bestState);
            }
            state = bestState;
        }
        System.out.println(state);
        return new AbstractMap.SimpleEntry<>(state, state);
    }

    // FUNCTION TO VERIFY IF, FOR N = 3, STATE IS HALFWAY POINT IN PATHFINDING

    private static boolean isHalfState(State state) {
        return !state.isBoatLeft() && state.getLeftFemales().size() == 1 && state.getLeftMales().size() == 1 && state.getLeftMales().get(0).getIndex() == state.getLeftFemales().get(0).getIndex();
    }

    // HEURISTIC FUNCTION FOR HILLCLIMB

    private static int HillClimbHeuristic(State state) {
        if (state.isBoatLeft())
            return state.getLeftMales().size() + state.getLeftFemales().size() - 1;
        else
            return state.getLeftMales().size() + state.getLeftFemales().size();
    }

    // FUNCTION TO COMPUTE ALL POSSIBLE TRANSITIONS FROM A GIVEN STATE

    private static List<State> computeAllTransitions(State beginning) {
        List<State> possibleStates = new LinkedList<>();
        List<Person> allPersons = new LinkedList<>();
        if (beginning.isBoatLeft()) {
            allPersons.addAll(beginning.getLeftMales());
            allPersons.addAll(beginning.getLeftFemales());
        } else {
            allPersons.addAll(beginning.getRightFemales());
            allPersons.addAll(beginning.getRightMales());
        }
        for (int i = 0; i < allPersons.size() - 1; i++) {
            for (int j = i + 1; j < allPersons.size(); j++) {
                if (validate(transition(beginning, allPersons.get(i), allPersons.get(j))) && !possibleStates.contains(transition(beginning, allPersons.get(i), allPersons.get(j))))
                    possibleStates.add(transition(beginning, allPersons.get(i), allPersons.get(j)));
                if (validate(transition(beginning, null, allPersons.get(j))) && !possibleStates.contains(transition(beginning, null, allPersons.get(j))))
                    possibleStates.add(transition(beginning, null, allPersons.get(j)));
                if (validate(transition(beginning, allPersons.get(i), null)) && !possibleStates.contains(transition(beginning, allPersons.get(i), null)))
                    possibleStates.add(transition(beginning, allPersons.get(i), null));
            }
        }
        return possibleStates;
    }
}
