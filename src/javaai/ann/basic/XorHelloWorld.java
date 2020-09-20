package javaai.ann.basic;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.BasicTraining;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
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

    /** Maximum number of iterations to run */
    public final static long MAX_EPOCHS = 100000L;

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
        // Create a neural network, without using a factory
        BasicNetwork network = new BasicNetwork();

        // Add input layer with no activation function, bias enabled, and two neurons
        network.addLayer(new BasicLayer(null, true, 2));

        // Add hidden layer with ramped activation, bias enabled, and five neurons
        // NOTE: ActivationReLU is not in javadoc but can be found here http://bit.ly/2zyxk7A.
        // network.addLayer(new BasicLayer(new ActivationReLU(), true, 5));

        // Add hidden layer with sigmoid activation, bias enabled, and two neurons
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 2));

        // Add report layer with sigmoid activation, bias disable, and one neuron
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        // No more layers to be added
        network.getStructure().finalizeStructure();

        // Randomize the weights
        network.reset();

        report(network);

        // Create training data
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUTS, XOR_IDEALS);

        // Train the neural network.
        // Use a training object to train the network, in this case, an improved
        // back propagation. For details on what this does see the javadoc.
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);

        int epoch = 0;

        report(epoch, train,false);
        do {
            train.iteration();

            long now = System.currentTimeMillis();

            epoch++;

            report(epoch,train,false);

        } while (train.getError() > TOLERANCE && epoch < MAX_EPOCHS);

        train.finishTraining();

        report(epoch, train,true);

        report(trainingSet, network);

        report(network);

        Encog.getInstance().shutdown();
    }

    /**
     * Reports results.
     * @param trainingSet Training set of data
     * @param network Network
     */
    protected static void report(MLDataSet trainingSet,BasicNetwork network) {
        // Test the neural network
        System.out.println("Neural Network Results:");

        System.out.printf("%3s   %3s   %3s   %6s\n","x1","x2","t1","y1");
        for (MLDataPair pair : trainingSet) {

            final MLData output = network.compute(pair.getInput());

            double x1 = pair.getInput().getData(0);
            double x2 = pair.getInput().getData(1);
            double ideal = pair.getIdeal().getData(0);
            double actual = output.getData(0);

            System.out.printf("%3.1f   %3.1f   %3.1f   %6.4f\n",x1,x2,ideal,actual);
        }
    }

    /**
     * Reports each epoch.
     * @param epoch Epoch number
     * @param train Training results
     * @param done True if the training is done
     */
    protected static void report(int epoch, BasicTraining train,boolean done) {
        final int FREQUENCY = 10;

        // Report only the header
        if(epoch == 0)
            System.out.printf("%8s %6s\n","epoch","error");

        else if(epoch == 1 || (!done && (epoch % FREQUENCY) == 0)) {
            System.out.printf("%8d %6.4f\n", epoch, train.getError());
        }
        // Report only if we haven't just reported
        else if(done && (epoch % FREQUENCY) != 0)
            System.out.printf("%8d %6.4f\n", epoch, train.getError());

        if(epoch >= MAX_EPOCHS)
            System.out.println("--- DO NOT CONVERGE!");
    }

    /**
     * Reports summary of network weights.
     * @param network
     */
    protected static void report(BasicNetwork network) {
        int layerCount = network.getLayerCount();

        int neuronCount = 0;
        for(int layerNum=0; layerNum < layerCount; layerNum++) {
            // int neuronCount = network.calculateNeuronCount();  // Should work but doesn't appear to
            neuronCount += network.getLayerTotalNeuronCount(layerNum);
        }

        System.out.println("total layers: "+layerCount+" neurons: "+neuronCount);
        System.out.printf("%5s %5s %5s %10s\n","layer","from","to","wt");

        for(int layerNum=0; layerNum < layerCount; layerNum++) {
            if(layerNum+1 < layerCount) {
                int fromNeuronCount = network.getLayerNeuronCount(layerNum);

                int toNeuronCount = network.getLayerNeuronCount(layerNum+1);

                for (int fromNeuron = 0; fromNeuron < fromNeuronCount; fromNeuron++) {
                    for (int toNeuron = 0; toNeuron < toNeuronCount; toNeuron++) {
                        double wt = network.getWeight(layerNum, fromNeuron, toNeuron);
                        System.out.printf("%5d %5d %5d %10.4f\n",layerNum,fromNeuron,toNeuron,wt);
                    }
                }

                if(network.isLayerBiased(layerNum)) {
                    double bias = network.getLayerBiasActivation(layerNum);
                    System.out.printf("%5d %5d %5d %10.4f BIAS\n",layerNum,layerNum,layerNum+1,bias);
                }
            }
        }
    }
}
