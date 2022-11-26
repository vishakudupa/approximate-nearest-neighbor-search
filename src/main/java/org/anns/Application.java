package org.anns;

import com.google.gson.Gson;
import org.anns.algorithms.build.NSGBuild;
import org.anns.algorithms.search.NSGSearch;
import org.anns.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) {
        build();
//        FileUtils.Files files = FileUtils.readFile();
//        search(FileUtils.readNSG(), files, 123742);
//        search(new int[0][], files);

    }

    private static void build() {
        FileUtils.Files files = FileUtils.readFile();
        int[][] knnGraph = FileUtils.readKnn();
        NSGBuild nsgBuild = new NSGBuild(files.getBase(), knnGraph);

        search(nsgBuild.getNsg(), files, nsgBuild.getMedoid());
    }

    private static void search(int[][] nsg, FileUtils.Files files, int medoid) {
        NSGSearch nsgSearch = new NSGSearch(nsg, files.getBase());
//        NSGSearch nsgSearch = new NSGSearch(FileUtils.readKnn(), files.getBase());

        logger.info("Starting search for pre-defined {} queries", files.getQuery().length);
        for (int i = 5; i <= 70; i += 5) {
            long millis = System.currentTimeMillis();
            nsgSearch.setL(i);
            double accuracy = 0;
            for (int q = 0; q < files.getQuery().length; q++) {
                int[] vectors = nsgSearch.searchKNearestNeighbor(100, files.getQuery()[q], medoid);

                List<Integer> groundTruth = Arrays.stream(files.getGroundTruth()[q]).boxed().toList();
                Set<Integer> topKVectorIndex = Arrays.stream(vectors).boxed().collect(Collectors.toSet());
                if (topKVectorIndex.size() != 100)
                    logger.debug("Size set {}", topKVectorIndex.size());
                groundTruth.forEach(topKVectorIndex::remove);
                int v = 100 - topKVectorIndex.size();
                accuracy += v;
            }
            logger.info("Accuracy: " + accuracy/files.getQuery().length
                    + " QPS: " + 10_000_000 / (System.currentTimeMillis() - millis) + " L: " + i);
        }
    }
}
