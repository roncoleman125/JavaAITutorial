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
package javaai.ann.basicx;

/**
 * This class implements a basic ANN as an example of backpropagation.
 * @see "https://www.nnwj.de/backpropagation.html"
 * @author Ron.Coleman
 */
public class BackpropNetwork {
    public final static int NUM_EPOCHS = 500;

    public final static double LEARNING_RATE = 0.25;

    // Weight matrix
    private double[][][] ws = {
            // layer 0 (input)

            // layer 1 (hidden)
            {
                    {0.62, 0.55},   // neuron 0 w. two inputs no bias
                    {0.42, -0.17},  // neuron 1 w. two inputs no bias
            },

            // layer 2 (output)
            {
                    {0.35, 0.81}    // neuron 0 w. two inputs no bias
            }
    };

    // Input pattern to train
    protected double[][] inputs  = {
            {0, 1},
            {1, 1} ,
    };

    // Output ideal pattern to train
    protected double[] ideals = {
            0,
            1
    };

    public static void main(String[] args) {
        BackpropNetwork bp = new BackpropNetwork();

        run(bp);
    }

    /**
     * Runs one epoch of the training function.
     * @return Training error as mean square error.
     */
    public double train() {
        // Error squared sum
        double errorSum = 0;

        // For each set of inputs, compute the training error
        for (int inputno = 0; inputno < inputs.length; inputno++) {
            // Clear the temporary storage.
            clear();

            // Get the inputs for this input cycle
            double[] _inputs = inputs[inputno];

            // For each layer do feedforward
            for (int layer = 0; layer < ws.length; layer++) {

                // Allocate storage for layer outputs
                double[] outputs = new double[ws[layer].length];

                // For each neuron in the layer
                for (int neuron = 0; neuron < ws[layer].length; neuron++) {

                    // Initialize the sum
                    double sum = 0;

                    // Compute the weighted sum
                    for (int i = 0; i < ws[layer][neuron].length; i++) {
                        // Get weight for the input
                        double weight = ws[layer][neuron][i];

                        // Get input
                        double input = _inputs[i];

                        // Get the strength of this output
                        double strength = weight * input;

                        // Add to total strength
                        sum += strength;
                    }

                    // Get the neuron's activation as its output
                    double activation = sigmoid(sum);

                    // Update the neuron's output
                    outputs[neuron] = activation;
                }

                // Outputs of neuron are inputs to next layer
                _inputs = outputs;

                // Unless layer is last layer, save layer's output which we need for backpropagation
                if (layer != ws.length - 1) {
                    push(outputs);

                    continue;
                }

                // This is the output of the three layers 0, 1, 2 ANN.
                double output2 = outputs[0];

                // Compute the error
                double error = ideals[inputno] - output2;

                // Get the hidden layer outputs
                double[] outputs1 = pop();

                // Backpropagate the error to hidden layer
                for (int i = layer; i > 0; i--) {
                    // For each neuron in the layer...
                    for (int neuron = 0; neuron < ws[layer].length; neuron++) {
                        // For each output of a neuron in the layer...
                        for (int j = 0; j < outputs1.length; j++) {
                            // Get hidden neuron's output
                            double output1 = outputs1[j];

                            // Compute the weight change in direction of first derivative
                            double dw = LEARNING_RATE * error * output1 * output2 * (1.0 - output2);

                            // Get current weight
                            double curw = ws[layer][neuron][j];

                            // Compute new weight
                            double neww = curw + dw;

                            // Update weight for the layer's neuron's output
                            ws[layer][neuron][j] = neww;
                        }
                    }

                }

                // Backpropagate error to input layer.
                // (This is a hack as the code is nearly same for hidden layers...we'll integrate the two some day.)
                // Also note: the outer j loop is over the outputs whereas the inner loop is over the neurons --
                // opposite for propagating the error to the hidden layer.
                final int INPUT_LAYER = 0;
                for (int j = 0; j < outputs1.length; j++) {
                    for (int neuron = 0; neuron < ws[INPUT_LAYER].length; neuron++) {
                        // Get input neuron's output
                        double output0 = inputs[inputno][j];

                        // Get hidden neuron's output
                        double output1 = outputs1[neuron];

                        // Compute the weight change in direction of first derivative
                        double dw = LEARNING_RATE * error * output0 * output1 * (1.0 - output1);

                        // Get current weight
                        double curw = ws[INPUT_LAYER][neuron][j];

                        // Compute new weight
                        double neww = curw + dw;

                        // Update weight for the layer's neuron's output
                        ws[INPUT_LAYER][neuron][j] = neww;
                    }
                }

                // Update total square error
                errorSum += error * error;
            }
        }

        // Compute the mean square error
        double mse = errorSum / ideals.length;

        return mse;
    }

    public double compute(double[] inputs) {
        double[] outputs = null;

        // For each layer do feedforward
        for (int layer = 0; layer < ws.length; layer++) {

            // Allocate storage for layer outputs
            outputs = new double[ws[layer].length];

            // For each neuron in the layer
            for (int neuron = 0; neuron < ws[layer].length; neuron++) {

                // Initialize the sum
                double sum = 0;

                // Compute the weighted sum
                for (int i = 0; i < ws[layer][neuron].length; i++) {
                    // Get weight for the input
                    double weight = ws[layer][neuron][i];

                    // Get input
                    double input = inputs[i];

                    // Get the strength of this output
                    double strength = weight * input;

                    // Add to total strength
                    sum += strength;
                }

                // Get the neuron's activation as its output
                double activation = sigmoid(sum);

                // Update the neuron's output
                outputs[neuron] = activation;
            }

            // Outputs of neuron are inputs to next layer
            inputs = outputs;
        }

        return outputs[0];
    }

    public static void run(BackpropNetwork bp) {
        for(int epoch=0; epoch < NUM_EPOCHS; epoch++) {
            double error = bp.train();

            System.out.println(error);
        }

        test(bp);
    }

    public static void test(BackpropNetwork bp) {
        double[][] inputs = {
                {0, 1},
                {1, 1},
                {1, 0},
                {0, 0}
        };

        double[] ideals = {
                0,
                1,
                0,
                0
        };

        for(int k=0; k < inputs.length; k++) {
            double actual = bp.compute(inputs[k]);

            System.out.println("actual="+actual+" ideal="+ideals[k]);
        }
    }

    protected double[][] stack = new double[inputs.length][];
    protected int top = -1;

    protected void push(double[] outputs) {
        double[] store = new double[outputs.length];

        for(int k=0; k < outputs.length; k++)
            store[k] = outputs[k];

        top++;
        stack[top] = store;
    }

    protected double[] pop() {
        if(top < 0)
            return null;

        return stack[top--];
    }

    protected void clear() {
        top = -1;
    }


    protected double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }
}
