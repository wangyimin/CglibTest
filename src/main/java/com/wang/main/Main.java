package com.wang.main;

import com.wang.core.Factory;
import com.wang.demo.P;

public class Main {
	public static void main(String[] args) throws Exception {
		P p = Factory.getInstance(P.class);
		p.test();
	}
}
