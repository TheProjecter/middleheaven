package org.middleheaven.util.coersion;

class InvertedTypeCoersor<O,R> implements TypeCoersor<O,R>{

	TypeCoersor<R,O> original;
	
	public InvertedTypeCoersor(TypeCoersor<R,O> original){
		this.original = original;
	}
	
	@Override
	public TypeCoersor<R, O> inverse() {
		return original;
	}

	@Override
	public <T extends R> T coerceForward(O value, Class<T> type) {
		return original.coerceReverse(value, type);
	}

	@Override
	public <T extends O> T coerceReverse(R value, Class<T> type) {
		return original.coerceForward(value, type);
	}

}
