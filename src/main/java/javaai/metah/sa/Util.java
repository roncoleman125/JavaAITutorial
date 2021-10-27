package javaai.metah.sa;

public class Util {
    /**
     * Gets the probability of a jumping into a higher state.
     * @param state1 State 1
     * @param state2 State 2
     * @param temp At this temperature
     * @return
     */
    public static double probability(double state1, double state2, double temp) {
        if (state2 < state1)
            return 1;
        return Math.exp(-(state2 - state1) / temp);
    }
}
