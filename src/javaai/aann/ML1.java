package javaai.aann;

import java.util.*;

public class ML1 extends BaseML {
    public static void main(String[] args) {
        Map<Measure, Species> map = new HashMap<>();
        load(map);

        List<Measure> tests = new ArrayList<>(Arrays.asList(new Measure(5.1,3.5,1.4,0.2)));

        for(Measure measure: tests) {
            System.out.println(map.get(measure) + " " + measure);
        }

        Measure m1 = new Measure(5.9,3.0,5.1,1.8);
        Measure m2 = new Measure(5.9,3.0,5.1,1.8);
        System.out.println(m1.equals(m2));
    }

    public static void load(Map<Measure, Species> target) {
        BaseML.load();

        for(int k = 0; k < measures.size(); k++)
            target.put(measures.get(k),flowers.get(k));
    }
}
