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

import javaai.util.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This code demonstrates how to use one-of-n encoding with the Iris data.
 * It "learns" the encoding.
 * For more details see Heaton (2011), p.24.
 * @author Ron.Coleman
 */
public class OneOfNEncoding {

    public static void main(String[] args) {
        try {
            // Load the data
            Helper.loadCsv("iris.csv", Ontology.parsers);

            // If we get here without an exception, then get title of last column
            String title = Helper.getTitle(4);

            System.out.println(title);

            // Calculate the one-of-n
            HashMap<String, List<Integer>> oneofn = Helper.encodeOneOfN(4);

            // Output the results
            for (String key : oneofn.keySet()) {
                List<Integer> ideals = oneofn.get(key);

                System.out.print(key + ": ");
                for(Integer ideal: ideals) {
                    System.out.print(ideal + " ");
                }
                System.out.println();
            }
        } catch (Exception ex) {
            Logger.getLogger(OneOfNEncoding.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
