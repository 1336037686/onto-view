package com.fjut.core.Handler;

import java.io.File;
import java.util.List;

import com.fjut.pojo.MyCellData;
import com.fjut.util.OWLUtil;

/**
 * 标准文件处理
 * @author LGX
 *
 */
public class RefalignHandler {
	
	/**
	 * 标准文件路径
	 */
	public static String refalignFilePath;
	
	
	/**
	 * 判断该标准文件是否存在
	 * @return
	 */
	public static boolean refalignFileIsExist() {
		if("".equals(refalignFilePath) || refalignFilePath == null) return false;
		if(!new File(refalignFilePath).exists()) return false;
		return true;
	}
		
	/**
	 * 获取标准匹配数据
	 * "E:\\DevelopProjects\\Java_Project\\eclipse_project\\framework-learn\\ontology-view\\source\\101\\refalign.rdf"
	 * @return
	 */
	public static List<MyCellData> getRefalign() {
		try {
			if(!refalignFileIsExist()) return null;
			return OWLUtil.parseRefalignFile(refalignFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
