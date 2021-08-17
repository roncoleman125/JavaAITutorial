package javaai.nlp.maxent.itsallbinary;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorFactory;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.langdetect.LanguageDetectorSampleStream;
import opennlp.tools.langdetect.LanguageSample;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.model.ModelUtil;

public class OpenNLPLanguageDetectionExamle {

    public static void main(String[] args) throws Exception {

        /**
         * Read human understandable data & train a model
         */

        // Read file with greetings in many languages
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(
                new File("helloInManyLanguages.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<LanguageSample> sampleStream = new LanguageDetectorSampleStream(lineStream);

        // Train a model from the greetings with many languages.
        LanguageDetectorModel model = LanguageDetectorME.train(sampleStream,
                ModelUtil.createDefaultTrainingParameters(), new LanguageDetectorFactory());

        // Serialize model to some file so that next time we don't have to again train a
        // model. Next time We can just load this file directly into model.
        model.serialize(new File("helloInManyLanguagesModel.bin"));

        /**
         * Load model from serialized file & lets detect languages.
         */
        try (InputStream modelIn = new FileInputStream("helloInManyLanguagesModel.bin");
             Scanner scanner = new Scanner(System.in);) {

            // Load serialized trained model
            LanguageDetectorModel trainedModel = new LanguageDetectorModel(modelIn);

            while (true) {
                // Get inputs in loop
                System.out.println("Enter a greeting:");
                String inputText = scanner.nextLine();

                // Initialize language detector tool
                LanguageDetectorME myCategorizer = new LanguageDetectorME(trainedModel);

                // Get language prediction based on learnings.
                Language bestLanguage = myCategorizer.predictLanguage(inputText);
                System.out.println("Best language: " + bestLanguage.getLang());
                System.out.println("Best language confidence: " + bestLanguage.getConfidence());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
