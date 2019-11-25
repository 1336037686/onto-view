package com.fjut.test;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.fjut.pojo.AnnotationType;
import com.fjut.util.JenaUtil;

public class Test02_OWLAnalyse {
	
	private static OntDocumentManager ontDocMgr = new OntDocumentManager();
	private static OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
	
	public static void main(String[] args) {
		OntModel ontModel = JenaUtil.createOntologyModelByOWLFile("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#","file:source/101/onto.rdf");
		ExtendedIterator<OntClass> listClasses = ontModel.listClasses();
		while(listClasses.hasNext()) {
			OntClass ontClass = listClasses.next();
			try {
				if(ontClass.getURI() != null) {				
					System.out.println(ontClass.getURI());
					AnnotationProperty annotationProperty = JenaUtil.getAnnotationProperty(ontModel, ontClass.getURI());
					System.out.println("\t" + JenaUtil.getProperty(annotationProperty, AnnotationType.ID));
					System.out.println("\t" + JenaUtil.getProperty(annotationProperty, AnnotationType.LABEL));
					System.out.println("\t" + JenaUtil.getProperty(annotationProperty, AnnotationType.COMMENT));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
