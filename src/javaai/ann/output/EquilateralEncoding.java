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
package javaai.ann.output;

import org.encog.mathutil.Equilateral;
import javaai.util.Helper;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This code demonstrates how to use equilateral encoding with the Iris data.
 * See Heaton (2011), p.25-27 for more details.
 * @author Ron.Coleman
 */
public class EquilateralEncoding {
    /** Equilateral encoder */
    static Equilateral eq = null;

    /** Nominal subtypes */
    static List<String> subtypes = null;

    /**
     * Launch point for program.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Load the data
        load();

        // Output ideal values nominal as point in 2D (or subtypes.size()-1) hyperspace
        for (int index = 0; index < subtypes.size(); index++) {
            String subtype = subtypes.get(index);

            System.out.print(subtype + ": ");

            double[] encoding = eq.encode(index);

            for (int k = 0; k < encoding.length; k++) {
                if (k > 0) {
                    System.out.print(", ");
                }
                System.out.printf("%5.4f", encoding[k]);
            }

            System.out.println();
        }
    }

    /**
     * Loads the iris data with a default path.
     */
    public static void load() {
        EquilateralEncoding.load("iris.csv");
    }

    /**
     * Loads the iris data at a specified path.
     * @param path Path
     */
    public static void load(String path) {
        try {
            // Load the data
            Helper.loadCsv("iris.csv", Ontology.parsers);

            // Use equilateral encoding for [-1, 1] range for TANH activation function.
            subtypes = Helper.getNominalSubtypes(4);

            eq = new Equilateral(subtypes.size(), 1, -1);

        } catch (Exception ex) {
            Logger.getLogger(EquilateralEncoding.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
