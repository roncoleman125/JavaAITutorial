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
package javaai.ann.learn.meta.pso;

import java.util.Random;

/**
 * This class uses particle swarm optimization unsupervised learning to find base point (5, 3).
 * @author Ron.Coleman
 * @see "<a href=https://www.amazon.com/Programming-Example-Wordware-Developers-Library-ebook/dp/B0029LCJXE>Buckland, M., Programming Game AI by Example, p.91</a>"
 */
public class Pso1 {
    /** Want to try to find this point in the plane. */
    public final static double[] XY_GOAL = {5, 3};

    /** Number of particles in the swarm */
    public final static int SWARM_SIZE = 100;

    /** How fast the particles move toward the base point */
    public final static double SPEED = 1.0;

    /** Convergence tolerance */
    public final static double TOLERANCE = 0.01;

    /** Search space upper bound in 2D */
    public final static double MAX = 10.0;

    /** Search space lower bound in 2D */
    public final static double MIN = -10.0;

    /** Particles in swarm in 2D */
    protected double[][] particles = new double[SWARM_SIZE][];

    /** Random number generator to initialize and agitate the particles */
    Random ran = new Random(0);

    /** Best z=predict(x,y) found so far -- we are minimizing so... */
    double bestz = Double.MAX_VALUE;

    /** Last best z found */
    double obestz = Double.MAX_VALUE;

    /** Best x, y */
    double[] bestxy = {0.0, 0.0};

    /** Same count of times z has not changed substantially */
    int sameCount = 0;

    /**
     * Launches the app.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Pso1 pso = new Pso1();

        pso.solve();
    }

    /**
     * Solves the problem.
     */
    public void solve() {
        // Initialize the ensemble
        init();

        // Get the best particle
        updateBest();

        // Initialize convergence state
        boolean converged = false;

        // Intializa the iteration count
        int iteration = 1;

        // Do the following until we converge
        while(!converged) {
            System.out.println(iteration+" best x="+bestxy[0]+" y="+bestxy[1]+" z="+bestz+" same count="+sameCount);

            // Disturb the particles
            agitate();

            // Move them toward the best one
            move();

            // Get the new best particle
            updateBest();

            // Check for convergence
            converged = didConverge();

            iteration++;
        }

        System.out.println("CONVERGED: best x="+bestxy[0]+" y="+bestxy[1]+" z="+bestz+" same count="+sameCount);
    }

    /**
     * Initializes the ensemble.
     */
    protected void init() {
        // Randomly initialize every particle within the search space
        for(int k=0; k < SWARM_SIZE; k++) {
            double x = ran.nextDouble()*(MAX - MIN) + MIN;
            double y = ran.nextDouble()*(MAX - MIN) + MIN;

            double[] xy = new double[2];
            xy[0] = x;
            xy[1] = y;

            particles[k] = xy;
        }
    }

    /**
     * Tests for convergence
     * @return True if we converge, false otherwise
     */
    protected boolean didConverge() {
        if(Math.abs(bestz - obestz) < TOLERANCE)
            sameCount++;
        else
            sameCount = 1;

        return sameCount >= 5;
    }


    /**
     * Moves the particles toward the best particle.
     */
    protected void move() {
        // Here's the one we want to move towards
        double[] target = bestxy;

        // Move every particle in direction of the target
        for(int k=0; k < SWARM_SIZE; k++) {
            double[] current = particles[k];

            // This is the difference between where we want to go and where we are
            double[] delta = subtract(target, current);

            // Make the difference a unit vector
            delta = normalize(delta);

            // Set the speed of movement
            delta = multiply(delta, SPEED);

            // Get the new vector
            double[] newCurrent = add(current, delta);

            // Update that particle
            particles[k] = newCurrent;
        }
    }

    /**
     * Disturbs the particles.
     */
    protected void agitate() {
        for(int k=0; k < SWARM_SIZE; k++) {
            double dx = ran.nextDouble();
            double dy = ran.nextDouble();

            particles[k][0] += dx;
            particles[k][1] += dy;
        }
    }

    /**
     * Computes the objective function.
     * @param x X coordinate
     * @param y Y coordinate
     * @return Distance to the goal
     */
    protected double getFitness(double x, double y) {
        double dx = (x - XY_GOAL[0]);
        double dy = (y - XY_GOAL[1]);

        double dist = Math.sqrt(dx*dx + dy*dy);

        return dist;
    }

    /**
     * Updates the best particle.
     */
    public void updateBest() {
        obestz = bestz;
        for(int k=0; k < SWARM_SIZE; k++) {
            double x = particles[k][0];
            double y = particles[k][1];

            double z = getFitness(x, y);

            if(z < bestz) {
                bestz = z;
                bestxy[0] = x;
                bestxy[1] = y;
            }
        }
    }


    /**
     * Subtracts two vectors.
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return v1-v2
     */
    protected double[] subtract(double[] v1, double[] v2) {
        double[] result = new double[2];

        result[0] = v1[0] - v2[0];

        result[1] = v1[1] - v2[1];

        return result;
    }

    /**
     * Normalizes a vector, ie, returns a vector of unit length
     * @param v Vector
     * @return Normalized vector
     */
    protected double[] normalize(double[] v) {
        double[] result = new double[2];

        double length = getLength(v);

        result[0] = v[0] / length;
        result[1] = v[1] / length;

        return result;
    }

    /**
     * Multiplies a vector times a scalar constant.
     * @param v Vector
     * @param c Constant
     * @return c*v
     */
    protected double[] multiply(double[] v, double c) {
        double[] result = new double[2];

        result[0] = v[0] * c;
        result[1] = v[1] * c;

        return result;
    }

    /**
     * Adds two vectors.
     * @param v1
     * @param v2
     * @return v1 + v2
     */
    protected double[] add(double[] v1, double[] v2) {
        double[] result = new double[2];

        result[0] = v1[0] + v2[0];
        result[1] = v1[1] + v2[1];

        return result;
    }

    /**
     * Gets the vector length.
     * @param v Vector
     * @return Length
     */
    protected double getLength(double[] v) {
        return Math.sqrt(v[0]*v[0] + v[1]*v[1]);
    }
}


