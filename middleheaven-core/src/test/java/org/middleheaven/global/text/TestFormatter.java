package org.middleheaven.global.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.middleheaven.global.Culture;
import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.money.CentsMoney;
import org.middleheaven.quantity.unit.SI;

public class TestFormatter {

	@Test
	public void quantityMeasureFormatter(){
		QuantityFormatter formatterPtBR = new QuantityFormatter(Culture.valueOf("pt", "BR"));
		
		assertEquals("123\u00B134 m" ,formatterPtBR.format(DecimalMeasure.measure(123, 34, SI.METER)));
		assertEquals("123\u00B10,34 m" ,formatterPtBR.format(DecimalMeasure.measure(123, 0.34, SI.METER)));
		assertEquals("123,09\u00B10,34 N" ,formatterPtBR.format(DecimalMeasure.measure(123.09, 0.34, SI.NEWTON)));
	}
	
	@Test
	public void quantityMoneyFormatter(){
		QuantityFormatter formatterPtBR = new QuantityFormatter(Culture.valueOf("pt", "BR"));
		
		Real real = Real.valueOf("3000.68");
		
		assertEquals("R$ 3.000,68" ,formatterPtBR.format(CentsMoney.valueOf("3000.68", "BRL")));
		assertEquals("US$ 3.000,68" ,formatterPtBR.format(CentsMoney.valueOf("3000.68", "USD")));
		assertEquals("EUR 3.000,68" ,formatterPtBR.format(CentsMoney.valueOf("3000.68", "EUR")));
		
		QuantityFormatter formatterPtPT = new QuantityFormatter(Culture.valueOf("pt", "PT"));
		
		assertEquals("3.000,68 BRL" ,formatterPtPT.format(CentsMoney.valueOf("3000.68", "BRL")));
		assertEquals("3.000,68 USD" ,formatterPtPT.format(CentsMoney.valueOf("3000.68", "USD")));
		assertEquals("3.000,68 €" ,formatterPtPT.format(CentsMoney.valueOf("3000.68", "EUR")));
		
		QuantityFormatter formatterEnUS = new QuantityFormatter(Culture.valueOf("en", "US"));
		
		assertEquals("BRL3,000.68" ,formatterEnUS.format(CentsMoney.valueOf("3000.68", "BRL")));
		assertEquals("$3,000.68" ,formatterEnUS.format(CentsMoney.valueOf("3000.68", "USD")));
		assertEquals("EUR3,000.68" ,formatterEnUS.format(CentsMoney.valueOf("3000.68", "EUR")));
		
		
	}
	
	@Test
	public void quantityRealFormatter(){
		QuantityFormatter formatterPtBR = new QuantityFormatter(Culture.valueOf("pt", "BR"));
		
		assertEquals("3.000,68" ,formatterPtBR.format(Real.valueOf("3000.68")));
		assertEquals("3.000,0008" ,formatterPtBR.format(Real.valueOf("3000.0008")));
		assertEquals("300,0008" ,formatterPtBR.format(Real.valueOf("300.0008")));
		assertEquals("0,00098" ,formatterPtBR.format(Real.valueOf("0.00098")));
		
		QuantityFormatter formatterPtPT = new QuantityFormatter(Culture.valueOf("pt", "PT"));
		
		assertEquals("3.000,68" ,formatterPtPT.format(Real.valueOf("3000.68")));
		assertEquals("3.000,0008" ,formatterPtPT.format(Real.valueOf("3000.0008")));
		assertEquals("300,0008" ,formatterPtPT.format(Real.valueOf("300.0008")));
		
		QuantityFormatter formatterEnUS = new QuantityFormatter(Culture.valueOf("en", "US"));
		
		assertEquals("3,000.68" ,formatterEnUS.format(Real.valueOf("3000.68")));
		assertEquals("3,000.0008" ,formatterEnUS.format(Real.valueOf("3000.0008")));
		assertEquals("300.0008" ,formatterEnUS.format(Real.valueOf("300.0008")));
		
	}
	
	@Test
	public void quantityIntegerFormatter(){
		QuantityFormatter formatterPtBR = new QuantityFormatter(Culture.valueOf("pt", "BR"));
		
		assertEquals("3.000" ,formatterPtBR.format(BigInt.valueOf("3000")));
		assertEquals("3.012" ,formatterPtBR.format(BigInt.valueOf("3012")));
		assertEquals("300" ,formatterPtBR.format(BigInt.valueOf("300")));
	
		QuantityFormatter formatterPtPT = new QuantityFormatter(Culture.valueOf("pt", "PT"));
		
		assertEquals("3.000" ,formatterPtPT.format(BigInt.valueOf("3000")));
		assertEquals("3.000" ,formatterPtPT.format(BigInt.valueOf("3000")));
		assertEquals("300" ,formatterPtPT.format(BigInt.valueOf("300")));
		
		QuantityFormatter formatterEnUS = new QuantityFormatter(Culture.valueOf("en", "US"));
		
		assertEquals("3,000" ,formatterEnUS.format(BigInt.valueOf("3000")));
		assertEquals("3,000" ,formatterEnUS.format(BigInt.valueOf("3000")));
		assertEquals("300" ,formatterEnUS.format(BigInt.valueOf("300")));
		
	}
}
