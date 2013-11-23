package org.middleheaven.process.progress;

/**
 * Something that is connected to a progress. Normally a process.
 * 
 */
public interface Progressable {

    public Progress getProgress();
}
