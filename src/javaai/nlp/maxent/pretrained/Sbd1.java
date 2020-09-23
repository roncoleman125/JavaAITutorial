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
package javaai.nlp.maxent.pretrained;

import javaai.util.Helper;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;
import java.io.File;
import java.io.FileInputStream;

/**
 * Tests SBD with pretained sentence model.
 * @author Ron.Coleman
 */
public class Sbd1 {
    public static void main(String[] args) {
        String text = "";
        if(args.length == 0)
            text = "We will start with a simple sentence. However, is it possible for " +
                    "a sentence to end with a question mark? Obviously that is possible! " +
                    "Another complication is the use of a number such as 56.32 or " +
                    "ellipses such as ... Ellipses may be found ... with a sentence! " +
                    "Of course, we may also find the use of abbreviations such as " +
                    "Mr. Smith or Dr. Jones.";
        else {
            // For further testing use fallback1.txt
            String path = args[0];
            text = Helper.mkstring(Helper.loadText(path), " ");
        }

        try {
            FileInputStream is = new FileInputStream(new File("en-sent.bin"));
            SentenceModel sentenceModel = new SentenceModel(is);
            SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
            Span[] spans = sentenceDetector.sentPosDetect(text);
            double[] probs = ((SentenceDetectorME) sentenceDetector).getSentenceProbabilities();

            for(int k=0; k < spans.length; k++) {
                String sentence = text.substring(spans[k].getStart(),spans[k].getEnd());
                System.out.printf("%s %6.4f [%s]\n",spans[k]+"",probs[k],sentence);
            }
            for(Span span: spans) {

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
