/**
 * 
 */
package org.middleheaven.io.repository.upload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFileRepository;

/**
 * 
 */
public class ServletRequestPartFileRepository extends AbstractRequestFileRepository{

	/**
	 * @param request
	 * @return
	 * @throws ServletException 
	 * @throws IOException 
	 */
	public static ManagedFileRepository newInstance(HttpServletRequest request) throws IOException, ServletException {
		ServletRequestPartFileRepository repo = new ServletRequestPartFileRepository(request);


		for (Part part :request.getParts()){
			repo.getRoot().add(new ServletUploadFile(repo, part, repo.getRoot()));
		}

		return repo;
	}

	/**
	 * Constructor.
	 * @param request
	 */
	public ServletRequestPartFileRepository(HttpServletRequest request) {
		super(request);
	}





}
