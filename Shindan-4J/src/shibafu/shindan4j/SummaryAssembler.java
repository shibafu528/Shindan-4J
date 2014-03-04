package shibafu.shindan4j;

import java.util.List;

/**
 * Created by shibafu on 14/03/04.
 */
class SummaryAssembler {
    protected String pageURL;
    protected String name;
    protected int counter;
    protected String author;
    protected String hashTag;
    protected List<String> themeLabel;
    protected String description;

    public SummaryAssembler setPageURL(String pageURL) {
        this.pageURL = pageURL;
        return this;
    }

    public SummaryAssembler setName(String name) {
        this.name = name;
        return this;
    }

    public SummaryAssembler setCounter(int counter) {
        this.counter = counter;
        return this;
    }

    public SummaryAssembler setAuthor(String author) {
        this.author = author;
        return this;
    }

    public SummaryAssembler setHashTag(String hashTag) {
        this.hashTag = hashTag;
        return this;
    }

    public SummaryAssembler setThemeLabel(List<String> themeLabel) {
        this.themeLabel = themeLabel;
        return this;
    }

    public SummaryAssembler setDescription(String description) {
        this.description = description;
        return this;
    }

    public ShindanSummary create() {
        return new ShindanSummary(pageURL, name, counter, author, hashTag, themeLabel, description);
    }
}
