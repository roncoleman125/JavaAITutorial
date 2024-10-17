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
package javaai.metah.ga;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.population.BasicPopulation;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.BasicSpecies;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.genetic.crossover.Splice;
import org.encog.ml.genetic.genome.DoubleArrayGenome;
import org.encog.ml.genetic.genome.DoubleArrayGenomeFactory;
import java.util.Random;
import static javaai.util.Helper.asString;

/**
 * This class learns the weights using GA as the optimize algorithm for the XOR problem.
 */
public class XorGa {
    /** Stopping criteria as difference between best solution and last best one */
    public final double TOLERANCE = 0.01;

    /** Convergence criteria: number of time best solution stays best */
    public final static int MAX_SAME_COUNT = 100;

    /** Population has this many individuals. */
    public final static int POPULATION_SIZE = 10000;

    /** Chromosome size (ie, number of genes): domain is [0, (2^n-1)]. */
    public final static int CHROMOSOME_SIZE = 9;

    /** Mutation rate */
    public final static double MUTATION_RATE = 0.01;

    /** Used to initialize an individual in population */
    protected Random ran = new Random(0);

    protected int sameCount = 0;
    protected double yLast;

    public static void main(String[] args) {
        XorGa ga = new XorGa();

        DoubleArrayGenome best = ga.solve();

        System.out.println("best = "+asString(best)+" fitness = "+best.getScore());
    }

    public DoubleArrayGenome solve() {
        // Initialize a population
        Population pop = initPop();

        // Get the fitness measure
        XorGaObjective objective = new XorGaObjective();

        // Create the evolutionary training algorithm
        TrainEA ga = new TrainEA(pop, objective);

        // Set the mutation rate
        ga.addOperation(MUTATION_RATE, new MutateDoubleArrayGenome(0.001));
        ga.addOperation(0.9, new Splice(CHROMOSOME_SIZE/2));

        // Do the learning algorithm
        train(ga);

        // Return the best individual
        DoubleArrayGenome best = (DoubleArrayGenome)ga.getBestGenome();

        // Output the best results

        System.out.printf("%3s  %3s  %3s  %s\n","x1","x2","t1","y1");
        for(int k = 0; k < XorGaObjective.XOR_INPUTS.length; k++) {
            double x1 = XorGaObjective.XOR_INPUTS[k][0];
            double x2 = XorGaObjective.XOR_INPUTS[k][1];

            double actual = objective.feedforward(x1, x2, best.getData());

            double ideal = XorGaObjective.XOR_IDEALS[k][0];

//            System.out.println(x1+" "+x2+" ideal = "+ideal+" actual = "+actual);
            System.out.printf("%3.1f  %3.1f  %3.1f  %f\n", x1, x2, ideal, actual);
        }

        return best;
    }

    /**
     * Runs the learning algorithm.
     * @param ga
     */
    protected void train(TrainEA ga) {
        int iteration = 0;

        boolean converged = false;

        // Loop until the best answer doesn't change for a while
        System.out.printf("%3s  %7s  %4s  %s\n","#", "loss", "same","best");
        while(!converged) {
            //output("iteration = "+iteration, genetic.getPopulation());

            // Run natural selection iteration.
            ga.iteration();

            // Get the value of the best solution for f(x)
            double loss = ga.getError();

            DoubleArrayGenome best = (DoubleArrayGenome) ga.getBestGenome();

//            System.out.printf("%d y=%4.2f same=%d >> %s\n",iteration, y, sameCount, asString(best));
            System.out.printf("%3d  %7.4f  %4d  %s\n",iteration, loss, sameCount,asString(best));

            // System.out.println("epoch = "+iteration);
            iteration++;

            converged = didConverge(loss,  ga.getPopulation());
        }
    }

    /**
     * Tests whether GA has converged.
     * @param y Y value in y=f(x)
     * @param pop Population of individuals
     * @return True if the GA has converge, otherwise false
     */
    public boolean didConverge(double y, Population pop) {
        if(sameCount >= MAX_SAME_COUNT)
            return true;

        if(Math.abs(yLast - y) < TOLERANCE) {
            sameCount++;
        }
        else
            sameCount = 0;

        yLast = y;

        return false;
    }

    /**
     * Initializes a population.
     * @return Population
     */
    protected Population initPop() {
        Population pop = new BasicPopulation(POPULATION_SIZE, null);

        BasicSpecies species = new BasicSpecies();

        species.setPopulation(pop);

        for(int k=0; k < POPULATION_SIZE; k++) {
            final DoubleArrayGenome genome = randomGenome(CHROMOSOME_SIZE);

            species.getMembers().add(genome);
        }

        pop.setGenomeFactory(new DoubleArrayGenomeFactory(CHROMOSOME_SIZE));
        pop.getSpecies().add(species);

        return pop;
    }

    /**
     * Gets a random individual
     * @param sz Number of genes
     * @return
     */
    public DoubleArrayGenome randomGenome(int sz) {
        DoubleArrayGenome genome = new DoubleArrayGenome(sz);

        final double[] organism = genome.getData();

        for(int k=0; k < organism.length; k++) {
            organism[k] = XorGaObjective.getRandomWeight();
        }

        return genome;
    }

    /**
     * Outputs the population.
     * @param title Title
     * @param pop Population
     */
    protected void output(final String title, final Population pop) {
        //final Species species = pop.getSpecies().get(0);

        System.out.println("----- "+title);

        int n = 1;

        for(Species species: pop.getSpecies()) {
            for (Genome genome : species.getMembers()) {

                DoubleArrayGenome individual = (DoubleArrayGenome) genome;

                System.out.printf("%3d | %s y = %f\n", n, asString(individual), individual.getScore());

                n++;
            }
        }
    }
}
