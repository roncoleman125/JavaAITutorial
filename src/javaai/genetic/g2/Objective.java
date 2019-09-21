package javaai.genetic.g2;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.genetic.genome.DoubleArrayGenome;

public class Objective implements CalculateScore {
    public static double XOR_INPUTS[][] = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
    };

    /**
     * The ideal data necessary for XOR.
     */
    public static double XOR_IDEALS[][] = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}};
    double w1, w2, w3, w4, w5, w6, b1, b2;
    @Override
    public double calculateScore(MLMethod phenotype) {

        DoubleArrayGenome genome = (DoubleArrayGenome)phenotype;

        double[] ws = genome.getData();

        w1 = ws[0];
        w2 = ws[1];
        w3 = ws[2];
        w4 = ws[3];
        w5 = ws[4];
        w6 = ws[5];
        b1 = ws[6];
        b2 = ws[7];

        double sumError = 0.0;

        for(int k=0; k < XOR_INPUTS.length; k++) {
            double x1 = XOR_INPUTS[k][0];
            double x2 = XOR_INPUTS[k][1];

            double actual = feedforward(x1, x2);

            double ideal = XOR_IDEALS[k][0];
            double error = (actual - ideal) * (actual - ideal);
            sumError += error;
        }

        double soe = Math.sqrt(sumError / XOR_INPUTS.length);

        return soe;
    }

    public double predict(double x1, double x2, double[] ws) {
//        double[] ws = genome.getData();

        w1 = ws[0];
        w2 = ws[1];
        w3 = ws[2];
        w4 = ws[3];
        w5 = ws[4];
        w6 = ws[5];
        b1 = ws[6];
        b2 = ws[7];

        double y1 = feedforward(x1, x2);

        return y1;
    }

    @Override
    public boolean shouldMinimize() {
        return true;
    }

    @Override
    public boolean requireSingleThreaded() {
        return false;
    }

    protected double feedforward(double x1, double x2) {
        double zh1 = w1*x1 + w3*x2 + b1;
        double zh2 = w2*x1 + w4*x2 + b1;
        double h1 = sigma(zh1);
        double h2 = sigma(zh2);
        double zy1 = w5*h1 + w6*h2 + b2;
        double y1 = sigma(zy1);
        return y1;
    }

    protected double sigma(double z) {
        double y = 1.0 / (1+ Math.exp(-z));

        return y;
    }
}
