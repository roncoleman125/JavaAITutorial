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
 * This class implements a basic ANN as an example of backpropagation with bias.
 * @see "https://www.nnwj.de/backpropagation.html"
 * @author Ron.Coleman
 */
public class BackpropBiasNetwork extends BackpropNetwork {
    // Weight matrix
    protected double[][][] ws = {
            // layer 0 (input)

            // layer 1 (hidden)
            {
                    {0.62, 0.55, 0.29},   // neuron 0 w. two inputs plus bias (0.29)

                    {0.42, -0.17, 0.10},  // neuron 1 w. two inputs plus bias (0.10)
            },

            // layer 2 (output)
            {
                    {0.35, 0.81, -0.73}   // neuron 0 w. two inputs plus bias (-0.73)
            }
    };

    public static void main(String[] args) {
        BackpropNetwork bp = new BackpropBiasNetwork();

        run(bp);
    }

    /**
     * Runs one epoch of the training function.
     * @return Training error as mean square error.
     */
    @Override
    public double train() {
        double errorSum = 0;

        for (int inputno = 0; inputno < inputs.length; inputno++) {
            top = -1;
            double[] _inputs = inputs[inputno];
            for (int layer = 0; layer < ws.length; layer++) {

                double[] outputs = new double[ws[layer].length];

                for (int neuron = 0; neuron < ws[layer].length; neuron++) {
                    double sum = 0;

                    for (int i = 0; i < ws[layer][neuron].length; i++) {
                        double weight = ws[layer][neuron][i];

                        double input = 0;

                        // The last input is the bias or "pseudo input" 1.0
                        if(i < ws[layer][neuron].length-1)
                            input = _inputs[i];
                        else
                            input = 1.0;

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

                // Unless layer is last layer, save layer's outputs which we need for backpropagation
                if (layer != ws.length - 1) {
                    push(outputs);
                    continue;
                }

                // This is the output of the three layers 0, 1, 2 ANN.
                double output2 = outputs[0];

                // Compute the error
                double error = ideals[inputno] - output2;

                double[] outputs1 = pop();

                // Backpropagate error through the hidden layer
                for (int i = layer; i > 0; i--) {
                    for (int neuron = 0; neuron < ws[layer].length; neuron++) {
                        for (int j = 0; j < outputs1.length; j++) {
                            double output1 = outputs1[j];

                            double dw = LEARNING_RATE * error * output1 * output2 * (1.0 - output2);

                            double curw = ws[layer][neuron][j];

                            double neww = curw + dw;

                            ws[layer][neuron][j] = neww;
                        }
                    }

                }

                // Backpropagate the error to the input layer
                for (int j = 0; j < outputs1.length; j++) {
                    for (int neuron = 0; neuron < ws[0].length; neuron++) {
                        double output0 = inputs[inputno][j];

                        double output1 = outputs1[neuron];

                        double dw = LEARNING_RATE * error * output0 * output1 * (1.0 - output1);

                        double curw = ws[0][neuron][j];

                        double neww = curw + dw;

                        ws[0][neuron][j] = neww;
                    }
                }
                errorSum += error * error;
            }
        }

        double mse = errorSum / ideals.length;

        return mse;
    }
}
