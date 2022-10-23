package org.anns.algorithms.build;

import org.anns.algorithms.search.NSGSearch;
import org.anns.utils.DistanceUtils;
import org.anns.utils.Neighbor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;

import static org.anns.utils.DistanceUtils.squaredEuclideanDistance;

public class NSGBuild {

    private static final Logger logger = LoggerFactory.getLogger(NSGBuild.class);
    private int medoid = 0;
    private int[][] nsg;

    private final int M = 50;

    public NSGBuild(float[][] baseVector, int[][] knnGraph) {
        nsg = new int[knnGraph.length][];
        build(baseVector, knnGraph);
    }

    private void build(float[][] baseVector, int[][] knnGraph) {
        logger.debug("building NSG from KNN graph");
        logger.debug("Step 1: Load medoid from the dataset");
        loadMedoid(baseVector, knnGraph);

        logger.debug("Step 2: Build the NSG graph");
        buildNSG(baseVector, knnGraph);
    }

    private void buildNSG(float[][] baseVector, int[][] knnGraph) {
        NSGSearch nsgSearch = new NSGSearch(knnGraph, baseVector);
        IntStream.range(0, knnGraph.length).parallel().forEach(v -> {
            Set<Neighbor> nodeVisitedWhileSearching = nsgSearch.getNeighborVisitedWhileSearching(medoid, v);
            for (int i = 1; i < knnGraph[v].length; i++) {
                if (!nodeVisitedWhileSearching.contains(new Neighbor(knnGraph[v][i], 0d))) {
                    nodeVisitedWhileSearching.add(new Neighbor(knnGraph[v][i],
                            squaredEuclideanDistance(baseVector[v], baseVector[knnGraph[v][i]])));
                }
            }
            List<Integer> list = new ArrayList<>();
            list.add(nodeVisitedWhileSearching.iterator().next().getId());

            while (!nodeVisitedWhileSearching.isEmpty() && list.size() < M) {
                Neighbor p = nodeVisitedWhileSearching.iterator().next();
                nodeVisitedWhileSearching.remove(p);
                boolean add = true;
                for (Integer a : list) {
                    if (a == p.getId()) {
                        add = false;
                        break;
                    }

                    if (p.getSquaredEuclideanDistance() >
                            squaredEuclideanDistance(baseVector[p.getId()],
                                    baseVector[a])) {
                        add = false;
                        break;
                    }
                }
                if(add)
                    list.add(p.getId());
            }
            nsg[v] = list.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            if (v % 10000 == 0)
                logger.debug("Completed {} iterations", v);
        });
        boolean[] spanningVector = new boolean[knnGraph.length];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(medoid);
        spanningVector[medoid] = true;
        while (!queue.isEmpty()) {
            Integer i = queue.poll();
            for(Integer j : nsg[i]) {
                if (!spanningVector[j]) {
                    queue.add(j);
                    spanningVector[j] = true;
                }
            }
        }
        int count = 0;
        for (boolean b : spanningVector) {
            if (!b) count++;
        }
        logger.debug("count : {}", count);
    }

    public void loadMedoid(float[][] baseVector, int[][] knnGraph) {
        logger.debug("loading medoid");
        float[] centroid = findCentroid(baseVector);
        logger.debug("Centroid found: " + Arrays.toString(centroid));
        NSGSearch nsgSearch = new NSGSearch(knnGraph, baseVector);

        medoid = nsgSearch.searchKNearestNeighbor(1, centroid)[0];
        logger.debug("Setting medoid to : {}", medoid);
    }

    public float[] findCentroid(float[][] baseVector) {
        int d = baseVector[0].length;
        float[] centroid = new float[d];
        for (float[] floats : baseVector) {
            for (int j = 0; j < d; j++) {
                centroid[j] += (floats[j] / baseVector.length);
            }
        }
        return centroid;
    }

    public int[][] getNsg() {
        return nsg;
    }
}
