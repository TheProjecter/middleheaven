package org.middleheaven.core.reflection;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.LinkedList;

import org.junit.Test;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.annotations.Publish;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.logging.LogBookFactory;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.NotReadablePropertyException;
import org.middleheaven.reflection.ReflectedProperty;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.reflection.inspection.Introspector;


public class TestIntrospector {

	@Test(expected=NotReadablePropertyException.class)
	public void testPropertyAcessor(){
		
		SomeBean sb = new SomeBean();
		
		ReflectedProperty pa = Introspector.of(SomeBean.class).inspect().properties().named("list").retrive();
		
		pa.setValue(sb, new LinkedList());
		
		pa.getValue(sb);
		
		pa = Introspector.of(SomeBean.class).inspect().properties().named("name").retrive();
		
		pa.setValue(sb, "A");
	
		pa.getValue(sb);

	
	}
	
	@Test
	public void testInterfaces(){
		
		final ReflectedClass<SomeObject> type = Reflector.getReflector().reflect(SomeObject.class);
		Enumerable<ReflectedClass<?>> i = type.getInterfaces();
		
		assertEquals(1, i.size());
		
	}
	
	@Test
	public void testMethodParameters(){
		ReflectedMethod method = Reflector.getReflector().reflect(SomeObject.class).getMethod("someOtherMethod", String.class, int.class);
		
		assertEquals(method.getParameters(), method.getParameters());
	}
	
	@Test
	public void testAnnotationsInParents(){
		
		Enumerable<ReflectedMethod> methods =  Reflector.getReflector().reflect(LogBookFactory.class).inspect().methods().notInheritFromObject().beingStatic(false).searchHierarchy().retriveAll();

		boolean found = false;
		for (ReflectedMethod method : methods){

			if (method.isAnnotationPresent(Publish.class)){
				found = true;
			}
		}
		
		assertTrue(found);
		
		Enumerable<ReflectedMethod> all = Reflector.getReflector().reflect(SomeObject.class).inspect()
		.methods()
		.notInheritFromObject()
		.annotatedWith(Wire.class).retriveAll();
		
		assertFalse(all.isEmpty());
	}
}
