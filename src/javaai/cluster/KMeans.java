package javaai.cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class KMeans {
    public final static int MAX_ITERATIONS = 300;

    /** High index */
    public final static int HI = 1;

    /** Low index */
    public final static int LO = 0;

    protected List<Point1D> points = null;

    public static void main(final String[] args) {
        if(args.length < 2) {
            System.out.println("usage: KMeans data-path num-clusters");
            System.exit(0);
        }
        String path = args[0];
        Integer num = Integer.parseInt(args[1]);

        List<Double> list = load(path);
        List<Point1D> points = asPoints(list);

        KMeans km = new KMeans(points);
        List<Cluster<Point1D>> clusters = km.getClusters(num);
    }

    private static List<Point1D> asPoints(List<Double> list) {
        List<Point1D> points = new ArrayList<>();
        for(Double datum: list) {
            points.add(new Point1D(datum));
        }
        return points;
    }

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

    public KMeans(List<Point1D> points) {
        this.points = points;
    }

    public List<Cluster<Point1D>> getClusters(Integer n) {
        return getClusters(n,true);
    }

    public List<Cluster<Point1D>> getClusters(Integer n, Boolean details) {
        Point1D[] range = getRange(points);

        // TODO: make step a point if we have operator overloading
        double step = (range[HI].d - range[LO].d) / n;

        List<Cluster<Point1D>> clusters = new ArrayList<>();
        for(int k=0; k < n; k++) {
            clusters.add(new Cluster<Point1D>(new ArrayList<Point1D>(), new Point1D(range[HI].d - step*k)));
        }

        int epoch = 0;
        double oldEntropy = getEntropy(clusters);

        while(epoch < MAX_ITERATIONS) {
            for(int dataIndex=0; dataIndex < points.size(); dataIndex++) {
                Double shortest = Double.MAX_VALUE;

                Point1D datum = points.get(dataIndex);
                Cluster nearest = clusters.get(0);

                for(Cluster<Point1D> cluster: clusters) {
                    Double dist = cluster.centroid.distanceTo(datum);

                    if (dist < shortest) {
                        shortest = dist;
                        nearest = cluster;
                    }
                }

                nearest.add(datum);
            }


            double newEntropy = getEntropy(clusters);

            if(newEntropy == oldEntropy)
                break;

            oldEntropy = newEntropy;

            recenter(clusters);

            epoch++;
        }

        return clusters;
    }

    protected void recenter(List<Cluster<Point1D>> clusters) {
        for(Cluster<Point1D> cluster: clusters) {
            Point1D sum = new Point1D(0.0);
            for(Point1D pt: cluster.buffer) {
                sum = sum.add(pt);
            }
            Point1D centroid = sum.div((double)cluster.size());
            cluster.update(centroid);
            cluster.clear();
        }
    }

    protected long hash(List<Cluster<Point1D>> clusters) {
        Long h = 1L;
        for(Cluster cluster: clusters)
            h = h * cluster.size();
        return h;
    }

    protected double getEntropy(List<Cluster<Point1D>> clusters) {
        double total = 0;
        for(Cluster cluster: clusters)
            total += cluster.size();

        double h = 0;
        final double c = 3.32192809489;
        for(Cluster cluster: clusters) {
            double p = cluster.size() / total;
            h += -p * c * Math.log10(p);
        }

        return h;
    }

    /**
     * Gets the hi-lo range for a list.
     * @param points Points
     * @return 2-tuple of doubles for hi and low
     */
    protected static Point1D[] getRange(List<Point1D> points) {
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
}
