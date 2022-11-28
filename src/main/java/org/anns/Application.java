package org.anns;

import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import org.anns.algorithms.NSGFacade;
import org.anns.algorithms.build.NSGBuildService;
import org.anns.algorithms.search.NSGSearchService;
import org.anns.utils.Config;
import org.anns.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) throws FileNotFoundException {
        if (args == null || args.length == 0)
            throw new RuntimeException("Path to config file is required");
        logger.info("args : {}", args);
        NSGFacade nsgFacade = new NSGFacade(
                new Gson()
                        .fromJson(
                                new FileReader(args[0]),
                Config.class));
        nsgFacade.runOperation();
    }
}
