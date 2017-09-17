package com.wang.demo;

import com.wang.core.WrapperImpl;
import com.wang.type.InjectLogging;

public class P {
	@SuppressWarnings("unused")
	private String property;
	
	public void setProperty(String property) {
		this.property = property;
	}

	@InjectLogging(using=WrapperImpl.class)
    public void test(){
        System.out.println("hello world.");
    }
}
