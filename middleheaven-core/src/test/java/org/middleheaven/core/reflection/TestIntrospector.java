package org.middleheaven.core.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;

import org.junit.Test;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.core.reflection.inspection.Introspector;


public class TestIntrospector {

	@Test(expected=WriteOnlyPropertyException.class)
	public void testPropertyAcessor(){
		
		SomeBean sb = new SomeBean();
		
		PropertyHandler pa = Introspector.of(SomeBean.class).inspect().properties().named("list").retrive();
		
		pa.setValue(sb, new LinkedList());
		
		pa.getValue(sb);
		
		pa = Introspector.of(SomeBean.class).inspect().properties().named("name").retrive();
		
		pa.setValue(sb, "A");
		
	
		pa.getValue(sb);

	
	}
	
	@Test
	public void testInterfaces(){
		
		Enumerable<Class<?>> i = Introspector.of(SomeObject.class).getImplementedInterfaces();
		
		assertEquals(1, i.size());
		
		i = Introspector.of(SomeObject.class).getDeclaredInterfaces();
		
		assertEquals(1, i.size());
	}
	
	
	@Test
	public void testAnnotationsInParents(){
		
		Enumerable<MethodHandler> all = Introspector.of(SomeObject.class).inspect()
		.methods()
		.notInheritFromObject()
		.annotatedWith(Wire.class).retriveAll();
		
		assertFalse(all.isEmpty());
	}
}
