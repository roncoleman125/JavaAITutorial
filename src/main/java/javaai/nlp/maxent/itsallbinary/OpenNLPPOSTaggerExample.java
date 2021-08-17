package javaai.nlp.maxent.itsallbinary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerFactory;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

public class OpenNLPPOSTaggerExample {

    public static void main(String[] args) throws Exception {

        /**
         * Read human understandable data & train a model
         */
        // Read file with examples of pos tags.
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("postagdata.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        FilterObjectStream<String, POSSample> sampleStream = new WordTagSampleStream(lineStream);

        // Train a model from the file read above
        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0);
        POSModel model = POSTaggerME.train("en", sampleStream, params, new POSTaggerFactory());

        // Serialize model to some file so that next time we don't have to again train a
        // model. Next time We can just load this file directly into model.
        model.serialize(new File("postagdata.bin"));

        /**
         * Lets tag sentences
         */
        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                // Get inputs in loop
                System.out.println("Enter a sentence:");

                // Initialize POS tagger tool
                POSTaggerME myCategorizer = new POSTaggerME(model);

                // Tag sentence.
                String[] tokens = myCategorizer.tag(getTokens(scanner.nextLine()));
                for (String t : tokens) {
                    System.out.println("Tokens: " + t);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Tokenize sentence into tokens.
     *
     * @param sentence
     * @return
     */
    private static String[] getTokens(String sentence) {

        try (InputStream modelIn = new FileInputStream("tokenizermodel.bin")) {

            TokenizerModel model = new TokenizerModel(modelIn);

            TokenizerME myCategorizer = new TokenizerME(model);

            String[] tokens = myCategorizer.tokenize(sentence);

            for (String t : tokens) {
                System.out.println("Tokens: " + t);
            }
            return tokens;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
