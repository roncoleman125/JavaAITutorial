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

import java.io.Serializable;

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
     * @param other Particle
     * @return A new particle
     */
    public Particle add(Particle other) {
        Particle result = new Particle(x+other.x,y+other.y);

        return result;
    }

    /**
     * Gets the length of this particle.
     * @return A new particle
     */
    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Multiplies this particle by a scalar constant functionally.
     * @param c Constant
     * @return A new particle
     */
    public Particle mult(double c) {
        Particle result = new Particle(c*x, c*y);

        return result;
    }

    /**
     * Subtracts that from this particle.
     * @param other Particle
     * @return A new particle
     */
    public Particle sub(Particle other) {
        Particle result = new Particle(x-other.x,y-other.y);

        return result;
    }

    /**
     * Normalizes this particle.
     * @return A new normalized particle
     */
    public Particle norm() {
        double length = getLength();

        Particle result = new Particle(x/length, y/length);

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
     * Gets distanceTo to that particle.
     * @param other Particle
     * @return RMSE
     */
    public double getDistanceTo(Particle other) {
        double dx = x - other.x;
        double dy = y - other.y;

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
