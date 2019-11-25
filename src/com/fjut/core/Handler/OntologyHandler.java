package com.fjut.core.Handler;

import java.io.File;

/**
 * 本体处理
 */
public class OntologyHandler {
	public static String onto1FilePath;
	public static String onto2FilePath;
	
	
	public static boolean onto1FileIsExist() {
		if("".equals(onto1FilePath) || onto1FilePath == null || !new File(onto1FilePath).exists()) return false;
		return true;
	}
	
	public static boolean onto2FileIsExist() {
		if("".equals(onto2FilePath) || onto2FilePath == null || !new File(onto2FilePath).exists()) return false;
		return true;
	}
}
