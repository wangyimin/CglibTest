package com.wang.demo;

import com.wang.core.WrapperImpl;
import com.wang.type.InjectLogging;

public class P {
	@InjectLogging(using=WrapperImpl.class)
    public void test(){
        System.out.println("hello world.");
    }
}
