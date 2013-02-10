/**
 * 
 */
package org.middleheaven.util;

import java.util.Arrays;


/**
 * 
 */
public class Joiner {

	private String separator;
	
	private Joiner(String separator){
		this.separator = separator;
	}
	
	public static Joiner with(String separator){
		return new Joiner(separator);
	}

	public String join(Iterable<?> all){
		StringBuilder builder = new StringBuilder();
		for (Object s : all){
			builder.append(s.toString()).append(separator);
		}
		// delete last separator
		if(builder.length()>0){
			builder.delete(builder.length()-separator.length(), builder.length());
		}
		return builder.toString();
	}
	
	
	public String join(String ... all){
		return join(Arrays.asList(all));
	}
	
}
