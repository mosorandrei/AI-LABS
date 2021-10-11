package main;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        //System.out.println(computeAllTransitions(initState(2)));
        //System.out.println(BFS(initState(2)));
        System.out.println(BackTracking(new LinkedList<>(),initState(2)));
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

    public static boolean validate(State state, Person p1, Person p2) {
        if (isStateFinal(state)) return false;
        if (p1 == null && p2 == null) return false;
        if (p1 != null && p2 != null) {
            if (p1.getIndex() == p2.getIndex() && p1.isMale() == p2.isMale()) return false;
            if (p1.isMale()) {
                if (!p2.isMale()) {
                    return p1.getIndex() == p2.getIndex();
                } else {
                    if (state.getLeftMales().size() + state.getRightMales().size() != 2) {
                        int femalesNumber = 0;
                        for (Female f : state.getLeftFemales()) {
                            if (f.getIndex() == p1.getIndex() || f.getIndex() == p2.getIndex()) femalesNumber++;
                        }
                        return femalesNumber == 0 || femalesNumber == 2;
                    } else return true;
                }
            } else {
                if (p2.isMale()) {
                    return p1.getIndex() == p2.getIndex();
                } else {
                    if (state.isBoatLeft()) {
                        for (Male i : state.getRightMales()) {
                            if (i.getIndex() != p1.getIndex() && i.getIndex() != p2.getIndex()) return false;
                        }
                    } else {
                        for (Male i : state.getLeftMales()) {
                            if (i.getIndex() != p1.getIndex() && i.getIndex() != p2.getIndex()) return false;
                        }
                    }
                    return true;
                }
            }
        } else return singlePersonValidation(state, Objects.requireNonNullElse(p1, p2));
    }

    private static boolean singlePersonValidation(State state, Person p) {
        if (!p.isMale()) {
            if (state.isBoatLeft()) {
                for (Male m : state.getRightMales()) {
                    if (m.getIndex() != p.getIndex()) return false;
                }
            } else {
                for (Male m : state.getLeftMales()) {
                    if (m.getIndex() != p.getIndex()) return false;
                }
            }
        } else {
            if (state.isBoatLeft()) {
                for (Female f : state.getLeftFemales()) {
                    if (f.getIndex() == p.getIndex()) {
                        return state.getLeftMales().size() == 1;
                    }
                }
            } else {
                for (Female f : state.getRightFemales()) {
                    if (f.getIndex() == p.getIndex()) {
                        return state.getRightMales().size() == 1;
                    }
                }
            }
        }
        return true;
    }

    public static List<State> BackTracking(List<State> visited, State currentState) {
        if (isStateFinal(currentState))
            return visited;
        for (State s : computeAllTransitions(currentState)) {
            if (!visited.contains(s)) {
                visited.add(s);
                List<State> solution = BackTracking(visited, s);
                return solution;
                //visited.remove(s);
            }
        }
        return null;
    }

    public static List<State> BFS(State initState) {
        Map<State, State> predecessors = new HashMap<>();
        List<State> visitedStates = new LinkedList<>();
        List<State> queue = new LinkedList<>();
        visitedStates.add(initState);
        queue.add(initState);
        while (!queue.isEmpty()) {
            State currentState = queue.get(0);
            queue.remove(currentState);
            for (State s : computeAllTransitions(currentState)) {
                if (!visitedStates.contains(s)) {
                    visitedStates.add(s);
                    queue.add(s);
                    predecessors.put(s, currentState);
                    if (isStateFinal(s)) {
                        System.out.println("Solution found!...\n");
                        List<State> solution = new LinkedList<>();
                        State toBeAddedState = s;
                        while (!predecessors.get(toBeAddedState).equals(initState)) {
                            solution.add(toBeAddedState);
                            toBeAddedState = predecessors.get(toBeAddedState);
                        }
                        solution.add(initState);
                        return solution;
                    }
                }
            }
        }
        return null;
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
                if (validate(beginning, allPersons.get(i), allPersons.get(j)) && !possibleStates.contains(transition(beginning, allPersons.get(i), allPersons.get(j))))
                    possibleStates.add(transition(beginning, allPersons.get(i), allPersons.get(j)));
                if (validate(beginning, null, allPersons.get(j)) && !possibleStates.contains(transition(beginning, null, allPersons.get(j))))
                    possibleStates.add(transition(beginning, null, allPersons.get(j)));
                if (validate(beginning, allPersons.get(i), null) && !possibleStates.contains(transition(beginning, allPersons.get(i), null)))
                    possibleStates.add(transition(beginning, allPersons.get(i), null));
            }
        }
        return possibleStates;
    }
}
