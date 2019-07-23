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
package ann.util;

import ann.io.Setosa;
import ann.io.Species;
import ann.io.Versicolor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/**
 * This is a container class of parser functionals.
 * @author Ron Coleman
 */
public class Ontology {
    private static HashMap<String, Species> asSpecies = new HashMap<String, Species>() {{
        put("setosa", new Setosa());
        put("versicolor", new Versicolor());
        put("virginica", new Versicolor());
    }};

    /** Parses a numeric string */
    static Function<String, Object> parseNumeric = (String s) -> Double.parseDouble(s);

    /** Parses a nominal as a flower species */
    static Function<String, Object> parseNominal = (String s) -> asSpecies.get(s);

    /** Parses a "don't care" column */
    static Function<String, Object> parseIgnore = (String s) -> None.getInstance();

    /** Parser table: iris.csv has columns of these ontologies. */
    public static ArrayList<Function<String, Object>> parsers =
            new ArrayList<Function<String, Object>>(Arrays.asList(
                    parseIgnore,
                    parseNumeric,
                    parseNumeric,
                    parseNumeric,
                    parseNumeric,
                    parseNominal));
}
