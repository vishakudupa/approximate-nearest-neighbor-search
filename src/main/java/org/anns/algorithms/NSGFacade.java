package org.anns.algorithms;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.AtomicDouble;
import org.anns.Application;
import org.anns.algorithms.build.NSGBuildService;
import org.anns.algorithms.search.NSGSearchService;
import org.anns.utils.Config;
import org.anns.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NSGFacade {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private Config config;
    private FileUtils.Files files;
    public NSGFacade(Config config) throws FileNotFoundException {
        this.config = config;

        if (config.getBasePath() == null)
            throw new RuntimeException("Base Path is required");

        if (config.getOperationType() == null)
            throw new RuntimeException("Operator Path is required");

        switch (config.getOperationType()) {
            case BUILD:
                if (Strings.isNullOrEmpty(config.getKnnGraphPath()) ||
                        Strings.isNullOrEmpty(config.getBaseVectorPath())||
                        Strings.isNullOrEmpty(config.getSaveNSGFilePath())
                    )
                    throw new RuntimeException("KNN, Base Vector and savepath are required");
                break;
            case SEARCH:
                if (Strings.isNullOrEmpty(config.getLoadNSGFilePath()) ||
                        Strings.isNullOrEmpty(config.getQueryPath())||
                        Strings.isNullOrEmpty(config.getBaseVectorPath()) ||
                        Strings.isNullOrEmpty(config.getGroundTruthPath()))
                    throw new RuntimeException("Base Vector, NSG, Query are required");
                break;
            case BUILD_AND_SEARCH:
                if (Strings.isNullOrEmpty(config.getKnnGraphPath()) ||
                        Strings.isNullOrEmpty(config.getBaseVectorPath())||
                        Strings.isNullOrEmpty(config.getSaveNSGFilePath())
                )
                    throw new RuntimeException("KNN, Base Vector and savepath are required");
                if (Strings.isNullOrEmpty(config.getQueryPath())||
                        Strings.isNullOrEmpty(config.getGroundTruthPath()))
                    throw new RuntimeException("NSG, Query are required");
                break;
        }
        files = FileUtils.loadFiles(config);
    }

    public void runOperation() {
        switch (config.getOperationType()) {
            case BUILD: {
                NSGBuildService nsgBuildService = new NSGBuildService(files.getBase(), files.getKnn());
                NSG nsg = nsgBuildService.build();
                FileUtils.saveToFile(nsg, config.getBasePath() + config.getSaveNSGFilePath());
            }
            break;
            case SEARCH: search(files.getNsg(), files);
            break;
            case BUILD_AND_SEARCH: {
                NSGBuildService nsgBuildService1 = new NSGBuildService(files.getBase(), files.getKnn());
                NSG nsg = nsgBuildService1.build();
                FileUtils.saveToFile(nsg, config.getBasePath() + config.getSaveNSGFilePath());
                search(nsg, files);
            }
            break;
        }
    }

    private void search(NSG nsg, FileUtils.Files files) {
        NSGSearchService nsgSearchService = new NSGSearchService(nsg.getNsgGraph(), files.getBase());
        int medoid = nsg.getMedoid();
        List<List<Double>> result = new ArrayList<>();

        logger.info("Starting search for pre-defined {} queries", files.getQuery().length);
        for (int i = 10; i <= 200; i += 10) {
            long millis = System.currentTimeMillis();
            nsgSearchService.setL(i);
            AtomicDouble accuracy = new AtomicDouble();
            IntStream queryStream = IntStream.range(0, files.getQuery().length);
            if (config.getSearchType() != null && config.getSearchType().equals(Config.SearchType.PARALLEL))
                queryStream = queryStream.parallel();
            queryStream.parallel().forEach(q -> {
                int[] vectors = nsgSearchService.searchKNearestNeighbor(100, files.getQuery()[q], medoid);

                List<Integer> groundTruth = Arrays.stream(files.getGroundTruth()[q]).boxed().collect(Collectors.toList());
                Set<Integer> topKVectorIndex = Arrays.stream(vectors).boxed().collect(Collectors.toSet());
                groundTruth.forEach(topKVectorIndex::remove);
                int v = 100 - topKVectorIndex.size();
                accuracy.addAndGet(v);
            });
            long rpm = 1000L * files.getQuery().length / (System.currentTimeMillis() - millis);
            logger.info("Accuracy: " + accuracy.get()/files.getQuery().length
                    + " QPS: " + rpm + " L: " + i);
            result.add(List.of(accuracy.get()/files.getQuery().length, (double) rpm));
        }
        logger.info("Result : {}", result);
    }
}
