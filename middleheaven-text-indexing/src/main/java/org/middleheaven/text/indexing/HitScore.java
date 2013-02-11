package org.middleheaven.text.indexing;

import org.middleheaven.util.Hash;

public final class HitScore implements Comparable<HitScore> {


	public static HitScore valueOf(float score){
		return new HitScore(score);
	}
	
	private float score;
	
	private HitScore(float score){
		this.score = score;
	}
	

	public double doubleValue(){
		return score;
	}

	public float floatValue(){
		return score;
	}

	@Override
	public int compareTo(HitScore other) {
		return Float.compare(this.score, other.score);
	}

	public boolean equals(Object other){
		return (other instanceof HitScore) && equalsOther((HitScore) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(HitScore other) {
		return Float.compare(this.score, other.score) == 0;
	}
	
	public int hashCode(){
		return Hash.hash(score).hashCode();
	}
}
