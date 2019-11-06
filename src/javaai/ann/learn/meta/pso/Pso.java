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

import javaai.util.Helper;

import java.io.Serializable;
import java.util.Random;

/**
 * This class uses particle swarm optimization unsupervised learning to find base point (5, 3).
 * This class differs from Pso1 in that this one uses a separate particle class to be more extensible for
 * training a neural network.
 * @author Ron.Coleman
 * @see "<a href=https://en.wikipedia.org/wiki/Particle_swarm_optimization>Particle swarm optimization</a>"
 * @see "<a href=https://www.amazon.com/Programming-Example-Wordware-Developers-Library-ebook/dp/B0029LCJXE>Buckland, M., Programming Game AI by Example, p.91</a>"
 */
public class Pso {
    /** Want to try to find this point in the plane. */
    public final static Particle GOAL = new Particle(5, 3);

    /** Number of particles in the swarm */
    public final static int SWARM_SIZE = 100;

    /** Speed at which particles move toward best */
    public final static double SPEED = 1.0;

    /** Convergence test */
    public final static double TOLERANCE = 0.001;

    /** Search upper range in 2D */
    public final static double MAX = 10.0;

    /** Search lower range in 2D */
    public final static double MIN = -10.0;

    /** Swarm */
    protected Particle[] particles = new Particle[SWARM_SIZE];

    /** Random number generator to initialize and agitate swarm */
    Random ran = new Random(0);

    /** Initial best particle */
    Particle best = new Particle(Double.MAX_VALUE);

    /** Initial old best particle */
    Particle oldBest = best;

    /** Same count of times best has not changed within the tolerance */
    int sameCount = 0;

    /**
     * Launches the app.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Pso pso = new Pso();

        pso.solve();
    }

    /** Solves the learning problem. */
    public void solve() {
        // Initialize the ensemble
        init();

        // Get the best particle
        updateBest();

        // Initialize convergence state
        boolean converged = false;

        // Intializa the iteration count
        int iteration = 1;

        // Repeat until we converge
        System.out.println("PSO");
        System.out.printf("%3s %7s %7s %7s %s\n","#","x","y","RMSE","same");

        while (!converged) {
            System.out.printf("%3d %7.4f %7.4f %7.4f %d\n",iteration,best.getX(),best.getY(),best.fitness,sameCount);
            //System.out.println(iteration + " best = " + best + " fitness=" + best.fitness + " same count=" + sameCount);

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

        System.out.println("CONVERGED: best = " + best + " RMSE = " + best.fitness + " same count=" + sameCount);
    }

    /** Initializes the swarm. */
    private void init() {
        // Randomly initialize every particle within the search space
        for (int k = 0; k < SWARM_SIZE; k++) {
            double x = ran.nextDouble() * (MAX - MIN) + MIN;
            double y = ran.nextDouble() * (MAX - MIN) + MIN;

            particles[k] = new Particle(x, y);
        }
    }

    /**
     * Tests for convergence
     * @return True if we converge, false otherwise
     */
    private boolean didConverge() {
        if (Math.abs(best.fitness - oldBest.fitness) < TOLERANCE)
            sameCount++;
        else
            sameCount = 1;

        return sameCount >= 5;
    }

    /** Moves the particles toward the best particle. */
    private void move() {
        // Here's the one we want to move towards
        Particle target = best;

        // Move every particle in direction of the target
        for (int k = 0; k < SWARM_SIZE; k++) {
            Particle current = particles[k];

            // This is the difference between where we want to go and where we are
            Particle delta = target.subtract(current);

            // Make the difference a unit vector
            delta = delta.normalize();

            // Set the speed of movement
            delta = delta.multiply(SPEED);

            // Get the new vector
            Particle newCurrent = current.add(delta);

            // Update that particle
            particles[k] = newCurrent;
        }
    }

    /** Agitates the particles */
    private void agitate() {
        for (int k = 0; k < SWARM_SIZE; k++) {
            double dx = ran.nextDouble();
            double dy = ran.nextDouble();

            particles[k].perturb(dx, dy);
        }
    }

    /**
     * Runs the objective function.
     * @param that Particle
     * @return Distance to the goal
     */
    private double evaluate(Particle that) {
        double dist = that.getDistanceTo(GOAL);

        return dist;
    }

    /** Updates all the particles */
    private void updateBest() {
        oldBest = best;

        for (int k = 0; k < SWARM_SIZE; k++) {
            Particle that = particles[k];

            that.fitness = evaluate(that);

            if (that.fitness < best.fitness)
                best = Helper.deepCopy(that);
        }
    }
}

/**
 * Particle class
 * @author Ron.Coleman
 */
class Particle implements Serializable {
    /** Particle fitness */
    public double fitness;

    /** Particle's x component */
    protected double x = 0.0;

    /** Particle's y component */
    protected double y = 0.0;

    /**
     * Constructor
     * @param fitness Fitness
     */
    public Particle(double fitness) {
        this.fitness = fitness;
    }

    /** Constructor
     * @param x X component
     * @param y Y component
     */
    public Particle(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds particles this and that functionally.
     * @param that Particle
     * @return A new particle
     */
    public Particle add(Particle that) {
        Particle result = new Particle(this.x+that.x,this.y+that.y);

        return result;
    }

    /**
     * Gets the length of this particle.
     * @return A new particle
     */
    public double getLength() {
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    /**
     * Multiplies this particle by a scalar constant functionally.
     * @param c Constant
     * @return A new particle
     */
    public Particle multiply(double c) {
        Particle result = new Particle(c*this.x, c*this.y);

        return result;
    }

    /**
     * Subtracts that from this particle.
     * @param that Particle
     * @return A new particle
     */
    public Particle subtract(Particle that) {
        Particle result = new Particle(this.x-that.x,this.y-that.y);

        return result;
    }

    /**
     * Normalizes this particle.
     * @return A new normalized particle
     */
    public Particle normalize() {
        double length = getLength();

        Particle result = new Particle(this.x/length, this.y/length);

        return result;
    }

    /**
     * Perturbs this particle.
     * @param dx Amount in X component
     * @param dy Amount in Y component
     */
    public void perturb(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Gets distance to that particle.
     * @param that Particle
     * @return RMSE
     */
    public double getDistanceTo(Particle that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;

        double dist = Math.sqrt((dx*dx + dy*dy)/2.0);

        return dist;
    }

    /**
     * Gets the x component.
     * @return X
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y component.
     * @return Y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets string representation of the particle.
     * @return
     */
    @Override
    public String toString() {
        return "("+x+", "+y+")";
    }
}

