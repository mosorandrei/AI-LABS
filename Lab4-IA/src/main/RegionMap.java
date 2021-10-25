package main;

import java.util.LinkedList;
import java.util.List;

public class RegionMap {
    private final List<Region> regionList;
    private final int[][] regionMap;
    private final List<Region> regionListMRV;

    public RegionMap(List<Region> regionList, int[][] regionMap) {
        this.regionList = regionList;
        this.regionMap = regionMap;
        regionListMRV = new LinkedList<>(regionList);
    }

    public int[][] getRegionMap() {
        return regionMap;
    }

    public boolean checkConstraint() {
        int print = 0;
        for (int i = 0; i < regionList.size() - 1; i++) {
            if (regionList.get(i).getFinalColor() == Color.UNDETERMINED && print == 0) {
                print++;
            }
            for (int j = i + 1; j < regionList.size(); j++) {
                if (regionMap[i][j] == 1) {
                    if (regionList.get(i).getFinalColor() == regionList.get(j).getFinalColor() && regionList.get(i).getFinalColor() != Color.UNDETERMINED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean ForwardChecking(int i) {
        for (Color color : regionList.get(i).getPossibleColors()) {
            regionList.get(i).setFinalColor(color);
            if (this.checkConstraint()) {
                if (i == regionList.size() - 1) {
                    System.out.println(regionList);
                    return true;
                } else {
                    if (CheckF(i, color)) {
                        List<Integer> toBeRecoveredIndexes = new LinkedList<>();
                        for (int j = i + 1; j < regionList.size(); j++) {
                            if (this.getRegionMap()[i][j] == 1) {
                                toBeRecoveredIndexes.add(j);
                                regionList.get(j).removeColor(color);
                            }
                        }
                        boolean doWeRecover = ForwardChecking(i + 1);
                        if (!doWeRecover) {
                            for (Integer index : toBeRecoveredIndexes) {
                                regionList.get(index).addColor(color);
                            }
                            if (color == regionList.get(i).getPossibleColors().get(regionList.get(i).getPossibleColors().size() - 1)) {
                                regionList.get(i).setFinalColor(Color.UNDETERMINED);
                                return false;
                            } else {
                                regionList.get(i).setFinalColor(Color.UNDETERMINED);
                                continue;
                            }
                        }
                        return true;
                    } else {
                        regionList.get(i).setFinalColor(Color.UNDETERMINED);
                        if (color == regionList.get(i).getPossibleColors().get(regionList.get(i).getPossibleColors().size() - 1)) {
                            regionList.get(i).setFinalColor(Color.UNDETERMINED);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean CheckF(int i, Color currentColor) {
        for (int j = i + 1; j < regionList.size(); j++) {
            if (this.getRegionMap()[i][j] == 1) {
                List<Color> list = new LinkedList<>(regionList.get(j).getPossibleColors());
                list.remove(currentColor);
                if (list.isEmpty())
                    return false;
            }
        }
        return true;
    }

    public boolean MRV() {
        int i = regionList.indexOf(heuristic());
        for (Color color : regionList.get(i).getPossibleColors()) {
            regionList.get(i).setFinalColor(color);
            if (checkConstraint()) {
                if (checkUnassigned()) {
                    regionListMRV.get(0).setFinalColor(regionListMRV.get(0).getPossibleColors().get(0)); // We could choose any of the remaining available colors, and it would be correct
                    // We chose to take the first one
                    System.out.println(regionList);
                    return true;
                } else {
                    if (CheckF_MRV(i, color)) {
                        List<Integer> toBeRecoveredIndexes = new LinkedList<>();
                        Region toBeRecoveredRegion = regionList.get(i);
                        regionListMRV.remove(toBeRecoveredRegion);
                        for (int j = 0; j < regionList.size(); j++) {
                            if (this.getRegionMap()[i][j] == 1) {
                                toBeRecoveredIndexes.add(j);
                                regionList.get(j).removeColor(color);
                            }
                        }
                        boolean doWeRecover = MRV();
                        if (!doWeRecover) {
                            regionListMRV.add(toBeRecoveredRegion);
                            for (Integer index : toBeRecoveredIndexes) {
                                regionList.get(index).addColor(color);
                            }
                            if (color == regionList.get(i).getPossibleColors().get(regionList.get(i).getPossibleColors().size() - 1)) {
                                regionList.get(i).setFinalColor(Color.UNDETERMINED);
                                return false;
                            } else {
                                regionList.get(i).setFinalColor(Color.UNDETERMINED);
                                continue;
                            }
                        }
                        return true;
                    } else {
                        regionList.get(i).setFinalColor(Color.UNDETERMINED);
                        if (color == regionList.get(i).getPossibleColors().get(regionList.get(i).getPossibleColors().size() - 1)) {
                            regionList.get(i).setFinalColor(Color.UNDETERMINED);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkUnassigned() {
        return regionListMRV.size() == 1;
    }

    private Region heuristic() {
        if (regionListMRV.size() == 1)
            return regionListMRV.get(0);
        Region minIndexRegion = regionListMRV.get(0);
        for (int i = 1; i < regionListMRV.size(); i++) {
            if (regionListMRV.get(i).getPossibleColors().size() <= minIndexRegion.getPossibleColors().size()) {
                minIndexRegion = regionListMRV.get(i);
            }
        }
        return minIndexRegion;
    }

    private boolean CheckF_MRV(int i, Color currentColor) {
        for (int j = 0; j < regionList.size(); j++) {
            if (this.getRegionMap()[i][j] == 1) {
                List<Color> list = new LinkedList<>(regionList.get(j).getPossibleColors());
                list.remove(currentColor);
                if (list.isEmpty())
                    return false;
            }
        }
        return true;
    }
}