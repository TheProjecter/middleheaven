/**
 * 
 */
package org.middleheaven.util;

import java.util.regex.Pattern;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.collections.Pair;
import org.middleheaven.collections.PairEnumerable;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class Splitter {

	public interface KeyValueSplitter {

		public PairEnumerable<String, String> split(CharSequence charSequence);

		public KeyValueSplitter trim();

	}
	
	public static class AllwaysTrue implements Predicate<String>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(String obj) {
			return Boolean.TRUE;
		}

	}
	
	public static class NotEmpty implements Predicate<String>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(String obj) {
			return obj != null && obj.length() > 0;
		}

	}
	
	public static class SelfMapper implements Mapper<String, String>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String apply(String obj) {
			return obj;
		}
		
	}
	
	public static class TrimMapper implements Mapper<String, String>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String apply(String obj) {
			return obj.trim();
		}
		
	}
	
	public static class PairMapper implements Mapper<Pair<String, String>, String> {

		
		private String keyValueSeparator;
		private Mapper<String, String> transform;

		public PairMapper (String keyValueSeparator , Mapper<String, String> transform){
			
			this.keyValueSeparator= keyValueSeparator;
			this.transform = transform;
		}
		
		@Override
		public Pair<String, String> apply(String value) {
			final String[] p = value.split(keyValueSeparator);
			return new Pair<String, String>() {

				@Override
				public String getKey() {
					return transform.apply(p[0]);
				}

				@Override
				public String getValue() {
					return transform.apply(p[1]);
				}

			};
		}

	};

	private static class KeyValueSplitterImpl implements KeyValueSplitter {

		private Splitter spliter;
		private String keyValueSeparator;
		private Mapper<String, String> transform = new SelfMapper();
		
		public KeyValueSplitterImpl(Splitter spliter, String keyValueSeparator) {
			this.spliter = spliter;
			this.keyValueSeparator = keyValueSeparator;
		}

		@Override
		public PairEnumerable<String, String> split(CharSequence charSequence) {
			return spliter
					.split(charSequence)
					.pairMap(new PairMapper(keyValueSeparator, transform));
				
		}

		@Override
		public KeyValueSplitter trim() {
			KeyValueSplitterImpl trimmed = new KeyValueSplitterImpl(
					this.spliter, this.keyValueSeparator);

			trimmed.transform = new TrimMapper();

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
		return new Splitter(new String(new char[] { delimiter }));
	}

	private String delimiter;
	private Mapper<String, String> transform = new SelfMapper();
	private Predicate<String> filter = new AllwaysTrue();

	private Splitter(String delimiter) {
		this.delimiter = delimiter;
	}

	public KeyValueSplitter withKeyValueSeparator(String paramsSpliter) {
		return new KeyValueSplitterImpl(this, paramsSpliter);
	}

	public Splitter trim() {
		Splitter s = new Splitter(this.delimiter);
		s.transform = new TrimMapper();
		s.filter = new NotEmpty();
		return s;
	}

	public Enumerable<String> split(CharSequence charSequence) {

		if (charSequence == null) {
			throw new IllegalArgumentException("Argument is null");
		}
		String source = charSequence.toString();

		Enumerable<String> res;
		if (source.indexOf(delimiter) < 0) {
			// return the original sequence
			res = CollectionUtils.asEnumerable(source);
		}

		if (delimiter.equals(".")) {
			res = CollectionUtils.asEnumerable(source.split("\\."));
		} else if (delimiter.equals(" ")) {
			res = CollectionUtils.asEnumerable(source.split("\\s"));
		} else {
			res = CollectionUtils.asEnumerable(source.split(delimiter));
		}

		return res.map(transform).filter(filter);

	}
}
