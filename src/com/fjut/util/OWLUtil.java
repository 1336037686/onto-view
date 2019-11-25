package com.fjut.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.sparql.pfunction.library.concat;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fjut.pojo.AnnotationType;
import com.fjut.pojo.MyCellData;
import com.fjut.pojo.MyOntology;

import fr.inrialpes.exmo.align.parser.XMLParser;
import fr.inrialpes.exmo.ontowrap.HeavyLoadedOntology;
import fr.inrialpes.exmo.ontowrap.OntologyFactory;
import fr.inrialpes.exmo.ontowrap.owlapi30.OWLAPI3OntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

/**
 * OWL工具类
 * 
 * @author LGX
 * @version 2019.3.16
 * 
 *          案例写法：<br>
 *          public static void main(String[] args) throws Exception {<br>
 *          // 1. 加载文件<br>
 *          loadOWLUtil("file:source/101/onto.rdf");<br>
 *          <br>
 *          // 2. 调用相应处理方法 <br>
 *          List<\MyOntology\> list = getOWLDataProperties();<br>
 *          int i = 1;<br>
 *          for (MyOntology myOntology : list) {<br>
 *          System.out.println(i++ + "=" + myOntology);<br>
 *          }<br>
 *          }<br>
 *
 */
@SuppressWarnings("all")
public class OWLUtil {
	final static Logger logger = LoggerFactory.getLogger(OWLUtil.class);

	private static OntModel ontModel;
	public static HeavyLoadedOntology lonto;

	static {
		OntologyFactory.setDefaultFactory("fr.inrialpes.exmo.ontowrap.owlapi30.OWLAPI3OntologyFactory");
	}

	public static OntModel getOntModel() {
		return ontModel;
	}

	public static HeavyLoadedOntology getLonto() {
		return lonto;
	}

	/**
	 * 初始化OWLUtil工具类 加载需要解析的OWL文件
	 * 
	 * @param owlFileUri
	 *            传入OWL文件的uri路径
	 */
	public static void loadOWLUtil(String owlFileUri) {
		try {
			lonto = (HeavyLoadedOntology) new OWLAPI3OntologyFactory().loadOntology(new URI(owlFileUri));
			// 创建本体
			// ontModel = JenaUtil.createOntologyModelByOWLFile(lonto.getURI().toString() +
			// "#", owlFileUri);
			ontModel = JenaUtil.createOntologyModelByOWLFile("http://www.w3.org/1999/02/22-rdf-syntax-ns#", owlFileUri);
			//
		} catch (Exception e) {
			logger.error("loadOWLUtil Fail...");
			e.printStackTrace();
		}
	}

	/**
	 * 解析OWL文件获取其中的所有的OWL Entity 对象
	 * 
	 * @return List 泛型：MyOntology的集合，<br>
	 *         MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> getEntity() throws Exception {
		List<MyOntology> list = new ArrayList<>();
		// 获取全部个体 Class Set 集合
		Set allEntity = lonto.getEntities();
		// 解析个体
		int num = 0;
		for (Object entity : allEntity) {
			num++;
			MyOntology myOntology = new MyOntology();
			OWLEntity o = (OWLEntity) entity;

			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());

			setMyOntologyParams(node, myOntology);
			myOntology.setDomain(new HashSet<>());
			myOntology.setRange(new HashSet<>());
			list.add(myOntology);
		}
		System.out.println(num);
		return list;
	}

	/**
	 * 解析OWL文件获取其中的所有的OWL Instance 对象
	 * 
	 * @return List 泛型：MyOntology的集合，<br>
	 *         MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> getInstance() throws Exception {
		List<MyOntology> list = new ArrayList<>();
		// 获取全部个体 Class Set 集合
		List<MyOntology> allEntity = getEntity();
		List<MyOntology> allClass = getOWLClasses();
		List<MyOntology> allDatatypeProperty = getOWLDataProperties();
		List<MyOntology> allObjectProperty = getOWLObjectProperties();

		List<MyOntology> allConcept = new ArrayList<>();
		allConcept.addAll(allClass);
		allConcept.addAll(allDatatypeProperty);
		allConcept.addAll(allObjectProperty);

		List<MyOntology> allInstance = new ArrayList<>();
		for (MyOntology entity : allEntity) {
			int flag = 0;
			for (MyOntology concept : allConcept) {
				if (entity.getId().equals(concept.getId())) {
					flag = 1;
					continue;
				}
			}
			if (flag == 1) {
				continue;
			} else {
				allInstance.add(entity);
			}
		}

		return allInstance;
	}

	/**
	 * 解析OWL文件获取其中的所有的OWL Instance 对象
	 * 
	 * @return List 泛型：MyOntology的集合，<br>
	 *         MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> getIndividual() throws Exception {

		List<MyOntology> list = new ArrayList<>();
		// 获取全部个体 Class Set 集合
		Set classes = lonto.getIndividuals();
		// 解析个体
		for (Object object : classes) {
			MyOntology myOntology = new MyOntology();
			OWLEntity o = (OWLEntity) object;

			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());

			setMyOntologyParams(node, myOntology);
			// myOntology.setDomain(new HashSet<>());
			myOntology.setDomain(new HashSet<>());
			myOntology.setRange(new HashSet<>());
			list.add(myOntology);
		}
		return list;
	}

	/**
	 * 解析OWL文件获取其中的所有的OWL Class 对象
	 * 注：该API只能获取当前资源的，也就是当前RDF文件内的，无法获取外部引用资源如FOAF，RDF
	 * 
	 * @return List 泛型：MyOntology的集合，<br>
	 *         MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> getOWLClasses() throws Exception {
		List<MyOntology> list = new ArrayList<>();
		// 获取全部个体 Class Set 集合
		ExtendedIterator<OntClass> classes = ontModel.listClasses();
		// 解析个体
		while(classes.hasNext()) {
			OntClass ontClass = classes.next();
			if(ontClass.getURI() == null) continue;
			MyOntology myOntology = new MyOntology();
			
			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, ontClass.getURI());
			setMyOntologyParams(node, myOntology);
			myOntology.setDomain(new HashSet<>());
			myOntology.setRange(new HashSet<>());

			// 添加DataProperty
			setDataPropertyValue(myOntology);
			myOntology.setFather(getFatherClassesDirect(ontClass.getURI()));
			myOntology.setChilds(getChildClassesDirect(ontClass.getURI()));
			list.add(myOntology);
		}
		return list;
	}

	/**
	 * 解析OWL文件，提取所有的DataProperty信息。
	 * 注：该API只能获取当前资源的，也就是当前RDF文件内的，无法获取外部引用资源如FOAF，RDF
	 * 
	 * @return List 泛型：MyOntology的集合，<br>
	 * 		MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> getOWLDataProperties() throws Exception {
		List<MyOntology> list = new ArrayList<>();

		// 获取全部个体 Class DataProperty ObjectProperty 的 Set 集合
		Set dataProperties = lonto.getDataProperties();

		// 解析个体
		for (Object object : dataProperties) {
			MyOntology myOntology = new MyOntology();
			OWLEntity o = (OWLEntity) object;
			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
			setMyOntologyParams(node, myOntology);

			// 如果是 ObjectProperty or DataProperty 提取 range 和 domain
			if (o.isOWLDataProperty() || o.isOWLObjectProperty()) {
				Set<MyOntology> range = getRange(lonto, o);
				Set<MyOntology> domain = getDomain(lonto, o);
				myOntology.setDomain(domain);
				myOntology.setRange(range);
			} else {
				myOntology.setDomain(new HashSet<>());
				myOntology.setRange(new HashSet<>());
			}
			list.add(myOntology);
		}
		return list;
	}

	/**
	 * 解析OWL文件，提取所有的ObjectProperty信息。
	 * 
	 * @return List 泛型：MyOntology的集合，<br>
	 * 		MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> getOWLObjectProperties() throws Exception {
		List<MyOntology> list = new ArrayList<>();

		// 获取全部个体 Class DataProperty ObjectProperty 的 Set 集合
		Set objProperties = lonto.getObjectProperties();

		// 解析个体
		for (Object object : objProperties) {
			MyOntology myOntology = new MyOntology();

			OWLEntity o = (OWLEntity) object;

			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
			setMyOntologyParams(node, myOntology);

			// 如果是 ObjectProperty or DataProperty 提取 range 和 domain
			if (o.isOWLDataProperty() || o.isOWLObjectProperty()) {
				Set<MyOntology> range = getRange(lonto, o);
				Set<MyOntology> domain = getDomain(lonto, o);
				myOntology.setDomain(domain);
				myOntology.setRange(range);
			} else {
				myOntology.setDomain(new HashSet<>());
				myOntology.setRange(new HashSet<>());
			}
			list.add(myOntology);
		}
		return list;
	}

	/**
	 * 对Class的MyOntology对象添加其拥有的DataProperty属性
	 * 
	 * @param input
	 *            输入的需要添加DataProperty属性的MyOntology对象
	 * @throws Exception
	 */
	private static void setDataPropertyValue(MyOntology input) throws Exception {
		if (input.getDataProperty() == null)
			input.setDataProperty(new ArrayList<>());
		List<MyOntology> owlDataProperties = getOWLDataProperties();
		if (owlDataProperties != null) {
			for (MyOntology myOntology : owlDataProperties) {
				Set<MyOntology> domain = myOntology.getDomain();
				if (domain != null) {
					for (MyOntology d : domain) {
						if (input.getId().equals(d.getId())) {
							input.getDataProperty().add(myOntology);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 获取Class 的直接父节点集合
	 * 
	 * @param id
	 *            ： 需要查找父节点的id
	 * @return 父节点的集合
	 * @throws Exception
	 */
	public static List<MyOntology> getFatherClassesDirect(String uri) throws Exception {
		List<MyOntology> list = new ArrayList<>();
		URI tempu = new URI(uri);
		Object entity = lonto.getEntity(tempu);
		if (entity == null) {
			return list;
		}
		Set<OWLClassImpl> tempans = lonto.getSuperClasses(entity, OntologyFactory.LOCAL, OntologyFactory.ASSERTED,
				OntologyFactory.NAMED);
		if (tempans != null && tempans.size() > 0) {
			for (OWLClassImpl owlClassImpl : tempans) {
				MyOntology myOntology = new MyOntology();
				OWLEntity o = (OWLEntity) lonto.getEntity(new URI(owlClassImpl.getIRI().toString()));
				// 解析基本信息 id，label，comment
				AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
				setMyOntologyParams(node, myOntology);
				list.add(myOntology);
			}
		} else {
			// 如果没有父类，其父类为Thing
			list.add(new MyOntology("Thing"));
		}
		return list;
	}

	/**
	 * 获取Class 的直接子节点集合
	 * 
	 * @param id
	 *            需要查找子节点的节点id
	 * @return 子节点集合
	 * @throws Exception
	 */
	public static List<MyOntology> getChildClassesDirect(String uri) throws Exception {
		List<MyOntology> result = new ArrayList<>();
		URI u = new URI(uri);
		Object entity = lonto.getEntity(u);
		if(entity == null) return result;
		// 下的getSubClasses(本体中的Object(通过URI获得),,,)获取指定的实体的uri的直接子类
		Set<OWLClassImpl> ans = lonto.getSubClasses(entity, OntologyFactory.LOCAL, OntologyFactory.ASSERTED,
				OntologyFactory.NAMED);
		for (OWLClassImpl owlClassImpl : ans) {
			MyOntology myOntology = new MyOntology();
			OWLEntity o = (OWLEntity) lonto.getEntity(new URI(owlClassImpl.getIRI().toString()));
			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
			setMyOntologyParams(node, myOntology);
			result.add(myOntology);
		}
		return result;
	}

	/**
	 * 解析OWL文件，提取其中的Class,DataProperty,ObjectProperty
	 * 
	 * @param owlFileUri
	 *            OWL 文件URI路径
	 * @return 包含了Class,DataProperty,ObjectProperty的Set集合<br>
	 *         Set 泛型的类型是MyOntology<br>
	 *         MyOntology {String id,String label,String comment,Set range, Set
	 *         domain}
	 * @throws Exception
	 */
	public static List<MyOntology> get3Params() throws Exception {
		List<MyOntology> list = new ArrayList<>();

		// 获取全部个体 Class DataProperty ObjectProperty 的 Set 集合
		Set totals = new HashSet<>();
		Set classes = lonto.getClasses();
		Set dataProperties = lonto.getDataProperties();
		Set objectProperties = lonto.getObjectProperties();
		totals.addAll(classes);
		totals.addAll(objectProperties);
		totals.addAll(dataProperties);

		// 解析个体
		for (Object object : totals) {
			MyOntology myOntology = new MyOntology();
			OWLEntity o = (OWLEntity) object;
			// 解析基本信息 id，label，comment
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
			setMyOntologyParams(node, myOntology);
			// 如果是 ObjectProperty or DataProperty 提取 range 和 domain
			if (o.isOWLDataProperty() || o.isOWLObjectProperty()) {
				Set<MyOntology> range = getRange(lonto, o);
				Set<MyOntology> domain = getDomain(lonto, o);
				myOntology.setDomain(domain);
				myOntology.setRange(range);
			} else {
				myOntology.setDomain(new HashSet<>());
				myOntology.setRange(new HashSet<>());
			}
			list.add(myOntology);
		}
		return list;
	}

	/**
	 * 创建树
	 * @param treeNode
	 * @return
	 */
	public static MyOntology getOWLTree(List<MyOntology> treeNode) {
		return createTreeNode(new MyOntology("Thing", "Thing", null), treeNode);
	}
	private static MyOntology createTreeNode(MyOntology treeNode, List<MyOntology> nodes) {
		List<MyOntology> list = new ArrayList<>();
		if(nodes == null || nodes.size() == 0) return treeNode;
		for (MyOntology myOntology : nodes) 
			if(myOntology.getFather().get(0).getId().equals(treeNode.getId())) 
				list.add(createTreeNode(myOntology, nodes));
		treeNode.setChilds(list);
		return treeNode;
	}

	/**
	 * 解析标准文件 refalign.rdf
	 * 
	 * @param refalignFilePath
	 *            标准文件路径
	 * @return 解析后的MyCellData list对象
	 */
	public static List<MyCellData> parseRefalignFile(String refalignFilePath) throws Exception {
		List<MyCellData> result = new ArrayList<>();
		Alignment alignment = new XMLParser().parse(new FileInputStream(new File(refalignFilePath)));
		for (Cell cell : alignment) {
			MyCellData m = new MyCellData(cell.getObject1().toString(), cell.getObject2().toString(),
					cell.getStrength(), cell.getRelation().getRelation().toString());
			result.add(m);
		}
		return result;
	}

	/**
	 * 获取Range
	 */
	protected static Set<MyOntology> getRange(HeavyLoadedOntology lonto, Object entity) throws Exception {
		Set<Object> tempans = lonto.getRange(entity, OntologyFactory.ASSERTED);
		Set<MyOntology> ans = new HashSet<>();
		for (Object obj : tempans) {
			OWLEntity o = (OWLEntity) obj;
			MyOntology myOntology = new MyOntology();
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
			setMyOntologyParams(node, myOntology);

			// 查找range和domain
			if (o.isOWLDataProperty() || o.isOWLObjectProperty()) {
				Set<MyOntology> range = getRange(lonto, o);
				Set<MyOntology> domain = getDomain(lonto, o);
				myOntology.setDomain(domain);
				myOntology.setRange(range);
			} else {
				myOntology.setDomain(new HashSet<>());
				myOntology.setRange(new HashSet<>());
			}
			ans.add(myOntology);
		}
		return ans;
	}

	/**
	 * 获取Domain
	 */
	protected static Set<MyOntology> getDomain(HeavyLoadedOntology lonto, Object entity) throws Exception {
		Set domain = lonto.getDomain(entity, OntologyFactory.ASSERTED);
		Set<MyOntology> domains = new HashSet();
		for (Object obj : domain) {
			MyOntology myOntology = new MyOntology();
			OWLEntity o = (OWLEntity) obj;
			AnnotationProperty node = JenaUtil.getAnnotationProperty(ontModel, o.getIRI().toString());
			setMyOntologyParams(node, myOntology);
			// 查找range和domain
			if (o.isOWLDataProperty() || o.isOWLObjectProperty()) {
				Set<MyOntology> range = getRange(lonto, o);
				Set<MyOntology> domain2 = getDomain(lonto, o);
				myOntology.setDomain(domain2);
				myOntology.setRange(range);
			} else {
				myOntology.setDomain(new HashSet<>());
				myOntology.setRange(new HashSet<>());
			}
			domains.add(myOntology);
		}
		return domains;
	}

	/**
	 * 从个体中获取数据，封装ID，Label，Comment
	 */
	private static void setMyOntologyParams(AnnotationProperty node, MyOntology myOntology) {
		String id = JenaUtil.getProperty(node, AnnotationType.ID);
		String label = JenaUtil.getProperty(node, AnnotationType.LABEL);
		String comment = JenaUtil.getProperty(node, AnnotationType.COMMENT);
		myOntology.setId(id);
		myOntology.setLabel(label);
		myOntology.setComment(comment);
	}

	/**
	 * 是不是Class类型
	 * 
	 * @param id
	 *            个体的id值<br>
	 *            例如：http://oaei.ontologymatching.org/2011/benchmarks/201/onto.rdf#zdzdzd，#后边的ID值
	 * @return 是Class ：true，不是Class ： false
	 * @throws Exception
	 */
	public static boolean isClass(String id) throws Exception {
		URI tempu = new URI(lonto.getURI().toString() + "#" + id);
		OWLEntity entity = (OWLEntity) lonto.getEntity(tempu);
		return entity.isOWLClass();
	}

	/**
	 * 是不是ObjectProperty类型
	 * 
	 * @param id
	 *            个体的id值<br>
	 *            例如：http://oaei.ontologymatching.org/2011/benchmarks/201/onto.rdf#zdzdzd，#后边的ID值
	 * @return 是ObjectProperty ：true，不是ObjectProperty ： false
	 * @throws Exception
	 */
	public static boolean isObjectProperty(String id) throws Exception {
		URI tempu = new URI(lonto.getURI().toString() + "#" + id);
		OWLEntity entity = (OWLEntity) lonto.getEntity(tempu);
		return entity.isOWLObjectProperty();
	}

	/**
	 * 是不是DataProperty类型
	 * 
	 * @param id
	 *            个体的id值<br>
	 *            例如：http://oaei.ontologymatching.org/2011/benchmarks/201/onto.rdf#zdzdzd，#后边的ID值
	 * @return 是DataProperty ：true，不是DataProperty ： false
	 * @throws Exception
	 */
	public static boolean isDataProperty(String id) throws Exception {
		URI tempu = new URI(lonto.getURI().toString() + "#" + id);
		OWLEntity entity = (OWLEntity) lonto.getEntity(tempu);
		return entity.isOWLDataProperty();
	}

	/**
	 * 将MyOntology解析后按照格式写入文件
	 * 
	 * @param list1
	 *            需要解析的OWL集合对象
	 * @param path
	 *            目标写入文件路径
	 * @throws Exception
	 */
	public static void writeToFile(List<MyOntology> list1, String path) throws Exception {
		FileOutputStream fos = new FileOutputStream(new File(path));
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (MyOntology myOntology : list1) {
			sb.append("★ " + i++ + "\r\n");
			sb.append("LocalName:" + myOntology.getId() + "\r\n");
			sb.append("Label:" + myOntology.getLabel() + "\r\n");
			sb.append("Comment:" + myOntology.getComment() + "\r\n");
			sb.append("Range:" + myOntology.getRange() + "\r\n");
			sb.append("domain:" + myOntology.getDomain() + "\r\n\r\n");
		}
		fos.write(sb.toString().getBytes());
		fos.close();
	}

	/**
	 * 解析本体匹配标准文件Refalign，并输出
	 * 
	 * @param sourceFile
	 *            源路径，被解析的Refalign文件路径
	 * @param dirFile
	 *            解析后，输出文件路径
	 * @return 成功/失败
	 */
	public static boolean parseRefalign(File sourceFile, File dirFile) {
		// 解析匹配标准文件
		try (FileOutputStream fos = new FileOutputStream(dirFile);) {
			XMLParser aparser = new XMLParser();
			// Alignment alignment = aparser.parse(new
			// FileInputStream("E:\\DevelopProjects\\Java_Project\\eclipse_project\\npl-learn-demo\\word-similarity\\source\\101\\refalign.rdf"));
			Alignment alignment = aparser.parse(new FileInputStream(sourceFile));
			List<MyCellData> result = new ArrayList<>();
			for (Cell cell : alignment) {
				String e1Str = cell.getObject1().toString();
				String e2Str = cell.getObject2().toString();
				String entity1 = e1Str.substring(e1Str.indexOf("#") + 1);
				String entity2 = e2Str.substring(e2Str.indexOf("#") + 1);
				MyCellData data = new MyCellData(entity1, entity2, cell.getStrength(),
						cell.getRelation().getRelation());
				result.add(data);
			}
			fos.write(JSON.toJSONString(result).getBytes());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 在控制台打印OWL解析树图
	 * @param m
	 */
	public static void printTree(MyOntology m) {
		printTree(m, 0);
	}
	
	private static void printTree(MyOntology m, int index) {
		String tab = "";
		for(int i = 0; i < index; i++) {
			tab += "\t";
		}
		System.out.println(tab + m.getId());
		if(m.getChilds() != null && m.getChilds().size() > 0) {
			for (MyOntology m2 : m.getChilds()) {
				int i = index + 1;
				printTree(m2, i);
			}
		}
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) throws Exception {
		// 1. 加载文件
		loadOWLUtil("file:source/101/onto.rdf");
		// 2. 调用提取方法
		List<MyOntology> classes = getOWLClasses();
		// String string = JSON.toJSONString(owlDataProperties);
		// System.out.println(string);
	}
}
