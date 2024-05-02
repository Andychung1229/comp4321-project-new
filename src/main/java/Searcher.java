import jdbm.helper.FastIterator;

import java.io.IOException;
import java.util.*;

public class Searcher extends Spider {

    public Vector<Page> search(Vector<String> keywords) {
        try {

            Vector<Page> result = new Vector<>();
            Vector<String> phrase_word = new Vector<>();
            Vector<String> wordlist = new Vector<>();
            Vector<String> uni_wordlist = new Vector<>();
            Vector<String> wordDocPos = new Vector<>();
            Vector<String> phrasewordDocPos = new Vector<>();
            int phrase_count = 0;
            for (int i = 0; i < keywords.size(); i++) {
                String word = keywords.get(i);
                if (word.contains("\"")) {
                    phrase_count++;
                }
                if (phrase_count > 0) {
                    String stemword = stopStem.stem(word);
                    if (!stemword.isEmpty() && !stopStem.isStopWord(word))
                        phrase_word.add(stemword);
                    if (phrase_count == 2)
                        phrase_count = -1;
                }
                if (!word.isEmpty() && !stopStem.isStopWord(word)) {
                    String stemword = stopStem.stem(word);
                    if (wordToDocPos.getValue(stemword) != null) {
                        wordlist.add(stemword);
                        wordDocPos.add(wordToDocPos.getValue(stemword));
                        System.out.println(word);
                        System.out.println(wordToDocPos.getValue(stemword));

                    }
                }
            }
            System.out.println("phrase_word:" + phrase_word);
            if (wordDocPos.isEmpty() && phrase_word.isEmpty()) {
                return null;
            }

            for (String word : wordlist) {
                if (!uni_wordlist.contains(word))
                    uni_wordlist.add(word);
            }
            System.out.println(uni_wordlist);
            int numKey = visitedPage.getNumKey();
            int numKey_2 = idToWord.getNumKey();
            int numKey_3 = wordToDocPos.getNumKey();
            System.out.println(numKey);

            //indexToWordWithFreq.printAll();
            //
            Map<String, Double>[] tfxidfMap = new HashMap[numKey];
            Vector<Double> dfMap = new Vector<>();
            HashMap<String, Double> keywordMap = new HashMap();

            Vector<Integer> phrase_doc = null;
            if (!phrase_word.isEmpty()) {


                String DocPos = wordToDocPos.getValue(phrase_word.get(0));
                if(DocPos==null)return null;

                String[] DocPoslist = DocPos.split(" ");

                for (int i = 1; i < phrase_word.size(); i++) {
                    for (int j= 0; j < DocPoslist.length; j += 2) {
                        DocPoslist[j + 1]=String.valueOf(Integer.parseInt(DocPoslist[j + 1]) + 1);
                    }
                    String tempDocPos = wordToDocPos.getValue(phrase_word.get(i));
                    if (tempDocPos ==null) return null;
                    for (int j = 0; j < DocPoslist.length; j += 2) {
                        String tempword = DocPoslist[j] +" "+ DocPoslist[j + 1];
                        if (!tempDocPos.contains(tempword)) {
                            DocPoslist[j + 1] = "-1";
                        }
                    }
                }
                phrase_doc = new Vector<>();
                for (int j = 0; j < DocPoslist.length; j += 2) {
                    if (!Objects.equals(DocPoslist[j + 1], "-1"))
                        phrase_doc.add(Integer.parseInt(DocPoslist[j].substring(3)));
                }


            }

            System.out.println("phrase" + phrase_doc);

            //calculate idf
            for (String word : uni_wordlist) {
                //System.out.println("word "+i+"  :");
                calculateDF(word, dfMap);
            }
            //calculate tfxidf
            for (int i = 0; i < numKey; i++) {
                tfxidfMap[i] = calculateTFxIDF(i, dfMap, uni_wordlist);
            }
            for (String word : wordlist) {
                double temp = (keywordMap.get(word) == null) ? 0 : keywordMap.get(word);
                keywordMap.put(word, temp + 1.0 / uni_wordlist.size());// normalize need /uni_wordlist.size()
            }

            for (int i = 0; i < uni_wordlist.size(); i++) {
                double temp = (keywordMap.get(uni_wordlist.get(i)) == null) ? 0 : keywordMap.get(uni_wordlist.get(i));
                double df = dfMap.get(i);
                double idf = Math.log(numKey / (df)) / Math.log(2);
                keywordMap.put(uni_wordlist.get(i), temp * idf);
            }

            //Cosine Similarity Measures
            double sumD = 0;
            double sumQ = 0;
            double sumDQ = 0;
            int titleMatch = 0;

            //for(int i=0;i<wordlist.size())
            for (double value : keywordMap.values()) {
                sumQ += value * value;
            }
            sumQ = Math.sqrt(sumQ);
            for (int doc_index = 0; doc_index < numKey; doc_index++) {

                if (tfxidfMap[doc_index].isEmpty()||phrase_doc!=null&&!phrase_doc.contains(doc_index)&&phrase_count==-1) continue;
                sumD = 0;
                sumDQ = 0;

                for (String key : tfxidfMap[doc_index].keySet()) {

                    double value = tfxidfMap[doc_index].get(key);
                    sumD += value * value;
                    sumDQ += value * keywordMap.get(key);
                }
                String title = indexToTitle.getValue(doc_index);
                String[] titlelist = title.split(" ");
                titleMatch = 0;
                for (String titleWord : titlelist) {
                    if (!stopStem.isStopWord(titleWord) && uni_wordlist.contains(stopStem.stem(titleWord)))
                        titleMatch++;
                }


                sumD = Math.sqrt(sumD);
                double cosineSimilarity = (sumDQ / sumD) / sumQ + titleMatch;


//                System.out.println("SumQ of DOC"+doc_index+":"+sumQ);
//                System.out.println("SumD of DOC"+doc_index+":"+sumD);
//                System.out.println("SumDQ of DOC"+doc_index+":"+sumDQ);

                Page currentPage = new Page();
                //create
                currentPage.setScore(cosineSimilarity);
                currentPage.setUrl(indexToPageURL.getValue(doc_index));
                currentPage.setPageSize(Integer.parseInt(indexToPageSize.getValue(doc_index)));
                currentPage.setPageTitle(indexToTitle.getValue(doc_index));
                currentPage.setLastUpdateTime(indexToLastModifiedDate.getValue(doc_index));
                //child link and parent link are added below
                if (indexToChildLink.checkEntry(doc_index)) {
                    String[] childLinks = indexToChildLink.getValue(doc_index).split(" ");
                    for (String childLink : childLinks) {
                        currentPage.addChildrenLink(childLink);
                    }
                }
                //please help check link 179-184, and the output, the first number after amount is amount of parentLink
                if (linkToParentLink.checkEntry(indexToPageURL.getValue(doc_index))) {        //seems have problem, request for checking
                    String[] parentLinks = linkToParentLink.getValue(indexToPageURL.getValue(doc_index)).split(" ");
                    for (String parentLink : parentLinks) {
                        currentPage.addParentLink(parentLink);
                    }
                } else if (linkToParentLink.checkEntry(indexToPageURL.getValue(doc_index) + "/")) {
                    String[] parentLinks = linkToParentLink.getValue(indexToPageURL.getValue(doc_index) + "/").split(" ");
                    for (String parentLink : parentLinks) {
                        currentPage.addParentLink(parentLink);
                    }
                }
                String[] wordListWithFrequency = indexToWordWithFreq.getValue(doc_index).split(" ");
                Vector<KeyWord> topFiveWord = new Vector<KeyWord>();
                for (int i = 0; i < wordListWithFrequency.length; i += 2) {
                    topFiveWord.addElement(new KeyWord(wordListWithFrequency[i], Integer.parseInt(wordListWithFrequency[i + 1])));
                }
                Collections.sort(topFiveWord);
                for (int i = 0; i < 5 && i < topFiveWord.size(); i++) {
                    currentPage.addTopFiveWord(topFiveWord.get(i));
                }
                result.add(currentPage);
            }


            for (int i = 0; i < numKey; i++) {
                if (!tfxidfMap[i].isEmpty())
                    System.out.println("Doc" + i + " :" + tfxidfMap[i]);
            }
            System.out.println("Qry:" + keywordMap);
            System.out.println(wordlist);
            System.out.println(uni_wordlist);
            for (int i = 0; i < uni_wordlist.size(); i++)
                System.out.println("df: " + uni_wordlist.get(i) + "" + dfMap.get(i));


            //System.out.println(tfMap[0].get("page"));
            //System.out.println(dfMap.get(Integer.parseInt(wordToid.getValue("paper"))));
            // System.out.println(wordToid.getValue("vigilant"));
            //System.out.println(idToWord.getValue(2517));
            Collections.sort(result); //sort from largest to smallest
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

            for (int i = 0; i < keywordsList.length; i += 2) {
                if(!wordlist.contains(keywordsList[i]))
                    continue;
                double tf = Integer.parseInt(keywordsList[i + 1])/sum ;// normalize need /sum
                int numKey= visitedPage.getNumKey();
                int index=wordlist.indexOf(keywordsList[i]);
                double df= dfMap.get(index);
                double idf = Math.log(numKey/(df))/Math.log(2);

                //testing use
//                System.out.println("Doc "+key+"  :");
//                System.out.println("TF:  "+keywordsList[i]+" : "+keywordsList[i + 1]);
//                System.out.println("TF(Norm):  "+keywordsList[i]+" : "+tf);
//                System.out.println("df:  "+keywordsList[i]+" : "+Math.round(numKey/Math.pow(2,idf)));
//                System.out.println("idf:  "+keywordsList[i]+" : "+idf);
//                System.out.println("TFxidf:  "+keywordsList[i]+" : "+tf*idf);
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

    public static void main(String[] args) throws IOException {
        String[] words;
        Spider.buildDataBase();
        Spider.crawl();
        //Spider.output();
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a string: ");
            String inputString = scanner.nextLine();
            // Split the string into an array of words
            words = inputString.split(" ");
            // Convert the array of words to a vector
            Vector<String> s = new Vector<>(Arrays.asList(words));

            Searcher se = new Searcher();
            Vector<Page> result = se.search(s);
            System.out.println(result);
        } while (!words.equals(""));
        Spider.recman.commit();
        Spider.recman.close();

    }
}
