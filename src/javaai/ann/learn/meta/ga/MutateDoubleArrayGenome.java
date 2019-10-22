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

import java.util.Random;
import javaai.util.Helper;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.opp.EvolutionaryOperator;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.ml.genetic.genome.DoubleArrayGenome;

/**
 * A simple mutation based on random numbers.
 * @author Ron.Coleman
 * @see org.encog.ml.genetic.mutate.MutatePerturb
 */
public class MutateDoubleArrayGenome implements EvolutionaryOperator {

    /**
     * The amount to perturb by.
     */
    private final double perturbAmount;

    /**
     * Construct a perturb mutation.
     * @param perturbAmount The amount to mutate by (percent).
     */
    public MutateDoubleArrayGenome(final double perturbAmount) {
        this.perturbAmount = perturbAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performOperation(Random rnd, Genome[] parents, int parentIndex, Genome[] offspring, int offspringIndex) {
        // Get a child genome as an identical copy of the parent.
        DoubleArrayGenome parent = (DoubleArrayGenome)parents[parentIndex];

        // (This statement from MutatePerturb.java throws an NPE.)
        //offspring[offspringIndex] = parent.getPopulation().getGenomeFactory().factor();
        offspring[offspringIndex] = Helper.deepCopy(parent);

        DoubleArrayGenome child = (DoubleArrayGenome)offspring[offspringIndex];

        // Mutate each gene of the child
        for(int i=0;i<parent.size();i++) {
            double gene = parent.getData()[i];

            gene += gene * (perturbAmount - (rnd.nextDouble() * perturbAmount * 2));

            child.getData()[i] = gene;
        }
    }

    /**
     * Gets the number of offspring needed for this mutation.
     * @return The number of offspring produced, which is 1 for this mutation.
     */
    @Override
    public int offspringProduced() {
        return 1;
    }

    /**
     * Gets the number of parents needed for this mutation.
     * {@inheritDoc}
     */
    @Override
    public int parentsNeeded() {
        return 1;
    }

    /**
     * Initializes this operator.
     * {@inheritDoc}
     */
    @Override
    public void init(EvolutionaryAlgorithm theOwner) {
    }
}
