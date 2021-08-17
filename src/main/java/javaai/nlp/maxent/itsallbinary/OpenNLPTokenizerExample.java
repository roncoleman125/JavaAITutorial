package javaai.nlp.maxent.itsallbinary;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.TokenizerFactory;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @see "https://itsallbinary.com/natural-language-processing-in-java-using-apache-opennlp-string-tokenizer-simple-example-for-beginners/"
 */
public class OpenNLPTokenizerExample {

    public static void main(String[] args) throws Exception {

        /**
         * Read human understandable data & train a model
         */
        // Read file with examples of tokenization.
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("tokenizerdata.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

        // Train a model from the file read above
        TokenizerFactory factory = new TokenizerFactory("en", null, false, null);
        TokenizerModel model = TokenizerME.train(sampleStream, factory, TrainingParameters.defaultParams());

        // Serialize model to some file so that next time we don't have to again train a
        // model. Next time We can just load this file directly into model.
        model.serialize(new File("tokenizermodel.bin"));

        /**
         * Lets tokenize
         */
        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                // Get inputs in loop
                System.out.println("Enter a sentence to tokenize:");

                // Initialize tokenizer tool
                TokenizerME myCategorizer = new TokenizerME(model);

                // Tokenize sentence.
                String[] tokens = myCategorizer.tokenize(scanner.nextLine());
                for (String t : tokens) {
                    System.out.println("Tokens: " + t);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
