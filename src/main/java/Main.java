import org.htmlparser.util.ParserException;

import java.util.Vector;

public class Main {
    public static void main (String[] args)
    {
        try
        {
            Crawler crawler = new Crawler("http://www.cs.ust.hk/~dlee/4321/");


            Vector<String> words = crawler.extractWords();

            System.out.println("Words in "+crawler.getUrl()+" (size = "+words.size()+") :");
            for(int i = 0; i < words.size(); i++)
                if(i<5 || i>words.size()-6){
                    System.out.println(words.get(i));
                } else if(i==5){
                    System.out.println("...");
                }
            System.out.println("\n\n");



            Vector<String> links = crawler.extractLinks();
            System.out.println("Links in "+crawler.getUrl()+":");
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
