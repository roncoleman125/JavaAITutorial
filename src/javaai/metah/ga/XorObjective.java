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
package javaai.metah.ga;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.genetic.genome.DoubleArrayGenome;
import java.util.Random;

/**
 * This class implements the objective function used by Encog's double-array genome class.
 * @author Ron.Coleman
 */
public class XorObjective implements CalculateScore {
    public final static boolean DEBUGGING = true;
    public final static String TEAM = "Ronz";
    public final static double RANGE_MAX = 10.0;
    public final static double RANGE_MIN = -10.0;
    protected static Random ran = null;

    static {
        long seed = System.nanoTime();
        if(DEBUGGING)
            seed = TEAM.hashCode();
        ran = new Random(seed);
    }

    /** The input data for XOR */
    public static double XOR_INPUTS[][] = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
    };

    /** The ideal data for XOR. */
    public static double XOR_IDEALS[][] = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}
    };

    /** Weight names */
    public static String[] wtNames = {"w1", "w2", "w3", "w4", "w5", "w6", "b1", "b2"};

    /**
     * Calculates the phenotypes fitness.
     * @param phenotype Phenotype
     * @return Fitness score
     */
    @Override
    public double calculateScore(MLMethod phenotype) {
        DoubleArrayGenome genome = (DoubleArrayGenome)phenotype;

        double[] ws = genome.getData();

        return getFitness(ws);
    }

    /**
     * Gets the fitness of weights.
     * @param ws Weights
     * @return Fitness value
     */
    public double getFitness(double[] ws) {
        // Sum of square error
        double sumSqrErr = 0.0;

        for(int k = 0; k < XOR_INPUTS.length; k++) {
            double x1 = XOR_INPUTS[k][0];
            double x2 = XOR_INPUTS[k][1];

            double y1 = feedforward(x1, x2, ws);
            double t1 = XOR_IDEALS[k][0];

            // Square error
            double delta = (y1 - t1);
            double sqrError =  delta * delta;

            // Summ the square error
            sumSqrErr += sqrError;
        }

        double rmse = Math.sqrt(sumSqrErr / XOR_INPUTS.length);

        return rmse;
    }

    /**
     * Tells GA the objective.
     * @return True to minimize
     */
    @Override
    public boolean shouldMinimize() {
        return true;
    }

    /**
     * Tells GA if we need single threading--might be useful for debugging.
     * @return True to run single threaded.
     */
    @Override
    public boolean requireSingleThreaded() {
        return true;
    }

    /**
     * Runs the inputs through the feedforward equations.
     * @param x1 X1 input
     * @param x2 X2 input
     * @param ws Weights w1-w6 and b1 and b2 -- in this order.
     * @return Actual, that is, Y1
     */
    public double feedforward(double x1, double x2, double[] ws) {
        double w1 = ws[0];
        double w2 = ws[1];
        double w3 = ws[2];
        double w4 = ws[3];
        double w5 = ws[4];
        double w6 = ws[5];
        double b1 = ws[6];
        double b2 = ws[7];

        double zh1 = w1*x1 + w3*x2 + b1*1.0;
        double zh2 = w2*x1 + w4*x2 + b1*1.0;
        double h1 = sigmoid(zh1);
        double h2 = sigmoid(zh2);
        double zy1 = w5*h1 + w6*h2 + b2*1.0;
        double y1 = sigmoid(zy1);

        return y1;
    }

    /**
     * Sigmoid activation function
     * @param z Input
     * @return sigmoid of z
     */
    protected double sigmoid(double z) {
        double y = 1.0 / (1+ Math.exp(-z));

        return y;
    }

    /**
     * Gets a random weight.
     * @return Random weight
     */
    public static double getRandomWeight() {
        double wt = ran.nextDouble()*(RANGE_MAX-RANGE_MIN)+RANGE_MIN;
        return wt;
    }

    /**
     * Tests the random weights method
     * @param args Command line arguments -- not used.
     */
    public static void main(String args[]) {
        double[] ws = new double[wtNames.length];

        System.out.printf("%2s\t%9s\n","Wt","Value");
        for(int k=0; k < wtNames.length; k++) {
            ws[k] = getRandomWeight();
            System.out.printf("%2s\t%9.5f\n",wtNames[k], ws[k]);
        }
    }
}
