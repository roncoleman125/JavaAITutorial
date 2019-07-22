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
package io;

import util.Helper;
import util.Ontology;
import java.util.HashMap;
import java.util.ArrayList;

public class IrisSorter {


    public static void main(String[] args) {
        ArrayList<Flower> flowers = load();

    }

    public static ArrayList<Flower> load() {
        ArrayList<Flower> flowers = null;

        try {
            // Load data column-oriented
            Helper.loadCsv("iris.csv", Ontology.parsers);

            // Transpose data to row-oriented flowers
            flowers = asFlowers(Helper.data);

            for(Flower flower: flowers) {
                System.out.println(flower);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return flowers;
    }

    private static ArrayList<Flower> asFlowers(HashMap<String, ArrayList> data) {
        // Flowers are stored here
        ArrayList<Flower> flowers = new ArrayList<>();

        // Get number of rows -- if there aren't any then data hasn't been loaded
        int rowCount = Helper.getRowCount();

        // Go through each row of each column and make a flower
        for(int row=0; row < rowCount; row++) {
            HashMap map = Helper.asMap(row);

            Flower flower = asFlower(map);

            flowers.add(flower);
        }

        return flowers;
    }

    private static Flower asFlower(HashMap map) {
        Double sepalLength = (Double) map.get("Sepal.Length");
        Double sepalWidth = (Double) map.get("Sepal.Width");
        Double petalLength = (Double) map.get("Petal.Length");
        Double petalWidth = (Double) map.get("Petal.Width");
        Species species = (Species) map.get("Species");

        return new Flower(sepalLength,sepalWidth,petalLength,petalWidth,species);
    }
}
