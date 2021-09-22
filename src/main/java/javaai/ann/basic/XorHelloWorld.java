package javaai.ann.basic;

import javaai.util.Helper;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming. This example shows how to construct an Encog neural network to
 * predict the report from the XOR operator. This example uses backpropagation
 * to train the neural network.
 *
 * This example attempts to use a minimum of Encog values to create and train
 * the neural network. This allows you to see exactly what is going on. For a
 * more advanced example, that uses Encog factories, refer to the XORFactory
 * example.
 *
 * The original version of this code does not appear to converge. I fixed this
 * problem by using two neurons in the hidden layer and instead of ramped activation,
 * sigmoid activation. This makes the network reflect the model in figure 1.1
 * in the book, d. 11. I also added more comments to make the code more explanatory.
 * @author Ron Coleman
 * @date 24 Oct 2017
 */
public class XorHelloWorld {
    /** Error tolerance */
    public final static double TOLERANCE = 0.01;
    public final static double LEARNING_RATE = 0.25;
    public final static double LEARNING_MOMENTUM = 0.25;

    /** The input necessary for XOR. */
    public static double XOR_INPUTS[][] = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
    };

    /** The ideal data necessary for XOR.*/
    public static double XOR_IDEALS[][] = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}};

    /**
     * The main method.
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {
        // Build the network
        BasicNetwork network = new BasicNetwork();

        // Input layer plus bias node
        network.addLayer(new BasicLayer(null, true, 2));

        // Hidden layer plus bias node
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));

        // Output layer
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        Helper.describe(network);

        // Create training data
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUTS, XOR_IDEALS);

        // Use a training object for the learning algorithm, backpropagation.
        final BasicTraining training = new Backpropagation(network, trainingSet,LEARNING_RATE,LEARNING_MOMENTUM);

        // Set learning batch size: 0 = batch, 1 = online, n = batch size
        // See org.encog.neural.networks.training.BatchSize
        // train.setBatchSize(0);

        int epoch = 0;

        Helper.log(epoch, training,false);
        do {
            training.iteration();

            epoch++;

            Helper.log(epoch, training,false);

        } while (training.getError() > TOLERANCE && epoch < Helper.MAX_EPOCHS);

        training.finishTraining();

        Helper.log(epoch, training,true);
        Helper.report(trainingSet, network);
        Helper.describe(network);

        Encog.getInstance().shutdown();
    }

}
