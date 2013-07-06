
package org.middleheaven.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.util.function.Maybe;

/**
 * Contains several utility methods when working with {@link String}s.
 */
public class StringUtils {

	protected StringUtils(){}
	
	/**
	 * Counts how much a given pattern appears in a given {@link CharSequence}.
	 * @param text the given text.
	 * @param pattern the given pattern.
	 * @return how much a given pattern appears in a given text.
	 */
	public static int countMatches(CharSequence text, CharSequence pattern){
		Pattern p = Pattern.compile(pattern.toString());
		int count=0;
		final Matcher matcher = p.matcher(text);
		while (matcher.find()){
			count++;
		}
		return count;
	}
	
	/**
	 * Creates a string by repeadly concatenating a pattern with it self {@code count} times.
	 * @param pattern pattern to repeat
	 * @param count how many times 
	 * @return
	 */
	public static String repeat(String pattern, int count){
		StringBuilder builder = new StringBuilder();
		
		for (int i = 1 ; i < count; i++){
			builder.append(pattern);
		}
		
		builder.append(pattern);
		return builder.toString();
	}
	
	/**
	 * Creates a string by repeadly concatenating a pattern with it self {@code count} times, separatin each concatenation with a {@code separator}
	 * @param pattern
	 * @param count
	 * @param separator
	 * @return
	 */
	public static String repeat(String pattern, int count , String separator){
		StringBuilder builder = new StringBuilder();
		
		for (int i = 1 ; i < count; i++){
			builder.append(pattern).append(separator);
		}
		
		builder.append(pattern);
		return builder.toString();
	}

	public static String ensureEndsWith(String text, String suffix){
		if(text==null){
			return suffix;
		}
		
		if (!text.endsWith(suffix)){
			return text.concat(suffix);
		} 
		return text;
	}
	

	public static String ensureStartsWith(String text, String prefix){
		if(text==null){
			return prefix;
		}
		
		if (!text.startsWith(prefix)){
			return prefix.concat(text);
		} 
		return text;
	}
	
	public static String join(String separator, String ... original){
		return join(separator, 0,original.length, original);
	}

	public static String join(String separator, String first,  String second){
		return join(separator, 0, new String[]{first,second});
	}
	
	public static String join(String separator, String first,  String ... original){
		return join(separator, 0,original.length + 1, CollectionUtils.appendToArrayBegining(original, first));
	}
	
	public static String join(String separator, int start, String ... original){
		return join(separator, start, original.length, original);
	}
	
	public static String join(String separator, int start, int end, String ... original){
		StringBuilder builder = new StringBuilder();
		for (int i=start; i < end;i++){
			builder.append(original[i]).append(separator);
		}
		if(builder.length()>0){
			builder.delete(builder.length()-separator.length(), builder.length());
		}
		return builder.toString();
	}

	public static String concatIfNotEmptyPrefix(CharSequence prefix, CharSequence content, CharSequence separator ){
		if (isEmptyOrBlank(prefix)){
			return content.toString();
		} else {
			return new StringBuilder(prefix).append(separator).append(content).toString();
		}
	}
	
	public static boolean isInArray(CharSequence candidate, CharSequence ... set){
		if (candidate==null || set.length == 0){
			return false;
		}

		for (CharSequence s: set){
			if (candidate.equals(s)){
				return true;
			}
		}
		return false;
	}

	public static Boolean booleanValueOf (CharSequence value){
		if (value==null || value.toString().trim().isEmpty()) {
			// explicitly return null
			return null;
		}
		return Boolean.valueOf(logicValueOf(value,false));
	}

	public static Boolean booleanValueOf (CharSequence value, boolean defaultValue){
		return Boolean.valueOf(logicValueOf(value,defaultValue));
	}

	public static String objectToString(Object obj){
		return objectToString(obj,"");
	}

	/**
	 * Removes all occurrences of {@code textToRemove} from {@code text} 
	 * @param text
	 * @param textToRemove
	 * @return
	 */
	public static String removeAll(CharSequence text, CharSequence textToRemove){
		return text.toString().replaceAll(textToRemove.toString(), "");
	}
	
	/**
	 * Removes the first ocurrence of {@code textToRemove} in {@code text} an all the text
	 * before it. 
	 * @param text
	 * @param textToRemove
	 * @return
	 */
	public static String removeStart(CharSequence text, CharSequence textToRemove){
		final String textString = text.toString();
		int pos = textString.indexOf(textToRemove.toString());
		if (textToRemove.length() != 0 && pos>=0){
			return textString.substring(pos+1+textToRemove.length());
		} else {
			return textString;
		}
	}
	
	/**
	 * Removes the first ocurrence of {@code textToRemove} in {@code text} an all the text
	 * after it. 
	 * @param text
	 * @param textToRemove
	 * @return
	 */
	public static String removeEnd(CharSequence text, CharSequence textToRemove){
		final String textString = text.toString();
		int pos = textString.indexOf(textToRemove.toString());
		if (textToRemove.length() != 0 && pos>=0){
			return textString.substring(0,pos);
		} else {
			return textString;
		}
	}
	
	public static String objectToString(Object obj, String defaultValue){
		return obj == null ? defaultValue : obj.toString(); 
	}

	public static boolean logicValueOf (final CharSequence value, boolean defaultValue){
		if (value==null){
			return defaultValue;
		}
		String v = value.toString().trim();
		return v.indexOf('y')==0 || v.indexOf('t')==0 || v.indexOf("on")==0;
	}

	public static long longValueOf (final CharSequence value, long defaultValue){
		if (value==null || value.toString().trim().isEmpty()){
			return defaultValue;
		}
		try {
			return Long.parseLong(value.toString().trim());
		} catch (NumberFormatException e){
			return defaultValue;
		}
	}

	public static String getStackStrace(Throwable t){
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}

	// TODO put in a StringFormat
	public static String capitalizeFirst(String text) {
		StringBuilder builder  = new StringBuilder(text);
		builder.replace(0, 1, builder.substring(0,1).toUpperCase());
		return builder.toString();
	}
	
	
	public static boolean simplePatternMatch(String simplePattern, CharSequence text){
		return compile(simplePattern).matcher(text).find();
	}
	
	/**
	 * Compiles a glob pattern string into a regex {@link Pattern}.
	 * A glob pattern only uses '*' and '?' for matching "all" and "one" 
	 * respectively
	 * @param simplePattern a simple glob pattern
	 * @return the pattern.
	 */
	public static Pattern compile(String glob){

	    return Pattern.compile(
	            "^\\Q" 
	            + glob.replace("*", "\\E.*\\Q")
	                  .replace("?", "\\E.\\Q") 
	            + "\\E$");
	}
	
	public static String removeDiacritics(CharSequence text) {
	    return Normalizer.normalize(text, Normalizer.Form.NFD)
	    .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); 
	}
	
	public static String camelCaseToIffenDelimited(CharSequence text){
		StringBuilder builder = new StringBuilder(text);
		
		for (int i=1; i < builder.length(); i++){
			if (Character.isUpperCase(builder.charAt(i))){
				builder.setCharAt(i, Character.toLowerCase(builder.charAt(i)));
				builder.insert(i, "-");
				i++;
			}
		}
		
		return builder.toString();
	}
	
	public static String iffenDelimitedToCamelCase(CharSequence text){
		StringBuilder builder = new StringBuilder(text);
		
		int pos = builder.indexOf("-");
		while (pos >=0){
			builder.setCharAt(pos, Character.toUpperCase(builder.charAt(pos+1)));
			builder.deleteCharAt(pos+1);
			pos = builder.indexOf("-");
		}
		
		return builder.toString();
	}

	public static String firstLetterToLower(CharSequence text) {
		StringBuilder builder = new StringBuilder(text);
		final char c = builder.charAt(0);
		builder.setCharAt(0, Character.toLowerCase(c));
		return builder.toString();
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String firstLetterToUpper(CharSequence text) {
		StringBuilder builder = new StringBuilder(text);
		final char c = builder.charAt(0);
		builder.setCharAt(0, Character.toUpperCase(c));
		return builder.toString();
	}


	/**
	 * @param value
	 * @return
	 */
	public static boolean isEmptyOrBlank(CharSequence value) {
		return value == null || value.toString().trim().length() == 0;
	}

	/**
	 * Return a {@link CharSequence} constituted from the <code>max</code> first characters in the sequence.
	 * @param value the source of characters
	 * @param max the maximum of characters to return.
	 * @return  a {@link CharSequence} constituted from the <code>max</code> first characters in the sequence, 
	 * or <code>null</code> if <code>value</code> is null
	 */
	public static CharSequence subString(CharSequence value, int max) {
		return value.subSequence(0, Math.min(value.length(), max));
	}

	/**
	 * Returns an absent {@link Maybe} if {@code this#isEmptyOrBlank(CharSequence)} returns <code>true</code>, otherwise returns a {@link Maybe} with the given string inside.
	 * @param value a given {@link String}.
	 */
	public static <C extends CharSequence> Maybe<C> maybe(C value) {
		return isEmptyOrBlank(value.toString()) ? Maybe.<C>absent() : Maybe.of(value);
	}

}
