package org.middleheaven.math.expression;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;


public class ExpressionParserTest {



	@Test
	public void testParser(){
		Expression exp;
		InfixParser parser = new InfixParser();
		 exp = parser.parse("3-4*5");
		
		assertEquals("5;4;*;3;-;" , exp.toString());
		
		 exp = parser.parse("(3-4)*5");
		
		assertEquals("4;3;-;5;*;" , exp.toString());
		
		exp = parser.parse("(3-4)*5 + 4*(1-9)+3");
			
		assertEquals("3;9;1;-;4;*;4;3;-;5;*;+;+;" , exp.toString());
			
		 exp = parser.parse("3-4*5+15/3+32*5+2^4");
		
		assertEquals("4;2;^;5;32;*;3;15;/;5;4;*;3;-;+;+;+;" , exp.toString());
		
		 exp = parser.parse("(3-4)*5+15/3+32*5+2^4");
		
		assertEquals("4;2;^;5;32;*;3;15;/;4;3;-;5;*;+;+;+;" , exp.toString());
	}
	
	@Test
	public void testEvaluate(){
		
		InfixParser parser = new InfixParser();

		assertEquals(new BigDecimal(-17), parser.parse("3-4*5").evaluate());
		assertEquals(new BigDecimal(-5), parser.parse("(3-4)*5").evaluate());
		assertEquals(new BigDecimal(-34),  parser.parse("(3-4)*5 + 4*(1-9)+3").evaluate());
		assertEquals(new BigDecimal(164),  parser.parse("3-4*5+15/3+32*5+2^4").evaluate());
	}
	
	@Test
	public void testLiteral(){
		
		Expression exp;
		InfixParser parser = new InfixParser();
		 exp = parser.parse("3-x^5");
		
		assertEquals("5;x;^;3;-;" , exp.toString());
		
		 exp = parser.parse("-5*x^4");
			
		assertEquals("4;x;^;5;*;-;" , exp.toString());
			
		 exp = parser.parse("(3-4)*PI");
		
		assertEquals("4;3;-;PI;*;" , exp.toString());
	}
}
