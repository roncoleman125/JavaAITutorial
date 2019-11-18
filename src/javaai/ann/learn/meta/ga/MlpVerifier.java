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
package javaai.ann.learn.meta.ga;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Function;

/**
 * This class checks the MLP learning for XOR and Circuit 1 given the interneuron weights.
 * As this class is abstract, it cannot be instantiated directly but can only be subclassed.
 * @author Ron.Coleman
 */
abstract public class MlpVerifier {
    /**
     * This method implements the feedforward equations.
     * @param xs X inputs
     * @param ws Interneuron weights
     * @return Double
     */
    abstract public double feedforward(double[] xs, double[] ws);

    /**
     * Outputs the interneuron weights.
     * @param ws Interneuron weights
     */
    abstract public void output(double[] ws);


    public final static int XOR_SIZE = 8;
    public final static int CIRCUIT1_SIZE = 10;

    /** Inputs */
    protected double[][] inputs;

    /** Ideal outputs */
    protected double[][] ideals;

    public static void main(String[] args)
    {
        try {
            // Read in the weights from the console
            System.out.print("Input weights: ");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String line = reader.readLine();

            String[] fields = line.split(" +");

            if(fields.length < 8 || fields.length > 10)
                throw new Exception("invalid input");

            double[] weights = new double[fields.length];

            for(int k=0; k < fields.length; k++)
                weights[k] = Double.parseDouble(fields[k]);

            // Instantiate and invoke the verifier
            MlpVerifier verifier = null;

            if(verifying(XOR, weights))
                verifier = new XorVerifier();

            else if(verifying(CIRCUIT1, weights))
                verifier = new Circuit1Verifier();

            else
                throw new Exception("invalid number of interneuron weights");

            verifier.analyze(weights);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Does the sigmoid activation function.
     * @param z Parameter
     * @return Double
     */
    protected double sigmoid(double z) {
        double y = 1.0 / (1.0 + Math.exp(-z));

        return y;
    }

    /**
     * Analyzes the interneuron weights.
     * @param ws Interneuron weights
     */
    public void analyze(double[] ws) {
        output(ws);

        for(int k=0; k < inputs[0].length; k++)
            System.out.printf("x%-3s",(k+1));
        System.out.printf("%-3s %s\n","t1","y1");

        double sumSqrErr = 0;

        for(int k=0; k < inputs.length; k++) {
            double[] xs = inputs[k];
            double t1 = ideals[k][0];
            double y1 = feedforward(xs, ws);

            double sqrError = (y1 - t1) * (y1 - t1);
            sumSqrErr += sqrError;

            for(double x: xs)
                System.out.printf("%-3.0f ",x);

            System.out.printf("%-3.0f %f\n",t1,y1);
        }

        double rmse = Math.sqrt(sumSqrErr / inputs.length);

        System.out.printf("RMSE = %f\n",rmse);
    }

    /** Verifying for XOR */
    static Function<double[], Boolean> XOR = (weights) -> weights.length == 8;

    /** Verifying for circuit 1 */
    static Function<double[], Boolean> CIRCUIT1 = (weights) -> weights.length == 10;

    /**
     * Verifies the interneuron choice.
     * @param function
     * @param ws
     * @return
     */
    public static Boolean verifying(Function<double[], Boolean> function, double[] ws) {
        return function.apply(ws);
    }

}

/**
 * Verifies the XOR circuit.
 */
class XorVerifier extends MlpVerifier {
    public final static double INPUTS[][] = {
            {0.0, 0.0},
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
    };

    public final static double IDEALS[][] = {
            {0.0},
            {1.0},
            {1.0},
            {0.0}
    };

    /**
     * Constructor
     */
    public XorVerifier() {
        this.inputs = INPUTS;
        this.ideals = IDEALS;
    }

    /**
     * {@inheritDoc}
     */
    public void output(double[] ws) {
        for(int k=0; k < ws.length; k++)
            System.out.printf("%s%d = %7.3f\n",
                    (k<6)?"w":"b",
                    (k<6)?(k+1):(k-6+1),
                    ws[k]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double feedforward(double[] xs, double[] ws) {
        double x1 = xs[0];
        double x2 = xs[1];
        double w1 = ws[0];
        double w2 = ws[1];
        double w3 = ws[2];
        double w4 = ws[3];
        double w5 = ws[4];
        double w6 = ws[5];
        double b1 = ws[6];
        double b2 = ws[7];

        double zh1 = w1*x1 + w3*x2 + b1;
        double zh2 = w2*x1 + w4*x2 + b1;
        double h1 = sigmoid(zh1);
        double h2 = sigmoid(zh2);
        double zy1 = w5*h1 + w6*h2 + b2;
        double y1 = sigmoid(zy1);

        return y1;
    }
}

/**
 * Verifies circuit 1.
 */
class Circuit1Verifier extends MlpVerifier {
    public static double INPUTS[][] = {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0},
            {0.0, 1.0, 0.0},
            {0.0, 1.0, 1.0},
            {1.0, 0.0, 0.0},
            {1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 1.0}
    };

    public static double IDEALS[][] = {
            {1.0},
            {1.0},
            {0.0},
            {1.0},
            {0.0},
            {0.0},
            {0.0},
            {1.0},
    };

    /**
     * Constructor
     */
    public Circuit1Verifier() {
        this.inputs = INPUTS;
        this.ideals = IDEALS;
    }

    /**
     * {@inheritDoc}
     */
    public void output(double[] ws) {
        for(int k=0; k < ws.length; k++)
            System.out.printf("%s%d = %7.3f\n",
                    (k<8)?"w":"b",
                    (k<8)?(k+1):(k-8+1),
                    ws[k]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double feedforward(double[] xs, double[] ws) {
        double x1 = xs[0];
        double x2 = xs[1];
        double x3 = xs[2];
        double w1 = ws[0];
        double w2 = ws[1];
        double w3 = ws[2];
        double w4 = ws[3];
        double w5 = ws[4];
        double w6 = ws[5];
        double w7 = ws[6];
        double w8 = ws[7];

        double b1 = ws[8];
        double b2 = ws[9];

        double zh1 = w1*x1 + w3*x2 + w7* x3 + b1;
        double zh2 = w2*x1 + w4*x2 + w8* x3 + b1;
        double h1 = sigmoid(zh1);
        double h2 = sigmoid(zh2);
        double zy1 = w5*h1 + w6*h2 + b2;
        double y1 = sigmoid(zy1);

        return y1;
    }
}


