package javaai.ann.learn.meta.genetic.g2;

import org.encog.ml.CalculateScore;
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

import static javaai.ann.learn.meta.genetic.g2.Objective.XOR_IDEALS;
import static javaai.util.Helper.asString;

public class XorGA {
    /** Stopping criteria as difference between best solution and last best one */
    public final double TOLERANCE = 0.01;

    /** Convergence criteria: number of time best solution stays best */
    public final static int MAX_SAME_COUNT = 100;

    /** Population has this many individuals. */
    public final static int POPULATION_SIZE = 10000;

    /** Chromosome size (ie, number of genes): domain is [0, (2^n-1)]. */
    public final static int CHROMOSOME_SIZE = 8;

    /** Mutation rate */
    public final static double MUTATION_RATE = 0.20;

    /** Used to initialize an individual in population */
    protected Random ran = new Random(0);

    protected int sameCount = 0;
    protected double yLast;

    public static void main(String[] args) {
        XorGA poly = new XorGA();

        DoubleArrayGenome best = poly.solve();

        System.out.println("best = "+asString(best)+" score = "+best.getScore());
    }

    public DoubleArrayGenome solve() {
        // Initialize a population
        Population pop = initPop();
//        dump("before", pop);

        // Get the fitness measure
        CalculateScore fitness = new Objective();

        // Create the evolutionary training algorithm
        TrainEA genetic = new TrainEA(pop, fitness);

        // Set the mutation rate
//        genetic.addOperation(MUTATION_RATE, new MutateShuffle());
//        genetic.addOperation(MUTATION_RATE, new ConstMutation(new EncogProgramContext(),0.01,0.01));
        genetic.addOperation(0.9, new Splice(CHROMOSOME_SIZE/2));

        // Do the learning algorithm
        train(genetic);

        // Return the best individual
        DoubleArrayGenome best = (DoubleArrayGenome)genetic.getBestGenome();

        pop = genetic.getPopulation();

//        dump("after", pop);
        // best = -18.83 -11.80 -10.50 -19.15 -16.55 -19.03 5.02 17.88  score = 1.826802124032332E-8 // -20 to 20
//        double[] ws = {-18.83, -11.80, -10.50, -19.15, -16.55, -19.03, 5.02, 17.88};

        // best = -9.73 11.00 9.67 -13.24 13.86 14.90 -4.19 -5.55  score = 0.0038154692404555584 // -15 to 15
//        double[] ws = {-9.73, 11.00, 9.67, -13.24, 13.86, 14.90, -4.19, -5.55};

        // -10 to 10
//        double[] ws = { 6.45, -8.83, -6.49, 7.33, 9.24, 9.93, -2.79, -3.70};

        // best = -5.77 5.89 5.98 -4.56 9.08 8.97 -3.71 -4.04  score = 0.029202385616846717 // -10 to 10
        double[] ws = {-5.77, 5.89, 5.98, -4.56, 9.08, 8.97, -3.71, -4.04 };

        Objective fga = new Objective();

        for(int k = 0; k < Objective.XOR_INPUTS.length; k++) {
            double x1 = Objective.XOR_INPUTS[k][0];
            double x2 = Objective.XOR_INPUTS[k][1];

            double actual = fga.predict(x1, x2, best.getData());

            double ideal = XOR_IDEALS[k][0];

            System.out.println(x1+" "+x2+" actual = "+actual+" ideal = "+ideal);
        }

        return best;
    }

    /**
     * Runs the learning algorithm.
     * @param genetic
     */
    protected void train(TrainEA genetic) {
        int iteration = 0;

        boolean converged = false;

        // Loop until the best answer doesn't change for a while
//        while(sameCount < MAX_SAME_COUNT) {
        while(!converged) {
//            dump("iteration = "+iteration, genetic.getPopulation());

            genetic.iteration();

            // Get the value of the best solution for f(x)
            double y = genetic.getError();

            DoubleArrayGenome best = (DoubleArrayGenome) genetic.getBestGenome();

            System.out.printf("%d y=%4.2f same=%d >> %s\n",iteration, y, sameCount, asString(best));

//            System.out.println("epoch = "+iteration);
            iteration++;

            converged = didConverge(y,  genetic.getPopulation());
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

        double max = 10;
        double min = -10;

        for(int k=0; k < organism.length; k++) {
            organism[k] = ran.nextDouble()*(max - min) + min;
        }

        return genome;
    }

    /**
     * Dumps the population.
     * @param title Title
     * @param pop Population
     */
    protected void dump(final String title, final Population pop) {
//        final Species species = pop.getSpecies().get(0);

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
