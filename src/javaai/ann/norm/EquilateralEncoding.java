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
package javaai.ann.norm;

import org.encog.mathutil.Equilateral;
import org.encog.util.Format;
import javaai.util.Helper;
import javaai.util.Ontology;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This code demonstrates how to use equilateral encoding with the Iris data.
 * Note: the number of outputs is one minus the number of nominal subtypes
 * See Heaton (2011), p.25-27 for more details.
 * @author Ron.Coleman
 */
public class EquilateralEncoding {
    public static void main(String[] args) {
        try {
            // Load the data
            Helper.loadCsv("iris.csv", Ontology.parsers);

            // If we get here without an exception, then get title of last column
            String title = Helper.getTitle(4);

            System.out.println(title);

            // To set up the equilateral encoding
            int numberNodes = Helper.getNominalSubtypeCount(4);

            // Get the equilateral encoding for [-1, 1] range.
            Equilateral eq = new Equilateral(numberNodes, -1, 1);

            List<String> subtypes = Helper.getNominalSubtypes(4);

            // Output the ideal values for each nominal as a point in
            // n-dimension hyperspace
            for (int index = 0; index < subtypes.size(); index++) {
                String subtype = subtypes.get(index);

                System.out.print(subtype + ": ");

                double[] encoding = eq.encode(index);

                for (int dim = 0; dim < encoding.length; dim++) {
                    if (dim > 0) {
                        System.out.print(", ");
                    }
                    System.out.print(Format.formatDouble(encoding[dim], 4));
                }

                System.out.println();
            }
        } catch (Exception ex) {
            Logger.getLogger(EquilateralEncoding.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
