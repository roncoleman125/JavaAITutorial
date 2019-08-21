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
package javaai.genetic;

import org.encog.ml.CalculateScore;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.population.BasicPopulation;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.BasicSpecies;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.genetic.genome.IntegerArrayGenome;
import org.encog.ml.genetic.genome.IntegerArrayGenomeFactory;
import org.encog.ml.genetic.mutate.MutateShuffle;
import java.util.Random;
import static javaai.util.Helper.asString;

/**
 * This class uses a genetic algorithm for unsupervised learning to solve y = (x-3)^2.
 */
public class Parabola {
    /** Stopping criteria as difference between best solution and last best one */
    public final double TOLERANCE = 1.0;

    /** Convergence criteria: number of time best solution stays best */
    public final static int MAX_SAME_COUNT = 5;

    /** Population has this many individuals. */
    public final static int POPULATION_SIZE = 50;

    /** Chromosome size (ie, number of genes): domain is [0, (2^n-1)]. */
    public final static int CHROMOSOME_SIZE = 7;

    /** Mutation rate */
    public final static double MUTATION_RATE = 0.10;

    /** Used to initialize an individual in population */
    protected Random ran = new Random(0);

    /**
     * Runs the program.
     * @param args Command line arguments not used.
     */
    public static void main(String[] args) {

        Parabola parabola = new Parabola();

        IntegerArrayGenome best = parabola.solve();

        System.out.println("best = "+asString(best));
    }

    /**
     * Solves the objective.
     * @return Best individual
     */
    public IntegerArrayGenome solve() {
        // Initialize a population
        Population pop = initPop();
        dump("before", pop);

        // Get the fitness measure
        CalculateScore fitness = new Fitness();

        // Create the evolutionary training algorithm
        TrainEA genetic = new TrainEA(pop, fitness);

        // Assume the default splice for crossover is random
//        genetic.addOperation(0.9, new SpliceNoRepeat(CHROMOSOME_SIZE/2));

        // Set the mutation rate
        genetic.addOperation(MUTATION_RATE, new MutateShuffle());

        // Do the learning algorithm
        learn(genetic);

        // Return the best individual
        IntegerArrayGenome best = (IntegerArrayGenome)genetic.getBestGenome();
        pop = genetic.getPopulation();

        dump("after", pop);

        return best;
    }

    /**
     * Runs the learning algorithm.
     * @param genetic
     */
    protected void learn(TrainEA genetic) {
        int sameCount = 0;

        int iteration = 0;

        double yLast = Double.MAX_VALUE;

        // Loop until the best answer doesn't change for a while
        while(sameCount < MAX_SAME_COUNT) {
            dump("iteration = "+iteration, genetic.getPopulation());

            genetic.iteration();

            // Get the value of the best solution for f(x)
            double y = genetic.getError();

            IntegerArrayGenome best = (IntegerArrayGenome) genetic.getBestGenome();

            System.out.printf("%d y=%4.2f >> %s\n",iteration, y, asString(best));

            iteration++;

            // Check if the solution has changed
            if(Math.abs(yLast - y) < TOLERANCE) {
                sameCount++;
            }
            else
                sameCount = 0;

            yLast = y;
        }
    }

    /**
     * Initializes a population.
     * @return Population
     */
    protected Population initPop() {
        Population pop = new BasicPopulation(100, null);

        BasicSpecies species = new BasicSpecies();

        species.setPopulation(pop);

        for(int k=0; k < POPULATION_SIZE; k++) {
            final IntegerArrayGenome genome = randomGenome(7);

            species.getMembers().add(genome);
        }

        pop.setGenomeFactory(new IntegerArrayGenomeFactory(CHROMOSOME_SIZE));
        pop.getSpecies().add(species);

        return pop;
    }

    /**
     * Gets a random individual
     * @param sz Number of genes
     * @return
     */
    public IntegerArrayGenome randomGenome(int sz) {
        IntegerArrayGenome genome = new IntegerArrayGenome(CHROMOSOME_SIZE);

        final int[] organism = genome.getData();

        for(int k=0; k < organism.length; k++) {
            organism[k] = ran.nextInt(2);
        }

        return genome;
    }

    /**
     * Dumps the population.
     * @param title Title
     * @param pop Population
     */
    protected void dump(final String title, final Population pop) {
        final Species species = pop.getSpecies().get(0);

        System.out.println("----- "+title);

        int n = 1;

        for (Genome genome : species.getMembers()) {
            IntegerArrayGenome individual = (IntegerArrayGenome) genome;

            System.out.printf("%3d | %s\n",n, asString(individual));

            n++;
        }
    }
}

