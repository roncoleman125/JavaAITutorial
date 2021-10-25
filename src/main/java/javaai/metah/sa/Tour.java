package javaai.metah.sa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tour {
    private List<City> cities;
    private int distance;

    public Tour(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        Collections.shuffle(this.cities);
    }
    public City getCity(int index) {
        return cities.get(index);
    }

    public int getTourLength() {
        if (distance != 0) return distance;

        int totalDistance = 0;

        for (int i = 0; i < noCities(); i++) {
            City start = getCity(i);
            City end = getCity(i + 1 < noCities() ? i + 1 : 0);
            totalDistance += Util.distance(start, end);
        }

        distance = totalDistance;
        return totalDistance;
    }

    public Tour duplicate() {
        return new Tour(new ArrayList<>(cities));
    }

    public int noCities() {
        return cities.size();
    }

    @Override
    public String toString() {
        String s = "";
        for(City city: cities) {
            s += String.format("city(%d, %d) -> ",city.getX(),city.getY());
        }
        s += "@";
        return s;
    }
    // Getters and toString()
}
