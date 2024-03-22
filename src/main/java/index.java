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
    public void addEntryWithFreq(String key,String value) throws IOException {//to do
        if(hashtable.get(key)==null){
            hashtable.put(key,value+" "+"1");
        }else{
            String words=getValue(key);
            String[] word_list = words.split(" ");
            boolean inserted = false;
            for(int i=0;i<word_list.length;i+=2) {
                if (word_list[i].equals(value)) {
                    inserted = true;
                    int freq = Integer.parseInt(word_list[i + 1]);
                    freq++;
                    word_list[i + 1] = String.valueOf(freq);
                }
            }
            if(!inserted){
                int length=word_list.length;
                String[] newWord_list = new String[length + 2];
                System.arraycopy(word_list, 0, newWord_list, 0, length);
                newWord_list[length] = value;
                newWord_list[length + 1] = "1";
                word_list = newWord_list;
            }
            String modifiedString = String.join(" ", word_list);
            hashtable.put(key,modifiedString);
        }
        //System.out.println(getValue(key));
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
            index.addEntryWithFreq("1","good");
            index.addEntryWithFreq("1","good");
            index.addEntryWithFreq("2","good");
            index.addEntryWithFreq("2","bad");
            index.addEntryWithFreq("1","bad");
            index.addEntryWithFreq("2","bad");
            index.addEntryWithFreq("2","evil");
            index.addEntryWithFreq("2","bad");
            index.addEntryWithFreq("1","bad");
            index.addEntryWithFreq("2","2");
            index.addEntryWithFreq("2","good");
            index.addEntryWithFreq("2","2");
            index.addEntryWithFreq("2","2");


            index.printAll();
            index.finalize();
        }
        catch(IOException ex)
        {
            System.err.println(ex.toString());
        }

    }
}



