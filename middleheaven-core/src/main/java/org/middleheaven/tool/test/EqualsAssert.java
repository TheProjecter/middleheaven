package org.middleheaven.tool.test;

import java.lang.reflect.Modifier;

public class EqualsAssert {


	private EqualsAssert(){}

	public static <T> void assertEqualsImplementation(T original ,  T diferent, T  duplicated ){
		assertEqualsImplementation(original, diferent, duplicated, null, null);
	}
	
	public static <T> void assertEqualsImplementation(T original ,  T diferent, T  duplicated,  T  subClassEquals  ){
		assertEqualsImplementation(original, diferent, duplicated, subClassEquals , null );
	}
	
	public static <T> void assertEqualsImplementation(T original ,  T diferent, T  duplicated,   T subClassEquals ,T otherDuplicated ){


		if(original == null){
			throw new IllegalArgumentException("Original cannnot be null");
		} else if(duplicated == null){
			throw new IllegalArgumentException("duplicated cannnot be null");
		} else if(diferent == null){
			throw new IllegalArgumentException("diferent cannnot be null");
		}  else if(original == duplicated){
			throw new IllegalArgumentException("duplicated cannot be the same object than original");
		}

		if( !isClassFinal( original.getClass() ) && subClassEquals == null) {
			throw new IllegalArgumentException("subClassEquals cannnot be null because " + original.getClass() + " is open to inheritance");
		}
		
		assertEqualsImplemented(original.getClass());
		assertHashCodeImplemented(original.getClass());
		
		assertSameClass( original, duplicated, "Duplicate");
		assertSameClass( original, diferent, "Diferent" );

		assertNotEqualsNull( original );
		assertEqualsSelf( original );
		
		assertNotEqualsNull( duplicated );
		assertEqualsSelf( duplicated );
		
		assertNotEqualsNull( diferent );
		assertEqualsSelf( diferent );
		
		
		assertEqualsDuplicate( original, duplicated );
		assertNotEqualsDifferent(original, diferent);
		
		if (subClassEquals!=null){
			assertClassEqualsSubclass( original, subClassEquals );
			assertDifferentClass( original, subClassEquals );

			assertNotEqualsNull( subClassEquals );
			assertEqualsSelf( subClassEquals );
			
			
		}
		
		if (otherDuplicated != null){ // transitive
			assertEqualsDuplicate(duplicated, otherDuplicated);
			assertEqualsDuplicate(original, otherDuplicated);
		}
	}

	private static void assertEqualsImplemented(Class<? extends Object> type) {
		try {
			type.getDeclaredMethod("equals", new Class[]{Object.class});
		} catch (SecurityException e) {
			// TODO Handle SecurityException on EqualsAssert.assertEqualsImplemented 
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			fail("Equals is not overriden");
		}
	
	}
	
	private static void assertHashCodeImplemented(Class<? extends Object> type) {
		try {
			type.getDeclaredMethod("hashCode", new Class[0]);
		} catch (SecurityException e) {
			// TODO Handle SecurityException on EqualsAssert.assertEqualsImplemented 
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			fail("hashCode is not overriden");
		}
	
	}

	private static void assertDifferentClass( final Object a, final Object d ) {
		if( a.getClass() == d.getClass() ) {
			fail(
					"D must not be of same class as A.  A.class=["
					+ a.getClass().getName()
					+ "] d.class=["
					+ d.getClass().getName()
					+ "]" );
		}
	}

	private static void assertClassEqualsSubclass( final Object original, final Object subClassEqual ) {
	
			if( !original.equals( subClassEqual ) ) {
				fail( "original not equals  subClassEqual" );
			}

			if( !subClassEqual.equals( original )) {
				fail( "subClassEqual not equals original" );
			}
		
	}


	private static void assertSameClass( final Object a, final Object object, final String name ) {
		if( a.getClass() != object.getClass() ) {
			fail(
					name
					+ " must be of same class as A.  A.class=["
					+ a.getClass().getName()
					+ "] "
					+ name
					+ ".class=["
					+ object.getClass().getName()
					+ "]" );
		}
	}

	private static void fail(String message) {
		throw new AssertionError( message);
	}

	private static boolean isClassFinal( final Class<?> type ) {
		return Modifier.isFinal( type.getModifiers() );
	}

	private static void assertNotEqualsNull( Object a ) {
		try {
			// explicitly compare with null 
			if( a.equals( null ) ) {
				fail( "original.equals(null) returned true");
			}
		} catch( Exception e ) {
			fail( "original.equals(null) threw an Exception.  It should have returned false" );
		}
	}

	private static void assertEqualsSelf( Object a ) {
		assertTrue( "Self equals", a.equals( a ) );
		assertEquals( "Self hashcode equals", a.hashCode(), a.hashCode() );
	}

	private static void assertEqualsDuplicate( final Object a, final Object b ) {
		assertTrue( "Original equals Duplicate", a.equals( b ) );
		assertTrue( "Duplicate equals Original", b.equals( a ) );
		assertEquals( "Original hashcode equals Duplicate  hashcode", a.hashCode(), b.hashCode() );
	}

	private static void assertNotEqualsDifferent( final Object a, final Object c ) {
		         assertFalse( "Original equals Diferent", a.equals( c ) );
		         assertFalse( "Diferent equals Original", c.equals( a ) );
	}

	private static void assertEquals(String message, int a, int b) {
		if (a != b){
			throw new AssertionError(message);
		}
	}

	private static void assertTrue( String message, boolean value ) {
		if (!value){
			throw new AssertionError(message);
		}
	}

	private static void assertFalse( String message, boolean value ) {
		if (value){
			throw new AssertionError(message);
		}
	}




}
