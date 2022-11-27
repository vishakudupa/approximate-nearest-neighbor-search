package org.anns.utils;

public class Config {
    public enum Operation {
        SEARCH, BUILD, BUILD_AND_SEARCH
    }
    private Operation operationType;

    private String basePath;
    private String knnGraphPath;
    private String baseVectorPath;
    private String queryPath;
    private String groundTruthPath;
    private String saveNSGFilePath;
    private String loadNSGFilePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getLoadNSGFilePath() {
        return loadNSGFilePath;
    }

    public void setLoadNSGFilePath(String loadNSGFilePath) {
        this.loadNSGFilePath = loadNSGFilePath;
    }

    public Operation getOperationType() {
        return operationType;
    }

    public void setOperationType(Operation operationType) {
        this.operationType = operationType;
    }

    public String getKnnGraphPath() {
        return knnGraphPath;
    }

    public void setKnnGraphPath(String knnGraphPath) {
        this.knnGraphPath = knnGraphPath;
    }

    public String getBaseVectorPath() {
        return baseVectorPath;
    }

    public void setBaseVectorPath(String baseVectorPath) {
        this.baseVectorPath = baseVectorPath;
    }

    public String getQueryPath() {
        return queryPath;
    }

    public void setQueryPath(String queryPath) {
        this.queryPath = queryPath;
    }

    public String getGroundTruthPath() {
        return groundTruthPath;
    }

    public void setGroundTruthPath(String groundTruthPath) {
        this.groundTruthPath = groundTruthPath;
    }

    public String getSaveNSGFilePath() {
        return saveNSGFilePath;
    }

    public void setSaveNSGFilePath(String saveNSGFilePath) {
        this.saveNSGFilePath = saveNSGFilePath;
    }
}
