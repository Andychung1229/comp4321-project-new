import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.*;

public class Searcher extends Spider {

    public Vector<Page> search(Vector<String> keywords) {
        try {

            Vector<Page> result = new Vector<>();
            Vector<Integer> moreThanOneWord = new Vector<>();
            Vector<String> wordlist = new Vector<>();
            Vector<String> wordDocPos = new Vector<>();

            for(int i = 0; i < keywords.size(); i++){
                String word = keywords.get(i);
                if(word.contains(" ")){
                    moreThanOneWord.add(i);
                    continue;
                }
                if (!stopStem.isStopWord(word)) {
                    String stemword = stopStem.stem(word);
                    if (wordToDocPos.getValue(word) != null) {
                        wordlist.add(word);
                        wordDocPos.add(wordToDocPos.getValue(stemword));
                        System.out.println(wordToDocPos.getValue(stemword));

                    }
                }
            }
            if (wordDocPos.isEmpty()&& moreThanOneWord.isEmpty()) {
                return null;
            }
            int numKey= visitedPage.getNumKey();
            int numKey_2= idToWord.getNumKey();
            System.out.println(numKey);

            //indexToWordWithFreq.printAll();
            //
            Map<String, Double>[] tfMap = new HashMap[numKey];
            Vector<Double> idfMap = new Vector<>();



            //calculate idf
            for(int i=0;i<numKey_2;i++){
                //System.out.println("word "+i+"  :");
                calculateIDF((int)i,numKey,idfMap);
            }
            //calculate tfxidf
            for(int i=0;i<numKey;i++){
                System.out.println("Doc "+i+"  :");
                tfMap[i]=calculateTFxIDF((int)i,idfMap);
            }






            System.out.println(tfMap[0].get("page"));
            return result;
            //


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public HashMap<String,Double> calculateTFxIDF(int key,Vector<Double> idfMap) {
        HashMap<String,Double> termFreqMap = new HashMap<>();
        try {
            String keywords = indexToWordWithFreq.getValue(key);
            if (keywords == null) return null;
            String[] keywordsList = keywords.split(" ");
            double sum = 0.0;
            //Get the sum of all elements in hashmap
            for (int i = 0; i < keywordsList.length; i += 2) {
                sum += Integer.parseInt(keywordsList[i + 1]);
            }        //create a new hashMap with Tf values in it.
            for (int i = 0; i < keywordsList.length; i += 2) {
                double tf = Integer.parseInt(keywordsList[i + 1]) / sum;
                System.out.println("TF:  "+keywordsList[i]+" : "+tf);
                int index =Integer.parseInt(wordToid.getValue(keywordsList[i]));
                double idf= idfMap.get(index);
                int numKey= visitedPage.getNumKey();
                System.out.println("df:  "+keywordsList[i]+" : "+Math.round(numKey/Math.exp(idf)-1));
                System.out.println("idf:  "+keywordsList[i]+" : "+idf);
                System.out.println("TFxidf:  "+keywordsList[i]+" : "+tf*idf);
                termFreqMap.put(keywordsList[i], tf*idf);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return termFreqMap;
    }
    public void calculateIDF(int key,int docSize,Vector<Double> idfMap) throws IOException {
        HashMap<String,Double> InverseDocFreqMap = new HashMap<>();
        String word= idToWord.getValue(key);
        double df =0 ;
        for(int i=0;i<docSize;i++){
            String keywords = indexToWordWithFreq.getValue(i);
            if (keywords == null) continue;
            String[] keywordsList = keywords.split(" ");
            for(int j = 0; j < keywordsList.length; j += 2) {
                if (keywordsList[j].equals(word)) {
                    //System.out.println("DF:  " + i + " : ");
                    df++;
                    break;
                }
            }
        }
        double idf = Math.log(docSize/(df+1));
        //System.out.println("DF:  "+word+" : "+df);
        //System.out.println("IDF:  "+word+" : "+idf);
        idfMap.add(idf);

    }

    public static void main(String[] args) {
        Vector<String> s = new Vector<>();
        s.add("super");
        s.add("the");
        s.add("gay");

        Searcher.buildDataBase();
        Searcher.crawl();
        Searcher se = new Searcher();
        Vector<Page> result=se.search(s);
        System.out.println(result);

    }
}
