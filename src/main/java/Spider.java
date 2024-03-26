import java.io.*;
import java.util.Vector;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import org.htmlparser.util.ParserException;
import jdbm.helper.FastIterator;

public class Spider {
    static String RootPage = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    static int maxPages = 300;
    static int phase1_Pages=30;
    static RecordManager recman;
    static StopStem stopStem = new StopStem("src/main/java/stopwords.txt");
    static index visitedPage;//URL->PageID
    static index indexToPageURL;
    static index indexToTitle;
    static index indexToLastModifiedDate;
    static index indexToPageSize;
    static index indexToWordWithFreq;
    static index indexToChildLink;
    static index linkToParentLink;
    public static void buildDataBase(){
        try {
            recman = RecordManagerFactory.createRecordManager("database");
            visitedPage = new index(recman,"visitedPage");
            indexToPageURL = new index(recman, "indexToPage");
            indexToTitle = new index(recman, "indexToTitle");
            indexToLastModifiedDate = new index(recman, "indexToLastModifiedDate");
            indexToWordWithFreq = new index(recman, "indexToWordWithFreq");
            indexToChildLink = new index(recman, "indexToChildLink");
            indexToPageSize = new index(recman, "indexToPageSize");
            linkToParentLink = new index(recman,"indexToParentLink");
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
    public static void crawl(){
        try{
            Vector<String> pageQueue=new Vector<String>();// create a to-do queue
            pageQueue.add(RootPage);//initialize it with the first page
            int num_pages=0;//pages crawled
            while(!pageQueue.isEmpty()&&num_pages<phase1_Pages){// crawl 30 pages only in this phase
                try{
                    String current_url=pageQueue.get(0);
                    pageQueue.remove(0);

                    if(visitedPage.checkEntry(current_url)){
                        //System.out.println("this is added before");

                    }else{
                        Crawler crawler=new Crawler(current_url);
                        //get this information
                        pageQueue.addAll(crawler.extractLinks()) ;
                        //add all the child links into the to-do list
                        // add entry current only accept (string,string)
                        visitedPage.addEntry(current_url, String.valueOf(num_pages));//num_pages=="ID"
                        indexToPageURL.addEntry(num_pages,current_url);
                        indexToTitle.addEntry(num_pages,crawler.extractTitle());
                        indexToLastModifiedDate.addEntry(num_pages,crawler.extractModifiedDate());
                        indexToPageSize.addEntry(num_pages,crawler.extractPageSize());
                        addEntryWordFreq(String.valueOf(num_pages),crawler.extractWords());
                        for(String link:crawler.extractLinks()){
                            indexToChildLink.addLinkRelationships(String.valueOf(num_pages),link);
                            linkToParentLink.addLinkRelationships(link,current_url);
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
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
    public static void addEntryWordFreq(String key,Vector<String> words){
        try {
            for (int i = 0; i < words.size(); i++) {
                String word=words.get(i);
                //System.out.println(word);
                if (!stopStem.isStopWord(word)) {
                    String stemword=stopStem.stem(word);
                    if(stemword.contains("http")||stemword.equals(" ")||stemword.equals("")) {
                         //System.out.println("skip http");
                        continue;
                    }
                    //System.out.println(stopStem.stem(words.get(i)));
                    indexToWordWithFreq.addEntryWithFreq(key, stemword);
                }
            }
        }catch (Exception e) {
        e.printStackTrace();
        }
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
            String key;
            String title;
            key= (String) itor.next();
            while(key!= null) {
                System.out.println("PageID = "+key);
                title = indexToTitle.getValue(key);
                if(!title.isEmpty()) {
                    writer.write(title);
                    System.out.println("Title: " + title);
                }else {
                    writer.write("No title");
                    System.out.println("No title");
                }
                System.out.println("——————————————–——————————————–———————————");
                writer.newLine();
                writer.write(indexToPageURL.getValue(key));
                writer.newLine();
                writer.write(indexToLastModifiedDate.getValue(key));
                writer.write(", ");
                writer.write(indexToPageSize.getValue(key)); /* to-be-updated */
                writer.newLine();
                String allS = indexToWordWithFreq.getValue(key);
                String[] allList;
                int i;
                if(allS != null) {
                    allList = allS.split(" ");
                    String current, freq;
                    for(i = 0; i < allList.length-2; i+=2) {
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
                allS = (String) indexToChildLink.getValue(key);
                if(allS != null) {
                    allList = allS.split(" ");
                    for(i = 0; i < allList.length; ++i) {
                        writer.write(allList[i]);
                        writer.newLine();
                    }
                }else {
                    writer.write("No child link");
                    writer.newLine();
                }
                if((key = (String) itor.next())!=null) {
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
    public static void main(String[] arg){
        Spider.buildDataBase();
        Spider.crawl();
        Spider.Test();
        Spider.output();

    }

}
