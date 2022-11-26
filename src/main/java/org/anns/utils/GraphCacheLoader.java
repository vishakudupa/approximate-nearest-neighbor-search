package org.anns.utils;

import com.google.common.cache.CacheLoader;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GraphCacheLoader extends CacheLoader<Integer, int[]> {

    private static final Logger logger = LoggerFactory.getLogger(GraphCacheLoader.class);
    private static final Gson gson = new Gson();
    private int[] lineOffsets;

    private final RandomAccessFile randomAccessFile;

    public GraphCacheLoader(String fileName, String lineOffSets) {
        try {
            FileReader fileReader = new FileReader(lineOffSets);
            lineOffsets = new Gson().fromJson(fileReader, int[].class);
            fileReader.close();
            randomAccessFile = new RandomAccessFile(fileName, "r");
        } catch (IOException e) {
            logger.error("error occured while reading files", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int[] load(Integer index) throws Exception {
        randomAccessFile.seek(lineOffsets[index]);
        String json = randomAccessFile.readLine();
        return gson.fromJson(json, int[].class);
    }
}
