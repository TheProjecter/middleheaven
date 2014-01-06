package org.middleheaven.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.validation.DefaultValidationResult;
import org.middleheaven.validation.NotEmptyValidator;
import org.middleheaven.validation.SimpleEmailAddressValidator;
import org.middleheaven.validation.ValidationResult;
import org.middleheaven.validation.Validator;
import org.middleheaven.validation.ValidatorBuilder;

public class TestValidators {

	
	@Test
	public void testNotEmptyValidator (){
		
		Validator<String> validator =  new NotEmptyValidator<String>();
		
		assertFalse(validator.validate("").isValid());
		assertFalse(validator.validate("   ").isValid());
		assertTrue(validator.validate("something").isValid());
		assertTrue(validator.validate("  something  ").isValid());
	}
	
	@Test
	public void testSimpleEmailValidator (){
		
		Validator<String> validator =  new SimpleEmailAddressValidator<String>();
		
		assertFalse(validator.validate("").isValid());
		assertFalse(validator.validate("   ").isValid());
		assertFalse(validator.validate("some.usersome.server.com").isValid());
		assertTrue(validator.validate("some.user@some.server.com").isValid());
		
	}
	
	@Test
	public void testValidationBuilder (){
		
		Validator<Company> validator = ValidatorBuilder.newValidatorFor(Company.class)
				.property("name").isNotEmpty()
				.property("email").isAValidEmail()
				.property("").instanceWith(new Validator<Company>(){

					@Override
					public ValidationResult validate(Company object) {
						return new DefaultValidationResult();
					}})
				.validator();
		
		assertFalse(validator.validate(new Company()).isValid());
		
		Company comp = new Company();
		comp.setEmail("some.email@some.server.com");
		comp.setName("some name");
		assertTrue(validator.validate(comp).isValid());
	}
	
}
