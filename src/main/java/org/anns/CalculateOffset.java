package org.anns;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CalculateOffset {

    public static final String SEPARATOR = "line.separator";

    public static void main(String[] args) throws Exception {
        List<Integer> fileOffSet = getFileOffSet("/Users/vishakudupa/Downloads/sift/knn_line_by_line.json");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("/Users/vishakudupa/Downloads/sift/knn_line_offset.json"), StandardCharsets.UTF_8))) {
            writer.write(new Gson().toJson(fileOffSet));
        } catch (Exception e) {
            //ignore
        }
    }
    public static List<Integer> getFileOffSet(String fileName) throws Exception {
        File readFile = new File(fileName);
        FileReader fileReader = new FileReader(readFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<Integer> lineOffset = new ArrayList<>();
        lineOffset.add(0);
        String line = bufferedReader.readLine();
        while (line != null) {
            lineOffset.add(System.getProperty(SEPARATOR).length() + line.length() + lineOffset.get(lineOffset.size() - 1));
            line = bufferedReader.readLine();
        }
        return lineOffset;
    }
}
