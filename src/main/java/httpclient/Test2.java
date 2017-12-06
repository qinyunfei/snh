package httpclient;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @Description: httpclent 4.2以上提供了一套流利的api方便调用
 * @author: Qin YunFei
 * @date: 2017年12月6日 下午4:26:23
 * @version V1.0
 */
public class Test2 {

	@Test
	public void name() throws ClientProtocolException, IOException, URISyntaxException {

		// 设置http请求对象 所有HTTP请求都有一个请求行，它包含一个方法名，一个请求URI和一个HTTP协议版本。
		URI uri = new URIBuilder().setScheme("http").setHost("jisutqybmf.market.alicloudapi.com").setPort(80)
				.setPath("/weather/query").setParameter("city", "深圳").build();

		Map<String, String> map = Request.Get(uri).connectTimeout(1000).socketTimeout(1000)
				.addHeader("Authorization", "APPCODE 50c0e17fb2284312a8a4d44aa33d75fd").execute()
				.handleResponse(new ResponseHandler<Map<String, String>>() {
					@Override
					public Map<String, String> handleResponse(HttpResponse response)
							throws ClientProtocolException, IOException {
						StatusLine statusLine = response.getStatusLine();
						HttpEntity entity = response.getEntity();
						if (statusLine.getStatusCode() > 300) {
							throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
						}
						if (entity == null) {
							throw new ClientProtocolException("Response contains no content");
						}
						return JSON.parseObject(entity.getContent(), Map.class);
					}
				});

		System.out.println(map);
	}
	//还可以这样设置post表单
	@Test
	public void name2() throws ClientProtocolException, IOException {
		Request.Post("http://somehost/some-form")
        .bodyForm(Form.form().add("username", "vip").add("password", "secret").build())
        .execute().saveContent(new File("result.dump"));
	}

}
