import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import shibafu.shindan4j.ShindanList;
import shibafu.shindan4j.ShindanPage;
import shibafu.shindan4j.ShindanResult;
import shibafu.shindan4j.ShindanSummary;


public class Tester {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("モード? ");

		String mode = br.readLine();

		if (mode.equals("")) {
			System.out.print("診断URLを入力してください: ");

			String url = br.readLine();

			System.out.println("取得中...");

			//ページ取得
			ShindanPage page = ShindanPage.getShindanPage(url);

			//情報出力
			System.out.println("\nタイトル: " + page.getTitle());
			System.out.println("説明: " + page.getDescription());
			System.out.println("\n診断リンク: ");
			for (String s : page.getLinkedPage()) {
				System.out.println(s);
			}

			//名前入力
			System.out.print("診断したい名前を入れてください: ");
			String name = br.readLine();

			//結果取得
			ShindanResult result = page.requestResult(name);

			//結果出力
			System.out.println("\n========[診断結果]========\n\n" + result.getDisplayResult());
			System.out.println("\n\n--------[コピペ用データ]--------\n\n" + result.getShareResult());
		}
		else if (mode.equals("list")) {
			System.out.print("\nmodeを入力してください: ");

			String qmode = br.readLine();

			System.out.print("\nページ?: ");

			int qpage = Integer.valueOf(br.readLine());

			System.out.println("取得中...");

			//リスト取得
			ShindanList list = ShindanList.getShindanList(qmode, qpage);

			//情報出力
			System.out.println("\n========[検索結果]========\n\n");
			List<ShindanSummary> results = list.getResults();
			for (ShindanSummary r : results) {
				System.out.println("タイトル: " + r.getName());
				System.out.println("カウンター: " + r.getCounter());
				System.out.println("URL: " + r.getPageURL());
				if (r.getAuthor() != null)
					System.out.println("作者: " + r.getAuthor());
				if (r.getHashTag() != null)
					System.out.println("ハッシュタグ: " + r.getHashTag());
				if (r.getThemeLabel().size() > 0) {
					System.out.println("テーマ:");
					for (String tl : r.getThemeLabel())
						System.out.println(tl);
				}
				System.out.println("説明: " + r.getDescription());
				System.out.println("\n---------------------------\n");
			}
		}
	}

}
