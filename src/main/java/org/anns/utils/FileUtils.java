package org.anns.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.anns.exceptions.ANNSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static final String BASE_PATH = "/Users/vishakudupa/Downloads/gist/";

    public static class Files {
        private final float[][] base;
        private final int[][] groundTruth;
        private final float[][] query;
        private final int n;

        public Files(float[][] base, int[][] groundTruth, float[][] query, int n) {
            this.base = base;
            this.groundTruth = groundTruth;
            this.query = query;
            this.n = n;
        }

        public float[][] getBase() {
            return base;
        }

        public int[][] getGroundTruth() {
            return groundTruth;
        }

        public float[][] getQuery() {
            return query;
        }

        public int getN() {
            return n;
        }
    }

    public static Files readFile() {
        String baseFile = BASE_PATH + "base_vectors.json";
        String groundTruthFile = BASE_PATH + "ground_truth_vectors.json";
        String queryFile = BASE_PATH + "query_vectors.json";

        try {
            logger.debug("Reading ground truth");
            int[][] groundTruth = new Gson().fromJson(new FileReader(groundTruthFile), new TypeToken<int[][]>() {}.getType());
            logger.debug("Reading query file");
            float[][] query = new Gson().fromJson(new FileReader(queryFile), new TypeToken<float[][]>() {}.getType());
            logger.debug("Reading base file");
            float[][] base = new Gson().fromJson(new FileReader(baseFile), new TypeToken<float[][]>() {}.getType());
            return new Files(base, groundTruth, query, base[0].length);
        } catch (FileNotFoundException e) {
            throw new ANNSException(e.getMessage());
        }
    }

    public static int[][] readNSG() {
        String baseFile = BASE_PATH + "sift_java_nsg.json";
        try {
            logger.debug("Reading NSG");
            return new Gson().fromJson(new FileReader(baseFile), new TypeToken<int[][]>() {}.getType());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] readKnn() {
        String baseFile = BASE_PATH + "knn.json";
        try {
            logger.debug("Reading knn");
            return new Gson().fromJson(new FileReader(baseFile), new TypeToken<int[][]>() {}.getType());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToFile(Object o, String fileName) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(BASE_PATH + fileName), StandardCharsets.UTF_8))) {
            writer.write(new Gson().toJson(o));
        } catch (Exception e) {
            //ignore
        }
    }
}
