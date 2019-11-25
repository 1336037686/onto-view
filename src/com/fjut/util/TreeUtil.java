package com.fjut.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.fjut.pojo.CellPoint;

/**
 * JTree工具类
 * @author LGX
 *
 */
public class TreeUtil {
	
	/**
	 * 获取树节点坐标
	 * @param tree
	 * @param change
	 * @return
	 */
	public static List<CellPoint> getTreePoint(JTree tree, double change) {
		List<CellPoint> list = new ArrayList<CellPoint>();
		for (int i = 0; i < tree.getRowCount(); i++) {
			Rectangle c = tree.getRowBounds(i);
			TreePath thisPath = tree.getPathForRow(i);
			list.add(new CellPoint((int)c.getCenterX(), (int)(c.getCenterY() - change), thisPath.getPath()[thisPath.getPathCount() - 1].toString()));
		}
		return list;
	}

}
