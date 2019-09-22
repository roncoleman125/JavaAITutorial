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
package javaai.ann.learn.meta;

/**
 * This class implements the hill climbing algorithm unsupervised learning to find base point (5, 3).
 * @see <a href="https://en.wikipedia.org/wiki/Hill_climbing">HillClimbing climbing</a>
 */
public class HillClimbing {
    // This is the tolerance below which hill climbing converges.
    public final double EPSILON = 0.01;

    /** Want to try to find this point in the plane. */
    public final static double[] XY_GOAL = {5, 3};

    protected double[] curPt = {0, 0};

    protected double[] stepSize = {1, 1};

    double acceleration = 1.2;

    /** Candidate amounts we'll attempt to accelerate **/
    double[] candidates = {-acceleration, -1/acceleration, 0, 1/acceleration, acceleration};

    public static void main(String[] args) {
        HillClimbing hill = new HillClimbing();

        double[] pt = hill.climb();

        System.out.println("solution: x="+pt[0]+" y="+pt[1]);
    }

    /**
     * Climbs the hill.
     * @return Destination
     */
    public double[] climb() {
        int iteration = 1;
        do {
            // Point before any changes
            double before = eval(curPt);

            // Test each dimension of the current point.
            for(int i=0; i < curPt.length; i++) {
                // Best candidate index so far
                int best = -1;

                // If we're minimizing, we should be descending from here
                double bestScore = Double.MAX_VALUE;

                // Try each candidate acceleration
                for(int j=0; j < candidates.length; j++) {
                    // Update the point in the ith direction we're moving
                    curPt[i] += stepSize[i]* candidates[j];

                    // Get its score
                    double newScore = eval(curPt);

                    // Move the point back
                    curPt[i] -= stepSize[i]* candidates[j];

                    // If we improved, remember this acceleration
                    if(newScore < bestScore) {
                        bestScore = newScore;

                        best = j;
                    }
                }

                // If we didn't improve, reduce the step size in this ith dimension
                if(candidates[best] == 0)
                    stepSize[i] /= acceleration;

                // If we improved, use the best acceleration we identified and
                // increase the step in that ith dimension.
                else {
                    curPt[i] += stepSize[i]* candidates[best];

                    stepSize[i] *= candidates[best];
                }
            }

            // Test for convergence
            double curScore = eval(curPt);

            double improved = Math.abs(curScore - before);

            System.out.println(iteration+": score="+curScore+" x="+curPt[0]+" y="+curPt[1]+" improved="+improved);

            if(improved < EPSILON)
                return curPt;

        } while(true);
    }

    /**
     * Implements the objective function.
     * @param pt Point in N-space we're testing
     * @return RMSE
     */
    public double eval(double[] pt) {
        // Compute the um of square error
        double sse = 0;

        for(int k=0; k < pt.length; k++) {
            double delta = pt[k] - XY_GOAL[k];

            sse += (delta * delta);
        }

        // Compute the RMSE
        double rmse = Math.sqrt(sse / pt.length);

        return rmse;
    }
}
