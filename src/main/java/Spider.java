import java.io.*;
import java.util.Objects;
import java.util.Vector;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import org.htmlparser.util.ParserException;
import jdbm.helper.FastIterator;

public class Spider {
    static String RootPage = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    static int maxPages = 300;
    static int phase1_Pages = 300;
    static RecordManager recman;
    static StopStem stopStem = new StopStem("src/main/java/stopwords.txt");
    static index visitedPage;//URL->PageID
    static index indexToPageURL;
    static index indexToTitle;
    static index TitleToIndex;
    static index indexToLastModifiedDate;
    static index indexToPageSize;
    static index indexToWordWithFreq;
    static index indexToChildLink;
    static index linkToParentLink;
    static index idToWord;
    static index wordToid;

    static index wordToDocPos;



    public static void buildDataBase() {
        try {
            recman = RecordManagerFactory.createRecordManager("database");
            visitedPage = new index(recman, "visitedPage");
            indexToPageURL = new index(recman, "indexToPage");
            indexToTitle = new index(recman, "indexToTitle");
            indexToLastModifiedDate = new index(recman, "indexToLastModifiedDate");
            indexToWordWithFreq = new index(recman, "indexToWordWithFreq");
            indexToChildLink = new index(recman, "indexToChildLink");
            indexToPageSize = new index(recman, "indexToPageSize");
            linkToParentLink = new index(recman, "linkToParentLink");
            idToWord = new index(recman, "idToWord");
            wordToid = new index(recman, "wordToid");
            TitleToIndex = new index(recman, "TitleToIndex");
            wordToDocPos= new index(recman, "wordToDocPos");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static boolean  modifyDateCheck(String url,Crawler crawler) throws IOException {
        crawler.extractModifiedDate();
        if(!visitedPage.checkEntry(url))
            return true;

        if(indexToLastModifiedDate.getValue(Integer.parseInt(visitedPage.getValue(url))).equals(crawler.extractModifiedDate())){
            return false;
        }
        return true;
    }
    public static void crawl() {
        try {
            Vector<String> pageQueue = new Vector<String>();// create a to-do queue
            pageQueue.add(RootPage);//initialize it with the first page
            int num_pages = 0;//pages crawled
            int wordID=0;
            while (!pageQueue.isEmpty() && num_pages < phase1_Pages) {// crawl 30 pages only in this phase
                try {
                    String current_url = pageQueue.get(0);
                    pageQueue.remove(0);
                    Crawler crawler = new Crawler(current_url);
                    //get this information
                    boolean modifydate_check=true;
                    boolean addentry_need=false;
                    //case 1 exist before
                    if(visitedPage.checkEntry(current_url)&&(indexToPageURL.getValue(num_pages)==null||!indexToPageURL.getValue(num_pages).equals(current_url))){
                        System.out.println(num_pages+"exist before");
                        continue;
                    }
                    //case 2 exist now
                    if(indexToPageURL.getValue(num_pages)!=null&&indexToPageURL.getValue(num_pages).equals(current_url)){
                        //Date unchanged
                        if(Objects.equals(indexToLastModifiedDate.getValue(Integer.parseInt(visitedPage.getValue(current_url))), crawler.extractModifiedDate()))
                        {
                            System.out.println(num_pages+"exist now , date unchanged");
                            pageQueue.addAll(crawler.extractLinks());
                            num_pages +=1;

                        }else{
                            System.out.println(num_pages+"exist now , date changed");
                            //del
                            indexToWordWithFreq.delEntry(num_pages);
                            indexToTitle.delEntry(num_pages);
                            TitleToIndex.delEntry(num_pages);
                            indexToLastModifiedDate.delEntry(num_pages);
                            indexToPageSize.delEntry(num_pages);
                            indexToChildLink.delEntry(num_pages);
                            addentry_need=true;
                        }
                    }


                    if (!visitedPage.checkEntry(current_url)) {
                        System.out.println(num_pages+"not ,exist");
                        addentry_need=true;
//                        System.out.println(num_pages+"date changed");
//                        visitedPage.delEntry(num_pages);//num_pages=="ID"
//                        indexToPageURL.delEntry(num_pages);
//                        indexToTitle.delEntry(num_pages);
//                        TitleToIndex.delEntry(num_pages);
//                        indexToLastModifiedDate.delEntry(num_pages);
//                        indexToPageSize.delEntry(num_pages);
//                        indexToWordWithFreq.delEntry(num_pages);
//                        indexToChildLink.delEntry(num_pages);

                    }
                    if(addentry_need) {
                        System.out.println(num_pages+"new pages");
                        //get this information
                        pageQueue.addAll(crawler.extractLinks());
                        //add all the child links into the to-do list
                        // add entry current only accept (string,string)
                        visitedPage.addEntry(current_url, String.valueOf(num_pages));//num_pages=="ID"
                        indexToPageURL.addEntry(num_pages, current_url);
                        indexToTitle.addEntry(num_pages, crawler.extractTitle());
                        TitleToIndex.addEntry(crawler.extractTitle(), String.valueOf(num_pages));
                        indexToLastModifiedDate.addEntry(num_pages, crawler.extractModifiedDate());
                        indexToPageSize.addEntry(num_pages, crawler.extractPageSize());
                        wordID=addEntryWordFreq(num_pages, crawler.extractWords(),wordID);
                        for (String link : crawler.extractLinks()) {
                            indexToChildLink.addLinkRelationships(num_pages, link);
                            linkToParentLink.addLinkRelationships(link, current_url);
                        }
                        num_pages++;
                        //System.out.println(num_pages);
                        //System.out.println(current_url);
                        //System.out.println(indexToWordWithFreq.getValue(String.valueOf(num_pages-1)));
                    }


                } catch (ParserException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finalize_() throws IOException {
        this.recman.commit();
        this.recman.close();
    }


    public static int addEntryWordFreq(int key,Vector<String> words,int wordID){
        try {
            int countPos=0;
            for (int i = 0; i < words.size(); i++) {
                countPos++;
                String word=words.get(i);
                //System.out.println(word);
                if (!stopStem.isStopWord(word)) {
                    String stemword=stopStem.stem(word);
                    if(stemword.contains("http")||stemword.equals(" ")||stemword.equals("")) {
                        //System.out.println("skip http");
                        countPos--;
                        continue;
                    }
                    //System.out.println(stopStem.stem(words.get(i)));
                    indexToWordWithFreq.addEntryWithFreq(key, stemword);
                    wordToDocPos.addEntry(stemword, key, countPos);
                    if(!wordToid.checkEntry(stemword)){
                        wordToid.addEntry(stemword,String.valueOf(wordID));
                        idToWord.addEntry(wordID,stemword);
                        wordID++;
                    }

                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return wordID;
    }
    public static void Test(){
        try{
            //visitedPage.printAll();
            //indexToPageURL.printAll();
            //indexToTitle.printAll();
            //indexToWordWithFreq.printAll();
            //indexToPageSize.printAll();
            //linkToParentLink.printAll();
            //indexToChildLink.printAll();
            //wordToid.printAll();
            //idToWord.printAll();
            //TitleToIndex.printAll();
            //wordToDocPos.printAll();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void output(){
        String fileName = "spider_result.txt";
        try {
            System.out.println("Writing onto spider_result:");
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            FastIterator itor = indexToTitle.getFastIterator();
            Object key;
            String title;
            key= itor.next();
            while(key!= null) {
                System.out.println("PageID = "+key);
                title = indexToTitle.getValue((int)key);
                if(!title.isEmpty()) {
                    writer.write(title);
                    System.out.println("Title: " + title);
                }else {
                    writer.write("No title");
                    System.out.println("No title");
                }
                System.out.println("——————————————–——————————————–———————————");
                writer.newLine();
                writer.write(indexToPageURL.getValue((int)key));
                writer.newLine();
                writer.write(indexToLastModifiedDate.getValue((int)key));
                writer.write(", ");
                writer.write(indexToPageSize.getValue((int)key)); /* to-be-updated */
                writer.newLine();
                String allS = indexToWordWithFreq.getValue((int)key);
                String[] allList;
                int i;
                if(allS != null) {
                    allList = allS.split(" ");
                    String current, freq;
                    for(i = 0; i < allList.length-2; i+=2) {
                        if(i>=18)break;
                        current = allList[i];
                        writer.write(current);
                        freq = allList[i+1];
                        writer.write(" "+freq+"; ");
                    }
                    // for the last keyword and frequency:
                    writer.write(allList[i]);
                    writer.write(" "+allList[i+1]);
                    writer.newLine();
                }else {
                    writer.write("No keyword indexed");
                    writer.newLine();
                }
                String links =  indexToChildLink.getValue((int)key);

                String[] linksList;
                if(links != null) {
                    linksList = links.split(" ");
                    for(i = 0; i < linksList.length; ++i) {
                        writer.write(linksList[i]);
                        writer.newLine();
                        if(i>=9)break;
                    }
                }else {
                    writer.write("No child link");
                    writer.newLine();
                }
                if((key =  itor.next())!=null) {
                    writer.write("——————————————–——————————————–——————————————–——————————————–——————————————–——————————————–");
                    writer.newLine();
                }
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] arg) throws IOException {
        Spider.buildDataBase();
        Spider.indexToLastModifiedDate.delEntry(180);
        Spider.indexToLastModifiedDate.delEntry(175);
        Spider.indexToLastModifiedDate.addEntry(180,"01");
        Spider.indexToLastModifiedDate.addEntry(175,"00");
        Spider.crawl();
        //Spider.Test();
        Spider.output();
        recman.commit();
        recman.close();

    }

}
