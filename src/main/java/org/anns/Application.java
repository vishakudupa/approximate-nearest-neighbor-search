package org.anns;

import org.anns.algorithms.build.NSGBuild;
import org.anns.algorithms.search.NSGSearch;
import org.anns.utils.FileUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
        build();
//        search();

    }

    private static void build() {
        FileUtils.Files files = FileUtils.readFile();
        int[][] knnGraph = FileUtils.readKnn();
        NSGBuild nsgBuild = new NSGBuild(files.getBase(), knnGraph);
        search(nsgBuild);
    }

    private static void search(NSGBuild nsgBuild) {
        FileUtils.Files files = FileUtils.readFile();
        NSGSearch nsgSearch = new NSGSearch(nsgBuild.getNsg(), files.getBase());
//        NSGSearch nsgSearch = new NSGSearch(FileUtils.readKnn(), files.getBase());

        for (int i = 5; i <= 70; i += 5) {
            long millis = System.currentTimeMillis();
            nsgSearch.setL(i);
            double accuracy = 0;
            for (int q = 0; q < files.getQuery().length; q++) {

                int[] vectors = nsgSearch.searchKNearestNeighbor(100, files.getQuery()[q]);

                List<Integer> groundTruth = Arrays.stream(files.getGroundTruth()[q]).boxed().toList();
                Set<Integer> topKVectorIndex = Arrays.stream(vectors).boxed().collect(Collectors.toSet());
                groundTruth.forEach(topKVectorIndex::remove);
                int v = 100 - topKVectorIndex.size();
                accuracy += v;
            }

            System.out.println("Accuracy: " + accuracy/files.getQuery().length
                    + " QPS: " + 10000000 / (System.currentTimeMillis() - millis) + " L: " + i);
        }
    }
}
