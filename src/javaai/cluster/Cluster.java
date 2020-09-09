package javaai.cluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster<T> {
    public List<T> buffer = new ArrayList<>();
    public T centroid;

    public Cluster(List<T> buffer,T centroid) {
        this.buffer = buffer;
        this.centroid = centroid;
    }

    public void update(T centroid) {
        this.centroid = centroid;
    }

    public void add(T datum) {
        buffer.add(datum);
    }

    public Integer size() {
        return buffer.size();
    }

    public void clear() {
        buffer.clear();
    }
}
