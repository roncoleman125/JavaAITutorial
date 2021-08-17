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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import static javaai.util.Option.None;

/**
 * This class contains parsers to interpret iris data need by Helper.loadCsv.
 * @author Ron.Coleman
 */
public class Ontology {
    /** Parses a numeric string */
    static Function<String, Object> parseNumeric = (String s) ->
    {
        try {
            return Double.parseDouble(s);
        }
        catch(Exception e) {
            return None;
        }
    };

    /** Parses a nominal as a flower species */
    static Function<String, Object> parseNominal = (String s) -> {

        return (s.length() == 0 || s == null) ? None : s;
    };

    /** Parses a "don't care" column */
    static Function<String, Object> parseNone = (String s) -> None;

    /** Parser table has columns of these ontologies from iris.csv. */
    public static ArrayList<Function<String, Object>> parsers =
            new ArrayList<Function<String, Object>>(Arrays.asList(
                    parseNone,        // Index
                    parseNumeric,     // Sepal length
                    parseNumeric,     // Sepal width
                    parseNumeric,     // Petal length
                    parseNumeric,     // Petal width
                    parseNominal));   // Species
}
