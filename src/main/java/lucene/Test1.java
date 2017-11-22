package lucene;



import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Test1 {
	//官方文档
	@Test
	public void name1() throws Exception{
			//创建词法分析器
		   Analyzer analyzer = new StandardAnalyzer();
		    // 将索引存储在内存中::
		   //创建目录
		    Directory directory = new RAMDirectory();
		    // 要在磁盘上存储索引，可以使用以下的索引:
		    //Directory directory = FSDirectory.open("/tmp/testindex");
		    IndexWriterConfig config = new IndexWriterConfig(analyzer);
		    IndexWriter iwriter = new IndexWriter(directory, config);
		    Document doc = new Document();
		    doc.add(new Field("fieldname", "This is the text to be indexed.", TextField.TYPE_STORED));
		    iwriter.addDocument(doc);
		    iwriter.close();
		    
		    // 现在搜索的索引:
		    //创建目录解析器
		    DirectoryReader ireader = DirectoryReader.open(directory);
		    //创建索引解析器
		    IndexSearcher isearcher = new IndexSearcher(ireader);
		    // 创建一个查询解析器
		    QueryParser parser = new QueryParser("fieldname", analyzer);
		    //获取一个查询对象
		    Query query = parser.parse("text");
		    ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;

		    // Iterate through the results:
		    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      System.out.println(hitDoc.get("fieldname"));
		    }
		    ireader.close();
		    directory.close();
	}
	
	//自己写的
	@SuppressWarnings("resource")
	@Test
	public void name2() throws IOException, ParseException, QueryNodeException {
		//写入索引
		
		//创建分词器
		Analyzer analyzer=new StandardAnalyzer();
		//创建目录 基于内存
		Directory directory = new RAMDirectory();
		//创建索引写入器
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
		//创建文档
		Document document = new Document();
		//TextField.TYPE_STORED:该索引存储
		//TextField.TYPE_NOT_STORED:该索引不存储
		document.add(new Field("name", "xiaohei", TextField.TYPE_STORED));
		document.add(new Field("msg", "This is the text to be indexed", TextField.TYPE_STORED));
		//使用索引写入器写入索引
		indexWriter.addDocument(document);
		//关闭索引写入器
		indexWriter.close();
		//检索索引
		//读取目录创建索引搜索器
		DirectoryReader directoryReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
		//创建查询解析器
		QueryParser queryParser = new QueryParser("msg", analyzer);
		//new MultiFieldQueryParser(fields, analyzer)
		//获取查询对象 查询对象有多种获取方式 查询解析器只是其中之一
		Query query = queryParser.parse("indexed");
		//Query query = new TermQuery(new Term("msg", "indexed"));
		//Query query = QueryParserUtil.parse(new String[] {"indexed"}, new String[] {"msg"}, analyzer);
		
		//TopDocs 指向相匹配的搜索条件的前N个搜索结果
		TopDocs search = indexSearcher.search(query, 100);
		//获取doc集合
		ScoreDoc[] scoreDocs = search.scoreDocs;
		
		for (ScoreDoc scoreDoc : scoreDocs) {
			//获取文档id
			int docID = scoreDoc.doc;
			Document doc = indexSearcher.doc(docID);
			System.out.println(doc.get("name"));
		}
		
	}
	
	
	
	
	 //测试分词器 分词效果
	 @Test
	 public void name() throws Exception {
		//创建分词器
		Analyzer analyzer=new IKAnalyzer();
		
		//获取数据流
		TokenStream tokenStream = analyzer.tokenStream("wcnm", "今天好无聊 我看了一天的梦幻诛仙");
		//使指针回答流最开始的位置
		tokenStream.reset();
		//获取分词数据对象
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		//获取偏移量对象
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		while (tokenStream.incrementToken()) {
			System.out.println(charTermAttribute.toString()+"["+offsetAttribute.startOffset()+","+offsetAttribute.endOffset()+"]");
		}
		analyzer.close();
	}
}
