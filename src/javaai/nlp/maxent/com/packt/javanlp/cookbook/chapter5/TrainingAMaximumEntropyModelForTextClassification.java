package javaai.nlp.maxent.com.packt.javanlp.cookbook.chapter5;

import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TrainingAMaximumEntropyModelForTextClassification {

    public static void main(String[] args) {
        // Training a maximum entropy universe for text classification
        try {
            InputStreamFactory dataInputStream = new MarkableFileInputStreamFactory(new File("en-frograt.train"));

            ObjectStream<String> objectStream =
                    new PlainTextByLineStream((InputStreamFactory) dataInputStream, StandardCharsets.UTF_8);
            ObjectStream<DocumentSample> documentSampleStream = new DocumentSampleStream(objectStream);

            TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
            params.put(TrainingParameters.CUTOFF_PARAM, 0);

            DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

            DoccatModel documentCategorizationModel =
//                    DocumentCategorizerME .train("en", documentSampleStream);
                    DocumentCategorizerME.train("en", documentSampleStream, params, factory);

            OutputStream modelOutputStream = new BufferedOutputStream(
                    new FileOutputStream(new File("en-frograt.bin")));
            OutputStream modelBufferedOutputStream = new BufferedOutputStream(modelOutputStream);
            documentCategorizationModel.serialize(modelBufferedOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
