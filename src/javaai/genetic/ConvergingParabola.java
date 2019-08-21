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

import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.ml.genetic.genome.IntegerArrayGenome;
import java.util.HashMap;
import static javaai.util.Helper.asString;

public class ConvergingParabola extends Parabola {
    @Override
    protected void learn(TrainEA genetic) {
        int iteration = 0;

        double diversity = 1.0;

        while(diversity > 0.30) {
            dump("iteration = "+iteration, genetic.getPopulation());

            genetic.iteration();

            // Get the value of the best solution for f(x)
            double y = genetic.getError();

            IntegerArrayGenome best = (IntegerArrayGenome) genetic.getBestGenome();

            System.out.printf("%d y=%4.2f >> %s\n",iteration, diversity, asString(best));

            iteration++;

            diversity = getDiversity(genetic.getPopulation());
        }
    }

    protected double getDiversity(Population pop) {
        final Species species = pop.getSpecies().get(0);

        HashMap<String, Integer> counter = new HashMap<>();

        for (Genome genome : species.getMembers()) {
            IntegerArrayGenome individual = (IntegerArrayGenome) genome;

            String key = asString(individual);

            Integer count = counter.getOrDefault(key,0);

            counter.put(key, count+1);
        }

        int max = -Integer.MAX_VALUE;
        for(String key: counter.keySet()) {
            Integer count = counter.get(key);

            if(count > max)
                count = max;
        }

        double diversity = 1 - (double) max / pop.size();

        return diversity;
    }
}
