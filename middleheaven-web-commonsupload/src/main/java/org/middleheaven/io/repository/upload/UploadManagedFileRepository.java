package org.middleheaven.io.repository.upload;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileRepository;

public final class UploadManagedFileRepository extends AbstractRequestFileRepository  {


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

	private UploadManagedFileRepository(HttpServletRequest request, Map<String, String[]> parameters, ManagedFile temporary){
		super(request);
	
		
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

						getRoot().add(new UploadManagedFile(this, item, getRoot()));
						
					} else if (parameters != null){
						parameters.put(item.getFieldName(), new String[]{item.getString()});
					}
				}

			} catch (FileUploadException e) {
				throw new ManagedIOException(e);
			}  
		} 

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long resolveRequestFileSize(ManagedFile managedFile) {
		 return ((UploadManagedFile)managedFile).fileItem.getSize();
	}

}
