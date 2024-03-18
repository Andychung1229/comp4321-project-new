
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

}


