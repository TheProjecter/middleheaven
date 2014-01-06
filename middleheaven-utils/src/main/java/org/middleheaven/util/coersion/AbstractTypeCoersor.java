package org.middleheaven.util.coersion;

public abstract class AbstractTypeCoersor<O,R> implements TypeCoersor<O,R> {


	@Override
	public final TypeCoersor<R,O> inverse() {
		return new InvertedTypeCoersor<R,O>(this);
	}

}
