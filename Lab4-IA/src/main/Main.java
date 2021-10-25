package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Region> regions = new LinkedList<>();
        regions.add(new Region(0, "SA", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN))));
        regions.add(new Region(1, "WA", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));
        regions.add(new Region(2, "NT", new LinkedList<>(Collections.singletonList(Color.GREEN))));
        int[][] regionMap = {
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };
        RegionMap map = new RegionMap(regions,regionMap);
        System.out.println("Solution for the first example computed with Forward Checking: " + map.ForwardChecking(0));
        System.out.println("Solution for the first example computed with MRV: " + map.MRV());

        List<Region> regionsEx2 = new LinkedList<>();
        regionsEx2.add(new Region(0, "T", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));
        regionsEx2.add(new Region(1, "WA", new LinkedList<>(Collections.singletonList(Color.RED))));
        regionsEx2.add(new Region(2, "NT", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));
        regionsEx2.add(new Region(3, "SA", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));
        regionsEx2.add(new Region(4, "Q", new LinkedList<>(Collections.singletonList(Color.RED))));
        regionsEx2.add(new Region(5, "NSW", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));
        regionsEx2.add(new Region(6, "V", new LinkedList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE))));

        int[][] regionMapEx2 = {
                {0,0,0,0,0,0,1},
                {0,0,1,1,0,0,0},
                {0,1,0,1,1,0,0},
                {0,1,1,0,1,1,1},
                {0,0,1,1,0,1,0},
                {0,0,0,1,1,0,1},
                {1,0,0,1,0,1,0}
        };
        RegionMap map2 = new RegionMap(regionsEx2,regionMapEx2);
        System.out.println("Solution for the second example computed with Forward Checking: " + map2.ForwardChecking(0));
        System.out.println("Solution for the second example computed with MRV: " + map2.MRV());
    }
}