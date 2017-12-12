package html;

import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdongMain {

	// log4j的是使用，不会的请看之前写的文章
	static final Log logger = LogFactory.getLog(JdongMain.class);

	public static void main(String[] args) throws Exception {
		//及其简单的爬虫小小例子
		URL url = new URL("http://search.jd.com/Search?keyword=Python&enc=utf-8&book=y&wq=Python&pvid=33xo9lni.p4a1qb"); // 生成传入的URL的对象
		String content = IOUtils.toString(url.openStream(), "utf-8");
		// 抓取的数据
		List<JdModel> bookdatas = JdParse.getData(content);
		// 循环输出抓取的数据
		for (JdModel jd : bookdatas) {
			logger.info("bookID:" + jd.getBookID() + "\t" + "bookPrice:" + jd.getBookPrice() + "\t" + "bookName:"
					+ jd.getBookName());
		}
		System.out.println(bookdatas);

	}

}
