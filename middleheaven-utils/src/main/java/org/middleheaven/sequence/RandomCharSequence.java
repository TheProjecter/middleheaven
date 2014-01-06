/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.sequence;

import java.util.Random;

/**
 * @author  Sergio M. M. Taborda 
 */
public class RandomCharSequence implements RandomSequence<CharSequence>{


	public enum TemplateType {
		NUMERIC, ALPHA_NUMERIC , ALPHABETIC
	}

	private static CharSequence getModeTemplate(TemplateType mode){
		StringBuilder builder;
		switch (mode){
		case NUMERIC:
			return "1234567890";
		case ALPHABETIC:
			builder = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
			builder.append(builder.toString().toUpperCase());
			return builder;
		case ALPHA_NUMERIC:
			builder = new StringBuilder("abcdefghijklmnopqrstuvwxyz");
			builder.append(builder.toString().toUpperCase());
			builder.append("1234567890");
			return builder;
		default :
			throw new IllegalArgumentException (mode.toString() + " is not a recognized TemplateType");    
		}
	}

	public static RandomCharSequence numeric(int length){
		return  numeric(length,new Random());
	}

	public static RandomCharSequence alphabetic(int length){
		return  alphabetic(length,  new Random());
	}

	public static RandomCharSequence alphabeticLowercase(int length){
		return  alphabeticLowercase(length, new Random());
	}

	public static RandomCharSequence alphabeticUppercase(int length){
		return  alphabeticUppercase(length, new Random());
	}

	public static RandomCharSequence alphaNumeric(int length){
		return  alphaNumeric(length, new Random());
	}

	public static RandomCharSequence subSequenceOf(CharSequence template , int length ){
		return subSequenceOf(template,length, new Random());
	}

	public static RandomCharSequence numeric(int length, Random random){
		return new RandomCharSequence(length,getModeTemplate(TemplateType.NUMERIC),random);
	}

	public static RandomCharSequence alphabetic(int length, Random random){
		return new RandomCharSequence(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",random);
	}

	public static RandomCharSequence alphabeticLowercase(int length, Random random){
		return new RandomCharSequence(length,getModeTemplate(TemplateType.ALPHABETIC),random);
	}

	public static RandomCharSequence alphabeticUppercase(int length, Random random){
		return new RandomCharSequence(length,getModeTemplate(TemplateType.ALPHABETIC).toString().toUpperCase(),random);
	}

	public static RandomCharSequence alphaNumeric(int length, Random random){
		return new RandomCharSequence(length,getModeTemplate(TemplateType.ALPHA_NUMERIC),random);
	}

	public static RandomCharSequence subSequenceOf(CharSequence template , int length, Random random ){
		return new RandomCharSequence(length,template,random);
	}

	int length;
	CharSequence template;
	Random random;
	protected RandomCharSequence(int length,CharSequence template, Random random){
		this.length = length;
		this.template = template;
		this.random = random;
	}

	@Override
	public SequenceToken<CharSequence> next() {
		final StringBuilder buffer = new StringBuilder();
		int templateLength = this.template.length();
		while (buffer.length () < length) {
			char c = template.charAt(random.nextInt(templateLength));
			buffer.append(c);
		}
		return new DefaultToken<CharSequence>(buffer);

	}

}
