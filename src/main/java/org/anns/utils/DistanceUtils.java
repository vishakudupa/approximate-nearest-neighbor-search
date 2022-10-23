package org.anns.utils;

public class DistanceUtils {
    public static double squaredEuclideanDistance(float[] a, float[] b) {
        double squaredDistance = 0d;
        for (int i = 0; i < a.length; i++) {
            float delta = a[i] - b[i];
            squaredDistance += (delta * delta);
        }
        return squaredDistance;
    }
}
