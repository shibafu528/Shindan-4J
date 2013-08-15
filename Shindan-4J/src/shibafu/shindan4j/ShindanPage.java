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

/**
 * 診断メーカーの、診断ページに関するデータが詰まってる
 * @author Shibafu
 *
 */
public class ShindanPage {
	//診断概要
	protected String Title; //診断タイトル
	protected String Description; //診断の説明文
	//各種処理用
	protected String PageURL; //診断ページのURL
	//診断リンクリスト
	protected List<String> LinkedPage = new ArrayList<String>();
	//テーマラベル
	protected List<String> ThemeLabel = new ArrayList<String>();
	//作者
	protected String Author;

	public ShindanPage(String title, String desc, String url, List<String> link, List<String> theme, String author) {
		this.Title = title;
		this.Description = desc;
		this.PageURL = url;
		this.LinkedPage = link;
		this.ThemeLabel = theme;
		this.Author = author;
	}

	/**
	 * 診断ページに接続してデータを取得します
	 * @param url 診断ページのURL
	 * @return 診断ページのデータ
	 * @throws IOException
	 */
	public static ShindanPage getShindanPage(String url) throws IOException {
		//ページに接続する
		Document doc = Jsoup.connect(url).timeout(20000).get();
		//METAタグを取得
		Elements meta = doc.select("meta");
		//タイトル、説明文を取得
		String title = meta.select("*[property=og:title]").first().attr("content");
		String desc = meta.select("*[property=og:description]").first().attr("content");
		//テーマラベルを取得
		List<String> theme = new ArrayList<String>();
		Elements themes = doc.select("a[class=themelabel]");
		for (Element e : themes) {
			theme.add(e.text());
		}
		//作者名を取得
		Elements elemAuthor = doc.select("a[href^=/author/]");
		String author = ((elemAuthor != null)? elemAuthor.select("a").text() : null);
		//説明文の接尾辞を削除する
		desc = desc.substring(0, desc.length() - 9);
		String regex = "(http://shindanmaker.com/\\d+)";
		//診断リンクを探索する
		List<String> matchedlink = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(desc);
		while (m.find()) {
			matchedlink.add(m.group(0));
		}
		//説明文からURLを除去する
		desc = desc.replaceAll(regex, "◆");
		//インスタンスを生成して返す
		return new ShindanPage(title, desc, url, matchedlink, theme, author);
	}

	/**
	 * 指定の名前で、診断の結果をリクエストします
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public ShindanResult requestResult(String name) throws IOException {
		//POSTを行う
		Document doc = Jsoup.connect(PageURL).data("u", name).timeout(20000).post();
		//結果を取得
		Elements displayElem = doc.select("meta[property=me2:post_body]");
		if (displayElem == null) {
			throw new IOException("meta[property=me2:post_body]がHTML上に見つかりません");
		}
		String display = displayElem.first().attr("content");
		Elements shareElem = doc.select("textarea[onclick=this.focus();this.select()]");
		if (shareElem == null) {
			throw new IOException("textarea[onclick=this.focus();this.select()]がHTML上に見つかりません");
		}
		String share = shareElem.first().text();
		//結果インスタンスを生成して返す
		return new ShindanResult(this, name, display, share);
	}

	public String getTitle() {
		return Title;
	}

	public String getDescription() {
		return Description;
	}

	public String getPageURL() {
		return PageURL;
	}

	public List<String> getLinkedPage() {
		return LinkedPage;
	}

	public List<String> getThemeLabel() {
		return ThemeLabel;
	}

	public String getAuthor() {
		return Author;
	}

}
