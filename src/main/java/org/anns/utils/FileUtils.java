package org.anns.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.anns.algorithms.NSG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    public static Files loadFiles(Config config) throws FileNotFoundException {
        Files files = new Files();
        if (!Strings.isNullOrEmpty(config.getBaseVectorPath())) {
            logger.debug("Loading : {}", config.getBaseVectorPath());
            files.setBase(new Gson().fromJson(new FileReader(config.getBasePath() + config.getBaseVectorPath()), new TypeToken<float[][]>() {}.getType()));
        }
        if (!Strings.isNullOrEmpty(config.getGroundTruthPath())) {
            logger.debug("Loading : {}", config.getGroundTruthPath());
            files.setGroundTruth(new Gson().fromJson(new FileReader(config.getBasePath() + config.getGroundTruthPath()), new TypeToken<int[][]>() {}.getType()));
        }
        if (!Strings.isNullOrEmpty(config.getQueryPath())) {
            logger.debug("Loading : {}", config.getQueryPath());
            files.setQuery(new Gson().fromJson(new FileReader(config.getBasePath() + config.getQueryPath()), new TypeToken<float[][]>() {}.getType()));
        }
        if (!Strings.isNullOrEmpty(config.getKnnGraphPath()) && (config.getOperationType() == Config.Operation.BUILD
                || config.getOperationType() == Config.Operation.BUILD_AND_SEARCH)) {
            logger.debug("Loading : {}", config.getKnnGraphPath());
            files.setKnn(new Gson().fromJson(new FileReader(config.getBasePath() + config.getKnnGraphPath()), new TypeToken<int[][]>() {}.getType()));
        }
        if (!Strings.isNullOrEmpty(config.getLoadNSGFilePath()) && (config.getOperationType() == Config.Operation.SEARCH
                || config.getOperationType() == Config.Operation.BUILD_AND_SEARCH)) {
            logger.debug("Loading : {}", config.getLoadNSGFilePath());
            files.setNsg(new Gson().fromJson(new FileReader(config.getBasePath() + config.getLoadNSGFilePath()), NSG.class));
        }
        return files;
    }

    public static class Files {
        private float[][] base;
        private int[][] groundTruth;
        private float[][] query;
        private NSG nsg;

        private int[][] knn;

        public int[][] getKnn() {
            return knn;
        }

        public void setKnn(int[][] knn) {
            this.knn = knn;
        }

        public NSG getNsg() {
            return nsg;
        }

        public void setNsg(NSG nsg) {
            this.nsg = nsg;
        }

        public float[][] getBase() {
            return base;
        }

        public void setBase(float[][] base) {
            this.base = base;
        }

        public int[][] getGroundTruth() {
            return groundTruth;
        }

        public void setGroundTruth(int[][] groundTruth) {
            this.groundTruth = groundTruth;
        }

        public float[][] getQuery() {
            return query;
        }

        public void setQuery(float[][] query) {
            this.query = query;
        }
    }

    public static void saveToFile(Object o, String filePath) {
        logger.info("Saving to file {}", filePath);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(new Gson().toJson(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
