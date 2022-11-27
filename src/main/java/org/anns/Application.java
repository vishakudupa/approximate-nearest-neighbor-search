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
        NSGFacade nsgFacade = new NSGFacade(
                new Gson()
                        .fromJson(
                                new FileReader(
                                        "/Users/vishakudupa/IdeaProjects" +
                                                "/approximate-nearest-neighbor-search/config.json"),
                Config.class));
        nsgFacade.runOperation();
    }
}
