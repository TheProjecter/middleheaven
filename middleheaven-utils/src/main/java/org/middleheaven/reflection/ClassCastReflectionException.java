package org.middleheaven.reflection;

public class ClassCastReflectionException extends ReflectionException {

	private String targetClassName;
	private String startClassName;

	public ClassCastReflectionException(String startClassName, String targetClassName) {
		super("Cannot cast " + startClassName + " to " + targetClassName);
		this.startClassName = startClassName;
		this.targetClassName = targetClassName;
	}

	protected String getTargetClassName() {
		return targetClassName;
	}

	protected String getStartClassName() {
		return startClassName;
	}

}
