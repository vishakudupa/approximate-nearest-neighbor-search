package org.anns.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.anns.exceptions.ANNSException;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static final String BASE_PATH = "/Users/vishakudupa/Downloads/sift/";



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
            System.out.println("Reading ground truth");
            int[][] groundTruth = new Gson().fromJson(new FileReader(groundTruthFile), new TypeToken<int[][]>() {}.getType());
            System.out.println("Reading query file");
            float[][] query = new Gson().fromJson(new FileReader(queryFile), new TypeToken<float[][]>() {}.getType());
            System.out.println("Reading base file");
            float[][] base = new Gson().fromJson(new FileReader(baseFile), new TypeToken<float[][]>() {}.getType());
            return new Files(base, groundTruth, query, base[0].length);
        } catch (FileNotFoundException e) {
            throw new ANNSException(e.getMessage());
        }
    }

    public static int[][] readNSG() {
        String baseFile = BASE_PATH + "sift_nsg_vector.json";
        try {
            System.out.println("Reading NSG");
            return new Gson().fromJson(new FileReader(baseFile), new TypeToken<int[][]>() {}.getType());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] readKnn() {
        String baseFile = BASE_PATH + "knn.json";
        try {
            System.out.println("Reading knn");
            return new Gson().fromJson(new FileReader(baseFile), new TypeToken<int[][]>() {}.getType());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
