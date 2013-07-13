package shibafu.shindan4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ShindanList {


	//
	// 定数
	//

	private static final String LISTPAGE_URL = "http://shindanmaker.com/c/list?";

	/** 取得モード: 総合ランキング */
	public static final String MODE_BEST = "1";
	/** 取得モード: 新着診断 */
	public static final String MODE_NEW = "0";
	/** 取得モード: HOT診断 */
	public static final String MODE_HOT = "hot";
	/** 取得モード: PICKUP診断 */
	public static final String MODE_PICKUP = "pickup";
	/** 取得モード: デイリーランキング */
	public static final String MODE_DAILY = "daily";
	/** 取得モード: 月間ランキング */
	public static final String MODE_MONTHLY = "monthly";
	/** 取得モード: お気に入りランキング */
	public static final String MODE_FAVORITE = "fav";
	/** 取得モード: 画像系診断 */
	public static final String MODE_PICTURE = "pic";
	/** 取得モード: 動画系診断 */
	public static final String MODE_MOVIE = "mov";


	//
	// プライベートフィールド
	//

	private String QueryMode; //最後に使用された取得モード
	private int QueryPage; //最後に取得したページ
	private List<ShindanSummary> Results; //

	public String getQueryMode() {
		return QueryMode;
	}

	public int getQueryPage() {
		return QueryPage;
	}

	public List<ShindanSummary> getResults() {
		return Results;
	}


	//
	// コンストラクタ
	//

	public ShindanList(String queryMode, int queryPage,
			List<ShindanSummary> results) {
		QueryMode = queryMode;
		QueryPage = queryPage;
		Results = results;
	}
	
	
	//
	// インスタンスメソッド
	
	/**
	 * ページカウントを1つ進めて、次のページの要素を取得します。<br>
	 * @return 新たに取得した要素の数。IO例外等が発生した場合は-1が返る。
	 */
	public int getNextPage() {
		try {
			//GETを行う
			Document doc = Jsoup.connect(getQuery(QueryMode, ++QueryPage))
					.timeout(20000).get();
			//ドキュメントから要素を抽出する
			List<ShindanSummary> summaries = getListElements(doc);
			//Resultsリストに追加する
			Results.addAll(summaries);
			//取得した要素の数を返す
			return summaries.size();
		} catch (IOException e) {
			return -1;
		}
	}

	
	//
	// スタティックメソッド
	//

	/**
	 * クエリを付加したlistページのURLを取得します
	 * @param mode 取得モード
	 * @param page 取得するページ(1～)
	 * @return listページURL
	 */
	private static String getQuery(String mode, int page) {
		StringBuilder sb = new StringBuilder(LISTPAGE_URL);
		sb.append("mode=");
		sb.append(mode);
		sb.append("&p=");
		sb.append(page);
		return sb.toString();
	}

	/**
	 * 診断メーカーのランキングページ等から、診断の一覧を取得します
	 * @param mode 取得モード -- 新着やHOTなど、どのページから取得するかを MODE_* 定数から
	 * @param page ページ数 -- 何ページ目を取得するか指定 (1以上の整数)
	 * @return
	 * @throws IOException
	 */
	public static ShindanList getPage(String mode, int page) throws IOException {
		//GETを行う
		Document doc = Jsoup.connect(getQuery(mode, page)).timeout(20000).get();
		//ドキュメントから要素を抽出する
		List<ShindanSummary> summaries = getListElements(doc);
		//結果インスタンスを生成して返す
		return new ShindanList(mode, page, summaries);
	}
	
	/**
	 * 引数に渡したドキュメントから、診断メーカーのリストページ内要素"list_list"を抽出します
	 * @param doc 抽出対象のドキュメント
	 * @return 抽出されたリスト要素
	 */
	public static List<ShindanSummary> getListElements(Document doc) {
		//要素リストを作成
		List<ShindanSummary> summaries = new ArrayList<ShindanSummary>();
		//リストの各要素の親をとる
		Elements items = doc.select("li[class=list_list]");
		//各要素をパース
		for (Element e : items) {
			//タイトルとURL
			Elements elemTitle = e.select("a[class=list_title]");
			String title = elemTitle.text();
			String url = "http://shindanmaker.com" + elemTitle.attr("href");
			//カウンター
			Elements elemNum = e.select("span[class=list_num]");
			String regex = "\\d+";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(elemNum.text().replaceAll(",", ""));
			int counter = 0;
			if (matcher.find()) {
				counter = Integer.valueOf(matcher.group());
			}
			//作者
			Elements elemAuthor = e.select("span[class=list_author]");
			String author = ((elemAuthor != null)? elemAuthor.select("a").text() : null);
			//テーマラベル
			Elements elemTheme = e.select("a[class=themelabel]");
			List<String> themelabel = new ArrayList<String>();
			if (elemTheme != null) {
				for (Element et : elemTheme) {
					themelabel.add(et.text());
				}
			}
			//ハッシュタグ
			Elements elemHashtag = e.select("span[class=hushtag]");
			String hashtag = ((elemHashtag != null)? elemHashtag.text() : null);
			//概要
			Elements elemDesc = e.select("span[class=list_description]");
			String desc = elemDesc.text();
			//インスタンス作って要素リストに格納
			summaries.add(new ShindanSummary(url, title, counter, author, hashtag, themelabel, desc));
		}
		return summaries;
	}
}
