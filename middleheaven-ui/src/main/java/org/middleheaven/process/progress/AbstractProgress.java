/*
 * Created on 25/10/2005
 */
package org.middleheaven.process.progress;


import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 */
public abstract class AbstractProgress implements Progress {


    private Collection<ProgressListener> listeners = new CopyOnWriteArraySet<ProgressListener>();
    

    public final void addProgressListener(ProgressListener listener) {
    	listeners.add(listener);
    }
    
    public final void removeProgressListener(ProgressListener listener) {
    	listeners.remove(listener);   
    }


    public final void fireProgressChange() {
    	ProgressChangedEvent event = new ProgressChangedEvent(this);
    	for(ProgressListener listener : listeners  ){
    		listener.onProcessChanged(event);
    	}

    }
    


    

}
