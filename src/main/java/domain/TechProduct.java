package domain;

import org.apache.solr.client.solrj.beans.Field;

public class TechProduct {
	

	public TechProduct() {
		super();
	}
	public TechProduct(String id, String name, String cone) {
		super();
		this.id = id;
		this.name = name;
		this.cone = cone;
	}
	@Field
	public String id;
	@Field
	public String name;
	@Field
	public String cone;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCone() {
		return cone;
	}
	public void setCone(String cone) {
		this.cone = cone;
	}
	@Override
	public String toString() {
		return "TechProduct [id=" + id + ", name=" + name + ", cone=" + cone + "]";
	}
	
	
	
	

}
