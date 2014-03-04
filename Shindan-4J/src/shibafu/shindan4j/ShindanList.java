package shibafu.shindan4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
	/** 取得モード: 診断検索 */
	public static final String MODE_SEARCH = "search";
	/** 取得モード: テーマ検索 */
	public static final String MODE_THEME = "tag";

	/**
	 * パラメータを付加したlistページのURLを取得します
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
	 * パラメータを付加した検索ページのURLを取得します
	 * @param searchWord 検索ワード
	 * @param page 取得するページ(1～)
	 * @param orderByNew 新着順で検索する (falseの場合は人気順)
	 * @return listページURL
	 * @throws UnsupportedEncodingException 検索ワードのURLエンコードに失敗した場合にスロー
	 */
	private static String getSearchQuery(String searchWord, int page, boolean orderByNew)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(LISTPAGE_URL);
		sb.append("mode=");
		sb.append(MODE_SEARCH);
		sb.append("&q=");
		sb.append(URLEncoder.encode(searchWord, "utf-8"));
		sb.append("&p=");
		sb.append(page);
		if (orderByNew) {
			sb.append("&order=new");
		}
		return sb.toString();
	}
	
	/**
	 * パラメータを付加したテーマ検索ページのURLを取得します
	 * @param searchTheme 検索テーマ
	 * @param page 取得するページ(1～)
	 * @param orderByNew 新着順で検索する (falseの場合は人気順)
	 * @return listページURL
	 * @throws UnsupportedEncodingException 検索ワードのURLエンコードに失敗した場合にスロー
	 */
	private static String getThemeSearchQuery(String searchTheme, int page, boolean orderByNew) 
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(LISTPAGE_URL);
		sb.append("mode=");
		sb.append(MODE_THEME);
		sb.append("&tag=");
		sb.append(URLEncoder.encode(searchTheme, "utf-8"));
		sb.append("&p=");
		sb.append(page);
		if (orderByNew) {
			sb.append("&order=new");
		}
		return sb.toString();
	}

	/**
	 * 診断メーカーのランキングページ等から、診断の一覧を取得します
	 * @param mode 取得モード -- 新着やHOTなど、どのページから取得するかを MODE_* 定数から
	 * @param page ページ数 -- 何ページ目を取得するか指定 (1以上の整数)
	 * @return
	 * @throws IOException
	 */
	public static List<ShindanSummary> getList(String mode, int page) throws IOException {
		//GETを行う
		Document doc = Jsoup.connect(getQuery(mode, page)).timeout(20000).get();
		//ドキュメントから要素を抽出する
		List<ShindanSummary> summaries = getListElements(doc);
		//結果を返す
		return summaries;
	}
	
	/**
	 * 診断メーカーの検索ページにクエリをPOSTし、検索結果を取得します
	 * @param searchWord 検索ワード
	 * @param page ページ数 -- 何ページ目を取得するか指定 (1以上の整数)
	 * @param orderByNew 新着順で検索する (falseの場合は人気順)
	 * @return
	 * @throws IOException
	 */
	public static List<ShindanSummary> getSearchResults(
			String searchWord, int page, boolean orderByNew) throws IOException {
		//GETを行う
		Document doc = Jsoup.connect(getSearchQuery(searchWord, page, orderByNew)).timeout(20000).get();
		//ドキュメントから要素を抽出する
		List<ShindanSummary> summaries = getListElements(doc);
		//結果を返す
		return summaries;
	}
	
	/**
	 * 診断メーカーのテーマ検索ページにクエリをPOSTし、検索結果を取得します
	 * @param searchTheme 検索テーマ
	 * @param page ページ数 -- 何ページ目を取得するか指定 (1以上の整数)
	 * @param orderByNew 新着順で検索する (falseの場合は人気順)
	 * @return
	 * @throws IOException
	 */
	public static List<ShindanSummary> getThemeSearchResults(
			String searchTheme, int page, boolean orderByNew) throws IOException {
		//GETを行う
		Document doc = Jsoup.connect(getThemeSearchQuery(searchTheme, page, orderByNew)).timeout(20000).get();
		//ドキュメントから要素を抽出する
		List<ShindanSummary> summaries = getListElements(doc);
		//結果を返す
		return summaries;
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
		Elements tables = doc.select("table[class=list_list]");
        //各要素をパース
        //  2014/3/4以降ページ構造変化により、trタグ2つで1組となった、めんどくさい
        SummaryAssembler assembler = null;
        for (Element e : tables.select("tr")) {
            if (assembler == null) {
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
                //組立機に突っ込む
                assembler = new SummaryAssembler()
                        .setPageURL(url)
                        .setName(title)
                        .setCounter(counter);
            }
            else {
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
                assembler.setAuthor(author)
                        .setHashTag(hashtag)
                        .setThemeLabel(themelabel)
                        .setDescription(desc);
                summaries.add(assembler.create());
                assembler = null;
            }
        }
		return summaries;
	}
}
