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
package javaai.ann.input;

import javaai.util.Helper;
import java.util.HashMap;
import java.util.List;
import static javaai.ann.output.Ontology.parsers;
import static javaai.util.Helper.loadCsv;
import static javaai.util.Helper.headers;

/**
 * This class loads the input "real-world" iris data to be normalized.
 */
public class Main {
    /** Contains real-world data */
    protected static HashMap<String, List<Double>> reals = new HashMap<>();

    /**
     * Launches the program.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        load();
    }

    /**
     * Loads the real data.
     */
    protected static void load() {
        try {
            loadCsv("iris.csv", parsers);

            for(String title: headers) {
                List list = Helper.data.get(title);

                // Make sure the list contains doubles
                // Note: nominal species data needs 1-of-n or equilateral encoding done elsewhere.
                if(list == null || list.isEmpty() || !(list.get(0) instanceof Double))
                    continue;

                // Add copy of reference, not copy of data.
                reals.put(title,(List<Double>)list);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
