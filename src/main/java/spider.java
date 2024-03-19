import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
public class spider {
    static String RootPage = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
    static int maxPages = 300;
    static int phase1_Pages=30;
    static RecordManager recman;
    static StopStem stopStem = new StopStem("C:\\Users\\Andyc\\IdeaProjects\\comp4321-project\\src\\main\\java\\stopwords.txt");
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

    }
    public static void output(){

    }
    public static void main(String[] arg){
        spider.buildDataBase();
        spider.crawl();
        spider.output();

    }

}
