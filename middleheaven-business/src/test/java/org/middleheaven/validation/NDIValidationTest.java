package org.middleheaven.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.global.gov.NDI;
import org.middleheaven.global.gov.br.BRCNPJValidator;
import org.middleheaven.global.gov.br.BRCPFValidator;
import org.middleheaven.global.gov.us.USSocialSecurityNumberValidator;
import org.middleheaven.validation.DefaultValidationResult;
import org.middleheaven.validation.LogicValidator;

public class NDIValidationTest {

	@Test
	public void testBRGovCPFValidate(){
		
		BRCPFValidator validator = new BRCPFValidator();
		NDI ndi;
		
		validator.setAcceptAllEqual(true);
		ndi = new NDI("11111111111");
	
		assertTrue(validator.validate(ndi).isValid());
		
		validator.setAcceptAllEqual(false);
			
	    ndi = new NDI("12345678909");
		assertTrue(validator.validate(ndi).isValid());
		

		ndi = new NDI("55847609604");
		assertTrue(validator.validate(ndi).isValid());
		
		
		ndi = new NDI("12345678919");
		assertFalse(validator.validate(ndi).isValid());
		

		ndi = new NDI("11111111111");

		assertFalse(validator.validate(ndi).isValid());
	}
	
	@Test
	public void testBRGovCNPJValidate(){
		
	
		DefaultValidationResult context = new DefaultValidationResult();
		NDI ndi;
		
		BRCNPJValidator validator = new BRCNPJValidator();

		context.clear();
		ndi = new NDI("11222333000181");
		assertTrue(validator.validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		assertTrue(validator.validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("11222333000196");
		assertFalse(validator.validate(ndi).isValid());

	}
	
	@Test
	public void testLogicValidatorValidate(){
		DefaultValidationResult context = new DefaultValidationResult();
		NDI ndi;
			
		BRCNPJValidator a = new BRCNPJValidator();
		BRCPFValidator b = new BRCPFValidator();
		
		
		// use a CNPJ

		ndi = new NDI("32249467000166");
		assertFalse(LogicValidator.and(a, b).validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		assertFalse(LogicValidator.and(a, b).validate(ndi).isValid());

		context.clear();
		ndi = new NDI("32249467000166");
		assertTrue(LogicValidator.or(a, b).validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		assertTrue(LogicValidator.or(a, b).validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("32249467000166");
		assertFalse(LogicValidator.or(a, b).validate(ndi).isValid());
	}
	
	@Test
	public void testUSGovValidate(){
		
		DefaultValidationResult context = new DefaultValidationResult();
		USSocialSecurityNumberValidator validator = new USSocialSecurityNumberValidator();
		
		NDI ndi = new NDI("123456789");
		
		assertTrue(validator.validate(ndi).isValid());
		
		ndi = new NDI("987654328");
		
		assertFalse(validator.validate(ndi).isValid());

		context.clear();
		ndi = new NDI("77390890");

		assertFalse(validator.validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("00089765");
		
		assertFalse(validator.validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("12300765");

		assertFalse(validator.validate(ndi).isValid());
		
		context.clear();
		ndi = new NDI("12367000");
		
		assertFalse(validator.validate(ndi).isValid());
		
		// accept advertisement
		ndi = new NDI("987654328");
		
		context.clear();
		validator.setAcceptAdvertisementReserved(true);

		assertTrue(validator.validate(ndi).isValid());
	}
}
