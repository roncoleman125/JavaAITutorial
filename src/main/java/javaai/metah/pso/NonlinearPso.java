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
 * This class uses particle swarm optimization reinforcment learning to find max point
 *  * for f(x,y)=e^-(x^2+y^2) + 2*e^-((x-1.7)^2+(y-1.7)^2).
 *  * @author Ron.Coleman
 * @author Ron.Coleman
 * @see "<a href=https://en.wikipedia.org/wiki/Particle_swarm_optimization>Particle swarm optimization</a>"
 * @see "<a href=https://www.amazon.com/Programming-Example-Wordware-Developers-Library-ebook/dp/B0029LCJXE>Buckland, M., Programming Game AI by Example, d.91</a>"
 */
public class NonlinearPso  extends Pso {
    /**
     * Launches the app.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        NonlinearPso pso = new NonlinearPso();

        pso.train();
    }

    /**
     * Constructor
     */
    public NonlinearPso() {
        this.best = new Particle(-Double.MAX_VALUE);
        this.oldBest = best;
    }

    /** Updates all the particles */
    @Override
    protected void updateBest() {
        this.oldBest = this.best;

        for (Particle that: particles) {
            that.fitness = evaluate(that);

            if (that.fitness > best.fitness)
                best = Helper.deepCopy(that);
        }
    }

    /**
     * Implements the objective function.
     * @param particle Point in N-space we're testing
     * @return fitness
     */
    @Override
    public double evaluate(Particle particle) {
        double x = particle.x;
        double y = particle.y;

        double result = Math.exp(-(x*x + y*y)) + 2 * Math.exp(-((x-1.7)*(x-1.7) + (y-1.7)*(y-1.7)));

        return result;
    }
}

