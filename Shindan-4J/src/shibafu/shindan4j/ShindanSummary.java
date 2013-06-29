package shibafu.shindan4j;

import java.util.List;

/**
 * 検索結果から取得できるデータが詰まってる
 * @author Shibafu
 *
 */
public class ShindanSummary {
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

}
