package com.wang.core;

public class WrapperImpl implements Wrapper{
	public void _before(Object[] args)
	{
		System.out.println("Method[" + args[0] + "] is started.");
	}
	
	public void _after(Object[] args)
	{
		System.out.println("Method[" + args[0] + "] is ended.");
	}
	
	public static class None extends WrapperImpl{ }
}
