package com.fjut.pojo;

/**
 * 节点坐标+数据
 * @author LGX
 *
 */
public class CellPoint {
	/**
	 * X坐标
	 */
	public int X;
	/**
	 * Y坐标
	 */
	public int Y;
	/**
	 * 该节点ID值
	 */
	public String id;
	/**
	 * 该节点数据
	 */
	public MyOntology data;
	
	public CellPoint() {
	
	}
	
	public CellPoint(int x, int y) {
		X = x;
		Y = y;
	}
	
	public CellPoint(int x, int y, MyOntology data) {
		X = x;
		Y = y;
		this.data = data;
	}
	
	public CellPoint(int x, int y, String id) {
		X = x;
		Y = y;
		this.id = id;
	}
	
	public CellPoint(int x, int y, String id, MyOntology data) {
		X = x;
		Y = y;
		this.id = id;
		this.data = data;
	}

	@Override
	public String toString() {
		return "CellPoint [X=" + X + ", Y=" + Y + ", id=" + id + "]";
	}
	
}
