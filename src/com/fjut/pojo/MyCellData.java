package com.fjut.pojo;

/**
 * 标准文件解析实体类 Refalign
 * @author LGX
 *
 */
public class MyCellData {
	/**
	 * 实体1 链接
	 */
	private String entity1;
	
	/**
	 * 实体2 链接
	 */
	private String entity2;
	
	/**
	 * 实体1 ID
	 */
	private String entity1Id;
	
	/**
	 * 实体2 ID
	 */
	private String entity2Id;
	
	/**
	 * 度量值
	 */
	private double measure;
	
	/**
	 * 
	 */
	private String relation;
	
	public MyCellData(String entity1, String entity2, double measure, String relation) {
		this.entity1 = entity1;
		this.entity2 = entity2;
		this.entity1Id = entity1.substring(entity1.indexOf("#") + 1);
		this.entity2Id = entity2.substring(entity2.indexOf("#") + 1);
		this.measure = measure;
		this.relation = relation;
	}
	
	public MyCellData() {

	}
	public String getEntity1() {
		return entity1;
	}
	public void setEntity1(String entity1) {
		this.entity1 = entity1;
	}
	public String getEntity2() {
		return entity2;
	}
	public void setEntity2(String entity2) {
		this.entity2 = entity2;
	}
	public double getMeasure() {
		return measure;
	}
	public void setMeasure(double measure) {
		this.measure = measure;
	}
	public String getRelation() {
		return relation;
	}
	
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public String getEntity1Id() {
		return entity1Id;
	}

	public void setEntity1Id(String entity1Id) {
		this.entity1Id = entity1Id;
	}

	public String getEntity2Id() {
		return entity2Id;
	}

	public void setEntity2Id(String entity2Id) {
		this.entity2Id = entity2Id;
	}

	@Override
	public String toString() {
		return "MyCellData [entity1=" + entity1 + ", entity2=" + entity2 + ", entity1Id=" + entity1Id + ", entity2Id="
				+ entity2Id + ", measure=" + measure + ", relation=" + relation + "]";
	}
}
