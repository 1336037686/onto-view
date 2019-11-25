package com.fjut.test;

import java.io.File;

public class Test04_URI {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\LGX\\Desktop\\jfreechart-1.0.16-demo.jnlp");
		System.out.println(file.toURI());
	}

}
