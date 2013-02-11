/**
 * 
 */
package org.middleheaven.process.web.server.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mateches server paths to a given pattern.
 */
public final class PathMatcher {


	/**
	 * Creates a new instance of {@link PathMatcher} based on the given pattern.
	 * @param pattern the pattern to match.
	 * @return the created {@link PathMatcher}.
	 */
	public static PathMatcher newInstance(String pattern){

		if(pattern.contains("?")){
			throw new IllegalArgumentException("'?' is not a valid pattern character ");
		}
		
		if (pattern.contains("{")){
			StringBuilder builder = new StringBuilder(pattern);
			int posStart = builder.indexOf("{");
		    int posStartAll = builder.indexOf("*");
		    
			if ( posStartAll >= 0 && posStartAll < posStart){
				builder.replace(posStartAll, posStartAll+1, "{??}");
			}
			
			posStart = builder.indexOf("{");
			int posEnd;

			ArrayList<String> names = new ArrayList<String>();
			while (posStart > 0){

				posEnd = builder.indexOf("}", posStart+1);

				String name = builder.substring(posStart + 1, posEnd);

				builder.replace(posStart, posEnd + 1, "(.*)");

				
				names.add(name);
				
				posStart = builder.indexOf("{", posStart +1);
			}

			return new PathMatcher(names, Pattern.compile(builder.toString()));
		} else {
			return new PathMatcher(new ArrayList<String>(0),Pattern.compile(pattern.replace("*", "(.*)") + "$"));
		}

	}


	private Pattern pattern;
	private ArrayList<String> names;


	/**
	 * Constructor.
	 * @param names 
	 * @param compile
	 */
	private PathMatcher(ArrayList<String> names, Pattern pattern) {
		this.pattern = pattern;
		this.names = names;
	}


	public Map<String, String> parse(String url){
		if (names.isEmpty()){
			return Collections.emptyMap();
		}
		
		return getPathVariables(this.pattern, names, url);
	
	}

	private static final Map<String, String> getPathVariables(Pattern p,
			ArrayList<String> names, String path) {

		Map<String, String> tokenMap = new HashMap<String, String>();

		Matcher matcher = p.matcher(path);

		if (matcher.find()) {
			// Get all groups for this match
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				if (i != 0 && !names.get(i - 1).equals("??")) {
					tokenMap.put(names.get(i - 1), groupStr);
				}
			}
		}
		return tokenMap;
	}


	/**
	 * @param string
	 * @return
	 */
	public double match(String url) {
		if(this.pattern.matcher(url).find()){
			if(names.isEmpty()){
				return 1d;
			} else {
				return 0.5d;
			}
		}
		return 0d;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return this.pattern.toString() + "=>" + this.names.toString();
	}
}
