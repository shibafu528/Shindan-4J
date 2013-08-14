package shibafu.shindan4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {	
	public static String[] extractURL(String text) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list.toArray(new String[list.size()]);
	}
}
