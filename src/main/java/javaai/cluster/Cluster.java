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
package javaai.cluster;

import java.util.ArrayList;
import java.util.List;

/**
 * Container of cluster info by type.
 * @param <T> Type
 * @author Ron.Coleman
 */
public class Cluster<T> {
    public List<T> buffer = new ArrayList<>();
    public T centroid;

    /**
     * Constructor
     * @param buffer Initial buffer.
     * @param centroid Centroid for cluster
     */
    public Cluster(List<T> buffer,T centroid) {
        this.buffer = buffer;
        this.centroid = centroid;
    }

    /**
     * Updates the centroid.
     * @param centroid Centroid
     */
    public void update(T centroid) {
        this.centroid = centroid;
    }

    /**
     * Adds a datum to the cluster.
     * @param datum Datum
     */
    public void add(T datum) {
        buffer.add(datum);
    }

    /**
     * Gets the size of the cluster.
     * @return Cluster size
     */
    public int size() {
        return buffer.size();
    }

    /**
     * Clears the cluster of all its members.
     */
    public void clear() {
        buffer.clear();
    }

    /**
     * Converts cluster to a string.
     * @return
     */
    @Override
    public String toString() {
        int min = Math.min(5,buffer.size());
        String str = "centroid: "+centroid+" sz: "+buffer.size() + " [";
        for(int k=0; k < min; k++)
            str += buffer.get(k) + " ";

        str += "...]";
        return str;
    }
}
