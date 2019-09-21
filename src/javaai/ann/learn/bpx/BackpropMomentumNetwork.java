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
package javaai.ann.learn.bpx;

/**
 * This class implements a basic ANN as an example of backpropagation with bias and momentum.
 * @see "https://www.nnwj.de/backpropagation.html"
 * @author Ron.Coleman
 */
public class BackpropMomentumNetwork extends BackpropBiasNetwork {
    // See https://jamesmccaffrey.wordpress.com/2017/06/06/neural-network-momentum/
    public final static double MOMENTUM = 0.30;

    // Prior weight change container
    double[] dws;

    public static void main(String[] args) {
        BackpropNetwork bp = new BackpropMomentumNetwork();

        run(bp);
    }

    /**
     * Runs one epoch of training.
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
                        if(i < ws[layer][neuron].length-1)
                            input = _inputs[i];
                        else
                            input = 1.0;
                        double strength = weight * input;
                        sum += strength;
                    }

                    double activation = sigmoid(sum);

                    outputs[neuron] = activation;
                }

                _inputs = outputs;

                if (layer != ws.length - 1) {
                    push(outputs);
                    continue;
                }

                // This is the output from the ANN.
                double output2 = outputs[0];

                // So here's it error.
                double error = ideals[inputno] - output2;

                double[] outputs1 = pop();

                // Initialize index into prior weight changes container for momentum.
                int dwi = 0;

                // Initialiaze the prior weight container
                // Note: we could possibly initialize this more elegantly elsewhere except we may NOT know
                // the dynamic size of output1.
                if(dws == null) {
                    int sz = (ws.length - 1) * ws[layer].length * outputs1.length + outputs1.length * ws[0].length;

                    dws = new double[sz];
                }

                // Backpropagate error to hidden layers.
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

                            // Compute new weight taking into account momentum
                            double neww = curw + dw + dws[dwi] * MOMENTUM;

                            // Update weight for the layer's neuron's output
                            ws[layer][neuron][j] = neww;

                            // Update the weight change
                            dws[dwi++] = dw;
                        }
                    }

                }

                // Backpropagate error to input layer.
                // (This is a hack as the code is nearly same for hidden layers...we'll integrate the two some day.)
                for (int j = 0; j < outputs1.length; j++) {
                    for (int neuron = 0; neuron < ws[0].length; neuron++) {
                        double output0 = inputs[inputno][j];

                        double output1 = outputs1[neuron];

                        double dw = LEARNING_RATE * error * output0 * output1 * (1.0 - output1);

                        double curw = ws[0][neuron][j];

                        double neww = curw + dw + dws[dwi] * MOMENTUM;

                        ws[0][neuron][j] = neww;

                        // Update the weight change
                        dws[dwi++] = dw;

                    }
                }
                errorSum += error * error;

            }
        }

        double mse = errorSum / ideals.length;

        return mse;
    }
}
