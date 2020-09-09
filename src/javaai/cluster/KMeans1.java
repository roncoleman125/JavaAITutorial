package javaai.cluster;

import java.util.ArrayList;
import java.util.List;

public class KMeans1 {
    /** High index */
    public final static int HI = 1;

    /** Low index */
    public final static int LO = 0;

    public List<Cluster> getClusters(List<Double> data, Integer numClusters, Boolean details) {

        double[] range = getRange(data);
        double step = (range[HI] - range[LO]) / numClusters;

        List<Cluster> clusters = new ArrayList<>();
        for(int k=0; k < numClusters; k++) {
            clusters.add(new Cluster(new ArrayList<Double>(), range[HI] - step*k));
        }

        return null;
    }

    protected List<Integer> asList(Integer len, Integer def) {
        List<Integer> list = new ArrayList<>();
        for(int k=0; k < len; k++)
            list.add(def);
        return list;
    }

    /**
     * Gets the hi-lo range for a list.
     * @param list List
     * @return 2-tuple of doubles for hi and low
     */
    protected static double[] getRange(List<Double> list) {
        // Current high and low values
        double[] range = {Double.MAX_VALUE, -Double.MAX_VALUE};

        // Go through each value in the list
        for(Double value: list) {
            // If value above current high, update the high
            if(value > range[HI])
                range[HI] = value;

            // If value below current low, update the low.
            if(value < range[LO])
                range[LO] = value;
        }

        return range;
    }
}
