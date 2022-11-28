# How to get the datastes?
http://corpus-texmex.irisa.fr/ here all the datasets can be retrieved, but as all the datasets require pre-processing, we have created a preprocessed dataset and uploaded it to one drive. 
One Drive link for siftsmall: https://fsu-my.sharepoint.com/:u:/g/personal/vu22_fsu_edu/EfIb6MzmbSZErlr09FQn90sBvLyWCE0WXDg_qzaBSAJ2BA?e=hG7lzr \
One Drive link for sift: https://fsu-my.sharepoint.com/:u:/g/personal/vu22_fsu_edu/EZjWHWQLPNhLlUnwIiErdVUBamiTkLB0IC9XUuDT5QgAhg?e=a5Kd6v

Extract the dataset into a folder and copy the path to the folder.
Now, if you look at the config.json file in the root directory it would look something like this.
\
``` 
{
"operationType": "BUILD_AND_SEARCH",
"basePath" : "/Users/vishakudupa/IdeaProjects/anns_datasets/siftsmall/",
"knnGraphPath" : "knn.json",
"baseVectorPath" : "base_vectors.json",
"queryPath" : "query_vectors.json",
"groundTruthPath": "ground_truth_vectors.json",
"saveNSGFilePath": "sift_from_java_new.json",
"loadNSGFilePath": "sift_from_java_new.json"
} 
```

In this three types of operators are supported: 1. BUILD, SEARCH, BUILD_AND_SEARCH. So, choose any of the operator. SEARCH would run the pre-defined queries and validate against the ground truth. BUILD would build the graph and save with the name saveNSGFilePath. SEARCH_AND_BUILD would do both of these things. \
After this you would need to paste the copied path to the extracted folder including the ending '/'. 
For simplicity, we have kept all the json files name same across the datasets, so that those things does not have to mentioned again. \
We have also included a pre-built graph, which can be directly run on the query set and verified against the ground truth. As It might take around 400s(On macbook) to build the graph. This number varies heavily on the number of cores and the type of processor.\
If you want to test the whole code, I would recommend running siftsmall as it requires very less time to build the graph. 

# How to run the code?
To make things simpler we have built the project using maven.\
So, first you would need to install maven, it can be done by running `sudo apt-get install maven` or `brew install maven`. 
After that to compile and package the project run `mvn clean package` \
And finally, to run the code `java -Xmx4096m -jar <base_path>/approximate-nearest-neighbor-search/target/approximate-nearest-neighbor-search-1.0-SNAPSHOT-jar-with-dependencies.jar config.json` where `config.json` is the config file.

# The output should look something like this
```
2022-11-27 22:34:31,652 [INFO ] [main] - args : config.json
2022-11-27 22:34:31,738 [DEBUG] [main] - Loading : base_vectors.json
2022-11-27 22:34:32,040 [DEBUG] [main] - Loading : ground_truth_vectors.json
2022-11-27 22:34:32,057 [DEBUG] [main] - Loading : query_vectors.json
2022-11-27 22:34:32,066 [DEBUG] [main] - Loading : knn.json
2022-11-27 22:34:32,111 [DEBUG] [main] - Loading : sift_from_java_new.json
2022-11-27 22:34:32,141 [DEBUG] [main] - building NSG from KNN graph
2022-11-27 22:34:32,141 [DEBUG] [main] - Step 1: Load medoid from the dataset
2022-11-27 22:34:32,141 [DEBUG] [main] - loading medoid
2022-11-27 22:34:32,149 [DEBUG] [main] - Centroid found: [29.054651, 16.785505, 10.708337, 9.764523, 11.308627, 13.300035, 15.288352, 17.619198, 32.840225, 31.009216, 35.90999, 21.50898, 16.00538, 28.09364, 32.125, 22.923843, 36.247986, 22.53412, 36.420395, 29.186214, 16.463156, 19.899763, 30.53054, 34.248325, 27.013966, 15.566971, 17.084585, 17.197092, 14.266072, 9.911511, 9.4123125, 17.454088, 56.877045, 24.603748, 13.720941, 16.600628, 22.062578, 27.747578, 24.728745, 27.44944, 61.25285, 41.69714, 36.553413, 23.185299, 23.075014, 37.34253, 35.133087, 30.17916, 58.946373, 25.034664, 40.73815, 40.789043, 26.500282, 23.02099, 29.470758, 45.474735, 51.758152, 20.661997, 24.361755, 31.922735, 30.068012, 20.075073, 14.327928, 28.16413, 56.22999, 20.610916, 23.896109, 26.348307, 22.603096, 18.007624, 14.595493, 29.84178, 62.9649, 24.63266, 35.6167, 34.456306, 22.788483, 23.76455, 33.19221, 49.40977, 57.792828, 37.628773, 32.40933, 22.223785, 26.90709, 43.55833, 39.67902, 29.810741, 52.783207, 23.480097, 14.266836, 19.176504, 28.799925, 32.971268, 25.821377, 26.553654, 28.621765, 15.458603, 16.77534, 14.228924, 11.778851, 9.043198, 9.502526, 18.150036, 36.72385, 21.609892, 33.162025, 25.907982, 15.449086, 20.737194, 33.755936, 36.19272, 32.264744, 29.110893, 32.918606, 20.323772, 16.624514, 31.502934, 35.22045, 22.394539, 28.10226, 15.747117, 10.476509, 10.448345, 13.39396, 15.767845, 16.265219, 17.000645]
2022-11-27 22:34:32,167 [DEBUG] [main] - Setting medoid to : 3732
2022-11-27 22:34:32,167 [DEBUG] [main] - Step 2: Build the NSG graph
2022-11-27 22:34:34,554 [DEBUG] [main] - Completed 10000 iterations
2022-11-27 22:34:34,555 [DEBUG] [main] - time taken for running MRNG edge selection on KNN: 2.388s
2022-11-27 22:34:34,556 [DEBUG] [main] - Starting with adding backward edges
2022-11-27 22:34:34,852 [DEBUG] [main] - Completed 10000 iterations
2022-11-27 22:34:34,852 [DEBUG] [main] - time taken to create backward edges: 0.296s
2022-11-27 22:34:34,881 [DEBUG] [main] - time taken for DFS: 0.029s
2022-11-27 22:34:34,935 [DEBUG] [ForkJoinPool.commonPool-worker-7] - Completed 10000 iterations
2022-11-27 22:34:34,935 [DEBUG] [main] - time taken for building complete MSNET: 0.054s
2022-11-27 22:34:34,959 [DEBUG] [main] - time taken to convert list to array: 0.024s
2022-11-27 22:34:34,960 [INFO ] [main] - Saving to file /Users/vishakudupa/IdeaProjects/anns_datasets/siftsmall/sift_from_java_new.json
2022-11-27 22:34:35,026 [INFO ] [main] - Starting search for pre-defined 100 queries
2022-11-27 22:34:35,067 [INFO ] [main] - Accuracy: 55.13 QPS: 3333 L: 10
2022-11-27 22:34:35,095 [INFO ] [main] - Accuracy: 72.85 QPS: 3703 L: 20
2022-11-27 22:34:35,131 [INFO ] [main] - Accuracy: 83.16 QPS: 2777 L: 30
2022-11-27 22:34:35,175 [INFO ] [main] - Accuracy: 89.32 QPS: 2272 L: 40
2022-11-27 22:34:35,233 [INFO ] [main] - Accuracy: 93.35 QPS: 1724 L: 50
2022-11-27 22:34:35,298 [INFO ] [main] - Accuracy: 95.51 QPS: 1538 L: 60
2022-11-27 22:34:35,371 [INFO ] [main] - Accuracy: 97.25 QPS: 1369 L: 70
2022-11-27 22:34:35,479 [INFO ] [main] - Accuracy: 97.98 QPS: 925 L: 80
2022-11-27 22:34:35,586 [INFO ] [main] - Accuracy: 98.69 QPS: 934 L: 90
2022-11-27 22:34:35,699 [INFO ] [main] - Accuracy: 98.97 QPS: 884 L: 100
2022-11-27 22:34:35,815 [INFO ] [main] - Accuracy: 99.26 QPS: 862 L: 110
2022-11-27 22:34:35,934 [INFO ] [main] - Accuracy: 99.43 QPS: 840 L: 120
2022-11-27 22:34:36,065 [INFO ] [main] - Accuracy: 99.58 QPS: 763 L: 130
2022-11-27 22:34:36,208 [INFO ] [main] - Accuracy: 99.68 QPS: 704 L: 140
2022-11-27 22:34:36,364 [INFO ] [main] - Accuracy: 99.74 QPS: 641 L: 150
2022-11-27 22:34:36,533 [INFO ] [main] - Accuracy: 99.83 QPS: 591 L: 160
2022-11-27 22:34:36,725 [INFO ] [main] - Accuracy: 99.86 QPS: 520 L: 170
2022-11-27 22:34:36,923 [INFO ] [main] - Accuracy: 99.89 QPS: 507 L: 180
2022-11-27 22:34:37,142 [INFO ] [main] - Accuracy: 99.91 QPS: 460 L: 190
2022-11-27 22:34:37,374 [INFO ] [main] - Accuracy: 99.91 QPS: 431 L: 200
2022-11-27 22:34:37,375 [INFO ] [main] - Result : [[55.13, 3333.0], [72.85, 3703.0], [83.16, 2777.0], [89.32, 2272.0], [93.35, 1724.0], [95.51, 1538.0], [97.25, 1369.0], [97.98, 925.0], [98.69, 934.0], [98.97, 884.0], [99.26, 862.0], [99.43, 840.0], [99.58, 763.0], [99.68, 704.0], [99.74, 641.0], [99.83, 591.0], [99.86, 520.0], [99.89, 507.0], [99.91, 460.0], [99.91, 431.0]]
```

# Thanks to the original authors of the paper
This project is a Java implementation of https://arxiv.org/abs/1707.00143. 

# Thank You for trying out the project. 
