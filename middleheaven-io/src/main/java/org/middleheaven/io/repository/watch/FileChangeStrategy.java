/*
 * Created on 2006/08/12
 *
 */
package org.middleheaven.io.repository.watch;

/**
 * 
 */
public interface FileChangeStrategy {

    public void onChange(WatchEvent event);
    
}
