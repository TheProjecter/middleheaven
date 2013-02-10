/**
 * 
 */
package org.middleheaven.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.collections.Pair;
import org.middleheaven.util.collections.PairEnumerable;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class Splitter {

	
	public interface KeyValueSplitter {
		
		public PairEnumerable<String,String> split(CharSequence charSequence);

		public KeyValueSplitter trim();
		

	}


	private static class KeyValueSplitterImpl  implements KeyValueSplitter{
		
		
		private Splitter spliter;
		private String keyValueSeparator;
		private Mapper<String, String> transform = new Mapper <String, String> (){

			@Override
			public String apply(String obj) {
				return obj;
			}
			
		};

		public KeyValueSplitterImpl(Splitter spliter, String keyValueSeparator){
			this.spliter = spliter;
			this.keyValueSeparator = keyValueSeparator;
		}

		@Override
		public PairEnumerable<String, String> split(CharSequence charSequence) {
			return spliter.split(charSequence).pairMap(new Mapper<Pair<String,String>, String>(){

				@Override
				public Pair<String, String> apply(String value) {
					final String[] p = value.split(keyValueSeparator);
					return new Pair<String, String>(){

						@Override
						public String getKey() {
							return p[0];
						}

						@Override
						public String getValue() {
							return p[1];
						}
						
					};
				}
				
			}).pairMap(new Mapper<Pair<String,String>, Pair<String,String>>(){

				@Override
				public Pair<String, String> apply(final Pair<String, String> pair) {
					return new Pair<String,String>(){

						@Override
						public String getKey() {
							return transform.apply(pair.getKey());
						}

						@Override
						public String getValue() {
							return transform.apply(pair.getValue());
						}
						
					};
				}
				
			});
		}

		@Override
		public KeyValueSplitter trim() {
			KeyValueSplitterImpl trimmed = new KeyValueSplitterImpl(this.spliter, this.keyValueSeparator);
			
			trimmed.transform = new Mapper <String, String> (){

				@Override
				public String apply(String obj) {
					return obj.trim();
				}
				
			};
			
			return trimmed;
		}
		
	}
	
	public static Splitter on(String delimiter) {
		return new Splitter(delimiter);
	}
	
	public static Splitter on(Pattern pattern) {
		return on(pattern.pattern());
	}
	
	public static Splitter on(char delimiter) {
		return new Splitter(new String(new char[]{delimiter}));
	}

	private String delimiter;
	private Mapper<String, String> transform = new Mapper<String,String>(){

		@Override
		public String apply(String object) {
		 return object;
		}
		
	};
	
	private Predicate<String> filter = new Predicate<String>(){

		@Override
		public Boolean apply(String obj) {
			return Boolean.TRUE;
		}
		
	};
	
	
	private Splitter (String delimiter){
		this.delimiter = delimiter;
	}
	
	public KeyValueSplitter withKeyValueSeparator (String paramsSpliter){
		return new KeyValueSplitterImpl(this,paramsSpliter);
	}
	
	
	public Splitter trim(){
		Splitter s = new Splitter(this.delimiter);
		s.transform = new Mapper<String,String>(){

			@Override
			public String apply(String object) {
			 return object.trim();
			}
			
		};
		s.filter = new Predicate<String>(){

			@Override
			public Boolean apply(String obj) {
				return obj.length() > 0;
			}
			
		};
		return s;
	}
	
	public Enumerable<String> split(CharSequence charSequence){
		
		if(charSequence == null){
			throw new IllegalArgumentException("Argument is null");
		}
		String source = charSequence.toString();

		Enumerable<String> res;
		if (source.indexOf(delimiter)<0){
			// return the original sequence
			res = CollectionUtils.asEnumerable(source);
		}

		if (delimiter.equals(".")){
			res = CollectionUtils.asEnumerable(source.split("\\."));
		}else  if (delimiter.equals(" ")){
			res = CollectionUtils.asEnumerable(source.split("\\s"));
		} else {
			res = CollectionUtils.asEnumerable(source.split(delimiter));
		}
		
		
		return res.map(transform).filter(filter);
		
	}
}
