package com.fjut.core.Handler;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.fjut.pojo.MyOntology;
import com.fjut.util.OWLUtil;

/**
 * 本体树相关操作
 * @author LGX
 *
 */
public class OntoTreeHandler {
	
	/**
	 * 创建OWL结构树JTree 节点模型
	 * @param owlFileUri
	 * @return
	 */
	public DefaultMutableTreeNode createOWLTree(String owlFileUri) {
		try {
			OWLUtil.loadOWLUtil(owlFileUri);
			List<MyOntology> result = OWLUtil.getOWLClasses();
			MyOntology owlTree = OWLUtil.getOWLTree(result);
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(new OntoTreeNodeData(owlTree));
			createOWLTreeNode(node, owlTree);
			return node;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 迭代创建树层次节点
	 * @param node
	 * @param m
	 * @return
	 */
	private DefaultMutableTreeNode createOWLTreeNode(DefaultMutableTreeNode node, MyOntology m) {
		if(m.getChilds() != null && m.getChilds().size() > 0) {
			for (MyOntology m2 : m.getChilds()) {
				DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new OntoTreeNodeData(m2));
				createOWLTreeNode(node2, m2);
				node.add(node2);
			}
		}
		return node;
	}
	
	/**
	 * OWL结构树JTree 节点数据
	 * @author LGX
	 *
	 */
	public static class OntoTreeNodeData {
		private MyOntology onto;

		public OntoTreeNodeData(MyOntology onto) {
			this.onto = onto;
		}
		
		public MyOntology getOnto() {
			return onto;
		}

		public void setOnto(MyOntology onto) {
			this.onto = onto;
		}

		@Override
		public String toString() {
			return onto.getId();
		}
	}
	
	

}
