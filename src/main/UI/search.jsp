<%@ page import="java.util.ArrayList, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    // Dummy search results data
    private static final List<SearchResult> DUMMY_RESULTS = new ArrayList<SearchResult>() {{
        add(new SearchResult("Result 1", "This is a dummy result for demonstration purposes.", 0.85, "Dummy Page Title 1", "http://www.example.com/page1", "January 1, 2023", "2.5 MB", "keyword1 (3), keyword2 (5)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 2", "This is another dummy result for demonstration purposes.", 0.75, "Dummy Page Title 2", "http://www.example.com/page2", "February 2, 2023", "1.8 MB", "keyword3 (2), keyword4 (4)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 3", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        // Add more dummy results here...
        add(new SearchResult("Result 4", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 5", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)",new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 6", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 7", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 8", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 9", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));

        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));
        add(new SearchResult("Result 10", "This is yet another dummy result for demonstration purposes.", 0.95, "Dummy Page Title 3", "http://www.example.com/page3", "March 3, 2023", "3.2 MB", "keyword2 (4), keyword5 (3)", new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}, new ArrayList<String>() {{
            add("Parent Link 1");
            add("Parent Link 2");
            add("Parent Link 3");
        }}));

    }};

    // SearchResult class to hold title, description, and additional features
    public static class SearchResult {
        private String title;
        private String description;
        private double score;
        private String pageTitle;
        private String url;
        private String modificationDate;
        private String pageSize;
        private String keywords;
        private List<String> parentLinks;
        private List<String> childLinks;

        public SearchResult(String title, String description, double score, String pageTitle, String url, String modificationDate, String pageSize, String keywords, List<String> parentLinks, List<String> childLinks) {
            this.title = title;
            this.description = description;
            this.score = score;
            this.pageTitle = pageTitle;
            this.url = url;
            this.modificationDate = modificationDate;
            this.pageSize = pageSize;
            this.keywords = keywords;
            this.parentLinks = parentLinks;
            this.childLinks = childLinks;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public double getScore() {
            return score;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public String getUrl() {
            return url;
        }

        public String getModificationDate() {
            return modificationDate;
        }

        public String getPageSize() {
            return pageSize;
        }

        public String getKeywords() {
            return keywords;
        }

        public List<String> getParentLinks() {
            return parentLinks;
        }

        public List<String> getChildLinks() {
            return childLinks;
        }
    }
%>
<%
    // Get the search query from the request parameter
    String query = request.getParameter("query");
    if (query != null) {
        // Filter the dummy results based on the query
        List<SearchResult> filteredResults = new ArrayList<>();
        for (SearchResult result : DUMMY_RESULTS) {
            if (result.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredResults.add(result);
            }
        }

        // Display the top 50 results
        for (int i = 0; i < Math.min(filteredResults.size(), 50); i++) {
            SearchResult result = filteredResults.get(i);
%>
<div class="result">
    <div class="title-container">
        <h3 class="score"><%= result.getScore() %></h3>
        <h3 class="title"><%= result.getTitle() %></h3>
    </div>
    <p>URL: <%= result.getUrl() %></p>
    <div class="Date-size">
        <p class="date">Last Modification Date: <%= result.getModificationDate() %></p>
        <p class="size">Size of Page: <%= result.getPageSize() %></p>
    </div>
    <div
        <p>Keywords: <%= result.getKeywords() %></p>
    </div>
    <p>Parent Links:</p>
    <ul class="parent-links">
        <% 
            List<String> parentLinks = result.getParentLinks(); // Replace with your logic to retrieve the parent links
            for (String link : parentLinks) {
        %>
        <li><a href="<%= link %>"><%= link %></a></li>
        <% } %>
    </ul>
    <p> Childs CLinks:</p>
    <ul class="child-links">
        <% 
            List<String> childLinks = result.getChildLinks(); // Replace with your logic to retrieve the parent links
            for (String link : childLinks) {
        %>
        <li><a href="<%= link %>"><%= link %></a></li>
        <% } %>
    </ul>
   
    
</div>

<%
        }
    }
%>