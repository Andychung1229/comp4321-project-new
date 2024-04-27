public class KeyWord implements Comparable<KeyWord>{
    public String word;
    public int frequency;

    public KeyWord(String word, int frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public int compareTo(KeyWord anotherWord) {
        return Integer.compare(anotherWord.frequency, frequency);
    }

//    public String getWord() {
//        return word;
//    }
//
//    public void setWord(String word) {
//        this.word = word;
//    }
//
//    public int getFrequency() {
//        return frequency;
//    }
//
//    public void setFrequency(int frequency) {
//        this.frequency = frequency;
//    }

    @Override
    public String toString() {
        return word + " " + frequency + " ";
    }
}