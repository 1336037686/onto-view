package com.fjut.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义本体类
 * @author LGX
 *
 */
public class MyOntology {
	private String 				id;												//ID
	private String 				label;											//Label
	private String 				comment;										//Comment
	private Set<MyOntology> 	range = new HashSet<>();						//Range
	private Set<MyOntology> 	domain = new HashSet<>();						//Domain
	private List<MyOntology>	father = new ArrayList<MyOntology>();			//父类
	private List<MyOntology> 	childs = new ArrayList<MyOntology>();;			//子类
	private List<MyOntology> 	dataProperty = new ArrayList<MyOntology>();; 	//dataproperty
	
	public MyOntology() {
		
	}
	
	public MyOntology(String id) {
		this.id = id;
	}

	public MyOntology(String id, String label, String comment) {
		this.id = id;
		this.label = label;
		this.comment = comment;
	}
	
	public MyOntology(String id, String label, String comment, Set<MyOntology> range, Set<MyOntology> domain,
			List<MyOntology> childs, List<MyOntology> dataProperty) {
		this.id = id;
		this.label = label;
		this.comment = comment;
		this.range = range;
		this.domain = domain;
		this.childs = childs;
		this.dataProperty = dataProperty;
	}

	public MyOntology(String id, String label, String comment, Set<MyOntology> range, Set<MyOntology> domain) {
		this.id = id;
		this.label = label;
		this.comment = comment;
		this.range = range;
		this.domain = domain;
	}
	
	public MyOntology(String id, String label, String comment, Set<MyOntology> range, Set<MyOntology> domain,
			List<MyOntology> father, List<MyOntology> childs, List<MyOntology> dataProperty) {
		super();
		this.id = id;
		this.label = label;
		this.comment = comment;
		this.range = range;
		this.domain = domain;
		this.father = father;
		this.childs = childs;
		this.dataProperty = dataProperty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<MyOntology> getRange() {
		return range;
	}

	public void setRange(Set<MyOntology> range) {
		this.range = range;
	}

	public Set<MyOntology> getDomain() {
		return domain;
	}

	public void setDomain(Set<MyOntology> domain) {
		this.domain = domain;
	}

	public List<MyOntology> getChilds() {
		return childs;
	}

	public void setChilds(List<MyOntology> childs) {
		this.childs = childs;
	}

	public List<MyOntology> getDataProperty() {
		return dataProperty;
	}

	public void setDataProperty(List<MyOntology> dataProperty) {
		this.dataProperty = dataProperty;
	}

	public List<MyOntology> getFather() {
		return father;
	}

	public void setFather(List<MyOntology> father) {
		this.father = father;
	}

	@Override
	public String toString() {
		return "MyOntology [id=" + id + ", label=" + label + ", comment=" + comment + ", range=" + range + ", domain="
				+ domain + ", father=" + father + ", childs=" + childs + ", dataProperty=" + dataProperty + "]";
	}

	/**
	 * 是否有range
	 * @return
	 */
	public boolean hasRange() {
		if(this.range == null) return false;
		return this.range.size() > 0;
	}
	
	/**
	 * 是否有domain
	 */
	public boolean hasDomain() {
		if(this.domain == null) return false;
		return this.domain.size() > 0;
	}
	
	/**
	 * 是否有label
	 * @return boolean
	 */
	public boolean hasLabel() {
		return this.label != null && !"".equals(this.label.trim());
	}
	
	/**
	 * 是否有comment
	 * @return
	 */
	public boolean hasComment() {
		return this.comment != null && !"".equals(this.comment.trim());
	}
	
}
