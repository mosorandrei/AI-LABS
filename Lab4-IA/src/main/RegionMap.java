package main;

import java.util.List;

public class RegionMap {
    private final List<Region> regionList;
    private final int[][] regionMap;

    public RegionMap(List<Region> regionList, int[][] regionMap) {
        this.regionList = regionList;
        this.regionMap = regionMap;
    }

    public boolean checkConstraint() {
        for (int i = 0; i < regionList.size() - 1; i++) {
            for (int j = i + 1; j < regionList.size(); j++) {
                if (regionMap[i][j] == 1) {
                    if (regionList.get(i).getFinalColor() != regionList.get(j).getFinalColor()) {
                        if (regionList.get(i).getFinalColor() == Color.UNDETERMINED || regionList.get(j).getFinalColor() == Color.UNDETERMINED) {
                            System.out.println("Coloring problem not yet solved!");
                            return false;
                        }
                    } else {
                        if (regionList.get(i).getFinalColor() == Color.UNDETERMINED)
                            System.out.println("Coloring problem not yet solved!");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
