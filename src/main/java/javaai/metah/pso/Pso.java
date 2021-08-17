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
package javaai.metah.pso;

import javaai.util.Helper;

import java.util.Random;

/**
 * This class uses particle swarm optimization reinforcment learning to find base point (5, 3).
 * This class differs from Pso1 in that this one uses a separate particle class to be more extensible for
 * training a neural network.
 * @author Ron.Coleman
 * @see "<a href=https://en.wikipedia.org/wiki/Particle_swarm_optimization>Particle swarm optimization</a>"
 * @see "<a href=https://www.amazon.com/Programming-Example-Wordware-Developers-Library-ebook/dp/B0029LCJXE>Buckland, M., Programming Game AI by Example, d.91</a>"
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

    /** Converges if we're at tolerance for this long */
    final static int MAX_SAME = 5;

    /** Search upper range in 2D */
    public final static double MAX = 10.0;

    /** Search lower range in 2D */
    public final static double MIN = -10.0;

    /** Swarm */
    protected Particle[] particles = new Particle[SWARM_SIZE];

    /** Initial best particle */
    protected Particle best = new Particle(Double.MAX_VALUE);

    /** Initial old best particle */
    protected Particle oldBest = best;

    /** Target */
    Particle target = null;


    /** Same count of times best has not changed within the tolerance */
    int sameCount = 0;

    /**
     * Launches the app.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Pso pso = new Pso(GOAL);

        pso.train();
    }

    /**
     * Constructor
     * @param target Unknown target.
     */
    public Pso(Particle target) {
        this.target = target;
    }

    public Pso() {

    }

    /** Solves the learning problem. */
    public void train() {
        // Initialize the ensemble
        init();

        // Get the best particle
        updateBest();

        // Initialize convergence state
        boolean converged = false;

        // Intialize the iteration count
        int iter = 1;

        // Repeat until we converge
        System.out.println(Pso.class.getSimpleName());
        System.out.printf("%3s %7s %7s %7s %s\n","#","x","y","fit","same");

        while (!converged) {
            System.out.printf("%3d %7.4f %7.4f %7.4f %d\n",iter,this.best.getX(),this.best.getY(),best.fitness,sameCount);
            //System.out.println(iteration + " best = " + best + " fitness=" + best.fitness + " same count=" + sameCount);

            // Disturb the particles
            agitate();

            // Move them toward the best one
            move();

            // Get the new best particle
            updateBest();

            // Check for convergence
            converged = didConverge();

            iter++;
        }

        System.out.println("CONVERGED: best = " + best + " fit = " + best.fitness + " same count=" + sameCount);
    }

    /** Initializes the swarm. */
    private void init() {
        // Randomly initialize every particle within the search space
        Random ran = new Random();

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

        return sameCount >= MAX_SAME;
    }

    /** Moves the particles toward the best particle. */
    private void move() {
        // Here's the one we want to move towards
        Particle target = best;

        // Move every particle in direction of the target
        for (int k = 0; k < SWARM_SIZE; k++) {
            Particle current = particles[k];

            // Steer particle in this direction
            Particle delta = target.sub(current);

            // Make the difference a unit vector
            delta = delta.norm();

            // Set the speed of movement
            delta = delta.mult(SPEED);

            // Get the new vector
            Particle newCurrent = current.add(delta);

            // Update that particle
            particles[k] = newCurrent;
        }
    }

    /** Agitates the particles */
    private void agitate() {
        for(Particle particle: particles) {
            particle.agitate();
        }
    }

    /**
     * Runs the objective function.
     * This function will be different for different problems.
     * @param that Particle
     * @return Distance to target
     */
    protected double evaluate(Particle that) {
        // Allowed only to know how far away that is to target
        double dist = that.getDistanceTo(target);

        return dist;
    }

    /** Updates all the particles */
    protected void updateBest() {
        oldBest = best;

        for (Particle that: particles) {
            that.fitness = evaluate(that);

            if (that.fitness < best.fitness)
                best = Helper.deepCopy(that);
        }
    }
}

