package org.middleheaven.util.coersion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.Reflector;

public class TypeCoercingTest {

	private enum A {
		
		AAA,
		BBB,
		CCC
	}
	
	@Test
	public void testEnumCoercing() {
		
		EnumNameTypeCoersor<A> c = new EnumNameTypeCoersor<A>();
		
		//A a = c.coerceForward("AAA",A.class);
		
		Enumerable<ReflectedMethod> all = Reflector.getReflector().reflect(A.class)
		.inspect().staticMethods().named("valueOf").withParametersType(new Class<?>[]{String.class}).retriveAll();
		
		assertFalse(all.isEmpty());
		Object f = all.getFirst();
		
		assertNotNull(f);
		
		Object a = Reflector.getReflector().reflect(A.class)
				.inspect()
				.staticMethods()
				.named("valueOf").withParametersType(new Class<?>[]{String.class})
				.retrive()
				.invokeStatic(new Object[]{"AAA"});
				
		assertNotNull(a);
	}
}
