package com.wang.core;

import java.lang.reflect.Method;

import com.wang.type.InjectLogging;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Factory {
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz){
		return (T) Enhancer.create(clazz, new Intercepter());
	}
}

class Intercepter implements MethodInterceptor{
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        
    	_before(obj, method, args);
        
        Object r = proxy.invokeSuper(obj, args);
        
        _after(obj, method, args);
        return r;
    }
    
    private void _before(Object obj, Method method, Object[] args)
    {
    	try{
            InjectLogging attr = method.getAnnotation(InjectLogging.class);
            if (attr != null){ 
            	Class<? extends WrapperImpl> clazz = attr.using();
            	clazz.newInstance()._before(new Object[]{method.getName(), args});
            }
    	}catch (Exception e){
    		return;
    	}
    }
    
    private void _after(Object obj, Method method, Object[] args)
    {
    	try{
            InjectLogging attr = method.getAnnotation(InjectLogging.class);
            if (attr != null){ 
            	Class<? extends WrapperImpl> clazz = attr.using();
            	clazz.newInstance()._after(new Object[]{method.getName(), args});
            }
    	}catch (Exception e){
    		return;
    	}
    }
}

