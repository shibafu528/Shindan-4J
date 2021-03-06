package shibafu.shindan4j;

import java.io.Serializable;
import java.util.List;

/**
 * 検索結果から取得できるデータが詰まってる
 * @author Shibafu
 *
 */
public class ShindanSummary implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected String PageURL;
	protected String Name;
	protected int Counter;
	protected String Author;
	protected String HashTag;
	protected List<String> ThemeLabel;
	protected String Description;

	public ShindanSummary(String pageURL, String name, int counter,
			String author, String hashTag, List<String> themeLabel,
			String description) {
		PageURL = pageURL;
		Name = name;
		Counter = counter;
		Author = author;
		HashTag = hashTag;
		ThemeLabel = themeLabel;
		Description = description;
	}

	public String getPageURL() {
		return PageURL;
	}

	public String getName() {
		return Name;
	}

	public int getCounter() {
		return Counter;
	}

	public String getAuthor() {
		return Author;
	}

	public String getHashTag() {
		return HashTag;
	}

	public List<String> getThemeLabel() {
		return ThemeLabel;
	}

	public String getDescription() {
		return Description;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (((ShindanSummary)obj).PageURL.equals(this.PageURL))
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		String[] s = PageURL.split("/");
		return Integer.parseInt(s[s.length - 1]);
	}

    @Override
    public String toString() {
        return String.format("「%s」by %s (%s) / %s", Name, Author, PageURL, Description);
    }
}
