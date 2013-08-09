/**
 * 
 */
package org.middleheaven.io.repository.upload;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.watch.WatchService;

/**
 * 
 */
public class AbstractRequestFileRepository extends AbstractManagedRepository{

	private UploadRootFolder root;
	private final HttpServletRequest request;
	private boolean open = true;
	
	public AbstractRequestFileRepository (HttpServletRequest request){
		this.request = request;
		root = new UploadRootFolder(this);
	}
	
	protected UploadRootFolder getRoot(){
		return root;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close(){
		this.root.clear();
		open = false;
	}
	

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWriteable() {
		return false;
	}

	@Override
	public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
		return root.retrive(path);
	}

	@Override
	public void store(ManagedFile file) throws RepositoryNotWritableException, ManagedIOException {
		throw new UnsupportedOperationException("Store is not supported");
	}


	public URL getURL() {
		try {
			return new URL(request.getRemoteAddr());
		} catch (MalformedURLException e) {
			throw new ManagedIOException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WatchService getWatchService() {
		throw new UnsupportedOperationException();
	}
	

	@Override
	public boolean isWatchable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<ManagedFilePath> getRoots() {
		return Arrays.<ManagedFilePath>asList(root.getPath());
	}
}
