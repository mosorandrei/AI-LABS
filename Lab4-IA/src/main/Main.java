package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int regionsNumber = 3;
        List<Region> regions = new LinkedList<>();
        regions.add(new Region(0, "SA", Arrays.asList(Color.RED, Color.GREEN)));
        regions.add(new Region(1, "WA", Arrays.asList(Color.RED, Color.GREEN, Color.BLUE)));
        regions.add(new Region(2, "NT", Collections.singletonList(Color.GREEN)));
        int[][] regionMap = {
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };
        regions.get(0).setFinalColor(Color.RED);
        regions.get(1).setFinalColor(Color.BLUE);
        regions.get(2).setFinalColor(Color.GREEN);
        RegionMap map = new RegionMap(regions,regionMap);
        System.out.println(map.checkConstraint());
    }
}
