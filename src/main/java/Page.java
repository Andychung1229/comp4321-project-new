import java.util.Vector;

public class Page implements Comparable<Page>{
    private double score;
    private String pageTitle;
    private String url;
    private String lastUpdateTime;
    private int pageSize;
    private Vector<String> parentLink;
    private Vector<String> childrenLink;
    private Vector<KeyWord> top_n_keyword;

    public Page() {
        parentLink = new Vector<String>();
        childrenLink = new Vector<String>();
        top_n_keyword = new Vector<KeyWord>();
    }



    @Override
    public String toString() {

        String storeString = "score: " + score + "\npage title: " + pageTitle + "\nurl: " + url + "\nlast update time: " + lastUpdateTime + "\npage size: " + pageSize + "\namount: " + parentLink.size() + " " + childrenLink.size() + "\n"
                + "top 5 words: ";
        for(int i = 0; i < top_n_keyword.size(); i++) {
            storeString += top_n_keyword.get(i) + " ";
        }
        return storeString;
    }



    @Override
    public int compareTo(Page anotherPage) {

        return Double.compare(anotherPage.score, score);
    }

    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public String getPageTitle() {
        return pageTitle;
    }
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public Vector<String> getParentLink() {
        return parentLink;
    }
    public void addParentLink(String parentLink) {
        this.parentLink.addElement(parentLink);
    }
    public Vector<String> getChildrenLink() {
        return childrenLink;
    }
    public void addChildrenLink(String childrenLink) {
        this.childrenLink.addElement(childrenLink);
    }

    public Vector<KeyWord> getTopFiveWord() {
        return top_n_keyword;
    }

    public void addTopFiveWord(KeyWord topFiveWord) {
        this.top_n_keyword.addElement(topFiveWord);
    }


}

