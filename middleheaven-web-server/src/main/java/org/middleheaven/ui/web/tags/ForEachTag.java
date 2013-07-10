package org.middleheaven.ui.web.tags;



public class ForEachTag extends AbstractIterationTagSupport {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8718515748936487189L;
	private int first = 0;
	private int maxCount = 0;
	private int count= 0;
	
	public void setMaxCount(int maxCount){
		this.maxCount = maxCount;
	}
	
	public void setFirst(int first){
		this.first = first;
	}
	
	protected void positionFirst() {
		// bias iteration
		for (int i=0; i < first && iterator().hasNext(); i++){
			iterator().next();
		}
	}
	
	protected boolean hasNextElement(){
		return (maxCount == 0 || ++count < maxCount) && iterator().hasNext();
	}
	
	public void releaseState(){
		super.releaseState();
		this.count = 0;
	}
}
