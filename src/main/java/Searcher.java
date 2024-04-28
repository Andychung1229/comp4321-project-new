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
            int numKey_3= wordToDocPos.getNumKey();
            System.out.println(numKey);

            //indexToWordWithFreq.printAll();
            //
            Map<String, Double>[] tfMap = new HashMap[numKey];
            Vector<Double> dfMap = new Vector<>();



            //calculate idf
            for(int i=0;i<numKey_2;i++){
                //System.out.println("word "+i+"  :");
                calculateDF(i,dfMap);
            }
            //calculate tfxidf
            for(int i=0;i<numKey;i++){
                tfMap[i]=calculateTFxIDF(i,dfMap);
            }






            System.out.println(tfMap[0].get("page"));
            System.out.println(dfMap.get(Integer.parseInt(wordToid.getValue("paper"))));
            System.out.println(wordToid.getValue("vigilant"));
            System.out.println(idToWord.getValue(2517));
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

                int index =Integer.parseInt(wordToid.getValue(keywordsList[i]));
                int numKey= visitedPage.getNumKey();
                double df= idfMap.get(index);
                double idf = Math.log(numKey/(df))/Math.log(2);
                //testing use
                System.out.println("Doc "+key+"  :");
                System.out.println("TF:  "+keywordsList[i]+" : "+keywordsList[i + 1]);
                System.out.println("TF(Norm):  "+keywordsList[i]+" : "+tf);
                System.out.println("df:  "+keywordsList[i]+" : "+Math.round(numKey/Math.pow(2,idf)));
                System.out.println("idf:  "+keywordsList[i]+" : "+idf);
                System.out.println("TFxidf:  "+keywordsList[i]+" : "+tf*idf);
                //testing use
                termFreqMap.put(keywordsList[i], tf*idf);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return termFreqMap;
    }
    public void calculateDF(int key,Vector<Double> idfMap) throws IOException {

        String word= idToWord.getValue(key);
        double df =1 ;
//        for(int i=0;i<docSize;i++){
//            String keywords = indexToWordWithFreq.getValue(i);
//            if (keywords == null) continue;
//            String[] keywordsList = keywords.split(" ");
//            for(int j = 0; j < keywordsList.length; j += 2) {
//                if (keywordsList[j].equals(word)) {
//                    //System.out.println("DF:  " + i + " : ");
//                    df++;
//                    break;
//                }
//            }
//        }

        String position = wordToDocPos.getValue(word);
        String[] positionList = position.split(" ");

        for(int j=2;j<positionList.length;j+=2){
            if(!positionList[j].equals(positionList[j-2])) {

                df++;
            }
        }

        //double idf = Math.log(docSize/(df))/Math.log(2);
        //System.out.println("DF:  "+word+" : "+df);
        //System.out.println("IDF:  "+word+" : "+idf);
        idfMap.add(df);

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
