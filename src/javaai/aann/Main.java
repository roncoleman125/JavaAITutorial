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
package javaai.aann;

import javaai.util.Helper;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads the iris csv file and outputs it.
 * @author Ron Coleman
 */
public class Main {
    /** Container of flower species */
    protected static List<Species> flowers = new ArrayList<>();

    /** Container of species measurements */
    protected static List<Measure> measures = new ArrayList<>();

    public static void main(String[] args) {
        load();

        for(int k=0; k < flowers.size(); k++)
            System.out.println(flowers.get(k)+""+measures.get(k));
    }

    /**
     * Loads the iris csv file from the default path.
     */
    public static void load() {
        load("iris.csv");
    }

    /**
     * Loads data in given a path.
     * @param path File path
     */
    public static void load(String path) {
        try {
            // Load data in column-oriented format preferred for normalization, missing values, etc.
            Helper.loadCsv(path, Ontology.parsers);

            // Get number of rows -- if there aren't any then data hasn't been loaded successfully
            int rowCount = Helper.getRowCount();

            // Go through each row of each column and make a flower
            for(int row=0; row < rowCount; row++) {
                HashMap map = Helper.asMap(row);

                flowers.add(asFlower(map));

                measures.add(asMeasure(map));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the species in the map.
     * @param map Row in iris data set
     * @return Species
     */
    private static Species asFlower(HashMap map) {
        return (Species) map.get("Species");
    }

    /**
     * Gets the measurement in the map.
     * @param map Row in iris data set
     * @return Measurement
     */
    private static Measure asMeasure(HashMap map) {
        Double sepalLength = (Double) map.get("Sepal.Length");
        Double sepalWidth = (Double) map.get("Sepal.Width");
        Double petalLength = (Double) map.get("Petal.Length");
        Double petalWidth = (Double) map.get("Petal.Width");

        return new Measure(sepalLength,sepalWidth,petalLength,petalWidth);
    }
}
