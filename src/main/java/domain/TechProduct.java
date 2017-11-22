package domain;

import org.apache.solr.client.solrj.beans.Field;

public class TechProduct {
	

	public TechProduct() {
		super();
	}
	public TechProduct(String id, String name, String coen) {
		super();
		this.id = id;
		this.name = name;
		this.coen = coen;
	}
	@Field
	public String id;
	@Field
	public String name;
	@Field("coen") //如果字段的名字和字段域里不同可以这样指定字段域的名字
	public String coen;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String coen) {
		this.name = name;
	}
	public String getCoen() {
		return coen;
	}
	public void setCoen(String coen) {
		this.coen = coen;
	}
	@Override
	public String toString() {
		return "TechProduct [id=" + id + ", name=" + name + ", cone=" + coen + "]";
	}
	
	
	
	

}
