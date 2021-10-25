package javaai.metah.sa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class City {

    private int x;
    private int y;

    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters and toString()
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof City) || obj == null)
            return false;
        City city = (City)obj;
        return city.x == this.x && city.y == this.y;
    }

    public static List<City> get(int n) {
        List<City> cities = new ArrayList<>();
        for(int i=0; i < n; i++) {
            City city = getCity(cities);
            cities.add(city);
        }
        return cities;
    }

    private final static int RANGE = 20;
    private static Random ran = new Random(0);
    private static City getCity(List<City> cities) {
        int x = ran.nextInt(RANGE)+1;
        int y = ran.nextInt(RANGE)+1;
        City city = new City(x,y);
        if(cities.contains(city))
            return getCity(cities);
        return city;
    }
}
