package com.fjut.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.fjut.core.Handler.OntoTreeHandler;
import com.fjut.core.Handler.OntologyHandler;
import com.fjut.core.Handler.RefalignHandler;
import com.fjut.pojo.MyCellData;
import com.fjut.util.TreeUtil;
import com.fjut.view.compoment.MyCoverageLabel;
import com.fjut.view.compoment.MyTree;

/**
 * 主界面
 * @author LGX
 *
 */
public class OntologyHomeFrame extends JFrame {

	/**
	 * 主面板
	 */
	private JPanel contentPane;
	
	/**
	 * 绘线面板
	 */
	MyCoverageLabel label;
	
	/**
	 * 显示标准数据List面板
	 */
	List list;
	
	/**
	 * 本体1树
	 */
	MyTree onto1Tree;
	
	/**
	 * 本体2树
	 */
	MyTree onto2Tree;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OntologyHomeFrame frame = new OntologyHomeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OntologyHomeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1374, 792);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("文件");
		menuBar.add(fileMenu);
		
		JMenuItem inportSourceOntoFileMenuItem = new JMenuItem("导入源本体文件");
		fileMenu.add(inportSourceOntoFileMenuItem);
		inportSourceOntoFileMenuItem.addActionListener((e)->{
			reset();
			File file = chooseFile();
			if(file == null) return;
			if(!(file.getName().endsWith(".rdf") || file.getName().endsWith(".owl"))) {
				JOptionPane.showMessageDialog(null, "文件选择出错!!!");
				return;
			}
			OntologyHandler.onto1FilePath = file.getAbsolutePath();
			// 本体1，列表
			DefaultMutableTreeNode treeNode1 = new OntoTreeHandler().createOWLTree(file.toURI().toString());
			DefaultTreeModel d1 = new DefaultTreeModel(treeNode1);
			onto1Tree.setModel(d1);
			expandTree(onto1Tree, new TreePath(treeNode1.getPath()));
		});
		
		JMenuItem inportTargetOntoFileMenuItem = new JMenuItem("导入目标本体文件");
		fileMenu.add(inportTargetOntoFileMenuItem);
		inportTargetOntoFileMenuItem.addActionListener((e)->{
			reset();
			File file = chooseFile();
			if(file == null) return;
			if(!(file.getName().endsWith(".rdf") || file.getName().endsWith(".owl"))) {
				JOptionPane.showMessageDialog(null, "文件选择出错!!!");
				return;
			}
			OntologyHandler.onto2FilePath = file.getAbsolutePath();
			// 本体2，列表
			DefaultMutableTreeNode treeNode2 = new OntoTreeHandler().createOWLTree(file.toURI().toString());
			DefaultTreeModel d2 = new DefaultTreeModel(treeNode2);
			onto2Tree.setModel(d2);
			expandTree(onto2Tree, new TreePath(treeNode2.getPath()));
		});
		
		JMenuItem importmRefalignMenuItem = new JMenuItem("导入标准匹配文件");
		fileMenu.add(importmRefalignMenuItem);
		importmRefalignMenuItem.addActionListener((e)->{
			reset();
			File file = chooseFile();
			if(file == null) return;
			if(!(file.getName().endsWith(".rdf") || file.getName().endsWith(".owl"))) {
				JOptionPane.showMessageDialog(null, "文件选择出错!!!");
				return;
			}
			RefalignHandler.refalignFilePath = file.getAbsolutePath();
            //重新加载标准文件
            addRefalignDataToList(RefalignHandler.getRefalign());
		});

		JMenu handlerMenu = new JMenu("操作");
		menuBar.add(handlerMenu);

		JMenuItem addStandardLineMenuItem = new JMenuItem("添加匹配线");
		handlerMenu.add(addStandardLineMenuItem);
		
		JMenuItem exportMenuItem = new JMenuItem("导出匹配对");
		handlerMenu.add(exportMenuItem);

		JMenu aboutUsMenu = new JMenu("关于我们");
		menuBar.add(aboutUsMenu);

		JMenuItem versionMenuItem = new JMenuItem("当前版本");
		aboutUsMenu.add(versionMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 10, 1338, 712);

		contentPane.add(tabbedPane);

		JPanel accordantPanel = new JPanel();
		accordantPanel.setBounds(10, 10, 500, 500);
		tabbedPane.addTab("树形匹配", null, accordantPanel, null);
		accordantPanel.setLayout(null);

		JPanel ontoPanel = new JPanel();
		ontoPanel.setBorder(BorderFactory.createTitledBorder("Ontology匹配"));
		ontoPanel.setBounds(199, 10, 941, 545);
		
		accordantPanel.add(ontoPanel);
		ontoPanel.setLayout(null);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(0);
		splitPane.setDividerLocation(437);
		splitPane.setBounds(10, 20, 921, 515);
		ontoPanel.add(splitPane);

		JPanel onto1Panel = new JPanel();
		splitPane.setLeftComponent(onto1Panel);
		onto1Panel.setLayout(null);

		onto1Tree = new MyTree();
		onto1Tree.setModel(new DefaultTreeModel(null));
		JScrollPane onto1TreeScrollPane = new JScrollPane(onto1Tree);
		
		JPopupMenu ontoTree1PopupMenu = new JPopupMenu();
		addPopup(onto1Tree, ontoTree1PopupMenu);
		
		JMenuItem ontoTree1DeleteMenuItem = new JMenuItem("删除");
		ontoTree1DeleteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) onto1Tree.getLastSelectedPathComponent();
				if(node == null) {
					JOptionPane.showMessageDialog(null, "需要选中一个节点");
					return;
				}
	            if (node.isRoot()) {
	                return;
	            }
	            //添加节点到删除列表
	            String ontoTree1DeleteParamId = ((OntoTreeHandler.OntoTreeNodeData)node.getUserObject()).toString();
	            MyCoverageLabel.Onto1RemoveList.add(ontoTree1DeleteParamId);
	            label.repaint();

	            //重新加载标准文件
	            addRefalignDataToList(RefalignHandler.getRefalign());
 
			}
		});
		ontoTree1PopupMenu.add(ontoTree1DeleteMenuItem);
		JScrollBar onto1TreeScrollBar = onto1TreeScrollPane.getVerticalScrollBar(); //JScrollBar
		onto1TreeScrollPane.setBounds(0, 0, 439, 513);
		onto1Panel.add(onto1TreeScrollPane);

		JPanel onto2Panel = new JPanel();
		splitPane.setRightComponent(onto2Panel);
		onto2Panel.setLayout(null);

		onto2Tree = new MyTree();
		onto2Tree.setModel(new DefaultTreeModel(null));
		JScrollPane onto2TreeScrollPane = new JScrollPane(onto2Tree);
		
		JPopupMenu ontoTree2PopupMenu = new JPopupMenu();
		addPopup(onto2Tree, ontoTree2PopupMenu);
		
		JMenuItem ontoTree2DeleteMenuItem = new JMenuItem("删除");
		ontoTree2DeleteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) onto2Tree.getLastSelectedPathComponent();
				if(node == null) {
					JOptionPane.showMessageDialog(null, "需要选中一个节点");
					return;
				}
	            if (node.isRoot()) {return;}
	            //添加节点到删除列表
	            String ontoTree2DeleteParamId = ((OntoTreeHandler.OntoTreeNodeData)node.getUserObject()).toString();
	            MyCoverageLabel.Onto2RemoveList.add(ontoTree2DeleteParamId);
	            label.repaint();
	            
	            //重新加载标准文件
	            addRefalignDataToList(RefalignHandler.getRefalign());
			}
		});
		ontoTree2PopupMenu.add(ontoTree2DeleteMenuItem);
		JScrollBar onto2TreeScrollBar = onto2TreeScrollPane.getVerticalScrollBar(); //JScrollBar
		onto2TreeScrollPane.setBounds(0, 0, 483, 513);
		onto2Panel.add(onto2TreeScrollPane);
		
		label = new MyCoverageLabel();
		ontoPanel.add(label);

		JPanel standardPanel = new JPanel();
		standardPanel.setBorder(BorderFactory.createTitledBorder("标准匹配"));
		standardPanel.setBounds(1150, 10, 173, 545);
		accordantPanel.add(standardPanel);
		standardPanel.setLayout(null);
		
		/**
		 * 标准文件匹配对
		 */
		list = new List();
		list.setBounds(10, 25, 153, 510);
		standardPanel.add(list);

		JPanel otherHandlerPanel = new JPanel();
		otherHandlerPanel.setBorder(BorderFactory.createTitledBorder("其他操作"));
		otherHandlerPanel.setBounds(10, 558, 1313, 115);
		accordantPanel.add(otherHandlerPanel);
		otherHandlerPanel.setLayout(null);

		JPanel fileHandlerPanel = new JPanel();
		fileHandlerPanel.setBorder(BorderFactory.createTitledBorder("文件操作"));
		fileHandlerPanel.setBounds(10, 10, 179, 545);
		accordantPanel.add(fileHandlerPanel);
		
		//点击画线按钮
		addStandardLineMenuItem.addActionListener((e) -> {	
			
			if(!OntologyHandler.onto1FileIsExist() || !OntologyHandler.onto2FileIsExist() || !RefalignHandler.refalignFileIsExist()) {
				JOptionPane.showMessageDialog(null, "文件不齐全!!!");
				return;
			}
			
			if(!MyCoverageLabel.drawLineSwitch) {
				MyCoverageLabel.drawLineSwitch = true;
				//设置坐标
				label.target = TreeUtil.getTreePoint(onto1Tree, onto1TreeScrollBar.getValue());
				label.source = TreeUtil.getTreePoint(onto2Tree, onto2TreeScrollBar.getValue());	
			} else {
				MyCoverageLabel.drawLineSwitch = false;
				label.repaint();
			}
		});

		//监听滚轮事件
		onto1TreeScrollPane.getViewport().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {	
				if(MyCoverageLabel.drawLineSwitch) {
					label.source = TreeUtil.getTreePoint(onto1Tree, onto1TreeScrollBar.getValue());
					label.target = TreeUtil.getTreePoint(onto2Tree, onto2TreeScrollBar.getValue());
					label.repaint();
				}
			}
		});
		
		//监听滚轮事件
		onto2TreeScrollPane.getViewport().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(MyCoverageLabel.drawLineSwitch) {
					label.source = TreeUtil.getTreePoint(onto1Tree, onto1TreeScrollBar.getValue());
					label.target = TreeUtil.getTreePoint(onto2Tree, onto2TreeScrollBar.getValue());
					label.repaint();
				}
			}
		});
		
		//开线程画线
		new Thread(label).start();
		
		//导出匹配对
		exportMenuItem.addActionListener((e) -> {
			if(!RefalignHandler.refalignFileIsExist()) {
				JOptionPane.showMessageDialog(null, "导出错误!!!");
				return;
			}
			exportRefalignFile();
		});

	}

	/**
	 * 默认展开本体树
	 * @param tree
	 * @param parent
	 */
	private static void expandTree(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandTree(tree, path);
			}
		}
		tree.expandPath(parent);
	}	
	
	/**
	 * 添加右键菜单
	 * @param component
	 * @param popup
	 */
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	/**
	 * 添加标准文件数据到List组件中
	 * @param parseRefalignList
	 */
	private void addRefalignDataToList(java.util.List<MyCellData> parseRefalignList) {
		//加载标准文件待提出封装
		try {
			list.removeAll();
			for (MyCellData m : parseRefalignList) {
				if(!MyCoverageLabel.Onto2RemoveList.contains(m.getEntity2Id()) && !MyCoverageLabel.Onto1RemoveList.contains(m.getEntity1Id())) {
					list.add(m.getEntity1Id() + " <---> " + m.getEntity2Id());
				}	
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 选择文件
	 * @return
	 */
	private File chooseFile() {
		JFileChooser jfc=new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.showDialog(new JLabel(), "选择文件");
		File file = jfc.getSelectedFile();
		if(!file.exists()) {
			JOptionPane.showMessageDialog(null, "文件不存在!!!");
			return null;
		}
		return file;
	}
	
	/**
	 * 重置
	 */
	public void reset() {
		MyCoverageLabel.drawLineSwitch = false;
		MyCoverageLabel.Onto1RemoveList.clear();
		MyCoverageLabel.Onto2RemoveList.clear();
	}
	
	/**
	 * 导出文件
	 * 有bug
	 */
	public void exportRefalignFile() {
		//弹出文件选择框
		JFileChooser chooser = new JFileChooser();
		int option = chooser.showSaveDialog(null);
		if(option==JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
			File file = chooser.getSelectedFile();
			StringBuffer sb = new StringBuffer();
			java.util.List<MyCellData> parseRefalignList = RefalignHandler.getRefalign();
			for (MyCellData m : parseRefalignList) {
				if(!MyCoverageLabel.Onto2RemoveList.contains(m.getEntity2Id()) && !MyCoverageLabel.Onto1RemoveList.contains(m.getEntity1Id())) 
					sb.append(m.getEntity1Id() + " <---> " + m.getEntity2Id() + "\r\n");
			}
			try(FileOutputStream fos = new FileOutputStream(file);)
			{
				fos.write(sb.toString().getBytes());
				fos.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "导出成功");	
		}
	}
}


