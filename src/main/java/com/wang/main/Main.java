package com.wang.main;

import com.wang.core.Factory;
import com.wang.demo.P;

public class Main {
	public static void main(String[] args) throws Exception {
		P p = new Factory().getInstance(P.class);

		p.test();
		
		p.getClass()
			.getMethod("setProperty", new Class[] { String.class })
			.invoke(p, new Object[] { "XXXXX" });
	}
}
