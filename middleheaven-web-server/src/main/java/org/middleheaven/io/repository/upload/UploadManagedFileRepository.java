package org.middleheaven.io.repository.upload;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.RepositoryNotWritableException;
import org.middleheaven.io.repository.watch.WatchService;

public final class UploadManagedFileRepository extends AbstractManagedRepository implements  ManagedFileRepository {


	public static ManagedFileRepository newInstance(HttpServletRequest request, Map<String, String[]> parameters) {
		return repositoryOf(request, parameters , null);
	}
	
	public static ManagedFileRepository repositoryOf(HttpServletRequest request, Map<String, String[]> parameters,ManagedFile temporary) {
		if (temporary != null && !(temporary.isWriteable() && temporary.getType().isOnlyFolder())){
			throw new ManagedIOException("Temporary location must be a writable folder");
		}

		return new UploadManagedFileRepository(request, parameters, temporary);
		
	}
	
	/**
	 * each file is put into a folder with the field name
	 */
	private final UploadRootFolder root;
	
	private HttpServletRequest request;
	
	private UploadManagedFileRepository(HttpServletRequest request, Map<String, String[]> parameters, ManagedFile temporary){

		root = new UploadRootFolder(this);
		
		this.request = request;
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		factory.setSizeThreshold(1024*1024);
		
		if (temporary != null){
			factory.setRepository(new File(temporary.getURI()));
		}

		final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {  
			
			// Create a factory for disk-based file items

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			// Parse the request
			try {

				
				for (Iterator<?> it = upload.parseRequest(request).iterator();it.hasNext();){
					FileItem item = (FileItem) it.next();
					if (!item.isFormField()) {  
						// is a file

						root.add(new UploadManagedFile(this, item, root));
						
					} else if (parameters != null){
						parameters.put(item.getFieldName(), new String[]{item.getString()});
					}
				}

			} catch (FileUploadException e) {
				throw new ManagedIOException(e);
			}  
		} 

	}
	
	private boolean open = true;
	
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
