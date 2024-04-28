import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.*;

public class Searcher extends Spider {

    public Vector<Page> search(Vector<String> keywords) {
        try {

            Vector<Page> result = new Vector<>();
            Vector<Integer> moreThanOneWord = new Vector<>();
            Vector<String> wordlist = new Vector<>();
            Vector<String> uni_wordlist = new Vector<>();
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
                        System.out.println(word);
                        System.out.println(wordToDocPos.getValue(stemword));

                    }
                }
            }
            if (wordDocPos.isEmpty()&& moreThanOneWord.isEmpty()) {
                return null;
            }

            for(String word:wordlist){
                if(!uni_wordlist.contains(word))
                    uni_wordlist.add(word);
            }
            System.out.println(uni_wordlist);
            int numKey= visitedPage.getNumKey();
            int numKey_2= idToWord.getNumKey();
            int numKey_3= wordToDocPos.getNumKey();
            System.out.println(numKey);

            //indexToWordWithFreq.printAll();
            //
            Map<String, Double>[] tfxidfMap = new HashMap[numKey];
            Vector<Double> dfMap = new Vector<>();
            HashMap<String, Double> keywordMap = new HashMap();


            //calculate idf
            for(String word: uni_wordlist){
                //System.out.println("word "+i+"  :");
                calculateDF(word,dfMap);
            }
            //calculate tfxidf
            for(int i=0;i<numKey;i++){
                tfxidfMap[i]=calculateTFxIDF(i,dfMap,uni_wordlist);
            }
            for(String word: wordlist){
                double temp= (keywordMap.get(word)==null)?0:  keywordMap.get(word);
                keywordMap.put(word,temp+1.0/uni_wordlist.size());
            }

            for(int i=0;i<uni_wordlist.size();i++){
                double temp= (keywordMap.get(uni_wordlist.get(i))==null)?0:  keywordMap.get(uni_wordlist.get(i));
                double df= dfMap.get(i);
                double idf = Math.log(numKey/(df))/Math.log(2);
                keywordMap.put(uni_wordlist.get(i),temp*idf);
            }

            //Cosine Similarity Measures
            double sumD=0;
            double sumQ=0;
            double sumDQ=0;

            //for(int i=0;i<wordlist.size())
            for(int i=0;i<keywordMap.size();i++){
                sumQ+=keywordMap.get(uni_wordlist.get(i));

            }

            for(int i=0;i<numKey;i++){

                if(tfxidfMap[i].isEmpty())continue;
                sumD= 0;
                sumDQ=0;

                for (String key : tfxidfMap[i].keySet()) {

                    double value=tfxidfMap[i].get(key);
                    sumD += value;
                    sumDQ+=value*keywordMap.get(key);
                }


                System.out.println("SumQ of DOC"+i+":"+sumQ);
                System.out.println("SumD of DOC"+i+":"+sumD);
                System.out.println("SumDQ of DOC"+i+":"+sumDQ);
            }



            for(int i=0;i<numKey;i++){
                if(!tfxidfMap[i].isEmpty())
                    System.out.println("Doc"+i+" :"+tfxidfMap[i]);
            }
            System.out.println("Qry:"+keywordMap);
            System.out.println(wordlist);
            System.out.println(uni_wordlist);
            for(int i=0;i<uni_wordlist.size();i++)
                System.out.println("df: "+ uni_wordlist.get(i) +""+ dfMap.get(i));


            //System.out.println(tfMap[0].get("page"));
            //System.out.println(dfMap.get(Integer.parseInt(wordToid.getValue("paper"))));
           // System.out.println(wordToid.getValue("vigilant"));
            //System.out.println(idToWord.getValue(2517));
            return result;
            //


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public HashMap<String,Double> calculateTFxIDF(int key,Vector<Double> dfMap,Vector <String>wordlist) {
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
            int temp_index=0;
            for (int i = 0; i < keywordsList.length; i += 2) {
                if(!wordlist.contains(keywordsList[i]))
                    continue;
                double tf = Integer.parseInt(keywordsList[i + 1]) / sum;
                temp_index++;
                int numKey= visitedPage.getNumKey();
                double df= dfMap.get(temp_index);
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
    public void calculateDF(String word,Vector<Double> idfMap) throws IOException {

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
        s.add("test");
        s.add("the");
        s.add("page");
        s.add("page");
        s.add("page");
        s.add("page");
        s.add("test");
        s.add("test");
        s.add("ug");
        s.add("buuuuuuuug");
        s.add("fighter");










        Searcher.buildDataBase();
        Searcher.crawl();
        Searcher se = new Searcher();
        Vector<Page> result=se.search(s);
        System.out.println(result);

    }
}
