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
package javaai.ann.coding;

import javaai.ann.output.EquilateralEncoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class tests equilateral decoding tolerance for the iris data set.
 * @author Ron.Coleman
 */
public class EquilateralDecoding {
    /** Number of tests to run */
    public final static int NUM_TESTS = 10;

    /** Tolerance as a percent, e.g., 1.0 == 1% */
    public final static double TOLERANCE = 100.0;

    /** We know these from running EquilateralEncoding */
    static double ideals[][] = {
            {-0.8660, -0.5000},  // Viginica
            {0.8660, -0.5000},   // Setosa
            {0.0000, 1.0000}     // Versicolor
    };

    /** Specie names -- order MUST correspond to ideals */
    static final List<String> species =
            new ArrayList<>(Arrays.asList("viginica", "setosa", "versicolor"));

    /**
     * Launch point for program.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        javaai.ann.output.EquilateralEncoding.load();

        Random ran = new Random(0);

        int success = 0;

        for(int n=0; n < NUM_TESTS; n++) {
            // Pick a species randomly
            int actualIndex = ran.nextInt(ideals.length);

            // TODO:
            // 1. Get a random encoding with the actual index.
            // 2. Create a new array of activations perturbed by the tolerance divided by 100.
            // 3. Decode these perturbed activations.
            // 4. If the predicted index equals the actual index, update success count.
            // Get its encoding
            double[] encodings = ideals[actualIndex];

            double[] activations = new double[encodings.length];

            // Perturb each activation by training error tolerance
            for(int k=0; k < encodings.length; k++) {
                double epsilon = 1 + ran.nextGaussian() * TOLERANCE / 100.;

                activations[k] = encodings[k] * epsilon;
            }

            int predictedIndex = EquilateralEncoding.eq.decode(activations);

            String species1 = species.get(actualIndex);
            String species2 = species.get(predictedIndex);
            String outcome = species1 == species2 ? "" : "MISSED!";

            System.out.println(
                    "actual: " + species1+asString(encodings) + " " +
                    "predicted: "+species2+asString(activations) + " " +
                    outcome);

            if(species1 == species2)
                success++;
        }

        double rate = (double)success / NUM_TESTS;

        System.out.printf("%d of %d or %4.2f%% tolerance = %5.2f%%\n",success, NUM_TESTS, rate, TOLERANCE);
    }

    /**
     * Gets the activation encodings as a string.
     * @param encodings Activation encodings
     * @return String
     */
    protected static String asString(double[] encodings) {
        String s = "(";

        int len = encodings.length;

        for(int k=0; k < len; k++) {
            s += String.format("%6.4f",encodings[k]);
            if(k == len-1)
                s += ")";

            else
                s += ", ";
        }
        return s;
    }
}
