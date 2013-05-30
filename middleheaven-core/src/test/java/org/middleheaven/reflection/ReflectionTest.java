package org.middleheaven.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.ClassCastReflectionException;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;


public class ReflectionTest {

	
	@Test
	public void testInstrospectionLoad(){
		
		ClassIntrospector<CharSequence> type = Introspector.of(CharSequence.class).load(String.class.getName());
		
		CharSequence s = type.newInstance();
		
		assertTrue(s instanceof String);
	}
	
	@Test(expected=ClassCastReflectionException.class)
	public void testInstrospectionIncorrectLoad(){
		
		// cannot load a charsequence as a string
		Introspector.of(String.class).load(CharSequence.class.getName());
		
	}
	
	
	@Test
	public void testProxyNotInterfaceWithArguments(){
		
		TestType instance = Introspector.of(TestType.class).newProxyInstance(new ProxyHandler() {
			
			@Override
			public Object invoke(Object proxy, Object[] args, MethodDelegator delegator)
					throws Throwable {
				return delegator.invokeSuper(proxy, args);
			}
		}, "a", 1);
		
		assertNotNull(instance);
		assertEquals("a1", instance.test());
		
	}

	@Test
	public void testInstropection(){
		
		List<Constructor<ArrayList>> c = new ArrayList<Constructor<ArrayList>>();
		
		 Introspector.of(ArrayList.class)
		.inspect().constructors()
		.sortedByQuantityOfParameters()
		.withAccess(MemberAccess.PUBLIC)
		.retriveAll().into(c);
		
		assertEquals(3 , c.size());
		assertTrue( c.get(0).getParameterTypes().length <= c.get(1).getParameterTypes().length);
		assertTrue( c.get(1).getParameterTypes().length <= c.get(2).getParameterTypes().length);
		
		Enumerable<MethodHandler> m = Introspector.of(String.class)
		.inspect().methods()
		.notInheritFromObject()
		.beingStatic(false)
		.withAccess(MemberAccess.PUBLIC)
		.retriveAll();
		
		assertEquals(54,m.size());
	}
	

	public void testwrongProxyClass(){
		
		Cloneable obj = Introspector.of(Date.class).newProxyInstance(new TestMethodHandler(), Cloneable.class);

		assertTrue("Is not a Clonable",obj instanceof Cloneable);
		assertTrue("Is not a Date", obj instanceof Date);
	}
	
	@Test
	public void testProxy(){
		
		Cloneable obj = Introspector.of(CharSequence.class).newProxyInstance(new TestMethodHandler(), Cloneable.class);

		assertTrue("Is not a Clonable",obj instanceof Cloneable);
		assertTrue("Is not a CharSequence", obj instanceof CharSequence);
	}
	
	public class TestMethodHandler  implements ProxyHandler {
		

		@Override
		public Object invoke(Object self, Object[] args,
				MethodDelegator delegator) throws Throwable {
			System.out.println("Name: " + delegator.getName());
			return delegator.invokeSuper(self, args);  // execute the original method.
		}
	}
}
