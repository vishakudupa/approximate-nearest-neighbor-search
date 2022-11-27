package org.anns.algorithms.build;

import org.anns.algorithms.NSG;
import org.anns.algorithms.search.NSGSearchService;
import org.anns.utils.FileUtils;
import org.anns.utils.Neighbor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.anns.utils.DistanceUtils.squaredEuclideanDistance;

public class NSGBuildService {

    private static final Logger logger = LoggerFactory.getLogger(NSGBuildService.class);
    private int medoid = 0;
    private int[][] nsg;

    private final int M = 50;

    private final float[][] baseVector;
    private final int[][] knnGraph;

    public NSGBuildService(float[][] baseVector, int[][] knnGraph) {
        nsg = new int[knnGraph.length][];
        this.baseVector = baseVector;
        this.knnGraph = knnGraph;
    }

    public NSG build() {
        logger.debug("building NSG from KNN graph");
        logger.debug("Step 1: Load medoid from the dataset");
        loadMedoid();

        logger.debug("Step 2: Build the NSG graph");
        return buildNSG(baseVector, knnGraph);
    }

    private NSG buildNSG(float[][] baseVector, int[][] knnGraph) {
        long start = System.currentTimeMillis();
        List<Neighbor>[] nsg = new List[knnGraph.length];
        NSGSearchService nsgSearchService = new NSGSearchService(knnGraph, baseVector);
        AtomicInteger counter = new AtomicInteger();
        IntStream.range(0, knnGraph.length).parallel().forEach(v -> {
            List<Neighbor> nodeToConsiderForMRNGEdgeSelection =
                    nsgSearchService.getNeighborVisitedWhileSearching(medoid, v);
            Set<Integer> ids = new HashSet<>();
            for (Neighbor neighbor : nodeToConsiderForMRNGEdgeSelection)
                ids.add(neighbor.getId());
            for (int node : knnGraph[v]) {
                if (ids.add(node)) {
                    nodeToConsiderForMRNGEdgeSelection.add(new Neighbor(node,
                            squaredEuclideanDistance(baseVector[v], baseVector[node])));
                }
            }
            nsg[v] =
                    mrngNodeSelectionStrategy(baseVector, nodeToConsiderForMRNGEdgeSelection);

            if (counter.incrementAndGet() % 10000 == 0)
                logger.debug("Completed {} iterations", counter.get());
        });
        logger.debug("time taken for running MRNG edge selection on KNN: {}s", (System.currentTimeMillis() - start) * 1.0/1000);
        start = System.currentTimeMillis();
        logger.debug("Starting with adding backward edges");
        counter.set(0);
        IntStream.range(0, knnGraph.length).parallel().forEachOrdered(v -> {
            nsg[v].stream().parallel().forEach(neighbor -> {
                int neighborId = neighbor.getId();
                List<Neighbor> neighborsOfNeighbors = nsg[neighborId];
                if (neighborsOfNeighbors.stream().anyMatch(n -> n.getId() == v))
                    return;
                if (nsg[neighborId].size() >= M) {
                    nsg[neighborId] = mrngNodeSelectionStrategy(baseVector, nsg[neighborId]);
                } else {
                    nsg[neighborId].add(new Neighbor(v, neighbor.getSquaredEuclideanDistance()));
                }
            });
            if (counter.incrementAndGet() % 10000 == 0) {
                logger.debug("Completed {} iterations", counter.get());
            }
        });

        logger.debug("time taken to create backward edges: {}s", (System.currentTimeMillis() - start) * 1.0/1000);
        start = System.currentTimeMillis();

        boolean[] spanningVector = new boolean[knnGraph.length];
        dfs(nsg, spanningVector);
        logger.debug("time taken for DFS: {}s", (System.currentTimeMillis() - start) * 1.0/1000);
        start = System.currentTimeMillis();
        counter.set(0);
        IntStream.range(0, knnGraph.length).parallel().forEach(i -> {
            if (!spanningVector[i]) {
                int[] indexes = nsgSearchService.searchKNearestNeighbor(200, baseVector[i], medoid);
                for (int in : indexes) {
                    if (spanningVector[in]) {
                        nsg[in].add(new Neighbor(i, 0.0));
                        break;
                    }
                }
            }
            if (counter.incrementAndGet() % 10000 == 0)
                logger.debug("Completed {} iterations", counter.get());
        });
        logger.debug("time taken for building complete MSNET: {}s", (System.currentTimeMillis() - start) * 1.0/1000);
        start = System.currentTimeMillis();
        IntStream.range(0, knnGraph.length).parallel().forEach(i -> {
            this.nsg[i] = nsg[i].stream()
                    .mapToInt(Neighbor::getId)
                    .toArray();
        });
        logger.debug("time taken to convert list to array: {}s", (System.currentTimeMillis() - start) * 1.0/1000);
        NSG nsg1 = new NSG();
        nsg1.setNsgGraph(this.nsg);
        nsg1.setMedoid(this.medoid);
        return nsg1;
    }

    private List<Neighbor> mrngNodeSelectionStrategy(float[][] baseVector,
                                                     List<Neighbor> nodeToConsiderForMRNGEdgeSelection) {
        nodeToConsiderForMRNGEdgeSelection.sort(Comparator.comparing(Neighbor::getSquaredEuclideanDistance));
        List<Neighbor> list = new ArrayList<>(20);
        list.add(nodeToConsiderForMRNGEdgeSelection.get(0));

        int i = 1;
        while (i < nodeToConsiderForMRNGEdgeSelection.size() && list.size() < M) {
            Neighbor p = nodeToConsiderForMRNGEdgeSelection.get(i++);
            boolean add = true;
            for (Neighbor a : list) {
                if (a.getId() == p.getId() ||
                        p.getSquaredEuclideanDistance() > squaredEuclideanDistance(baseVector[p.getId()],
                                baseVector[a.getId()])) {
                    add = false;
                    break;
                }
            }
            if(add)
                list.add(p);
        }
        return list;
    }


    private void dfs(List<Neighbor>[] nsg, boolean[] spanningVector) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(medoid);
        spanningVector[medoid] = true;
        while (!queue.isEmpty()) {
            Integer i = queue.poll();
            for(Neighbor j : nsg[i]) {
                if (!spanningVector[j.getId()]) {
                    queue.add(j.getId());
                    spanningVector[j.getId()] = true;
                }
            }
        }
    }

    public void loadMedoid() {
        logger.debug("loading medoid");
        float[] centroid = findCentroid(baseVector);
        logger.debug("Centroid found: " + Arrays.toString(centroid));
        NSGSearchService nsgSearchService = new NSGSearchService(knnGraph, baseVector);

        medoid = nsgSearchService.searchKNearestNeighbor(1, centroid, new Random().nextInt(knnGraph.length))[0];
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
    public int getMedoid() {
        return medoid;
    }
}
