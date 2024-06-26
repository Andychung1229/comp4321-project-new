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
        // ADD YOUR CODES HERE
        //prevent entering same entry
        if (hashtable.get(word) != null && ((String) hashtable.get(word)).contains("doc" + x + " " + y+" "))
        {
            return;
        }
        String original = (String) hashtable.get(word);
        String added = "doc" + x + " " + y+" ";
        if(original == null || original.equals(""));
        else {
            added = original + added;
        }
        hashtable.put(word, added);
    }
    public void modifiedEntry(int delDoc) throws IOException
    {

        FastIterator it =hashtable.keys();
        while(true) {
            Object key = it.next();
            if (key == null) {
                break;
            }
            String word=(String)key;
            String original = (String) hashtable.get(word);
            if(!original.contains("doc"+delDoc))continue;
            hashtable.put(word, "");
            String[] originallist = original.split(" ");
            for (int i = 0; i < originallist.length; i += 2) {
                if (!originallist[i].equals("doc" + delDoc)) {
                    addEntry(word, Integer.parseInt(originallist[i].substring(3)), Integer.parseInt(originallist[i + 1]));
                }
            }
        }

    }
    public void addEntry(int key, String value) throws IOException
    {
        // Add a "docX Y" entry for the key "word" into hashtable
        // ADD YOUR CODES HERE
        if(!checkEntry(key))
            hashtable.put(key, value);
    }
    public void addEntry(String key, String value) throws IOException
    {
        // Add a "docX Y" entry for the key "word" into hashtable
        // ADD YOUR CODES HERE
        if(!checkEntry(key))
            hashtable.put(key, value);
    }
    public void addLinkRelationships(String key, String value) throws IOException
    {
        String word_list =(String)hashtable.get(key);
        if(word_list!=null&&word_list.contains(value)){
           return;
        }
        else if(word_list==null||word_list.isEmpty()) {
            hashtable.put(key, value);
        }else{
            hashtable.put(key, word_list+" "+value);
        }

    }
    public void addLinkRelationships(int key, String value) throws IOException
    {
        String word_list =(String)hashtable.get(key);
        if(word_list!=null&&word_list.contains(value)){
            return;
        }
        else if(word_list==null||word_list.isEmpty()) {
            hashtable.put(key, value);
        }else{
            hashtable.put(key, word_list+" "+value);
        }

    }
    public void addEntryWithFreq(int key,String value) throws IOException {//to do
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
    public void delEntry(int word) throws IOException
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
        String original = (String) hashtable.get(check);
        return (original == null || original.equals("")) ? false : true;
    }
    public void printAll() throws IOException
    {
        // Print all the data in the hashtable
        // ADD YOUR CODES HERE
        FastIterator it =hashtable.keys();
        while(true){
            Object key = it.next();
            if (key == null) {
                break;
            }
            if (key instanceof Integer) {
                int intKey = (int) key;
                System.out.println(intKey + ": " + hashtable.get(intKey));
            } else if (key instanceof String) {
                String stringKey = (String) key;
                System.out.println(stringKey + ": " + hashtable.get(stringKey));
            }
        }

    }
    public FastIterator getFastIterator() throws IOException {
        return hashtable.keys();
    }
    public String getValue(String key) throws IOException {
        return (String) hashtable.get(key);
    }
    public String getValue(int key) throws IOException {
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
        Object key;
        while ((key =  it.next()) != null) {
            keysToRemove.add(String.valueOf(key));
        }
        for (String keyToRemove : keysToRemove) {
            if (hashtable.keys().next() instanceof Integer) {
                hashtable.remove(Integer.valueOf(keyToRemove));
            }
            else{
                hashtable.remove(keyToRemove);
            }

        }
    }

    public static void main(String[] args)
    {
        try
        {
            index intindex = new index("index test","ht1");
            index stringindex = new index("index test","ht1");
            intindex.clearAll();
            stringindex.clearAll();
            intindex.addEntry(1,"good");
            intindex.addEntry(1,"good");
            intindex.addEntry(2,"good");

            intindex.addEntry(2,"bad");

            stringindex.addEntry("a","good");
            stringindex.addEntry("a","good");
            //stringindex.clearAll();
            stringindex.addEntry("b","good");
            stringindex.addEntry("b","good");
            stringindex.addEntry("b","bad");
            stringindex.addEntry("c","bad");
            stringindex.delEntry("b");
            stringindex.delEntry("b");
            stringindex.delEntry("bccc");
            intindex.delEntry(1);

            stringindex.printAll();
            intindex.printAll();
            intindex.finalize();
        }
        catch(IOException ex)
        {
            System.err.println(ex.toString());
        }

    }
}



