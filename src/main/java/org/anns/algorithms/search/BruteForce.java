package org.anns.algorithms.search;

import org.anns.utils.DistanceUtils;
import org.anns.utils.FileUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BruteForce {

    public static void main(String[] args) {
        FileUtils.Files files = FileUtils.readFile();

        float[][] base = files.getBase();
        float[][] query = files.getQuery();

        for (int q = 0; q < query.length; q++) {
            PriorityQueue<Map.Entry<Integer, Double>> topK =
                    new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));
            long millis = System.currentTimeMillis();
            for (int i = 0; i < base.length; i++) {
                Map.Entry<Integer, Double> entry = Map.entry(i, DistanceUtils.squaredEuclideanDistance(base[i], files.getQuery()[q]));
                topK.add(entry);
                if (topK.size() > 100)
                    topK.poll();
            }
            List<Integer> groundTruth = Arrays.stream(files.getGroundTruth()[q]).boxed().toList();

            Set<Integer> set = topK
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(100)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            groundTruth.forEach(set::remove);
            System.out.println("Time taken : " + (System.currentTimeMillis() - millis) + " Accuracy: " + (100.0 - set.size()) + "%");
        }
    }

}