package javaai.ann.metah;

public class Hill {
    /** Want to try to find this point in the plane. */
    public final static double[] XY_GOAL = {5, 3};

    double[] curPt = {0, 0};
    double[] stepSz = {1, 1};

    double acceleration = 1.2;
    double[] candidates = {-acceleration, -1/acceleration, 0, 1/acceleration, acceleration};

    public static void main(String[] args) {
        Hill hill = new Hill();

        double[] pt = hill.climb();
    }

    public double[] climb() {
        do {
            double before = eval(curPt);
            for(int i=0; i < curPt.length; i++) {
                int best = -1;
                double bestScore = Double.MAX_VALUE;
                for(int j=0; j < candidates.length; j++) {
                    double old = curPt[i];

                    curPt[i] += stepSz[i]* candidates[j];

                    double move = eval(curPt);
//                    curPt[i] -= stepSz[i]* candidates[j];

                    curPt[i] = old;

                    if(move < bestScore) {
                        bestScore = move;
                        best = j;
                    }
                }
                if(candidates[best] == 0)
                    stepSz[i] /= acceleration;
                else {
                    curPt[i] += stepSz[i]* candidates[best];

                    stepSz[i] *= candidates[best];
                }
            }
            double curFitness = eval(curPt);
            double improved = Math.abs(curFitness - before);
            if(improved < 0.01)
                return curPt;
        } while(true);
    }

    public double eval(double[] pt) {
        double sse = 0;
        for(int k=0; k < pt.length; k++) {
            double delta = pt[k] - XY_GOAL[k];
            sse += (delta * delta);
        }

        double rmse = Math.sqrt(sse / pt.length);

        return rmse;
    }
}
