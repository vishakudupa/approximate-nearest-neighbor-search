package org.anns.algorithms.search;

import com.sun.source.tree.Tree;
import org.anns.utils.DistanceUtils;
import org.anns.utils.Neighbor;

import java.util.*;

public class NSGSearch {
    private final int[][] adjacencyGraph;
    private final float[][] baseVector;

    private int l = 100;

    private static final Random random = new Random();
    public NSGSearch(String adjacencyGraphFile, String baseVectorFile) {
        //TODO: load the file
        this.adjacencyGraph = new int[1][1];
        this.baseVector = new float[1][1];
    }

    public NSGSearch(int[][] adjacencyGraph, float[][] baseVector) {
        this.adjacencyGraph = adjacencyGraph;
        this.baseVector = baseVector;
    }

    public int[] searchKNearestNeighbor(int k, float[] vector) {
        int i = 0;
        List<Neighbor> list = new ArrayList<>(30 * 40);
        list.add(new Neighbor(123742, DistanceUtils.squaredEuclideanDistance(baseVector[123742], vector)));
        Set<Integer> visited = new HashSet<>();

        while (i < l) {
            for (int j = 0; j < list.size(); j++) {
                if (!visited.contains(list.get(j).getId())) {
                    i = j;
                    break;
                }
            }
            visited.add(list.get(i).getId());

            for (int node : adjacencyGraph[list.get(i).getId()]) {
                if (!visited.contains(node)) {
                    list.add(new Neighbor(node, DistanceUtils.squaredEuclideanDistance(baseVector[node], vector)));
                }
            }

            list.sort(Comparator.comparing(Neighbor::getSquaredEuclideanDistance));
        }
        int[] topK = new int[k];
        for (int o = 0; o < k; o++) {
            topK[o] = list.get(o).getId();
        }
        return topK;
    }

    public TreeSet<Neighbor> getNeighborVisitedWhileSearching(int startIndex, int findIndex) {
        int i = 0;
        float[] vector = baseVector[findIndex];

        List<Neighbor> list = new ArrayList<>(30 * 40);
        list.add(new Neighbor(startIndex, DistanceUtils.squaredEuclideanDistance(baseVector[startIndex], vector)));
        TreeSet<Neighbor> visited = new TreeSet<>();

        boolean foundNode = false;
        while (i < l) {
            for (int j = 0; j < list.size(); j++) {
                if (!visited.contains(list.get(j))) {
                    i = j;
                    break;
                }
            }
            visited.add(list.get(i));

            for (int node : adjacencyGraph[list.get(i).getId()]) {
                if (!visited.contains(new Neighbor(node, 0d))) {
                    list.add(new Neighbor(node, DistanceUtils.squaredEuclideanDistance(baseVector[node], vector)));
                }
                if (node == findIndex)
                    foundNode = true;
            }
            if (foundNode)
                break;

            list.sort(Comparator.comparing(Neighbor::getSquaredEuclideanDistance));
        }

        return visited;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getL() {
        return l;
    }


}
