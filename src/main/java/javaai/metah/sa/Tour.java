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

        distance = 0;

        for (int i = 0; i < getNumCities(); i++) {
            City start = getCity(i);
            City end = getCity(i + 1 < getNumCities() ? i + 1 : 0);
            distance += City.distance(start, end);
        }

        return distance;
    }

    public Tour duplicate() {
        return new Tour(new ArrayList<>(cities));
    }

    public int getNumCities() {
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
}
