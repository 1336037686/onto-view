package com.fjut.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.fjut.pojo.AnnotationType;
import com.fjut.pojo.MyOntology;

/**
 * Jena工具类
 * @author LGX
 *
 */
@SuppressWarnings("all")
public class JenaUtil {
	
	private static OntDocumentManager ontDocMgr = new OntDocumentManager();
	private static OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM);
	
	/**
	 * 读取文件构建本体
	 * @param source
	 * source = "http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#";
	 * @param owlFileUri owl文件存放URI
	 * ontoFileUri = "file:examples/benchmarks/onto.rdf"
	 * @return
	 */
	public static OntModel createOntologyModelByOWLFile(String source, String owlFileUri) {
		ontDocMgr.addAltEntry(source, owlFileUri);		
		ontModelSpec.setDocumentManager(ontDocMgr);
		OntModel baseOnt = ModelFactory.createOntologyModel(ontModelSpec);
		baseOnt.read(source, "RDF/XML");
		return baseOnt;
	}
	
	/**
	 * 获取个体属性
	 * 根据 http://oaei.ontologymatching.org/2011/benchmarks/201/onto.rdf#sqdnbs 获取相应OWL数据<br>
	 * 例如ID，label，Comment
	 * @param ontModel
	 * @param findURI
	 * @return
	 */
	public static AnnotationProperty getAnnotationProperty(OntModel ontModel, String findURI) {
		return ontModel.createAnnotationProperty(findURI);
	}
	
	
	/**
	 * 根据Type获取相应属性 ID,Label,Comment
	 * @param node
	 * @param annotationType 
	 * @return
	 */
	public static String getProperty(AnnotationProperty node, String annotationType) {
		if(annotationType.equals(AnnotationType.ID)) {
			return node.getLocalName();
		}
		if(annotationType.equals(AnnotationType.LABEL)) {
			return node.getLabel(null);
		}
		
		if(annotationType.equals(AnnotationType.COMMENT)) {
			return node.getComment(null);
		}
		return null;
	}
		
	public static void main(String[] args) {
		OntModel ontModel = createOntologyModelByOWLFile("http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#","file:source/101/onto.rdf");
		AnnotationProperty node = getAnnotationProperty(ontModel, "http://oaei.ontologymatching.org/2011/benchmarks/101/onto.rdf#Part");
		String id = getProperty(node, AnnotationType.COMMENT);
		System.out.println(id);
	}
	
	

}
