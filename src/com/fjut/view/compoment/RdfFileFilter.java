package com.fjut.view.compoment;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 文件过滤器
 * @author LGX
 *
 */
public class RdfFileFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		System.out.println(f.getName());
        if(f.getName().endsWith(".rdf") || f.getName().endsWith(".owl")) return true;
        return false;
	}

	@Override
	public String getDescription() {
		return "本体文件(*.rdf/*.owl)";
	}

}
