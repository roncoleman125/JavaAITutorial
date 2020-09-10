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

    protected List<Point> points = null;

    public static void main(final String[] args) {
        if(args.length < 2) {
            System.out.println("usage: KMeans data-path num-clusters");
            System.exit(0);
        }
        String path = args[0];
        Integer num = Integer.parseInt(args[1]);

        List<Double> list = load(path);
        List<Point> points = asPoints(list);

        KMeans km = new KMeans(points);
        List<Cluster<Point>> clusters = km.getClusters(num);
    }

    private static List<Point> asPoints(List<Double> list) {
        List<Point> points = new ArrayList<>();
        for(Double datum: list) {
            points.add(new Point(datum));
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

    public KMeans(List<Point> points) {
        this.points = points;
    }

    public List<Cluster<Point>> getClusters(Integer n) {
        return getClusters(n,true);
    }

    public List<Cluster<Point>> getClusters(Integer n, Boolean details) {
        Point[] range = getRange(points);

        // TODO: make step a point if we have operator overloading
        double step = (range[HI].d - range[LO].d) / n;

        List<Cluster<Point>> clusters = new ArrayList<>();
        for(int k=0; k < n; k++) {
            clusters.add(new Cluster<Point>(new ArrayList<Point>(), new Point(range[HI].d - step*k)));
        }

        int epoch = 0;
        Long oldHash = hash(clusters);

        while(epoch < MAX_ITERATIONS) {
            for(int dataIndex=0; dataIndex < points.size(); dataIndex++) {
                Double shortest = Double.MAX_VALUE;

                Point datum = points.get(dataIndex);
                Cluster nearest = clusters.get(0);

                for(Cluster<Point> cluster: clusters) {
                    Double dist = cluster.centroid.distanceTo(datum);

                    if (dist < shortest) {
                        shortest = dist;
                        nearest = cluster;
                    }
                }

                nearest.add(datum);
            }


            Long newHash = hash(clusters);

            if(newHash == oldHash)
                break;

            oldHash = newHash;

            recenter(clusters);

            epoch++;
        }

        return clusters;
    }

    protected void recenter(List<Cluster<Point>> clusters) {
        for(Cluster<Point> cluster: clusters) {
            Point sum = new Point(0.0);
            for(Point pt: cluster.buffer) {
                sum = sum.add(pt);
            }
            Point centroid = sum.div((double)cluster.size());
            cluster.update(centroid);
            cluster.clear();
        }
    }

    protected Long hash(List<Cluster<Point>> clusters) {
        Long h = 1L;
        for(Cluster cluster: clusters)
            h = h * cluster.size();
        return h;
    }

    /**
     * Gets the hi-lo range for a list.
     * @param points Points
     * @return 2-tuple of doubles for hi and low
     */
    protected static Point[] getRange(List<Point> points) {
        // Current high and low values
        Point[] range = {Point.getHi(), Point.getLo()};

        // Go through each value in the list
        for(Point point: points) {
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
