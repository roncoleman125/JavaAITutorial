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
import org.encog.ml.genetic.genome.IntegerArrayGenome;

/**
 * A simple mutation based on random numbers.
 * @author Ron.Coleman
 * @see @see "<a href=https://github.com/encog/encog-java-core/blob/master/src/main/java/org/encog/ml/genetic/mutate/MutatePerturb.java>MutatePertub.java</a>
 */
public class MutateIntegerArrayGenome implements EvolutionaryOperator {
    protected int range;

    /**
     * Constructor
     * @param range Range of positive integer values, exclusive.
     */
    public MutateIntegerArrayGenome(int range) {
        this.range = range;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performOperation(Random ran, Genome[] parents, int parentIndex,Genome[] offsprings, int offspringIndex) {
        // Get a child genome as an identical copy of the parent.
        IntegerArrayGenome parent = (IntegerArrayGenome) parents[parentIndex];

        // The line below copied from MutatePertub.java crashes with NPE.
        // Genome offspring = parent.getPopulation().getGenomeFactory().factor();

        offsprings[offspringIndex] = Helper.deepCopy(parent);

        IntegerArrayGenome child = (IntegerArrayGenome)offsprings[offspringIndex];

        // Update the child genes
        int[] childGenes = child.getData();

        for(int k=0; k <childGenes.length; k++) {

            int oldGene = child.getData()[k];

            int newGene = ran.nextInt(range);

            if(oldGene != newGene)
                child.getData()[k] = newGene;
        }

        // Uncomment this for debugging.
        // int[] parentGenes = parent.getData();
    }

    /**
     * Gets the number of offspring to produce
     * @return The number of offspring produced, which is 1 for this mutation.
     */
    @Override
    public int offspringProduced() {
        return 1;
    }

    /**
     * Gets the number of parents needed.
     */
    @Override
    public int parentsNeeded() {
        return 1;
    }

    /**
     * Initializes the operator
     */
    @Override
    public void init(EvolutionaryAlgorithm theOwner) {
    }
}
