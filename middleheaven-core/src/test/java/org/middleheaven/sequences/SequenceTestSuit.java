package org.middleheaven.sequences;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.middleheaven.collections.Range;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.sequence.IterableBasedSequence;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;


public class SequenceTestSuit {

	
	@Test
	public void testSequences(){
		
		Integer[] col = {new Integer(1), new Integer(2)};
		
		Sequence<Integer> a = IterableBasedSequence.sequenceFor(col);
		testSequence(a, Range.from(1).upTo(2).into(new ArrayList<Integer>()));
		
		Sequence<Integer> b = IterableBasedSequence.sequenceFor(Range.from(1).upTo(10).into(new ArrayList<Integer>()));
		
		testSequence(b, Range.from(1).upTo(10).into(new ArrayList<Integer>()));

		Sequence<Character> is2 = IterableBasedSequence.sequenceFor(Range.from('a').upTo('z'));
		
		testSequence(is2, Range.from('a').upTo('z').into(new ArrayList<Character>()));
		
		Sequence<Real> c = IterableBasedSequence.sequenceFor(Range.from(Real.valueOf(1)).upTo(Real.valueOf(2)).into(new ArrayList<Real>()));
		
		testSequence(c, Range.from(Real.valueOf(1)).upTo(Real.valueOf(10)).into(new ArrayList<Real>()));
	}
	
	@Test
	public void testDice(){
		
		Integer i = Range.from(1).upTo(6).random();
		
		assertNotNull(i);
	}
	
	private static <T> void testSequence(Sequence<T> seq, Collection<T> collection){
		SequenceToken<T> token;
		while ( (token=seq.next())!=null){
			assertTrue(collection.contains(token.value()));
		}
	}
}
