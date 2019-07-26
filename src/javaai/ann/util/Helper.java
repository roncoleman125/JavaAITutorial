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
package javaai.ann.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import static javaai.ann.util.Option.None;

/**
 * This is a helper convenience class which implements convenience methods.
 * @author Ron.Coleman
 */
public class Helper {
    public final static boolean DEBUGGING = false;

    // Respective data, not including headers
    public final static HashMap<String, List> data = new HashMap<>();

    // Respective headers
    public final static List<String> headers = new ArrayList<>();

    // 1 of N encoding
    static HashMap<String, List<Integer>> oneofn = new HashMap<>();

    public static void loadCsv(String path, List<Function<String, Object>> parsers, Random ran)
            throws FileNotFoundException, IOException, Exception {
        data.clear();

        headers.clear();

        oneofn.clear();

        ArrayList<String> lines = preload(path, ran);

        for(int row=0; row < lines.size(); row++) {
            if(DEBUGGING)
                System.out.println(row+": "+lines.get(row));

            String[] fields = lines.get(row).split(",");

            // Handle empty rows
            if (fields.length == 0) {
                continue;
            }

            // Validate fields and ontology length match
            if (fields.length != parsers.size()) {
                throw new Exception("fields mismatch row " + row);
            }

            // Assumes row zero is a header row
            if (row == 0) {
                // Create a title for each column
                for (String title : fields) {
                    headers.add(title);

                    // Create a dataset by name for each column
                    data.put(title, new ArrayList<>());
                }
            } else {
                // Go through each field and parse it according to its functional
                for (int col = 0; col < fields.length; col++) {
                    String title = headers.get(col);

                    List list = data.get(title);

                    Function<String, Object> parser = parsers.get(col);

                    Object obj = parse(parser, fields[col].trim());

                    if(obj == null)
                        System.out.println(lines.get(row));

                        // Note: == is normally a bad idea but None is static in this case
                        if(obj == None) {
                            continue;
                    }

                    list.add(obj);
                }
            }
        }

        // Delete the don't care columns in header and data.
        // First we have to get the don't care columns then delete them because Java list collection
        // does not permit updating the list while traversing it at the same time.
        ArrayList<String> trash = new ArrayList<>();
        for(String title: headers) {
            if(data.get(title).size() == 0)
                trash.add(title);
        }

        for(String title: trash) {
            data.remove(title);
            headers.remove(title);
        }
    }

    /**
     * Parses a string object.
     * @param function Parser
     * @param input Data we're going to parse
     * @return Object result of parsing
     */
    private static Object parse(Function<String, Object> function, String input) {
        return function.apply(input);
    }

    /**
     * Loads the data from a CSV file.
     * Assumes first row is the header row.
     * @param path File path
     * @param parsers Parsers of data
     * @throws Exception
     */
    public static void loadCsv(String path, List<Function<String, Object>> parsers) throws Exception {
        Helper.loadCsv(path, parsers, new Random(0));
    }

    /**
     * Gets the subtypes for
     * @param col Column index
     * @return Names of the subtypes
     */
    public static List<String> getNominalSubtypes(int col) {
        if(oneofn.isEmpty())
            oneofn = encodeOneOfN(col);

        Object[] keys = oneofn.keySet().toArray();

        ArrayList<String> subtypes = new ArrayList<>();
        for(Object key: keys) {
            subtypes.add((String)key);
        }

        return subtypes;
    }

    /**
     * Gets the number of subtypes for the nominal.
     * @param col Column index
     * @return Number of subtypes
     */
    public static int getNominalSubtypeCount(int col) {
        if(!oneofn.isEmpty())
            oneofn = encodeOneOfN(col);

        return getNominalSubtypes(col).size();
    }

    /**
     * Gets an encoded hash map of nominal types and their 1-of-n values as 1 or -1.
     * Assumes there's exactly one nominal type.
     * @param col Column of nominals
     * @return Hash map of nominal and its 1-of-n encoding
     */
    public static HashMap<String, List<Integer>> encodeOneOfN(int col) {
        if(!oneofn.isEmpty())
            return oneofn;

        // Hash map to return: nominal name -> 1-of-n encoding
        oneofn = new HashMap<String, List<Integer>>();

        // Use this title to retrieve the nominal column
        String title = Helper.headers.get(col);

        List<String> nominals = Helper.data.get(title);

        // Count the number of unique nominal values
        // (Note: we also keep a count of nominal values but it is not used.)
        HashMap<String, Integer> counter = new HashMap<>();

        nominals.forEach((nominal) -> {
            int count = counter.getOrDefault(nominal, 0);

            counter.put(nominal, count + 1);
        });

        int size = counter.size();

        // Encode each nominal
        for(int j = 0; j < size; j++) {
            ArrayList<Integer> encoding = new ArrayList<>();

            for (int k = 0; k < size; k++) {
                if (k == j) {
                    encoding.add(1);
                } else {
                    encoding.add(-1);
                }
            }

            // Store the encoding in the hash map
            Object[] keys = counter.keySet().toArray();

            oneofn.put((String) keys[j], encoding);
        }

        return oneofn;
    }

    /**
     * Pre-loads the data into memory buffer and if necessary, randomizes it.
     * @param path Path to the file
     * @param ran Whether or not to randomize the buffer
     * @return Buffer of lines
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static ArrayList<String> preload(String path, Random ran) throws FileNotFoundException, IOException {
        // All line are store here
        ArrayList<String> lines = new ArrayList<>();

        // Open the file
        BufferedReader br = new BufferedReader(new FileReader(path));

        int row = 0;

        // First line assumed to be the header
        String firstLine = null;

        do {
            // Gets a line and adds it to the buffer of lines
            String line = br.readLine();

            // If we get to the end, return the buffer
            if(line == null) {
                // Shuffle the lines but make the first line the header
                Collections.shuffle(lines, ran);

                lines.add(0, firstLine);

                br.close();

                break;
            }

            if(row == 0)
                firstLine = line;
            else
                lines.add(line);

            row++;
        } while(true);

        return lines;
    }

    /**
     * Gets the title for a column.
     * @param col Column
     * @return Title
     */
    public static String getTitle(int col) {
        assert(col >= 0 && col < headers.size());

        return headers.get(col);
    }

    /**
     * Gets the loaded data for a given row across all ontologies.
     * @param row Row number
     * @return Map of header title to object for each row
     */
    public static HashMap<String, Object> asMap(int row) {
        HashMap<String, Object> map = new HashMap<>();

        for(String header: headers)
            map.put(header,data.get(header).get(row));

        return map;
    }

    /**
     * Gets the row count of loaded data.
     * @return Row count
     */
    public static int getRowCount() {
        // Validate data has been loaded
        if(headers == null || headers.size() == 0)
            return 0;

        // Pick an arbitrary column and return it's size since data assumed rectangular.
        String header = headers.get(0);

        return data.get(header).size();
    }
}
