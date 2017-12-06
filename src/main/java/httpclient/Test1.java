package httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @Description: 基础
 * @author: Qin YunFei
 * @date: 2017年12月6日 下午4:07:49
 * @version V1.0
 */
public class Test1 {

	@Test
	public void name() throws Exception, IOException {
		// 创建HttpClient对象 HttpClient需要向目标服务器传送请求返回相应的响应对象，或者如果执行失败则抛出异常。
		CloseableHttpClient client = HttpClients.createDefault();

		// 设置一个主机地址
		HttpHost host = new HttpHost("jisutqybmf.market.alicloudapi.com", 80, "http");

		// 设置http请求对象 所有HTTP请求都有一个请求行，它包含一个方法名，一个请求URI和一个HTTP协议版本。
		HttpGet httpGet = new HttpGet("/weather/query?city=" + URLEncoder.encode("深圳", "utf-8"));

		String str = "APPCODE " + "50c0e17fb2284312a8a4d44aa33d75fd";
		// 设置请求对象的请求头
		httpGet.addHeader("Authorization", str);
		// HttpClient需要向目标服务器传送请求并返回相应的响应对象，或者如果执行失败则抛出异常。
		CloseableHttpResponse execute = client.execute(host, httpGet);
		// 获取响应体
		HttpEntity entity = execute.getEntity();
		// 使用EntityUtils工具类消费HttpEntity entity.getContent()其实是一个流
		String string = EntityUtils.toString(entity);

		System.out.println(string);
		client.close();

	}

	
	@Test
	public void name1() throws Exception, IOException {
		// 创建HttpClient对象 HttpClient需要向目标服务器传送请求返回相应的响应对象，或者如果执行失败则抛出异常。
		CloseableHttpClient client = HttpClients.createDefault();
		HttpClientContext context = HttpClientContext.create();

		// 设置http请求对象 所有HTTP请求都有一个请求行，它包含一个方法名，一个请求URI和一个HTTP协议版本。
		URI uri = new URIBuilder().setScheme("http").setHost("jisutqybmf.market.alicloudapi.com").setPort(80)
				.setPath("/weather/query").setParameter("city", "深圳").build();
		HttpGet httpget = new HttpGet(uri);
		System.out.println(httpget.getURI());
		// 设置请求对象的请求头
		httpget.addHeader("Authorization", "APPCODE 50c0e17fb2284312a8a4d44aa33d75fd");
		// HttpClient需要向目标服务器传送请求并返回相应的响应对象，或者如果执行失败则抛出异常。
		CloseableHttpResponse execute = client.execute(httpget, context);
		// 获取响应体
		HttpEntity entity = execute.getEntity();
		// 使用EntityUtils工具类消费HttpEntity entity.getContent()其实是一个流
		String string = EntityUtils.toString(entity);

		// 上下文使用扩展
		HttpHost target = context.getTargetHost();
		List<URI> redirectLocations = context.getRedirectLocations();
		URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
		System.out.println("Final HTTP location: " + location.toASCIIString());

		System.out.println(string);
		//释放链接
		client.close();
	}

	/*
	 * 处理响应的最简单和最方便的方法是使用ResponseHandler包含该handleResponse(HttpResponse
	 * response)方法的接口。 这种方法完全免除了用户不必担心连接管理的问题。当使用a时 ResponseHandler，
	 * 无论请求执行是否成功或者引起异常，HttpClient都会自动处理确保连接释放回连接管理器。
	 * 最佳使用
	 */
	@Test
	public void name11() throws ClientProtocolException, IOException, URISyntaxException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 设置http请求对象 所有HTTP请求都有一个请求行，它包含一个方法名，一个请求URI和一个HTTP协议版本。
		URI uri = new URIBuilder().setScheme("http").setHost("jisutqybmf.market.alicloudapi.com").setPort(80)
				.setPath("/weather/query").setParameter("city", "深圳").build();
		HttpGet httpget = new HttpGet(uri);
		
		//设置请求配置
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000)  //创建链接的最长时间
		.setConnectionRequestTimeout(500)//设置从连接池中取得链接的最长时间
		.setSocketTimeout(10*1000)  //数据传输的最长时间
		.setStaleConnectionCheckEnabled(true)
		.build();
		httpget.setConfig(requestConfig);
		
		httpget.addHeader("Authorization", "APPCODE 50c0e17fb2284312a8a4d44aa33d75fd");
		ResponseHandler<Map<String, Object>> rh = new ResponseHandler<Map<String, Object>>() {

			@SuppressWarnings("deprecation")
			@Override
			public Map<String, Object> handleResponse(final HttpResponse response) throws IOException {
				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
				}
				if (entity == null) {
					throw new ClientProtocolException("Response contains no content");
				}
				return JSON.parseObject(entity.getContent(), Map.class);
			}
		};
		Map<String, Object> map = httpclient.execute(httpget, rh);
		System.out.println(map);
	}

	// 由初始请求设置的请求配置将保留在执行上下文中，并传播到共享相同上下文的连续请求
	@Test
	public void name12() throws ClientProtocolException, IOException {
		// 上下文
		AtomicInteger count = new AtomicInteger(1);
		HttpClientContext context = HttpClientContext.create();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 请求配置
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).build();
	
		HttpGet httpget1 = new HttpGet("http://localhost/1");
		// 将配置设置到第一次请求中
		httpget1.setConfig(requestConfig);
		// 初始请求设置的请求配置将保留在执行上下文中，并传播到共享相同上下文的连续请求
		CloseableHttpResponse response1 = httpclient.execute(httpget1, context);
		try {
			HttpEntity entity1 = response1.getEntity();
		} finally {
			response1.close();
		}
		HttpGet httpget2 = new HttpGet("http://localhost/2");
		// 使用相同的上下文
		CloseableHttpResponse response2 = httpclient.execute(httpget2, context);
		try {
			HttpEntity entity2 = response2.getEntity();
		} finally {
			response2.close();
		}
	}

	// HTTP响应是服务器收到并解释请求消息后发送回客户端的消息。该消息的第一行由协议版本和数字状态代码及其相关的文本短语组成。
	@Test
	public void name3() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		// 获取协议版本
		System.out.println(response.getProtocolVersion());
		// 获取响应状态码
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getStatusLine().getReasonPhrase());
		// 获取响应信息的字符串
		System.out.println(response.getStatusLine().toString());
	}

	// 设置响应头
	@Test
	public void name4() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		// 设置响应头
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

		// 获取响应头
		Header h1 = response.getFirstHeader("Set-Cookie");
		System.out.println(h1);
		Header h2 = response.getLastHeader("Set-Cookie");
		System.out.println(h2);
		Header[] hs = response.getHeaders("Set-Cookie");
		System.out.println(hs.length);
	}

	@Test
	public void name5() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

		HeaderIterator it = response.headerIterator("Set-Cookie");

		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	// 便捷的方法来将HTTP消息解析为单独的头元素。
	@Test
	public void name6() {
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

		HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Set-Cookie"));

		while (it.hasNext()) {
			HeaderElement elem = it.nextElement();
			System.out.println(elem.getName() + " = " + elem.getValue());
			NameValuePair[] params = elem.getParameters();
			for (int i = 0; i < params.length; i++) {
				System.out.println(" " + params[i]);
			}
		}
	}

	//
	@Test
	public void name7() throws ParseException, IOException {
		StringEntity myEntity = new StringEntity("important message", ContentType.create("text/plain", "UTF-8"));

		System.out.println(myEntity.getContentType());
		System.out.println(myEntity.getContentLength());
		System.out.println(EntityUtils.toString(myEntity));
		System.out.println(EntityUtils.toByteArray(myEntity).length);
	}

	@Test
	public void name8() {
		File file = new File("somefile.txt");
		FileEntity entity = new FileEntity(file, ContentType.create("text/plain", "UTF-8"));

		HttpPost httppost = new HttpPost("http://localhost/action.do");
		httppost.setEntity(entity);
	}

	// 模拟表单
	@Test
	public void name9() {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("param1", "value1"));
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		HttpPost httppost = new HttpPost("http://localhost/handler.do");
		httppost.setEntity(entity);
	}

	//
	@Test
	public void name10() {
		StringEntity entity = new StringEntity("important message", ContentType.create("plain/text", Consts.UTF_8));
		entity.setChunked(true);
		HttpPost httppost = new HttpPost("http://localhost/acrtion.do");
		httppost.setEntity(entity);
	}
	
	//链接管理器
	@Test
	public void name13() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// 将最大连接总数增加到200
		cm.setMaxTotal(200);
		// 将每个路由的默认最大连接数增加到20
		cm.setDefaultMaxPerRoute(20);
		// 增加本地主机的最大连接:80到50
		HttpHost localhost = new HttpHost("locahost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);

		CloseableHttpClient httpClient = HttpClients.custom()
		        .setConnectionManager(cm)
		        .build();
	}

}
