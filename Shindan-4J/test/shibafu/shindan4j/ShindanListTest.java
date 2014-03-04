package shibafu.shindan4j;

import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by shibafu on 14/03/04.
 */
public class ShindanListTest {
    @Test
    public void testGetList() throws Exception {
        List<ShindanSummary> summaries = ShindanList.getList(ShindanList.MODE_BEST, 1);
        Assert.assertNotNull("一覧を取得できていない", summaries);
        for (ShindanSummary summary : summaries) {
            System.out.println(summary);
        }
    }
}
