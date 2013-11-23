
package org.middleheaven.text.indexing.lucene;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.store.NoSuchDirectoryException;
import org.apache.lucene.util.Constants;
import org.apache.lucene.util.ThreadInterruptedException;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.quantity.time.EpocTimePoint;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * Implementation of a {@link Directory} by means of a managed file.
 */
public abstract class ManagedFileDirectory extends Directory {

  private static final MessageDigest DIGESTER;

  static {
    try {
      DIGESTER = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e.toString(), e);
    }
  }

  /** The underlying filesystem directory */
  protected ManagedFile directory = null;
  
  private boolean checked;

  final void createDir() throws ManagedIOException {
    if (!checked) {
      if (!directory.exists()) {
    	  directory = directory.createFolder();
      }
    	  
      checked = true;
    }
  }

  /** Initializes the directory to create a new file with the given name.
   * This method should be used in {@link #createOutput}. */
  protected final void initOutput(String name) throws IOException {
    ensureOpen();
    createDir();
    ManagedFile file = directory.retrive(name);
    if (file.exists() && !file.delete()) { // delete existing, if any
    	throw new IOException("Cannot overwrite: " + file);
    }
  }


  /** Create a new FSDirectory for the named location (ctor for subclasses).
   * @param path the path of the directory
   * @param lockFactory the lock factory to use, or null for the default
   * ({@link NativeFSLockFactory});
   * @throws IOException
   */
  protected ManagedFileDirectory(ManagedFile directory) throws IOException {

   
    lockFactory = new ManagedFileLockFactory(directory);
    
    this.directory = directory;

    if (directory.exists() && !directory.getType().isFolder()){
      throw new NoSuchDirectoryException("file '" + directory + "' exists but is not a directory");
    }
    
    setLockFactory(lockFactory);

  }



  /** Just like {@link #open(File)}, but allows you to
   *  also specify a custom {@link LockFactory}. */
  public static ManagedFileDirectory open(ManagedFile directory) throws IOException {

   // if (Constants.WINDOWS) {
      return new SimpleFSDirectory(directory);
  //  } else {
   //   return new NIOFSDirectory(directory);
   // }
  }

  /** Lists all files (not subdirectories) in the
   *  directory.  This method never returns null (throws
   *  {@link IOException} instead).
   *
   *  @throws NoSuchDirectoryException if the directory
   *   does not exist, or does exist but is not a
   *   directory.
   *  @throws IOException if list() returns null */
  private static String[] listAll(ManagedFile dir) throws IOException {
    if (!dir.exists()) {
      throw new NoSuchDirectoryException("directory '" + dir + "' does not exist");
   } else if (!dir.getType().isFolder()){
      throw new NoSuchDirectoryException("file '" + dir + "' exists but is not a directory");
    }

    // Exclude subdirs 
    return dir.children().filter(new Predicate<ManagedFile>(){

		@Override
		public Boolean apply(ManagedFile file) {
			return file.getType().isFile();
		}
    	
    }).map(new Mapper<String, ManagedFile>(){

		@Override
		public String apply(ManagedFile obj) {
			return obj.getPath().getFileName();
		}
    	
    }).distinct().asArray();
  }

  /** Lists all files (not subdirectories) in the
   * directory.
   * @see #listAll(File) */
  @Override
  public String[] listAll() throws IOException {
    ensureOpen();
    return listAll(directory);
  }

  /** Returns true iff a file with the given name exists. */
  @Override
  public boolean fileExists(String name) {
    ensureOpen();
    return directory.retrive(name).exists();

  }

  /** Returns the time the named file was last modified. */
  @Override
  public long fileModified(String name) {
    ensureOpen();
    return directory.retrive(name).getModificationTrace().getLastModified().getMilliseconds();
  }

  /** Returns the time the named file was last modified. */
  public static long fileModified(File directory, String name) {
    File file = new File(directory, name);
    return file.lastModified();
  }

  /** Set the modified time of an existing file to now. */
  @Override
  public void touchFile(String name) {
    ensureOpen();
    directory.retrive(name).getModificationTrace().setLastModified(new EpocTimePoint(System.currentTimeMillis()));
  }

  /** Returns the length in bytes of a file in the directory. */
  @Override
  public long fileLength(String name) {
    ensureOpen();
    return directory.retrive(name).getSize();
  }

  /** Removes an existing file in the directory. */
  @Override
  public void deleteFile(String name) throws IOException {
    ensureOpen();
    ManagedFile file = directory.retrive(name);
    if (!file.delete()){
      throw new IOException("Cannot delete " + file);
    }
  }

  @Override
  public void sync(String name) throws IOException {
    ensureOpen();

    ManagedFile fullFile = directory.retrive(name);
    
    boolean success = false;
    int retryCount = 0;
    IOException exc = null;
    while(!success && retryCount < 5) {
      retryCount++;
      RandomAccessFile file = null;
      try {
        try {
          file = new RandomAccessFile(fullFile.getPath().toString(), "rw");
          file.getFD().sync();
          success = true;
        } finally {
          if (file != null)
            file.close();
        }
      } catch (IOException ioe) {
        if (exc == null)
          exc = ioe;
        try {
          // Pause 5 msec
          Thread.sleep(5);
        } catch (InterruptedException ie) {
          throw new ThreadInterruptedException(ie);
        }
      }
    }
    if (!success)
      // Throw original exception
      throw exc;
  }

  // Inherit javadoc
  @Override
  public IndexInput openInput(String name) throws IOException {
    ensureOpen();
    return openInput(name, BufferedIndexInput.BUFFER_SIZE);
  }

  /**
   * So we can do some byte-to-hexchar conversion below
   */
  private static final char[] HEX_DIGITS =
  {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

  
  @Override
  public String getLockID() {
    ensureOpen();
    String dirName;                               // name to be hashed

     dirName = directory.getPath().toString();


    byte digest[];
    synchronized (DIGESTER) {
      digest = DIGESTER.digest(dirName.getBytes());
    }
    StringBuilder buf = new StringBuilder();
    buf.append("lucene-");
    for (int i = 0; i < digest.length; i++) {
      int b = digest[i];
      buf.append(HEX_DIGITS[(b >> 4) & 0xf]);
      buf.append(HEX_DIGITS[b & 0xf]);
    }

    return buf.toString();
  }

  /** Closes the store to future operations. */
  @Override
  public synchronized void close() {
    isOpen = false;
  }

  protected ManagedFile getFile() {
    ensureOpen();
    return directory;
  }

  /** For debug output. */
  @Override
  public String toString() {
    return this.getClass().getName() + "@" + directory;
  }

  /**
   * Default read chunk size.  This is a conditional
   * default: on 32bit JVMs, it defaults to 100 MB.  On
   * 64bit JVMs, it's <code>Integer.MAX_VALUE</code>.
   * @see #setReadChunkSize
   */
  public static final int DEFAULT_READ_CHUNK_SIZE = Constants.JRE_IS_64BIT ? Integer.MAX_VALUE: 100 * 1024 * 1024;

  // LUCENE-1566
  private int chunkSize = DEFAULT_READ_CHUNK_SIZE;

  /**
   * Sets the maximum number of bytes read at once from the
   * underlying file during {@link IndexInput#readBytes}.
   * The default value is {@link #DEFAULT_READ_CHUNK_SIZE};
   *
   * <p> This was introduced due to <a
   * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6478546">Sun
   * JVM Bug 6478546</a>, which throws an incorrect
   * OutOfMemoryError when attempting to read too many bytes
   * at once.  It only happens on 32bit JVMs with a large
   * maximum heap size.</p>
   *
   * <p>Changes to this value will not impact any
   * already-opened {@link IndexInput}s.  You should call
   * this before attempting to open an index on the
   * directory.</p>
   *
   * <p> <b>NOTE</b>: This value should be as large as
   * possible to reduce any possible performance impact.  If
   * you still encounter an incorrect OutOfMemoryError,
   * trying lowering the chunk size.</p>
   */
  public final void setReadChunkSize(int chunkSize) {
    // LUCENE-1566
    if (chunkSize <= 0) {
      throw new IllegalArgumentException("chunkSize must be positive");
    }
    if (!Constants.JRE_IS_64BIT) {
      this.chunkSize = chunkSize;
    }
  }

  /**
   * The maximum number of bytes to read at once from the
   * underlying file during {@link IndexInput#readBytes}.
   * @see #setReadChunkSize
   */
  public final int getReadChunkSize() {
    // LUCENE-1566
    return chunkSize;
  }

}

