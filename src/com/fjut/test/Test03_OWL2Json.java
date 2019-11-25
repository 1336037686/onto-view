package com.fjut.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fjut.pojo.MyCellData;
import com.fjut.pojo.MyOntology;
import com.fjut.util.OWLUtil;

public class Test03_OWL2Json {
	
	static class Cell {
		 String entity1;
		 String entity2;
		 double measure;
		 String relation;
		  
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

		public Cell() {
		
		}

		public Cell(String entity1, String entity2, double measure, String relation) {
			this.entity1 = entity1;
			this.entity2 = entity2;
			this.measure = measure;
			this.relation = relation;
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		OWLUtil.loadOWLUtil("file:source/202-4/onto.rdf");
		List<MyOntology> result = OWLUtil.getOWLClasses();
		MyOntology owlTree = OWLUtil.getOWLTree(result);
		owlTree.setId(owlTree.getId().toUpperCase());
		id2Up(owlTree);
		String json = JSON.toJSONString(owlTree);
		System.out.println(json);

		List<MyCellData> list = OWLUtil.parseRefalignFile("E:\\DevelopProjects\\Java_Project\\eclipse_project\\framework-learn\\ontology-view\\source\\201\\refalign.rdf");
		
		for (MyCellData myCellData : list) {
			System.out.println(myCellData);
		}
		
		List<Cell> cl = new ArrayList<Test03_OWL2Json.Cell>();
		for(int i = 0; i < list.size(); i++) {
			MyCellData d = list.get(i);
			cl.add(new Cell(d.getEntity1Id().toUpperCase(), d.getEntity2Id().toUpperCase(), 
					d.getMeasure(), d.getRelation()));
		}
		json = JSON.toJSONString(cl);
		System.out.println(json);
		
	}
	
	public static void id2Up(MyOntology m) {
		//child
		if(m.getChilds() != null && m.getChilds().size() > 0) {
			for (MyOntology m2 : m.getChilds()) {
				m2.setId(m2.getId().toUpperCase());
				id2Up(m2);
			}
		}
		//father
		if(m.getFather() != null && m.getFather().size() > 0) {
			for (MyOntology m2 : m.getFather()) {
				m2.setId(m2.getId().toUpperCase());
				id2Up(m2);
			}
		}
		//domain
		if(m.getDomain() != null && m.getDomain().size() > 0) {
			for (MyOntology m2 : m.getDomain()) {
				m2.setId(m2.getId().toUpperCase());
				id2Up(m2);
			}
		}
		//range
		if(m.getRange() != null && m.getRange().size() > 0) {
			for (MyOntology m2 : m.getRange()) {
				m2.setId(m2.getId().toUpperCase());
				id2Up(m2);
			}
		}
		//date
		if(m.getDataProperty() != null && m.getDataProperty().size() > 0) {
			for (MyOntology m2 : m.getDataProperty()) {
				m2.setId(m2.getId().toUpperCase());
				id2Up(m2);
			}
		}
	}
}
