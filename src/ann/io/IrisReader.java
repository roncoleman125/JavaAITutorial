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
package ann.io;

import ann.util.Helper;
import ann.util.Ontology;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads the iris csv file and outputs it.
 */
public class IrisReader {
    public static void main(String[] args) {
        try {
            // Load data in column-oriented format which is preferred for normalization
            Helper.loadCsv("iris.csv", Ontology.parsers);

            // Transpose to row-oriented format which is preferred for output
            List<Flower> flowers = new ArrayList<>();

            // Get number of rows -- if there aren't any then data hasn't been loaded successfully
            int rowCount = Helper.getRowCount();

            // Go through each row of each column and make a flower
            for(int row=0; row < rowCount; row++) {
                HashMap map = Helper.asMap(row);

                Flower flower = asFlower(map);

                flowers.add(flower);
            }

            for(Flower flower: flowers) {
                System.out.println(flower);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts map to a single object.
     * @param map
     * @return
     */
    private static Flower asFlower(HashMap map) {
        // Get correspond value for each key which is directly from iris dataset.
        // Note the cast needed: Helper doesn't remember ontologies which we pass to it.
        Double sepalLength = (Double) map.get("Sepal.Length");
        Double sepalWidth = (Double) map.get("Sepal.Width");
        Double petalLength = (Double) map.get("Petal.Length");
        Double petalWidth = (Double) map.get("Petal.Width");
        Species species = (Species) map.get("Species");

        return new Flower(sepalLength,sepalWidth,petalLength,petalWidth,species);
    }
}
