package org.middleheaven.util.identity;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractIdentityModel implements IdentityModel {


	private final Map <String , IdentitySequence> sequences = new TreeMap<String , IdentitySequence>();
	
	@Override
	public final Identity nextIdentity(Class<?> type) {
		IdentitySequence<?> sequence = sequences.get(type.getName());
		if (sequence == null){
			sequence =  newSequenceInstance(type); 
			sequences.put(type.getName(), sequence);
		}
		return sequence.next().value();
	}
	
	protected abstract IdentitySequence<?> newSequenceInstance(Class<?> type);



}
