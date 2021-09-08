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
package javaai.util;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import static javaai.util.Option.None;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.genetic.genome.DoubleArrayGenome;
import org.encog.ml.genetic.genome.IntegerArrayGenome;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;

/**
 * This is a helper convenience class which implements convenience methods.
 * @author Ron.Coleman
 */
public class Helper {
    /** Maximum iterations to run */
    public final static long MAX_EPOCHS = 100000L;

    public final static boolean DEBUG = Boolean.parseBoolean(System.getProperty("debug","false"));

    /** Respective data, not including headers */
    public final static HashMap<String, List> data = new HashMap<>();

    /** Respective headers */
    public final static List<String> headers = new ArrayList<>();

    /** 1 of N encoding */
    static HashMap<String, List<Integer>> oneOfN = new HashMap<>();

    /**
     * Loads a CSV file -- column must be of same ontology.
     * @param path    Path in system to file
     * @param parsers Parsers to interpret the column
     * @param ran Random number generator to randomize the rows prior to parsing.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public static void loadCsv(String path, List<Function<String, Object>> parsers, Random ran)
            throws FileNotFoundException, IOException, Exception {
        data.clear();

        headers.clear();

        oneOfN.clear();

        ArrayList<String> lines = preload(path, ran);

        for (int row = 0; row < lines.size(); row++) {
            if (DEBUG)
                System.out.println(row + ": " + lines.get(row));

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

                    if (obj == null)
                        System.out.println(lines.get(row));

                    // Note: == is normally a bad idea but None is static in this case
                    if (obj == None) {
                        continue;
                    }

                    list.add(obj);
                }
            }
        }

        // Delete don't care columns in header and data.
        // First we have to get the don't care columns then delete them because Java list collection
        // does not permit updating the list while traversing it at the same time.
        ArrayList<String> trash = new ArrayList<>();
        for (String title : headers) {
            if (data.get(title).size() == 0)
                trash.add(title);
        }

        for (String title : trash) {
            data.remove(title);
            headers.remove(title);
        }
    }

    /**
     * Parses a string object.
     * @param function Parser
     * @param input    Data we're going to parse
     * @return Object result of parsing
     */
    private static Object parse(Function<String, Object> function, String input) {
        return function.apply(input);
    }

    /**
     * Loads the data from a CSV file.
     * Assumes first row is the header row.
     * @param path    File path
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
        if (oneOfN.isEmpty())
            oneOfN = encodeOneOfN(col);

        Object[] keys = oneOfN.keySet().toArray();

        ArrayList<String> subtypes = new ArrayList<>();

        for (Object key : keys) {
            subtypes.add((String) key);
        }

        return subtypes;
    }

    /**
     * Gets the number of subtypes for the nominal.
     * @param col Column index
     * @return Number of subtypes
     */
    public static int getNominalSubtypeCount(int col) {
        if (!oneOfN.isEmpty())
            oneOfN = encodeOneOfN(col);

        return getNominalSubtypes(col).size();
    }

    /**
     * Gets an encoded hash map of nominal types and their 1-of-n values as 1 or -1.
     * Assumes there's exactly one nominal type.
     * @param col Column of nominals
     * @return Hash map of nominal and its 1-of-n encoding
     */
    public static HashMap<String, List<Integer>> encodeOneOfN(int col) {
        if (!oneOfN.isEmpty())
            return oneOfN;

        // Hash map to return: nominal name -> 1-of-n encoding
        oneOfN = new HashMap<String, List<Integer>>();

        // Use this title to retrieve the nominal column
        String title = Helper.headers.get(col);

        List<String> nominals = Helper.data.get(title);

        // Count the number of unique nominal values
        // (Note: we also keep a count of nominal values but it is not used.)
        HashMap<String, Integer> counter = new HashMap<>();

        HashSet<String> uniques = new HashSet<>();

        nominals.forEach((nominal) -> {
            int count = counter.getOrDefault(nominal, 0);

            counter.put(nominal, count + 1);
        });

        int size = counter.size();

        // Encode each nominal

        // Here are the keys we're going to use and in this sequence
        Object[] keys = counter.keySet().toArray();

        for (int j = 0; j < size; j++) {
            ArrayList<Integer> encoding = new ArrayList<>();

            for (int k = 0; k < size; k++) {
                if (k == j) {
                    encoding.add(1);
                } else {
                    encoding.add(-1);
                }
            }

            oneOfN.put((String) keys[j], encoding);
        }

        return oneOfN;
    }

    /**
     * Pre-loads the data into memory buffer and if necessary, randomizes it.
     * @param path Path to the file
     * @param ran  Whether or not to randomize the buffer
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
            if (line == null) {
                // Shuffle the lines but make the first line the header
                Collections.shuffle(lines, ran);

                lines.add(0, firstLine);

                br.close();

                break;
            }

            if (row == 0)
                firstLine = line;
            else
                lines.add(line);

            row++;
        } while (true);

        return lines;
    }

    /**
     * Gets the title for a column.
     * @param col Column
     * @return Title
     */
    public static String getTitle(int col) {
        assert (col >= 0 && col < headers.size());

        return headers.get(col);
    }

    /**
     * Gets the loaded data for a given row across all ontologies.
     * @param row Row number
     * @return Map of header title to object for each row
     */
    public static HashMap<String, Object> asMap(int row) {
        HashMap<String, Object> map = new HashMap<>();

        for (String header : headers)
            map.put(header, data.get(header).get(row));

        return map;
    }

    /**
     * Gets the row count of loaded data.
     * @return Row count
     */
    public static int getRowCount() {
        // Validate data has been loaded
        if (headers == null || headers.size() == 0)
            return 0;

        // Pick an arbitrary column and return it's size since data assumed rectangular.
        String header = headers.get(0);

        return data.get(header).size();
    }

    /**
     * Gets the activation encodings as a string.
     * @param encodings Activation encodings
     * @return String
     */
    public static String asString(double[] encodings) {
        String s = "(";

        int len = encodings.length;

        for (int k = 0; k < len; k++) {
            s += String.format("%7.4f", encodings[k]);
            if (k == len - 1)
                s += ")";

            else
                s += ", ";
        }
        return s;
    }

    /**
     * Decodes binary integer genome as an integer.
     * @param genome Bitwise genome only 0,1 permitted.
     * @return Integer
     */
    public static int asInt(IntegerArrayGenome genome) {
        int[] genes = genome.getData();

        int n = 0;

        for (int k = 0; k < genes.length; k++) {
            n = n << 1;

            n = n | genes[k];
        }

        return n;
    }

    /**
     * Decodes binary integer genome as a double.
     * @param genome Bitwise genome only 0,1 permitted.
     * @return Double
     */
    public static double asDouble(IntegerArrayGenome genome) {
        int[] genes = genome.getData();

        double n = 0.0;

        double sign = 1;
        if (genes[0] == 1)
            sign = -1;

        double adder = 0.5;
        for (int k = 1; k < genes.length; k++) {
            if (genes[k] == 1)
                n += adder;
            adder /= 2;
        }

        return n;
    }

    /**
     * Decodes binary integer genome as a string.
     * @param genome Bitwise genome only 0,1 permitted.
     * @return String
     */
    public static String asString(IntegerArrayGenome genome) {
        String s = "";
        int[] genes = genome.getData();
        for (int gene : genes)
            s += gene + " ";

        s += "| ";
        int x = asInt(genome);

        s += x;

        return s;
    }

    /**
     * Decodes double-array genome as a string.
     * @param genome Genome
     * @return String
     */
    public static String asString(DoubleArrayGenome genome) {
        String s = "";

        double[] ws = genome.getData();

        for (double w : ws)
            s += String.format("%7.3f", w) + " ";

        return s;
    }

    /**
     * Deep copies an object.
     * @param object Serializabl bbject
     * @param <T>    Object type.
     * @return Specified type T
     */
    public static <T> T deepCopy(T object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(bais);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads lines from a file.
     * @param path File path
     * @return Collection of lines.
     */
    public static List<String> loadText(String path) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    /**
     * Makes a single string from a collection of strings.
     * @param lines Lines
     * @param pad   Padding string between lines
     * @return String
     */
    public static String mkstring(List<String> lines, String pad) {
        String str = "";
        for (String line : lines) {
            str += line.replaceAll("\n", " ") + pad;
        }

        return str;
    }

    /**
     * Reports training results.
     * @param trainingSet Training set of data
     * @param network     Network
     */
    public static void report(MLDataSet trainingSet, BasicNetwork network) {
        System.out.println("Network training results:");

        int sz = trainingSet.size();
        if(sz == 0)
            return;

        MLDataPair first = trainingSet.get(0);

        // Output xs header
        System.out.printf("%4s ","#");

        int szInputs = first.getInputArray().length;
        for(int k=0; k < szInputs; k++)
            System.out.printf("%7s ","x"+(k+1));

        // Output ts (ideals) header
        int szOutputs = first.getIdealArray().length;
        for(int k=0; k < szOutputs; k++)
            System.out.printf("%7s ","t"+(k+1));

        // Output ys (actuals) header
        for(int k=0; k < szOutputs; k++)
            System.out.printf("%7s ","y"+(k+1));

        System.out.println();

        // Report inputs and ideals vs. outputs.
        int n = 1;
        for (MLDataPair pair : trainingSet) {
            System.out.printf("%4d ",n);

            final MLData inputs = pair.getInput();
            final MLData outputs = network.compute(inputs);

            final double input[] = inputs.getData();
            for(double d: input)
                System.out.printf("%7.4f ",d);

            final MLData ideals = pair.getIdeal();
            final double ideal[] = ideals.getData();
            for(double d: ideal)
                System.out.printf("%7.4f ",d);

            final double actual[] = outputs.getData();
            for(double d: actual)
                System.out.printf("%7.4f ",d);

            System.out.println("");

            n += 1;
        }
    }

    /**
     * Describes details of a network.
     * @param network Network
     */
    public static void describe(BasicNetwork network) {
        int layerCount = network.getLayerCount();

        int neuronCount = 0;
        for (int layerNum = 0; layerNum < layerCount; layerNum++) {
            // int neuronCount = network.calculateNeuronCount();  // Should work but doesn't appear to
            neuronCount += network.getLayerTotalNeuronCount(layerNum);
        }

        System.out.println("total layers: " + layerCount + " neurons: " + neuronCount);
        System.out.printf("%5s %5s %5s %10s\n", "layer", "from", "to", "wt");

        for (int layerNum = 0; layerNum < layerCount; layerNum++) {
            if (layerNum + 1 < layerCount) {
                // Nodes not including bias
                int fromNeuronCount = network.getLayerNeuronCount(layerNum);

                // Account for bias node
                if (network.isLayerBiased(layerNum))
                    fromNeuronCount += 1;

                int toNeuronCount = network.getLayerNeuronCount(layerNum + 1);

                for (int fromNeuron = 0; fromNeuron < fromNeuronCount; fromNeuron++) {
                    for (int toNeuron = 0; toNeuron < toNeuronCount; toNeuron++) {
                        double wt = network.getWeight(layerNum, fromNeuron, toNeuron);
                        System.out.printf("%5d %5d %5d %10.4f ", layerNum, fromNeuron, toNeuron, wt);
                        if (network.isLayerBiased(layerNum) && fromNeuron == fromNeuronCount - 1)
                            System.out.println("BIAS");
                        else
                            System.out.println("");
                    }
                }
            }
        }
    }

    /**
     * Logs statistics for each epoch.
     * @param epoch Epoch number
     * @param train Training results
     * @param done  True if the training is done
     */
    public static void log(int epoch, BasicTraining train, boolean done) {
        if(DEBUG)
            return;

        final int FREQUENCY = 100;

        // Report only the header
        if (epoch == 0)
            System.out.printf("%8s %6s\n", "epoch", "error");

        else if (epoch == 1 || (!done && (epoch % FREQUENCY) == 0)) {
            System.out.printf("%8d %6.4f\n", epoch, train.getError());
        }
        // Report only if we haven't just reported
        else if (done && (epoch % FREQUENCY) != 0)
            System.out.printf("%8d %6.4f\n", epoch, train.getError());

        if (epoch >= MAX_EPOCHS && done)
            System.out.println("--- DID NOT CONVERGE!");
    }
}
