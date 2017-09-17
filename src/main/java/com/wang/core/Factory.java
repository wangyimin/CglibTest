package com.wang.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

import com.wang.type.InjectLogging;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Factory {
	private Class<?> clazz;

	private List<Class<?>> setters = new ArrayList<Class<?>>();

	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> clazz) {
		if (clazz == null) {
			throw new NullPointerException();
		}

		this.clazz = clazz;

		Enhancer e = new Enhancer();
		e.setSuperclass(clazz);
		e.setCallback(new Interceptor());
		e.setInterfaces(getInterfaces());

		return (T) (e.create());
	}

	private String getSetterByFieldName(String prop) {
		return "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
	}

	private boolean hasSetter(String prop) {
		boolean r = false;

		if (prop == null || "".equals(prop)) {
			throw new NullPointerException();
		}

		try {
			Field f = clazz.getDeclaredField(prop);
			clazz.getDeclaredMethod(getSetterByFieldName(prop), f.getType());

			return true;
		} catch (Exception e) {
		}

		return r;
	}

	private Class<?>[] getInterfaces() {
		for (Field f : clazz.getDeclaredFields()) {
			if (!hasSetter(f.getName())) {
				InterfaceMaker im = new InterfaceMaker();
				im.add(new Signature(getSetterByFieldName(f.getName()),
						Type.VOID_TYPE,
						new Type[] { Type.getType(f.getType()) }), null);
				Class<?> interfaces = im.create();
				setters.add(interfaces);
			}
		}

		if (!setters.isEmpty()) {
			Class<?>[] r = new Class<?>[setters.size()];
			return setters.toArray(r);
		}

		return new Class[] {};

	}
}

class Interceptor implements MethodInterceptor {
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		Object r = Type.VOID;

		_before(obj, method, args);

		if (method.getName().contains("set")) {
			Class<?> clazz = obj.getClass().getSuperclass();
			Field f = clazz.getDeclaredField(method.getName().substring(3, 4)
					.toLowerCase()
					+ method.getName().substring(4));
			f.setAccessible(true);
			f.set(obj, args[0]);
		} else {
			r = proxy.invokeSuper(obj, args);
		}

		_after(obj, method, args);
		return r;
	}

	private void _before(Object obj, Method method, Object[] args) {
		try {
			InjectLogging attr = method.getAnnotation(InjectLogging.class);
			if (attr != null) {
				Class<? extends WrapperImpl> clazz = attr.using();
				clazz.newInstance()._before(
						new Object[] { method.getName(), args });
			}
		} catch (Exception e) {
			return;
		}
	}

	private void _after(Object obj, Method method, Object[] args) {
		try {
			InjectLogging attr = method.getAnnotation(InjectLogging.class);
			if (attr != null) {
				Class<? extends WrapperImpl> clazz = attr.using();
				clazz.newInstance()._after(
						new Object[] { method.getName(), args });
			}
		} catch (Exception e) {
			return;
		}
	}

}
