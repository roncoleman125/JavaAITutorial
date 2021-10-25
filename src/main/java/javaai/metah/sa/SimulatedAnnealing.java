package javaai.metah.sa;

import java.util.Collections;
import java.util.List;
import java.util.Random;


public class SimulatedAnnealing {
    private static double temperature = 1000;
    private static double coolingFactor = 0.995;
    private static Random ran = new Random(0);
    public static final int NUM_CITIES = 10;

    public static void main(String[] args) {
        List<City> cities = City.get(NUM_CITIES);

        Tour current = new Tour(cities);
        Tour best = current.duplicate();

        int epoch = 1;
        for (double t = temperature; t > 1; t *= coolingFactor, epoch++) {
            Tour neighbor = current.duplicate();

            int index1 = (int) (neighbor.noCities() * Math.random());
            int index2 = (int) (neighbor.noCities() * Math.random());

//            Collections.swap(next.getCities(), index1, index2);
            Collections.swap(cities, index1, index2);

            int currentLength = current.getTourLength();
            int neighborLength = neighbor.getTourLength();

            if (Math.random() < Util.probability(currentLength, neighborLength, t)) {
                current = neighbor.duplicate();
            }

            if (current.getTourLength() < best.getTourLength()) {
                best = current.duplicate();
            }
            System.out.println("epoch: "+epoch+" length: "+currentLength+" best: "+best.getTourLength());
        }

        System.out.println("Final tour length: " + best.getTourLength());
        System.out.println("Tour: " + best);
    }
}
