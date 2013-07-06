package org.middleheaven.collections;

import java.util.Random;

public interface RandomEnumerable<T> {

	public T random();

	public T random(Random random);
}
