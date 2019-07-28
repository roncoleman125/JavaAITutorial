package javaai.ann.coding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import static javaai.util.Option.None;

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
