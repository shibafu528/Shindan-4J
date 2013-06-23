package shibafu.unofficialshindanapi;

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

	public ShindanResult(ShindanPage page, String name, String display, String share) {
		this.Page = page;
		this.Name = name;
		this.DisplayResult = display;
		this.ShareResult = share;
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
}
