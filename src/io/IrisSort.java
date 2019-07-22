package io;

import util.DontCare;
import util.Helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.function.Function;

public class IrisSort {
    public static HashMap<String, Species> asSpecies = new HashMap<String, Species>() {{
        put("setosa", new Setosa());
        put("versicolor", new Versicolor());
        put("viginica", new Versicolor());
    }};

    static Function<String, Object> parseNumeric = (String s) -> Double.parseDouble(s);
    static Function<String, Object> parseNominal = (String s) -> asSpecies.get(s);
    static Function<String, Object> parseIgnore = (String s) -> new DontCare();


    static ArrayList<Function<String, Object>> parsers =
            new ArrayList<Function<String, Object>>(Arrays.asList(
                    parseIgnore,
                    parseNumeric,
                    parseNumeric,
                    parseNumeric,
                    parseNumeric,
                    parseNominal));

    public static void main(String[] args) {
        ArrayList<Flower> flowers = load();

    }

    public static ArrayList<Flower> load() {
        ArrayList<Flower> flowers = null;

        try {
            // Load data column-oriented
            Helper.loadCsv("iris.csv", parsers);

            // Transpose data to row-oriented flowers
            flowers = asFlowers(Helper.data);
        }
        catch(Exception e) {
            System.err.println(e);
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
        int sepalLength = (Integer) map.get("Sepal.Length");
        int sepalWidth = (Integer) map.get("Sepal.Width");
        int petalLength = (Integer) map.get("Petal.Length");
        int petalWidth = (Integer) map.get("Petal.Width");
        Species species = (Species) map.get("Species");

        return new Flower(sepalLength,sepalWidth,petalLength,petalWidth,species);
    }
}
