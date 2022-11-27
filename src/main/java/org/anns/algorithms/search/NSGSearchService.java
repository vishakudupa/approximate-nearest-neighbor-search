package org.anns.algorithms.search;

import org.anns.utils.DistanceUtils;
import org.anns.utils.Neighbor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NSGSearchService {
    private static final Logger logger = LoggerFactory.getLogger(NSGSearchService.class);

    private final int[][] adjacencyGraph;
    private final float[][] baseVector;

    private int l = 40;

    private static final Random random = new Random();

    public NSGSearchService(int[][] adjacencyGraph, float[][] baseVector) {
        this.adjacencyGraph = adjacencyGraph;
        this.baseVector = baseVector;
    }

    public int[] searchKNearestNeighbor(int k, float[] vector, int startingNode) {
        int i = 0;
        List<Neighbor> list = new ArrayList<>(30 * 40);
        list.add(new Neighbor(startingNode, DistanceUtils.squaredEuclideanDistance(baseVector[startingNode], vector)));
        Set<Integer> visited = new HashSet<>();

        Set<Integer> inListNodes = new HashSet<>();
        inListNodes.add(startingNode);

        int itr = 0;
        while (i < l) {

            for (int j = 0; j < list.size(); j++) {
                if (!visited.contains(list.get(j).getId())) {
                    i = j;
                    break;
                }
            }
            if (itr++ > 10000)
                break;
            visited.add(list.get(i).getId());

            for (int node : adjacencyGraph[list.get(i).getId()]) {
                if (!visited.contains(node) && inListNodes.add(node)) {
                    list.add(new Neighbor(node, DistanceUtils.squaredEuclideanDistance(baseVector[node], vector)));
                }
            }

            list.sort(Comparator.comparing(Neighbor::getSquaredEuclideanDistance));
        }
        Set<Integer> set = new HashSet<>();
        int[] topK = new int[k];
        int m = 0;
        for (int o = 0; m < k && o<list.size(); o++) {
            if (set.add(list.get(o).getId()))
                topK[m++] = list.get(o).getId();
        }
        return topK;
    }

    public List<Neighbor> getNeighborVisitedWhileSearching(int startIndex, int findIndex) {
        int i = 0;
        float[] vector = baseVector[findIndex];

        List<Neighbor> list = new ArrayList<>(30 * 40);
        list.add(new Neighbor(startIndex, DistanceUtils.squaredEuclideanDistance(baseVector[startIndex], vector)));
        Set<Integer> visited = new HashSet<>();
        Set<Integer> inListNodes = new HashSet<>();
        inListNodes.add(startIndex);

        boolean foundNode = false;
        while (i < l) {
            for (int j = 0; j < list.size(); j++) {
                if (!visited.contains(list.get(j).getId())) {
                    i = j;
                    break;
                }
            }
            visited.add(list.get(i).getId());

            for (int node : adjacencyGraph[list.get(i).getId()]) {
                if (node == findIndex)
                    foundNode = true;
                else if (!visited.contains(node) && inListNodes.add(node)) {
                    list.add(new Neighbor(node, DistanceUtils.squaredEuclideanDistance(baseVector[node], vector)));
                }
            }
            list.sort(Comparator.comparing(Neighbor::getSquaredEuclideanDistance));
            if (foundNode) {
                break;
            }
        }

        return list;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getL() {
        return l;
    }
}
