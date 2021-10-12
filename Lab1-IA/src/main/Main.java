package main;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(computeAllTransitions(initState(3)));
        BFS(initState(3));
        System.out.println(BackTracking(new LinkedList<>(), initState(4)));
    }

    public static State initState(int couplesNum) {
        List<Male> initialMales = new LinkedList<>();
        List<Female> initialFemales = new LinkedList<>();
        for (int i = 0; i < couplesNum; i++) {
            initialMales.add(new Male(i));
            initialFemales.add(new Female(i));
        }
        return new State(initialMales, initialFemales, true, new LinkedList<>(), new LinkedList<>());
    }

    public static boolean isStateFinal(State state) {
        return state.getLeftMales().isEmpty() && state.getLeftFemales().isEmpty() && !state.isBoatLeft();
    }

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
                List<State> solution = new LinkedList<>();
                State toBeAddedState = currentState;
                while (!predecessors.get(toBeAddedState).equals(initState)) {
                    solution.add(toBeAddedState);
                    toBeAddedState = predecessors.get(toBeAddedState);
                }
                solution.add(toBeAddedState);
                solution.add(initState);
                Collections.reverse(solution);
                System.out.println(solution);
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
