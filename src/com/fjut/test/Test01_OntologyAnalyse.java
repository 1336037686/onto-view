package com.fjut.test;

import java.util.List;

import com.fjut.pojo.MyOntology;
import com.fjut.util.OWLUtil;

public class Test01_OntologyAnalyse {
	public static void main(String[] args) throws Exception {
		OWLUtil.loadOWLUtil("file:source/101/onto.rdf");
		List<MyOntology> result = OWLUtil.getOWLClasses();
		for (MyOntology myOntology : result) {
			System.out.println(myOntology);
		}

		System.out.println("============");
		MyOntology owlTree = OWLUtil.getOWLTree(result);
		print(owlTree, 0);
//		List<MyCellData> list = OWLUtil.parseRefalignFile("source/201/refalign.rdf");
//		for (MyCellData myCellData : list) {
//			System.out.println(myCellData);
//		}
	}
	
	public static void print(MyOntology m, int index) {
		String tab = "";
		for(int i = 0; i < index; i++) {
			tab += "\t";
		}
		System.out.println(tab + m.getId());
		if(m.getChilds() != null && m.getChilds().size() > 0) {
			for (MyOntology m2 : m.getChilds()) {
				int i = index + 1;
				print(m2, i);
			}
		}
	}
}
