package solrj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.TechProduct;

public class Test1 {

	// solrj有多种类型的与solr链接的客户端 httpSolrClient面向以查询为中心的工作负载。直接与单个Solr节点通信。
	private HttpSolrClient httpSolrClient;

	@Before
	public void befo() {
		String solrUrl = "http://10.211.55.19:8983/solr/mycollection";
		httpSolrClient = new HttpSolrClient.Builder(solrUrl).withConnectionTimeout(10000)// 通信的连接超时
				.withSocketTimeout(60000)// 通信的读取超时
				.build();
	}

	@After
	public void af() throws IOException {
		// 关闭链接
		httpSolrClient.close();
	}
	//添加和修改文档是同一操作 

	// 添加文档 如果solrUrl核心的话 不用在指定核心
	@Test
	public void name() throws SolrServerException, IOException {
		// TODO Auto-generated constructor stub

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "1");
		doc.addField("name", "南京");
		doc.addField("coen", "南京是一个文化的城市南京，简称宁，是江苏省会，");

		httpSolrClient.add(doc);
		// Indexed documents must be committed
		httpSolrClient.commit();

	}

	// 添加文档 如果solrUrl没有指定核心的话 需要指定核心
	@Test
	public void name2() throws SolrServerException, IOException {
		// TODO Auto-generated constructor stub

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "1");
		doc.addField("name", "南京");
		doc.addField("coen", "南京是一个文化的城市南京，简称宁，是江苏省会，");

		httpSolrClient.add("mycollection", doc);
		// Indexed documents must be committed
		httpSolrClient.commit("mycollection");

	}
	
	
	// 添加文档 以对象的方式 
	@Test
	public void name6() throws IOException, SolrServerException {
		//我自己的javabean
		TechProduct kindle = new TechProduct("2", "北京", "北京的雾霾非常的严重");
	    httpSolrClient.addBean(kindle);

		httpSolrClient.commit();
	}

	// 查询文档
	@Test
	public void name3() throws SolrServerException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		// q 查询字符串，如果查询所有*:*
		map.put("q", "*:*");
		// fl 查询哪些字段域出来（就是显示结果），不写的话，就查询全部
		map.put("fl", "id,name");
		// 查询参数有很多这里不一一列出来
		// 创建查询参数对象
		MapSolrParams mapSolrParams = new MapSolrParams(map);
		// 执行查询获取查询响应
		QueryResponse response = httpSolrClient.query(mapSolrParams);
		// 获取查询到的doc集合
		SolrDocumentList documents = response.getResults();
		System.out.println("查询到" + documents.getNumFound() + "个结果");
		for (SolrDocument solrDocument : documents) {
			System.out.println("id:" + solrDocument.get("id"));
			System.out.println("name:" + solrDocument.get("name"));
		}
	}
	
	//查询文档 以对象的方式
	@Test
	public void name7() throws SolrServerException, IOException {
		
		SolrQuery query = new SolrQuery("*:*");
		query.addField("id");
		query.addField("name");
		query.addField("coen");
		
		//query.set("fl", "id,name,coen"); 和上面3的功能是一样一样滴

		final QueryResponse response = httpSolrClient.query(query);
		List<TechProduct> products = response.getBeans(TechProduct.class);
		System.out.println(products);
	}


	// 查询文档简化版 SolrParams有一个SolrQuery子类，它提供了一些方便的方法，大大简化了查询的创建
	@Test
	public void name4() throws SolrServerException, IOException {
		// 构建查询条件
		SolrQuery query = new SolrQuery();
		// q :查询字符串
		query.set("q", "tit:南京"); //query.setQuery("tit:南京");
		// fl 查询哪些字段域出来（就是显示结果），不写的话，就查询全部
		query.set("fl", "id,name,coen"); //query.setFields("")
		// start row 分页信息，与mysql的limit的两个参数一致效果
		query.setStart(0);
		query.setRows(10);
		// fq 过滤条件 例如将查询结果中 id=2的过滤掉
		// query.set("fq", "id:2");
		// sort 排序，请注意，如果一个字段没有被索引，那么它是无法排序的
		// desc降序 asc 生序
		query.set("sort", "id desc"); //query.setSort("id",ORDER.desc);
		// df 默认搜索的域
		query.set("df", "coen");
		// ======高亮设置===
		// 开启高亮
		query.setHighlight(true);
		// 高亮域 这里必须和 fl指定的域相同
		query.addHighlightField("id,name,coen");
		// 前缀简写
		query.setHighlightSimplePre("<span style='color:red'>"); // query.set("hl.simple.pre", "<span style='color:red'>");
		// 后缀简写
		query.setHighlightSimplePost("</span>"); // query.set("hl.simple.post", "</span>");
		

		// 执行查询获取查询响应
		QueryResponse response = httpSolrClient.query(query);
		// 获取查询到的doc集合
		SolrDocumentList documents = response.getResults();
		System.out.println("查询到" + documents.getNumFound() + "个结果");
		System.out.println(">>>>>>>>>>>>普通显示>>>>>>>>>>>>>>>>>>>");
		// 遍历doc
		for (SolrDocument solrDocument : documents) {
			System.out.println("id:" + solrDocument.get("id"));
			System.out.println("name:" + solrDocument.get("name"));
		}

		System.out.println(">>>>>>>>>>>>高亮显示>>>>>>>>>>>>>>>>>>>");
		// 获取高亮 高亮的结果集比普通的要复杂一点 如下所示
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : documents) {
			System.out.println("id:" + solrDocument.get("id"));
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> list = map.get("name");
			if (list != null && list.size() > 0) {
				System.out.println("name:" + list.get(0));
			}
			List<String> list2 = map.get("coen");
			if (list != null && list2.size() > 0) {
				System.out.println("coen:" + list2.get(0));
			}
		}

	}

	//删除文档
	@Test
	public void name5() throws SolrServerException, IOException {
		//1.删除一个
//		httpSolrClient.deleteById("1");
//		
//		
//		 //2.删除多个
//        List<String> ids = new ArrayList<>();
//        ids.add("1");
//        ids.add("2");
//        httpSolrClient.deleteById(ids);
//        
//        
//        
//        //3.根据查询条件删除数据,这里的条件只能有一个，不能以逗号相隔
//        httpSolrClient.deleteByQuery("id:zxj1");
//        
        //4.删除全部，删除不可恢复
        httpSolrClient.deleteByQuery("*:*");
		httpSolrClient.commit();
	}
}
