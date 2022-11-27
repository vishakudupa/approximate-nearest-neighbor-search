package org.anns.utils;

import java.util.Objects;

public class Neighbor implements Comparable<Neighbor> {
    private final int id;
    private final double squaredEuclideanDistance;
    public Neighbor(int first, double second) {
        this.id = first;
        this.squaredEuclideanDistance = second;
    }

    public int getId() {
        return id;
    }

    public double getSquaredEuclideanDistance() {
        return squaredEuclideanDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neighbor neighbor = (Neighbor) o;
        return id == neighbor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Neighbor o) {
        return Double.compare(this.squaredEuclideanDistance, o.squaredEuclideanDistance);
    }
}
