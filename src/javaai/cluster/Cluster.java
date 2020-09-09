package javaai.cluster;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    public List<Double> buffer = new ArrayList<>();
    public double centroid;

    public Cluster(List<Double> buffer,double centroid) {
        this.buffer = buffer;
        this.centroid = centroid;
    }
}
