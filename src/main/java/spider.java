import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import org.htmlparser.util.ParserException;

public class spider {
    static String RootPage = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    static int maxPages = 300;
    static int phase1_Pages=30;
    static RecordManager recman;
    //static StopStem stopStem = new StopStem("C:\\Users\\Andyc\\IdeaProjects\\comp4321-project\\src\\main\\java\\stopwords.txt");
    static StopStem stopStem = new StopStem("src/main/java/stopwords.txt");
    static index visitedPage;
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
                    System.out.println(current_url);
                    if(visitedPage.checkEntry(current_url)){
                        System.out.println("this is added before");


                    }else{
                        Crawler crawler=new Crawler(current_url);
                        //get this information
                        pageQueue.addAll(crawler.extractLinks()) ;
                        //add all the child links into the to-do list
                        // add entry current only accept (string,string)
                        visitedPage.addEntry(current_url, String.valueOf(num_pages));//num_pages=="ID"
                        indexToPageURL.addEntry(String.valueOf(num_pages),current_url);
                        indexToTitle.addEntry(String.valueOf(num_pages),crawler.extractTitle());
                        indexToLastModifiedDate.addEntry(String.valueOf(num_pages),crawler.extractModifiedDate());
                        num_pages++;



                    }


                    System.out.println(num_pages);
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
    public static void output(){

    }
    public static void main(String[] arg){
        spider.buildDataBase();
        spider.crawl();
        spider.output();

    }

}
