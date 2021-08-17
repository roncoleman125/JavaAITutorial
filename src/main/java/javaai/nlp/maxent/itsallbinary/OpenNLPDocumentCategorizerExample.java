package javaai.nlp.maxent.itsallbinary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

/**
 * @see "https://itsallbinary.com/natural-language-processing-in-java-using-apache-opennlp-document-categorizer-simple-example-for-beginners/"
 */
public class OpenNLPDocumentCategorizerExample {

    public static void main(String[] args) throws Exception {

        /**
         * Read human understandable data & train a model
         */

        // Read file with classifications samples of sentences.
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("documentcategorizer.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        // Use CUT_OFF as zero since we will use very few samples.
        // BagOfWordsFeatureGenerator will treat each word as a feature. Since we have
        // few samples, each feature/word will have small counts, so it won't meet high
        // cutoff.
        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0);
        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

        // Train a model with classifications from above file.
        DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);

        // Serialize model to some file so that next time we don't have to again train a
        // model. Next time We can just load this file directly into model.
        model.serialize(new File("documentcategorizer.bin"));

        /**
         * Load model from serialized file & lets categorize reviews.
         */
        // Load serialized trained model
        try (InputStream modelIn = new FileInputStream("documentcategorizer.bin");
             Scanner scanner = new Scanner(System.in);) {

            while (true) {
                // Get inputs in loop
                System.out.println("Enter a sentence:");

                // Initialize document categorizer tool
                DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

                // Get the probabilities of all outcome i.e. positive & negative
                double[] probabilitiesOfOutcomes = myCategorizer.categorize(getTokens(/*"They like this"*/scanner.nextLine()));

                // Get name of category which had high probability
                String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
                System.out.println("Category: " + category);
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

        // Use model that was created in earlier tokenizer tutorial
        try (InputStream modelIn = new FileInputStream("tokenizermodel.bin")) {

            TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));

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
