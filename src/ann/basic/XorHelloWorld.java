package ann.basic;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming. This example shows how to construct an Encog neural network to
 * predict the output from the XOR operator. This example uses backpropagation
 * to train the neural network.
 *
 * This example attempts to use a minimum of Encog features to create and train
 * the neural network. This allows you to see exactly what is going on. For a
 * more advanced example, that uses Encog factories, refer to the XORFactory
 * example.
 *
 * The original version of this code does not appear to converge. I fixed this
 * problem by using two neurons in the hidden layer and instead of ramped activation,
 * sigmoid activation. This makes the network reflect the model in figure 1.1
 * in the book, p. 11. I also added more comments to make the code more
 * explanatory.
 * @author Ron Coleman
 * @date 24 Oct 2017
 */
public class XorHelloWorld {

    /**
     * The input necessary for XOR.
     */
    public static double XOR_INPUT[][] = {
            {0.0, 0.0},
            {1.0, 0.0},
            {0.0, 1.0},
            {1.0, 1.0}
    };

    /**
     * The ideal data necessary for XOR.
     */
    public static double XOR_IDEAL[][] = {{0.0}, {1.0}, {1.0}, {0.0}};

    /**
     * The main method.
     *
     * @param args No arguments are used.
     */
    public static void main(final String args[]) {

        // Create a neural network, without using a factory
        BasicNetwork network = new BasicNetwork();

        // Add input lauyer with no activation function, bias enabled, and two neurons
        network.addLayer(new BasicLayer(null, true, 2));

        // Add hidden lauyer with ramped activation, bias enabled, and five neurons
        // NOTE: ActivationReLU is not in javadoc but can be found here http://bit.ly/2zyxk7A.
//        network.addLayer(new BasicLayer(new ActivationReLU(), true, 5));

        // Add hidden lauyer with sigmoid activation, bias enabled, and two neurons
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));

        // Add output layer with sigmoid activation, bias disable, and one neuron
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        // Create training data
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);

        // Train the neural network
        // Use a training object to train the network, in this case, an improved
        // backpropagation. For details on what this does see the javadoc.
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;

        // Never train on a specific error rate but an acceptable tolerance and
        // if the error drops below that tolerance, the network has converged.
        do {
            train.iteration();

            System.out.println("Epoch #" + epoch + " Error:" + train.getError());

            epoch++;
        } while (train.getError() > 0.01);

        train.finishTraining();

        // test the neural network
        System.out.println("Neural Network Results:");

        for (MLDataPair pair : trainingSet) {
            final MLData output = network.compute(pair.getInput());

            System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
                    + ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
        }

        Encog.getInstance().shutdown();
    }
}