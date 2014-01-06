/**
 * 
 */
package org.middleheaven.util;

import java.util.regex.Pattern;

import org.middleheaven.collections.KeyValuePair;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.collections.enumerable.PairEnumerable;
import org.middleheaven.util.function.Function;
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
	
	public static class SelfFunction implements Function<String, String>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String apply(String obj) {
			return obj;
		}
		
	}
	
	public static class TrimFunction implements Function<String, String>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String apply(String obj) {
			return obj.trim();
		}
		
	}
	
	public static class PairFunction implements Function<KeyValuePair<String, String>, String> {

		
		private String keyValueSeparator;
		private Function<String, String> transform;

		public PairFunction (String keyValueSeparator , Function<String, String> transform){
			
			this.keyValueSeparator= keyValueSeparator;
			this.transform = transform;
		}
		
		@Override
		public KeyValuePair<String, String> apply(String value) {
			final String[] p = value.split(keyValueSeparator);
			return new KeyValuePair<String, String>() {

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
		private Function<String, String> transform = new SelfFunction();
		
		public KeyValueSplitterImpl(Splitter spliter, String keyValueSeparator) {
			this.spliter = spliter;
			this.keyValueSeparator = keyValueSeparator;
		}

		@Override
		public PairEnumerable<String, String> split(CharSequence charSequence) {
			return spliter
					.split(charSequence)
					.pairMap(new PairFunction(keyValueSeparator, transform));
				
		}

		@Override
		public KeyValueSplitter trim() {
			KeyValueSplitterImpl trimmed = new KeyValueSplitterImpl(
					this.spliter, this.keyValueSeparator);

			trimmed.transform = new TrimFunction();

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
	private Function<String, String> transform = new SelfFunction();
	private Predicate<String> filter = new AllwaysTrue();

	private Splitter(String delimiter) {
		this.delimiter = delimiter;
	}

	public KeyValueSplitter withKeyValueSeparator(String paramsSpliter) {
		return new KeyValueSplitterImpl(this, paramsSpliter);
	}

	public Splitter trim() {
		Splitter s = new Splitter(this.delimiter);
		s.transform = new TrimFunction();
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
			res = Enumerables.asEnumerable(source);
		}

		if (delimiter.equals(".")) {
			res = Enumerables.asEnumerable(source.split("\\."));
		} else if (delimiter.equals(" ")) {
			res = Enumerables.asEnumerable(source.split("\\s"));
		} else {
			res = Enumerables.asEnumerable(source.split(delimiter));
		}

		return res.map(transform).filter(filter);

	}
}
