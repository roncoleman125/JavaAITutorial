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
package javaai.ann.rnn;

/**
 * This class implements a simple word encoder/decoder to a 2D vector.
 * @author Ron.Coleman 
 */
public class WordEncoder {
    public static void main(String[] args) {
        WordEncoder we = new WordEncoder();
        
        Double[] encoding = we.encode("hello");
        
        for(Double subcoding: encoding) {
            int i = subcoding.intValue();
            System.out.print(i+" ");
        }
    }

    /**
     * Encodes a string to 2D vector.
     * @param s String
     * @return Encoding
     */
    public Double[] encode(String s) {
        int[] hashes = {s.hashCode(), reverse(s).hashCode()};
        Double[] subcodings = {new Double(hashes[0]), new Double(hashes[1])};
        
        for(int i=0; i < hashes.length; i++) 
            assert(subcodings[i].intValue() == hashes[i]);

        return subcodings;
    }

    /**
     * Decodes an encoding to a string.
     * @param encoding Encoding
     * @return Corresponding string
     */
    public String decode(Double[] encoding) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Reverses a string.
     * @param s String
     * @return String reversal
     */
    protected String reverse(String s) {
        String t = new StringBuffer(s).reverse().toString();
        return t;
    }
}
