package main;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Initial state with 3 couples: " + "\n");
        System.out.println(initState(3));
        System.out.println("\n" + "And the hard-coded final state with 5 couples: " + "\n");
        State generatedFinalState = finalState(5);
        System.out.println(generatedFinalState);
        System.out.println("\n" + "Let's test our verification method we implemented to validate the final state: ");
        System.out.println(isStateFinal(generatedFinalState));
        Male m1 = new Male(0);
        Male m2 = new Male(1);
        Female f1 = new Female(0);
        Female f2 = new Female(1);
        System.out.println(transition(initState(3),m1,f1));//after transition state
        System.out.println(validate(initState(2),m1,m2));
    }

    // This function is only to test our verification method, since we haven't YET implemented any algorithms to progress further starting from the initial state
    public static State finalState(int couplesNum) {
        List<Male> initialMales = new LinkedList<>();
        List<Female> initialFemales = new LinkedList<>();
        for (int i = 0; i < couplesNum; i++) {
            initialMales.add(new Male(i));
            initialFemales.add(new Female(i));
        }
        return new State(new LinkedList<>(), new LinkedList<>(), false, initialMales, initialFemales);
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

    public static State transition(State state,Person p1,Person p2)
    {   List<Male> newLeftMales=state.getLeftMales();
        List<Male> newRightMales=state.getRightMales();
        List<Female> newLeftFemales=state.getLeftFemales();
        List<Female> newRightFemales=state.getRightFemales();
        if(p1!=null) {
            if (p1.isMale()) {
                if(state.isBoatLeft())
                {newLeftMales.removeIf(i -> i.getIndex() == p1.getIndex());
                newRightMales.add((Male) p1);}
                else
                {
                    newRightMales.removeIf(i -> i.getIndex() == p1.getIndex());
                    newLeftMales.add((Male) p1);
                }
            } else {
                if(state.isBoatLeft()) {
                    newLeftFemales.removeIf(i -> i.getIndex() == p1.getIndex());
                    newRightFemales.add((Female) p1);
                }
                else
                {
                    newRightFemales.removeIf(i -> i.getIndex() == p1.getIndex());
                    newLeftFemales.add((Female) p1);
                }
            }
        }
        if(p2!=null) {
            if (p2.isMale()) {
                if(state.isBoatLeft())
                {newLeftMales.removeIf(i -> i.getIndex() == p2.getIndex());
                    newRightMales.add((Male) p2);}
                else
                {
                    newRightMales.removeIf(i -> i.getIndex() == p2.getIndex());
                    newLeftMales.add((Male) p2);
                }
            } else {
                if(state.isBoatLeft()) {
                    newLeftFemales.removeIf(i -> i.getIndex() == p2.getIndex());
                    newRightFemales.add((Female) p2);
                }
                else
                {
                    newRightFemales.removeIf(i -> i.getIndex() == p2.getIndex());
                    newLeftFemales.add((Female) p2);
                }
            }
        }
        return new State(newLeftMales,newLeftFemales,!state.isBoatLeft(),newRightMales,newRightFemales);
    }

    public static boolean validate(State state, Person p1, Person p2)
    {   if(isStateFinal(state)) return false; // no need to transit from final state to another
        if(p1==null && p2==null) return false; // at least one should navigate the boat
        if(p1!=null && p2!=null) {
            if (p1.getIndex() == p2.getIndex() && p1.isMale() == p2.isMale()) return false; // p1 != p2

            if (p1.isMale()) {
                if (!p2.isMale()) {
                    return p1.getIndex() == p2.getIndex(); // pairs can travel on the boat together , but a woman cannot travel with another man
                } else { //ambii barbati in barca
                    if (state.getLeftMales().size() + state.getRightMales().size() != 2) {
                        int ok = 0;
                        for (Female f : state.getLeftFemales()) {
                            if (f.getIndex() == p1.getIndex() || f.getIndex() == p2.getIndex()) ok++;
                        }
                        return ok == 0 || ok == 2; //0 daca nu le am gasit pe malul stang , 2 daca le am gasit
                        //ambele femei ale lor pe acelasi mal
                        //caz special n=2
                    } else return true; //daca sunt 2 cupluri , barbatii pot merge oricand impreuna

                }
            } else {
                if (p2.isMale()) {
                    return p1.getIndex() == p2.getIndex();// pairs can travel on the boat together , but a woman cannot travel with another man
                } else {   //ambele femei in barca
                    if (state.isBoatLeft())
                    // pe malul celalalt nu trebuie sa fie barbati ( exceptie a lor )
                    {
                        for (Male i : state.getRightMales()) {
                            if (i.getIndex() != p1.getIndex() && i.getIndex() != p2.getIndex()) return false;
                        }
                        return true;
                    } else {
                        for (Male i : state.getLeftMales()) {
                            if (i.getIndex() != p1.getIndex() && i.getIndex() != p2.getIndex()) return false;
                        }
                        return true;
                    }
                }
            }
        }
        else if(p1 != null)
            {
                if(!p1.isMale())
                {
                    if(state.isBoatLeft())
                    {
                        for(Male m : state.getRightMales())
                        {
                            if(m.getIndex()!=p1.getIndex()) return false;   // can go on the other side if there are no men , except his husband
                        }
                    }
                    else
                    {
                        for(Male m : state.getLeftMales())
                        {
                            if(m.getIndex()!=p1.getIndex()) return false;
                        }
                    }
                    return true;

                }
                else
                {
                    if(state.isBoatLeft())
                    {
                        for(Female f : state.getLeftFemales())
                        {
                            if(f.getIndex()==p1.getIndex()) // can go on the other side if there are no men , except his husband
                            {
                                return state.getLeftMales().size() == 1;
                            }
                        }
                    }
                    else
                    {
                        for(Female f : state.getRightFemales())
                        {
                            if(f.getIndex()==p1.getIndex()) // can go on the other side if there are no men , except his husband
                            {
                                return state.getRightMales().size() == 1;
                            }
                        }
                    }
                    return true;

                }
            }
            else
            {
                if(!p2.isMale())
                {
                    if(state.isBoatLeft())
                    {
                        for(Male m : state.getRightMales())
                        {
                            if(m.getIndex()!=p2.getIndex()) return false;   // can go on the other side if there are no men , except his husband
                        }
                    }
                    else
                    {
                        for(Male m : state.getLeftMales())
                        {
                            if(m.getIndex()!=p2.getIndex()) return false;
                        }
                    }
                    return true;

                }
                else
                {
                    if(state.isBoatLeft())
                    {
                        for(Female f : state.getLeftFemales())
                        {
                            if(f.getIndex()==p2.getIndex()) // can go on the other side if there are no men , except his husband
                            {
                                return state.getLeftMales().size() == 1;
                            }
                        }
                    }
                    else
                    {
                        for(Female f : state.getRightFemales())
                        {
                            if(f.getIndex()==p2.getIndex()) // can go on the other side if there are no men , except his husband
                            {
                                return state.getRightMales().size() == 1;
                            }
                        }
                    }
                   return true;
                }
            }

    }



}
