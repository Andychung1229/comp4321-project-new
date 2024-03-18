import java.util.Vector;
import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import java.util.StringTokenizer;
import org.htmlparser.beans.LinkBean;
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

    public static void main (String[] args)
    {
        try
        {
            Crawler crawler = new Crawler("http://www.cs.ust.hk/~dlee/4321/");


            Vector<String> words = crawler.extractWords();

            System.out.println("Words in "+crawler.url+" (size = "+words.size()+") :");
            for(int i = 0; i < words.size(); i++)
                if(i<5 || i>words.size()-6){
                    System.out.println(words.get(i));
                } else if(i==5){
                    System.out.println("...");
                }
            System.out.println("\n\n");



            Vector<String> links = crawler.extractLinks();
            System.out.println("Links in "+crawler.url+":");
            for(int i = 0; i < links.size(); i++)
                System.out.println(links.get(i));
            System.out.println("");

        }
        catch (ParserException e)
        {
            e.printStackTrace ();
        }

    }
}


