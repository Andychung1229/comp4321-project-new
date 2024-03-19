import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;
class Posting implements Serializable
{
    public String doc;
    public int freq;
    Posting(String doc, int freq)
    {
        this.doc = doc;
        this.freq = freq;
    }
}

public class index {
    private RecordManager recman;
    private HTree hashtable;

    index(String recordmanager, String objectname) throws IOException
    {
        recman = RecordManagerFactory.createRecordManager(recordmanager);
        long recid = recman.getNamedObject(objectname);

        if (recid != 0)
            hashtable = HTree.load(recman, recid);
        else
        {
            hashtable = HTree.createInstance(recman);
            recman.setNamedObject( "ht1", hashtable.getRecid() );
        }
    }
    index(RecordManager recordmanager, String objectname) throws IOException
    {
        recman = recordmanager;
        long recid = recman.getNamedObject(objectname);

        if (recid != 0)
            hashtable = HTree.load(recman, recid);
        else
        {
            hashtable = HTree.createInstance(recman);
            recman.setNamedObject( objectname, hashtable.getRecid() );
        }
    }


    @Override
    public void finalize() throws IOException
    {

        recman.commit();
        recman.close();
    }

    public void addEntry(String word, int x, int y) throws IOException
    {
        // Add a "docX Y" entry for the key "word" into hashtable
        if(hashtable.get(word)!=null){
            String temp=hashtable.get(word)+" doc"+String.valueOf(x)+" "+String.valueOf(y)+" ";
            hashtable.put(word,temp);
        }else{
            hashtable.put(word,"doc"+String.valueOf(x)+" "+String.valueOf(y));
        }
        // ADD YOUR CODES HERE

    }
    public void addEntry(String key, String value) throws IOException
    {
        // Add a "docX Y" entry for the key "word" into hashtable
        // ADD YOUR CODES HERE
        if(!checkEntry(key))
            hashtable.put(key, value);
    }
    public void addEntryWithFreq(String key,String value) throws IOException {
        if(hashtable.get(key)==null){
            hashtable.put(key,value+"1");
        }else{

        }
    }

    public void delEntry(String word) throws IOException
    {
        // Delete the word and its list from the hashtable
        if(hashtable.get(word)!=null){
            hashtable.remove(word);
        }
        // ADD YOUR CODES HERE

    }
    public boolean checkEntry(String check) throws IOException{
        String original = (String) hashtable.get(check);
        return (original == null || original.equals("")) ? false : true;
    }

    public boolean checkEntry(int check) throws IOException{
        String original = (String) hashtable.get("" + check);
        return (original == null || original.equals("")) ? false : true;
    }
    public void printAll() throws IOException
    {
        // Print all the data in the hashtable
        // ADD YOUR CODES HERE
        FastIterator it =hashtable.keys();
        String key;
        while((key=(String)it.next())!=null){
            System.out.println(key+": "+hashtable.get(key));
        }

    }

    public String getValue(String key) throws IOException {
        return (String) hashtable.get(key);
    }

    public int getNumKey() throws IOException {
        FastIterator iter = hashtable.keys();
        int numKey = 0;
        while(iter.next()!=null) {
            numKey++;
        }
        return numKey;
    }
    public void clearAll() throws IOException {
        List<String> keysToRemove = new ArrayList<>();
        FastIterator it = hashtable.keys();
        String key;
        while ((key = (String) it.next()) != null) {
            keysToRemove.add(key);
        }
        for (String keyToRemove : keysToRemove) {
            hashtable.remove(keyToRemove);
        }
    }

    public static void main(String[] args)
    {
        try
        {
            index index = new index("lab1","ht1");

            index.clearAll();
            index.addEntry("cat", 2, 6);
            index.addEntry("dog", 1, 33);
            System.out.println("First print");
            index.printAll();

            index.addEntry("cat", 8, 3);
            index.addEntry("dog", 6, 73);
            index.addEntry("dog", 8, 83);
            index.addEntry("dog", 10, 5);
            index.addEntry("cat", 11, 106);
            System.out.println("Second print");
            index.printAll();

            index.delEntry("dog");
            System.out.println("Third print");
            index.printAll();
            index.finalize();
        }
        catch(IOException ex)
        {
            System.err.println(ex.toString());
        }

    }
}



