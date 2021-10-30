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
package javaai.metah.generic;

/**
 * This is an interface for reinforcement learning with a feedforward multilayer perceptron.
 * @author Ron.Coleman
 */
public interface Heuristic {
    /**
     * Runs the learning the heuristic over all generations (or epochs).
     * @return Interneuron weights
     */
    public double[] learn();

    /**
     * Runs one cycle of the heuristic.
     * <p>It is invoked by <i>learn</i> method.</p>
     * @return Fitness of the current solution.
     */
    public double evolve();

    /**
     * Gets the best interneuron weights over all generations so far.
     * <p>It is invoked by <i>learn</i> method.</p>
     * @return Interneuron weights
     */
    public double[] getBest();

    /**
     * Gets the current interneuron weights for the most recent generation.
     * <p>Invoked by <i>learn</i> method.</p>
     * @return Interneuron weights
     */
    public double[] getCurrent();

    /**
     * Sets the batch training size.
     * <p>0 == batch all inputs</p>
     * <p>n == batch n inputs</p>
     * <p>Invoked by <i>learn</i> method..</p>
     * * @param size Batch size
     */
    public void setBatch(int size);
}
