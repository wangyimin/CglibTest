package com.wang.main;

import java.lang.reflect.Method;


import com.wang.core.Factory;
import com.wang.demo.P;

public class Main {
	public static void main(String[] args) throws Exception {
        P p = new Factory().getInstance(P.class);
        
		p.test();
		Method method = p.getClass().getMethod("setProperty", new Class[] {String.class});
        method.invoke(p, new Object[]{"XXXXX"});
	}
}
