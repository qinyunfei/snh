package html;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @Description: 获取网页内容的几种方式
 * @author: Qin YunFei
 * @date: 2017年12月11日 下午4:06:31
 * @version V1.0
 */
public class Test1 {

	/**
	 * 
	 * 使用java核心类库URL获取响应内容 InetAddress ip操作
	 */
	@Test
	public void name() throws UnsupportedEncodingException, IOException {
		URL url = new URL("https://www.baidu.com/"); // 生成传入的URL的对象
		String content = IOUtils.toString(url.openStream(), "utf-8");
		System.out.println(content);
	}
	/**
	 * 
	 * 和方法一一样
	 */
	@Test
	public void name2() throws Exception {
		URL url = new URL("https://www.baidu.com/");
		URLConnection uc = url.openConnection();
		//添加请求头
		uc.addRequestProperty("User-Agent",
				"Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
		String content = IOUtils.toString(url.openStream(), "utf-8");
		System.out.println(content);
	}
	
	/**
	 * 使用第3方工具包  html解释工具包Jsoup //还在试验中
	 * @throws IOException 
	 */
	@Test
	public void name3() throws IOException {
		Document document = Jsoup.connect("https://www.baidu.com").get();
	
	}
	
	/**
	 * 
	 */
	@Test
	public void name4() throws ClientProtocolException, IOException, URISyntaxException {

		// 设置http请求对象 所有HTTP请求都有一个请求行，它包含一个方法名，一个请求URI和一个HTTP协议版本。
		URI uri = new URIBuilder().setScheme("https").setHost("www.baidu.com").build();

		String string = Request.Get(uri).connectTimeout(1000).socketTimeout(1000).execute()
				.handleResponse(new ResponseHandler<String>() {
					@Override
					public String handleResponse(HttpResponse response)
							throws ClientProtocolException, IOException {
						StatusLine statusLine = response.getStatusLine();
						HttpEntity entity = response.getEntity();
						if (statusLine.getStatusCode() > 300) {
							throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
						}
						if (entity == null) {
							throw new ClientProtocolException("Response contains no content");
						}
						System.out.println("sasa");
						return EntityUtils.toString(entity,"utf-8");
					}
				});

		System.out.println(string);
	}
}
