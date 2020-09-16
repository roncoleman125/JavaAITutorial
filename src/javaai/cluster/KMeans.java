/*
 Copyright (c) Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package javaai.cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the k-means cluster algorithm.
 * @author Ron.Coleman
 */
public class KMeans {
    public final static int MAX_ITERATIONS = 300;

    /** High index */
    public final static int HI = 1;

    /** Low index */
    public final static int LO = 0;

    protected List<Point1D> points = null;

    protected List<Cluster<Point1D>> clusters = new ArrayList<>();

    /**
     * Drives the k-means test.
     * @param args Arguments
     */
    public static void main(final String[] args) {
        // Validate inputs
        if(args.length < 2) {
            System.out.println("usage: "+KMeans.class.getSimpleName()+" data-path num-clusters");
            System.exit(0);
        }

        // Get number of clusters and path to the data
        Integer num = Integer.parseInt(args[0]);
        String path = args[1];

        // Load the data and convert them to points
        List<Double> list = load(path);
        List<Point1D> points = asPoints(list);

        // Do the cluster analysis
        KMeans km = new KMeans(points);

        km.train(num);

        km.report();
    }

    /**
     * Convert list of doubles to 1D points.
     * @param list List of doubles
     * @return List of points
     */
    private static List<Point1D> asPoints(List<Double> list) {
        List<Point1D> points = new ArrayList<>();
        for(Double datum: list) {
            points.add(new Point1D(datum));
        }
        return points;
    }

    /**
     * Extracts, transforms, and loads the source data from the file path.
     * @param path File path
     * @return List of doubles.
     */
    private static List<Double> load(String path) {
        List<Double> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path)));
            String line = null;
            while((line = br.readLine()) != null) {
                Double value = Double.parseDouble(line);
                list.add(value);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Constructor
     * @param points Points
     */
    public KMeans(List<Point1D> points) {
        this.points = points;
    }

    /**
     * Gets the clusters with details enabled.
     * @param n Number of clusters
     * @return List of clusters
     */
    public void train(Integer n) {
        train(n,true);
    }

    /**
     * Gets the clusters.
     * @param n Number of clusters
     * @param details Toggle for details.
     * @return List of clusters
     */
    public void train(Integer n, Boolean details) {
        Point1D[] range = getRange(points);

        Point1D step = range[HI].sub(range[LO]).div(n);

        for(int k=0; k < n; k++) {
            clusters.add(new Cluster<Point1D>(new ArrayList<Point1D>(), new Point1D(range[HI].sub(step.mult(k)))));
        }

        // Use entropy as the convergence criteria.
        double oldEntropy = getEntropy(clusters);

        // Since clusters might not converge, limit the number of iterations
        int iter = 0;
        while(iter < MAX_ITERATIONS) {
            // Place every point into a cluster
            for(int k=0; k < points.size(); k++) {
                // Shortest distance & cluster so far
                Double shortest = Double.MAX_VALUE;

                Point1D datum = points.get(k);
                Cluster nearest = clusters.get(0);

                // Test distance to centroid of every cluster
                for(Cluster<Point1D> cluster: clusters) {
                    Double dist = cluster.centroid.distanceTo(datum);

                    if (dist < shortest) {
                        shortest = dist;
                        nearest = cluster;
                    }
                }

                // Put this datum in the nearest cluster
                nearest.add(datum);
            }

            // If the entropy has not changed, the clusters have settled.
            double newEntropy = getEntropy(clusters);

            if(newEntropy == oldEntropy)
                break;

            // Update the entropy
            oldEntropy = newEntropy;

            if(details) {
                System.out.println("iteration: "+iter);
                report();
            }

            // Recenter clusters for data we just added to them.
            recenter();

            iter++;
        }
    }

    /**
     * Recenters clusters.
     */
    protected void recenter() {
        for(Cluster<Point1D> cluster: clusters) {
            Point1D centroid = recenter(cluster);

            cluster.update(centroid);

            cluster.clear();
        }
    }

    /**
     * Recenters a cluster.
     * @param cluster Cluster
     * @return Cluster centroid
     */
    protected Point1D recenter(Cluster<Point1D> cluster) {
        Point1D sum = Point1D.zero();

        for(Point1D pt: cluster.buffer) {
            sum = sum.add(pt);
        }
        Point1D centroid = sum.div((double)cluster.size());

        return centroid;
    }

    /**
     * Calculates information entropy of clusters.
     * @param clusters Clusters
     * @return Entropy
     */
    protected double getEntropy(List<Cluster<Point1D>> clusters) {
        double n = points.size();

        double h = 0;

        for(Cluster cluster: clusters) {
            double p = cluster.size() / n;

            if(p == 0)
                return 0;

            h += -p * log2(p);
        }

        return h;
    }

    /**
     * Calculates hash of clusters.
     * @param clusters Clusters
     * @return Hash
     */
    protected long hash(List<Cluster<Point1D>> clusters) {
        Long h = 1L;
        for(Cluster cluster: clusters)
            h = h * cluster.size();
        return h;
    }

    /**
     * Calculates log base 2
     * @param x Value
     * @return log base 2 value.
     */
    protected double log2(double x) {
        final double c = 3.32192809489;

        return c * Math.log10(x);
    }

    /**
     * Gets the hi-lo range for a list.
     * @param points Points
     * @return 2-tuple of doubles for hi and low
     */
    protected Point1D[] getRange(List<Point1D> points) {
        // Current high and low values
        Point1D[] range = {Point1D.getHi(), Point1D.getLo()};

        // Go through each value in the list
        for(Point1D point: points) {
            // If value above current high, update the high
            if(point.gt( range[HI]))
                range[HI] = point;

            // If value below current low, update the low.
            if(point.lt(range[LO]))
                range[LO] = point;
        }

        return range;
    }

    /**
     * Gets the clusters.
     * @return Cluster list
     */
    public List<Cluster<Point1D>> getClusters() {
        return clusters;
    }

    /**
     * Output the clusters.
     */
    protected void report() {
        System.out.printf("%3s %6s %6s %6s %4s %3s\n","#","lo","hi","center","size","%");
        for(int k=0; k < clusters.size(); k++) {
            Cluster<Point1D> cluster = clusters.get(k);
            Point1D[] range = getRange(cluster.buffer);
            int sz = cluster.size();
            int percent = (int) ((double)sz / points.size() * 100 + 0.5);
            System.out.printf("%3d %6s %6s %6s %4d %3d\n", k, range[LO], range[HI],cluster.centroid,sz,percent);
        }
    }
}
