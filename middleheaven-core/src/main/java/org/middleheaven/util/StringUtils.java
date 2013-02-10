
package org.middleheaven.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.function.Maybe;

/**
 * Contains several utility methods when working with {@link String}s.
 */
public class StringUtils {

	StringUtils(){}
	
	/**
	 * Counts how much a given pattern appears in a given {@link CharSequence}.
	 * @param text the given text.
	 * @param pattern the given pattern.
	 * @return
	 */
	public static int countMatches(CharSequence text, String pattern){
		Pattern p = Pattern.compile(pattern);
		int count=0;
		final Matcher matcher = p.matcher(text);
		while (matcher.find()){
			count++;
		}
		return count;
	}
	
	public static String repeat(String pattern, int count){
		return repeat(pattern,count,"");
	}
	
	public static String repeat(String pattern, int count , String separator){
		String[] res = new String[count];
		Arrays.fill(res, pattern);
		return join(separator,res);
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

//	/**
//	 * Splits a CharSequence in the format paramname1=value1;paramname2=value2;...  
//	 * @param charSequence
//	 * @return
//	 */
//	public static Map<String,String> splitParams(CharSequence charSequence){
//		String[] paramPairs = split(charSequence, ";");
//
//		if (paramPairs.length==0){
//			return Collections.emptyMap();
//		}
//		HashMap<String, String> paramsMap = new HashMap<String, String>();
//		for (String s : paramPairs){
//			String[] p = split(s,"=");
//			paramsMap.put(p[0], p[1]);
//		}
//		return paramsMap;
//	}

//	public static String[] split(CharSequence charSequence, char delimiter){
//		return split(charSequence , Character.toString(delimiter));
//	}

//	/**
//	 * Split the given char sequence into an array of strings using the delimiter has would String.split() do.
//	 * However if the delimiter is not found the char sequence the char sequence it self will be returned
//	 * @param charSequence
//	 * @param delimiter
//	 * @return
//	 */
//	public static String[] split(CharSequence charSequence, String delimiter){
//		if (delimiter==null){
//			throw new IllegalArgumentException("parameter `delimiter` is required");
//		}
//		if (charSequence==null){
//			return new String[0];
//		}
//
//		String source = charSequence.toString();
//
//		if (source.indexOf(delimiter)<0){
//			// return the original sequence
//			return new String[]{source};
//		}
//
//		if (delimiter.equals(".")){
//			return source.split("\\.");
//		}else  if (delimiter.equals(" ")){
//			return source.split("\\s");
//		} else {
//			return source.split(delimiter);
//		}
//
//		
//
//	}

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
	 * A glob pattern only uses '*' for matching "all" and "one" 
	 * respectively
	 * @param simplePattern
	 * @return
	 */
	public static Pattern compile(String glob){

	    return Pattern.compile(
	            "^\\Q" 
	            + glob.replace("*", "\\E.*\\Q")
	                  .replace("?", "\\E.\\Q") 
	            + "\\E$");

	    
//		String pattern = glob.trim();
//	    int strLen = pattern.length();
//	    StringBuilder sb = new StringBuilder(strLen);
//	    // Remove beginning and ending * globs because they're useless
//	    if (pattern.startsWith("*"))
//	    {
//	        pattern = pattern.substring(1);
//	        strLen--;
//	    }
//	    if (pattern.endsWith("*"))
//	    {
//	        pattern = pattern.substring(0, strLen-1);
//	        strLen--;
//	    }
//	    boolean escaping = false;
//	    int inCurlies = 0;
//	    for (char currentChar : pattern.toCharArray())
//	    {
//	        switch (currentChar)
//	        {
//	        case '*':
//	            if (escaping)
//	                sb.append("\\*");
//	            else
//	                sb.append(".*");
//	            escaping = false;
//	            break;
//	        case '?':
//	            if (escaping)
//	                sb.append("\\?");
//	            else
//	                sb.append('.');
//	            escaping = false;
//	            break;
//	        case '.':
//	        case '(':
//	        case ')':
//	        case '+':
//	        case '|':
//	        case '^':
//	        case '$':
//	        case '@':
//	        case '%':
//	            sb.append('\\');
//	            sb.append(currentChar);
//	            escaping = false;
//	            break;
//	        case '\\':
//	            if (escaping)
//	            {
//	                sb.append("\\\\");
//	                escaping = false;
//	            }
//	            else
//	                escaping = true;
//	            break;
//	        case '{':
//	            if (escaping)
//	            {
//	                sb.append("\\{");
//	            }
//	            else
//	            {
//	                sb.append('(');
//	                inCurlies++;
//	            }
//	            escaping = false;
//	            break;
//	        case '}':
//	            if (inCurlies > 0 && !escaping)
//	            {
//	                sb.append(')');
//	                inCurlies--;
//	            }
//	            else if (escaping)
//	                sb.append("\\}");
//	            else
//	                sb.append("}");
//	            escaping = false;
//	            break;
//	        case ',':
//	            if (inCurlies > 0 && !escaping)
//	            {
//	                sb.append('|');
//	            }
//	            else if (escaping)
//	                sb.append("\\,");
//	            else
//	                sb.append(",");
//	            break;
//	        default:
//	            escaping = false;
//	            sb.append(currentChar);
//	        }
//	    }
//	    return Pattern.compile(sb.toString());


//		
//		if (simplePattern.startsWith("*") && simplePattern.endsWith("*")){
//			return Pattern.compile(simplePattern.replaceAll("\\*", ""));
//		} else if (simplePattern.startsWith("*")){
//			// end with text
//			return Pattern.compile(simplePattern.replaceAll("\\*", "") + "$");
//		} else if (simplePattern.endsWith("*")){
//			// starts with
//			return Pattern.compile("^" + simplePattern.replaceAll("\\*", ""));
//		} else {
//			// is exactly
//			return Pattern.compile("^" + simplePattern + "$");
//		}
	
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
		
		for (int i=1; i < builder.length(); i++){
			if (builder.charAt(i) == '-'){
				builder.setCharAt(i, Character.toUpperCase(builder.charAt(i+1)));
				builder.setCharAt(i+1, ' ');
				i++;
			}
		}
		
		return builder.toString().replaceAll(" ", "");
	}

	public static String firstLetterToLower(String text) {
		return text.substring(0,1).toLowerCase() + text.substring(1);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public static String firstLetterToUpper(String text) {
		return text.substring(0,1).toUpperCase() + text.substring(1);
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
	public static Maybe<String> maybe(String value) {
		return isEmptyOrBlank(value) ? Maybe.<String>absent() : Maybe.of(value);
	}

}
