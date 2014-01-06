package org.middleheaven.sequence;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.reflection.inspection.Introspector;

public class Sequences {

	private Sequences(){}
	
	private static final Map<String, String> SEQUENCES = new TreeMap<String, String>();

	static {
		setSequence(Long.class, LongSequence.class);
	}
	
	public static <T> Sequence<T> newSequenceFor (Class<T> elementType){
		String sequenceType = SEQUENCES.get(elementType.getName());
		if (sequenceType==null){
			throw new RuntimeException("Sequence not found for type " + elementType);
		}
		return Introspector.of(Sequence.class).load(sequenceType).newInstance();
	}
	
	public static <T, S extends Sequence<T>> void setSequence(Class<T> elementType,Class<S> sequenceType){
		SEQUENCES.put(elementType.getName(), sequenceType.getName());
	}
	
	public static <T> Sequence<T> over (T ... elements){
		return IterableBasedSequence.sequenceFor(elements);
	}
	
	public static <T> Sequence<T> over (List<T> elements ){
		return IterableBasedSequence.sequenceFor(elements);
	}
}
