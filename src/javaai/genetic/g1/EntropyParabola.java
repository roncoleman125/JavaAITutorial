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
package javaai.genetic.g1;

import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.Species;
import org.encog.ml.genetic.genome.IntegerArrayGenome;
import java.util.HashMap;
import static javaai.util.Helper.asString;

public class EntropyParabola extends Parabola {
    /** Number of bits in the population */
    public final static double TOLERANCE = 2.5;

    public static void main(String[] args) {
        Parabola parabola = new EntropyParabola();

        IntegerArrayGenome best = parabola.solve();

        System.out.println("best = "+asString(best));
    }

    /**
     * Tests whether GA has converged.
     * @param y Y value in y=f(x)
     * @param pop Population of individuals
     * @return True if the GA has converge, otherwise false
     */
    @Override
    public boolean didConverge(double y, Population pop) {
        double entropy = getEntropy(pop);

        return entropy < TOLERANCE;
    }

    /**
     * Gets the diversity.
     * @param pop Population
     * @return Information entropy in fractional bits
     */
    protected double getEntropy(Population pop) {
        final Species species = pop.getSpecies().get(0);

        HashMap<String, Integer> counter = new HashMap<>();

        for (Genome genome : species.getMembers()) {
            IntegerArrayGenome individual = (IntegerArrayGenome) genome;

            String key = asString(individual);

            Integer count = counter.getOrDefault(key,0);

            counter.put(key, count+1);
        }

        double entropy = 0;

        double sz = pop.size();

        for(String key: counter.keySet()) {
            double n = counter.get(key);

            double p = n / sz;

            entropy -= p * log2(p);
        }

        return entropy;
    }

    protected double log2(double x) {
        return Math.log10(x) / 0.30103;
    }
}
