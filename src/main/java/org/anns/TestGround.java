package org.anns;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.anns.utils.LFU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class TestGround {

    private static final Logger logger = LoggerFactory.getLogger(TestGround.class);


    public static void main(String[] args) throws IOException {
        int SIZE = 1_000_000;
        LoadingCache<Integer, Integer> nsgGraph = CacheBuilder.newBuilder()
                .maximumSize(SIZE)
                .initialCapacity(SIZE)
                .recordStats()
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public Integer load(Integer key) throws Exception {
                        return key;
                    }
                });
        List<Integer> list = new ArrayList<>(SIZE);
        int[] ints = new int[SIZE];
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        LFU lfu = new LFU(SIZE);


        long l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            list.add(i);
            ints[i] = i;
            nsgGraph.getUnchecked(i);
            map.put(i, i);
            treeMap.put(i, i);
            lfu.addCacheEntry(i, String.valueOf(i));
        }
        logger.debug("Time taken to load: {}ms", (System.nanoTime() * 1.0 - l)/1000000);
        l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            int a = ints[i];
        }
        logger.debug("Time taken for array: {}ms", (System.nanoTime() * 1.0 - l)/1000000);
        l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            int a = list.get(i);
        }
        logger.debug("Time taken for list: {}ms", (System.nanoTime() * 1.0 - l)/1000000);
        l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            int a = nsgGraph.getUnchecked(i);
        }
        logger.debug("Time taken for cache: {}ms", (System.nanoTime() * 1.0 - l)/1000000);
        l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            int a = map.get(i);
        }
        logger.debug("Time taken for map: {}ms", (System.nanoTime() * 1.0 - l)/1000000);
        l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            int a = treeMap.get(i);
        }
        logger.debug("Time taken for treemap: {}ms", (System.nanoTime() * 1.0 - l)/1000000);
        l = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            String a = lfu.getCacheEntry(i);
        }
        logger.debug("Time taken for lfu: {}ms", (System.nanoTime() * 1.0 - l)/1000000);

//        FileReader fileReader = new FileReader("/Users/vishakudupa/Downloads/sift/sift_java_nsg.json");
//        List<Integer>[] nsg = new Gson().fromJson(fileReader,
//                new TypeToken<List<Integer>[]>(){}.getType());
//        fileReader.close();
//
//        for (int i = 0; i < nsg.length; i++) {
//            for (int j = 0; j < nsg[i].size(); j++) {
//                if (nsg[i].get(j) != i)
//                    nsg[nsg[i].get(j)].add(i);
//            }
//            logger.debug("running {}", i);
//        }

//        FileUtils.saveToFile(nsg, "/Users/vishakudupa/Downloads/sift/new_sift_java_nsg.json");

    }
}
