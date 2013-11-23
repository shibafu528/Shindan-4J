package shibafu.shindan4j;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AuthorPage extends ShindanList{
	
	///
	/// 定数
	///
	
	private static final String LISTPAGE_URL = "http://shindanmaker.com/author/";
	
	/** 取得する時の順番 : 新着順 */
	public static final String SORT_NEW     = "0";
	/** 取得する時の順番 : 人気順 */
	public static final String SORT_POPULAR = "1";
	/** 取得する時の順番 : 名前順 */
	public static final String SORT_NAME    = "2";

	
	//
	// メソッド
	//

	/**
	 * クエリを付加したauthorページのURLを取得します
	 * @param name 対象ID
	 * @param mode 取得モード
	 * @param page 取得するページ(1～)
	 * @return authorページURL
	 */
	private static String getQuery(String name, String mode, int page) {
		//名前の@抜きを行う
		if (name.startsWith("@"))
			name = name.substring(1);
		//URLを組む
		StringBuilder sb = new StringBuilder(LISTPAGE_URL);
		sb.append(name);
		sb.append("?");
		sb.append("mode=");
		sb.append(mode);
		sb.append("&p=");
		sb.append(page);
		return sb.toString();
	}
	
	/**
	 * 診断メーカーの作者ページから、診断の一覧を取得します
	 * @param mode 取得モード -- データの並び順を SORT_* 定数から
	 * @param page ページ数 -- 何ページ目を取得するか指定 (1以上の整数)
	 * @return
	 * @throws IOException
	 */
	public static List<ShindanSummary> getAuthorPageResults(String name, String mode, int page) throws IOException {
		//名前の@抜きを行う
		if (name.startsWith("@"))
			name = name.substring(1);
		//GETを行う
		Document doc = Jsoup.connect(getQuery(name, mode, page)).timeout(20000).get();
		//ドキュメントから要素を抽出する
		List<ShindanSummary> summaries = ShindanList.getListElements(doc);
		//結果を返す
		return summaries;
	}
}
