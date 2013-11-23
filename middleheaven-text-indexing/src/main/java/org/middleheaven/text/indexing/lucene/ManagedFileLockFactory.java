/**
 * 
 */
package org.middleheaven.text.indexing.lucene;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.store.Lock;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.LockReleaseFailedException;
import org.middleheaven.io.repository.ManagedFile;

/**
 * 
 */
class ManagedFileLockFactory extends LockFactory {

	private ManagedFile directory;

	/**
	 * Constructor.
	 * @param directory
	 */
	public ManagedFileLockFactory(ManagedFile directory) {
		this.directory = directory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Lock makeLock(String lockName) {
	    if (lockPrefix != null){
	        lockName = lockPrefix + "-" + lockName;
	    }
	    
	    return new ManagedLock(directory, lockName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearLock(String lockName) throws IOException {
		// Note that this isn't strictly required anymore
	    // because the existence of these files does not mean
	    // they are locked, but, still do this in case people
	    // really want to see the files go away:
	    if (directory.exists()) {
	      if (lockPrefix != null) {
	        lockName = lockPrefix + "-" + lockName;
	      }
	      ManagedFile lockFile = directory.retrive(lockName);
	      if (lockFile.exists() && !lockFile.delete()) {
	        throw new IOException("Cannot delete " + lockFile);
	      }
	    }
	}

}

class ManagedLock extends Lock {

	  private RandomAccessFile f;
	  private FileChannel channel;
	  private FileLock lock;
	  
	  private ManagedFile lockFile;
	  private ManagedFile lockDir;

	  /*
	   * The javadocs for FileChannel state that you should have
	   * a single instance of a FileChannel (per JVM) for all
	   * locking against a given file.  To ensure this, we have
	   * a single (static) HashSet that contains the file paths
	   * of all currently locked locks.  This protects against
	   * possible cases where different Directory instances in
	   * one JVM (each with their own NativeFSLockFactory
	   * instance) have set the same lock dir and lock prefix.
	   */
	  private static final Set<String> LOCK_HELD = new HashSet<String>();

	  public ManagedLock(ManagedFile lockDir, String lockFileName) {
	    this.lockDir = lockDir;
	    lockFile = lockDir.retrive(lockFileName);
	  }

	  private synchronized boolean lockExists() {
	    return lock != null;
	  }

	  @Override
	  public synchronized boolean obtain() throws IOException {

	    if (lockExists()) {
	      // Our instance is already locked:
	      return false;
	    }

	    // Ensure that lockDir exists and is a directory.
	    if (!lockDir.exists()) {
	    	lockDir = lockDir.createFolder();
	    	
	    } else if (!lockDir.getType().isFolder()) {
	      throw new IOException("Found regular file where directory expected: " +  lockDir.getPath());
	    }

	    String canonicalPath = lockFile.getPath().toString();

	    boolean markedHeld = false;

	    try {

	      // Make sure nobody else in-process has this lock held
	      // already, and, mark it held if not:

	      synchronized(LOCK_HELD) {
	        if (LOCK_HELD.contains(canonicalPath)) {
	          // Someone else in this JVM already has the lock:
	          return false;
	        } else {
	          // This "reserves" the fact that we are the one
	          // thread trying to obtain this lock, so we own
	          // the only instance of a channel against this
	          // file:
	          LOCK_HELD.add(canonicalPath);
	          markedHeld = true;
	        }
	      }

	      try {
	        f = new RandomAccessFile(new File (lockFile.getURI()), "rw");
	      } catch (IOException e) {
	        // On Windows, we can get intermittent "Access
	        // Denied" here.  So, we treat this as failure to
	        // acquire the lock, but, store the reason in case
	        // there is in fact a real error case.
	        failureReason = e;
	        f = null;
	      }

	      if (f != null) {
	        try {
	          channel = f.getChannel();
	          try {
	            lock = channel.tryLock();
	          } catch (IOException e) {
	            // At least on OS X, we will sometimes get an
	            // intermittent "Permission Denied" IOException,
	            // which seems to simply mean "you failed to get
	            // the lock".  But other IOExceptions could be
	            // "permanent" (eg, locking is not supported via
	            // the filesystem).  So, we record the failure
	            // reason here; the timeout obtain (usually the
	            // one calling us) will use this as "root cause"
	            // if it fails to get the lock.
	            failureReason = e;
	          } finally {
	            if (lock == null) {
	              try {
	                channel.close();
	              } finally {
	                channel = null;
	              }
	            }
	          }
	        } finally {
	          if (channel == null) {
	            try {
	              f.close();
	            } finally {
	              f = null;
	            }
	          }
	        }
	      }

	    } finally {
	      if (markedHeld && !lockExists()) {
	        synchronized(LOCK_HELD) {
	          if (LOCK_HELD.contains(canonicalPath)) {
	            LOCK_HELD.remove(canonicalPath);
	          }
	        }
	      }
	    }
	    return lockExists();
	  }

	  @Override
	  public synchronized void release() throws IOException {
	    if (lockExists()) {
	      try {
	        lock.release();
	      } finally {
	        lock = null;
	        try {
	          channel.close();
	        } finally {
	          channel = null;
	          try {
	            f.close();
	          } finally {
	            f = null;
	            synchronized(LOCK_HELD) {
	              LOCK_HELD.remove(lockFile.getPath().toString());
	            }
	          }
	        }
	      }
	      if (!lockFile.delete())
	        throw new LockReleaseFailedException("failed to delete " + lockFile);
	    }
	  }

	  @Override
	  public synchronized boolean isLocked() {
	    // The test for is isLocked is not directly possible with native file locks:
	    
	    // First a shortcut, if a lock reference in this instance is available
	    if (lockExists()) return true;
	    
	    // Look if lock file is present; if not, there can definitely be no lock!
	    if (!lockFile.exists()) return false;
	    
	    // Try to obtain and release (if was locked) the lock
	    try {
	      boolean obtained = obtain();
	      if (obtained) release();
	      return !obtained;
	    } catch (IOException ioe) {
	      return false;
	    }    
	  }

	  @Override
	  public String toString() {
	    return "NativeFSLock@" + lockFile;
	  }
	}
