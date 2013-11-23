package org.middleheaven.global.text;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.middleheaven.culture.Culture;
import org.middleheaven.global.text.writeout.FormatNotFoundException;
import org.middleheaven.global.text.writeout.NumberWriteoutFormatter;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

public class NumberWordsFormatTest extends MiddleHeavenTestCase{


	@Test(expected=FormatNotFoundException.class)
	public void testaNumberToWordsUnknown(){
		
		NumberWriteoutFormatter format = NumberWriteoutFormatter.getInstance(Culture.valueOf("gr","GR"));
	}
	
	@Test
	public void testaNumberToWordsPtPT(){
		
		
		NumberWriteoutFormatter format = NumberWriteoutFormatter.getInstance(Culture.valueOf("pt","PT"));
		
		assertEquals("zero", format.format(0));
		assertEquals("cem", format.format(100));
		assertEquals("vinte e cinco", format.format(25));
		assertEquals("cento e cinquenta e nove", format.format(159));
		assertEquals("três mil cento e cinquenta e nove", format.format(3159));
		assertEquals("treze mil cento e cinquenta e noventa e nove centésimos", format.format(13150.99));
		assertEquals("duzentos e treze mil cento e cinquenta e noventa e nove centésimos", format.format(213150.99));
		assertEquals("um milhão , duzentos e treze mil cento e cinquenta e oitocentos e noventa e nove milésimos", format.format(1213150.899));;
		assertEquals("um milhão , três mil cento e cinquenta e nove décimos", format.format(1003150.9));
		assertEquals("um milhão , três mil cento e cinquenta e nove mil quatrocentos e setenta e seis décimos milésimos", format.format(1003150.9476));

	}
	
	@Test
	public void testaNumberToWordsBrPT(){
		
		
		NumberWriteoutFormatter format = NumberWriteoutFormatter.getInstance(Culture.valueOf("pt","BR"));
		
		assertEquals("zero", format.format(0));
		assertEquals("cem", format.format(100));
		assertEquals("vinte e cinco", format.format(25));
		assertEquals("cento e cinquenta e nove", format.format(159));
		assertEquals("três mil cento e cinquenta e nove", format.format(3159));
		assertEquals("treze mil cento e cinquenta e noventa e nove centêsimos", format.format(13150.99));
		assertEquals("duzentos e treze mil cento e cinquenta e noventa e nove centêsimos", format.format(213150.99));
		assertEquals("um milhão , duzentos e treze mil cento e cinquenta e oitocentos e noventa e nove milêsimos", format.format(1213150.899));;
		assertEquals("um milhão , três mil cento e cinquenta e nove dêcimos", format.format(1003150.9));
		assertEquals("um milhão , três mil cento e cinquenta e nove mil quatrocentos e setenta e seis dêcimos milêsimos", format.format(1003150.9476));

	}
	
	@Test
	public void testaNumberToWordsEnGB(){
		
		
		NumberWriteoutFormatter format = NumberWriteoutFormatter.getInstance(Culture.valueOf("en", "GB"));
		
		assertEquals("zero", format.format(0));
		assertEquals("one hundred", format.format(100));
		assertEquals("one hundred and one", format.format(101));
		assertEquals("twenty-five", format.format(25));
		assertEquals("one hundred and fifty-nine", format.format(159));
		assertEquals("two thousand and one", format.format(2001));
		assertEquals("three thousand , one hundred and fifty-nine", format.format(3159));
		assertEquals("thirteen thousand , one hundred and fifty and ninety-nine hundredth", format.format(13150.99));
		assertEquals("two hundred and thirteen thousand , one hundred and fifty and ninety-nine hundredth", format.format(213150.99));
		assertEquals("one million , two hundred and thirteen thousand , one hundred and fifty and eight hundred and ninety-nine thousandth", format.format(1213150.899));;
		assertEquals("one million and three thousand , one hundred and fifty and nine tenth", format.format(1003150.9));
		assertEquals("one million and three thousand , one hundred and fifty and nine thousand , four hundred and seventy-six millionth", format.format(1003150.9476));

	}
	
	@Test
	public void testaNumberToWordsEsES(){
		
		
		NumberWriteoutFormatter format = NumberWriteoutFormatter.getInstance(Culture.valueOf("es", "ES"));
		
		assertEquals("cero", format.format(0));
		assertEquals("cien", format.format(100));
		assertEquals("ciento cincuenta", format.format(150));
		assertEquals("ciento uno", format.format(101));
		assertEquals("veinticinco", format.format(25));
		assertEquals("cuarenta y dos", format.format(42));
		assertEquals("ciento cincuenta y nueve", format.format(159));
		assertEquals("dos mil uno", format.format(2001));
		assertEquals("tres mil ciento cincuenta y nueve", format.format(3159));
		assertEquals("trece mil ciento cincuenta y noventa y nueve centesimas", format.format(13150.99));
		assertEquals("doscientos trece mil ciento cincuenta y noventa y nueve centesimas", format.format(213150.99));
		assertEquals("uno millón doscientos trece mil ciento cincuenta y ochocientos noventa y nueve milesimas", format.format(1213150.899));;
		assertEquals("uno millón tres mil ciento cincuenta y nueve decimas", format.format(1003150.9));
		assertEquals("uno millón tres mil ciento cincuenta y nueve mil cuatrocientos setenta y seis decimas milesimas", format.format(1003150.9476));

	}
	
	@Test @Ignore
	public void testaNumberToWordsFrFR(){
		
		
		NumberWriteoutFormatter format = NumberWriteoutFormatter.getInstance(Culture.valueOf("fr", "FR"));
		
		assertEquals("zero", format.format(0));
		assertEquals("cent", format.format(100));
		assertEquals("cent et un", format.format(101));
		assertEquals("cent cinquante", format.format(150));
		assertEquals("cent cinquante et un", format.format(151));
		assertEquals("vignt et cinq", format.format(25));
		assertEquals("quarante et deux", format.format(42));
		assertEquals("cent cinquante et neuf", format.format(159));
		assertEquals("deux mile et un", format.format(2001));
		assertEquals("troi mile et cent cinquante et neuf", format.format(3159));
		assertEquals("treize mile et cent cinquante et quatre-vignt-dix neuf centièmes", format.format(13150.99));
	
		//	assertEquals("doscientos trece mil ciento cincuenta y noventa y nueve centesimas", format.inWords(213150.99));
	//	assertEquals("uno mill�n doscientos trece mil ciento cincuenta y ochocientos noventa y nueve milesimas", format.inWords(1213150.899));;
	//	assertEquals("uno mill�n tres mil ciento cincuenta y nueve decimas", format.inWords(1003150.9));
	//	assertEquals("uno mill�n tres mil ciento cincuenta y nueve mil cuatrocientos setenta y seis decimas milesimas", format.inWords(1003150.9476));

	}
}
