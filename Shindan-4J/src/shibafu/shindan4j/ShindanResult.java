package shibafu.shindan4j;

import java.util.ArrayList;
import java.util.List;

import shibafu.common.StringHelper;
import shibafu.common.TweetImageUrl;

/**
 * 診断メーカーでの診断結果が詰まってる
 * @author Shibafu
 *
 */
public class ShindanResult {
	//行った診断のページ
	protected ShindanPage Page;
	//診断に使用した名前
	protected String Name;
	//診断結果(ディスプレイ用)
	protected String DisplayResult;
	//診断結果(共有用)
	protected String ShareResult;
	//画像診断の結果URL
	protected List<LinkImage> ResultImageUrl = new ArrayList<LinkImage>();

	public ShindanResult(ShindanPage page, String name, String display, String share) {
		this.Page = page;
		this.Name = name;
		this.DisplayResult = display;
		this.ShareResult = share;
		extractImageUrl();
	}

	public ShindanPage getPage() {
		return Page;
	}

	public String getName() {
		return Name;
	}

	public String getDisplayResult() {
		return DisplayResult;
	}

	public String getShareResult() {
		return ShareResult;
	}
	
	public List<LinkImage> getResultImageUrl() {
		return ResultImageUrl;
	}

	private void extractImageUrl() {
		//診断結果から全てのURLを抽出
		String[] urls = StringHelper.extractURL(DisplayResult);
		//それらを画像URLであるか総当たり判定
		for (String url : urls) {
			String fullImg = TweetImageUrl.getFullImageUrl(url);
			if (fullImg != null) {
				//画像URLが取得出来たらリストに追加
				ResultImageUrl.add(new LinkImage(url, fullImg));
			}
		}
	}
	
	public static class LinkImage {
		public String Url;
		public String FullImageUrl;
		
		public LinkImage(String url, String fullImageUrl) {
			Url = url;
			FullImageUrl = fullImageUrl;
		}
	}
}
