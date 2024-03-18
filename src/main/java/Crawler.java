import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import java.util.StringTokenizer;
import org.htmlparser.beans.LinkBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;


public class Crawler
{
    private String url;
    Crawler(String _url)
    {
        url = _url;
    }

    public String getUrl(){
        return url;
    }
    public Vector<String> extractWords() throws ParserException
    {
        // extract words in url and return
        Vector<String> page_word = new Vector<String>();
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (true);
        sb.setURL (url);

        StringTokenizer st=new StringTokenizer(sb.getStrings (),"[ ,?]+");
        while(st.hasMoreTokens()) {
            page_word.add(st.nextToken());
        }
        return page_word;
    }
    public Vector<String> extractLinks() throws ParserException

    {
        // extract links in url and return
        Vector<String> v_link = new Vector<String>();
        LinkBean lb = new LinkBean();
        lb.setURL(url);
        URL[] URL_array = lb.getLinks();
        for(int i=0; i<URL_array.length; i++){
            v_link.add(URL_array[i].toString());
        }
        return v_link;
    }

    public String extractTitle() throws IOException

    {
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        return title;
    }

    public Date extractModifiedDate() {
        /*
        try {
            URL place = new URL(url);
            URLConnection connection = place.openConnection();
            long date = connection.getLastModified();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            if(date == 0) {
                date = connection.getDate();
            }
            return dateFormatter.format(new Date(date));
        }catch (Exception e) {
            return null;
        }

         */
        Date lastModifiedDate = null;
        try {
            URL webpageUrl = new URL(url);
            URLConnection connection = webpageUrl.openConnection();

            long lastModifiedTimestamp = connection.getLastModified();

            if (lastModifiedTimestamp > 0) {
                lastModifiedDate = new Date(lastModifiedTimestamp);
                //System.out.println("Last Modified Date: " + lastModifiedDate);
            } else {
                lastModifiedDate = new Date();
                //System.out.println("No Last Modified Date Available. Using System Time: " + lastModifiedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lastModifiedDate;
    }
    public static void main (String[] args)
    {
        try
        {
            Crawler crawler = new Crawler("https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm");


            String title = crawler.extractTitle();
            System.out.println(title);


            Vector<String> words = crawler.extractWords();

            System.out.println("Words in "+crawler.url+":");
            for(int i = 0; i < words.size(); i++)
                System.out.println(words.get(i)+" ");
            System.out.println("\n\n");

            Vector<String> links = crawler.extractLinks();
            System.out.println("Links in "+crawler.url+":");
            for(int i = 0; i < links.size(); i++)
                System.out.println(links.get(i));
            System.out.println("");

            Date date = crawler.extractModifiedDate();
            System.out.println(date);

        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }

    }
}


