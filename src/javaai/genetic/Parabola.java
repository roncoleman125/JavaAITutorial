package javaai.genetic;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.population.BasicPopulation;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.BasicSpecies;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.genetic.crossover.SpliceNoRepeat;
import org.encog.ml.genetic.genome.IntegerArrayGenome;
import org.encog.ml.genetic.genome.IntegerArrayGenomeFactory;
import org.encog.ml.genetic.mutate.MutateShuffle;

import java.util.Random;

public class Parabola {
    public final static int POPULATION_SIZE = 100;
    public final static int GENOME_SIZE = 7;
    public final static double MAX_SAME_FRACTION = 0.05;
    public final static int MAX_SAME_SOLUTION = (int)(MAX_SAME_FRACTION * POPULATION_SIZE);

    protected Random ran = new Random(0);

    public static void main(String[] args) {

        Parabola parabola = new Parabola();

        parabola.solve();

        System.out.println(parabola);
    }

    public void solve() {
        Population pop = initPopulation();

        CalculateScore score = new Score();

        TrainEA genetic = new TrainEA(pop, score);

//        genetic.addOperation(0.9, new SpliceNoRepeat(GENOME_SIZE/2));
        genetic.addOperation(0.1, new MutateShuffle());

        int sameSolutionCount = 0;
        int iteration = 1;
        double lastSolution = Double.MAX_VALUE;

        // Loop until the best answer doesn't change
        while(sameSolutionCount < MAX_SAME_SOLUTION) {
            genetic.iteration();

            // Get the value of the best solution for f(x)
            double thisSolution = genetic.getError();

            System.out.printf("|%d %f %f %d\n",iteration, thisSolution, lastSolution, sameSolutionCount);

            iteration++;

            // Check if the solution has changed
            if(Math.abs(lastSolution - thisSolution) < 1.0) {
                sameSolutionCount++;
            }
            else
                sameSolutionCount = 0;

            lastSolution = thisSolution;
        }

        IntegerArrayGenome best = (IntegerArrayGenome)genetic.getBestGenome();
        pop = genetic.getPopulation();
        Species species = pop.getSpecies().get(0);

        for (Genome genome : species.getMembers()) {
            IntegerArrayGenome gen = (IntegerArrayGenome) genome;
            System.out.println(asString(gen));
        }

    }

    public Population initPopulation() {
        Population pop = new BasicPopulation(100, null);

        BasicSpecies species = new BasicSpecies();

        species.setPopulation(pop);

        for(int k=0; k < POPULATION_SIZE; k++) {
            final IntegerArrayGenome genome = randomGenome(7);

            species.getMembers().add(genome);
        }

        pop.setGenomeFactory(new IntegerArrayGenomeFactory(GENOME_SIZE));
        pop.getSpecies().add(species);

        return pop;
    }

    public IntegerArrayGenome randomGenome(int sz) {
        IntegerArrayGenome genome = new IntegerArrayGenome(GENOME_SIZE);

        final int[] organism = genome.getData();

        for(int k=0; k < organism.length; k++) {
            organism[k] = ran.nextInt(2);
        }

        return genome;
    }


    public String asString(IntegerArrayGenome genome) {
        String s = "";
        int[] sol = genome.getData();
        for(int bit: sol)
            s += bit + " ";

        s += "| ";
        int x = asInt(genome);

        s += x;

        return s;
    }


    public static int asInt(IntegerArrayGenome genome) {
        int[] bits = genome.getData();
        int num = 0;
        for(int k=0; k < bits.length; k++) {
            num = num << 1;
            num = num | bits[k];
        }

        return num;
    }
}

class Score implements CalculateScore {

    @Override
    public double calculateScore(MLMethod phenotype) {
        IntegerArrayGenome genome = (IntegerArrayGenome) phenotype;

        int x = Parabola.asInt(genome);

        double y = f(x);

        return y;
    }

    @Override
    public boolean shouldMinimize() {
        return true;
    }

    @Override
    public boolean requireSingleThreaded() {
        return true;
    }

    protected double f(int x) {
        return (x - 3)*(x - 3);
    }
}
