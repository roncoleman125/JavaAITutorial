package javaai.cluster;

import java.util.ArrayList;
import java.util.List;

public class KMeans {
    public final static int MAX_ITERATIONS = 300;

    /** High index */
    public final static int HI = 1;

    /** Low index */
    public final static int LO = 0;

    public List<Cluster<Point>> getClusters(List<Point> points, Integer numClusters, Boolean details) {
        Point[] range = getRange(points);

        // TODO: make step a point if we have operator overloading
        double step = (range[HI].d - range[LO].d) / numClusters;

        List<Cluster<Point>> clusters = new ArrayList<>();
        for(int k=0; k < numClusters; k++) {
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
