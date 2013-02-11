/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.logging.Logger;
import org.middleheaven.process.web.server.ServletWebContext;
import org.middleheaven.ui.Rendering;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UIService;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

/**
 * 
 */
public class VaadinApplicationServletAdapter extends AbstractApplicationServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String applicationId;
	private UIService uiService;


	public VaadinApplicationServletAdapter (String applicationId, UIService uiService){
		this.applicationId = applicationId;
		this.uiService = uiService;
	}

	public void init(javax.servlet.ServletConfig servletConfig) throws javax.servlet.ServletException {
		super.init(servletConfig);
	}


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// get web context from envoirment
    	ServletWebContext context = (ServletWebContext)request.getAttribute("mhRequestResponseWebContext");

    	// set it in the session so it can be retrives in VaadinClientApplication
    	
    	request.getSession(true).setAttribute("mhRequestResponseWebContext", context);
    	
    	try { 
	        RequestType requestType = getRequestType(request);
	
	        if (requestType == RequestType.STATIC_FILE) {
	            serveStaticResources(request, response);
	            return;
	        }
	        
	        super.service(request, response);
    	} finally {
    	    //remove the context from the session
        	request.getSession().removeAttribute("mhRequestResponseWebContext");
    	}
    
    	
    }
    
	protected RequestType getRequestType(HttpServletRequest request) {
		if (isStaticResourceRequest(request)) {
			return RequestType.STATIC_FILE;
		} else {
			return super.getRequestType(request);
		}
	}

	private boolean isStaticResourceRequest(HttpServletRequest request) {
		return request.getRequestURI().contains(request.getContextPath() + "/VAADIN/");
	}

	/**
	 * Check if this is a request for a static resource and, if it is, serve the
	 * resource to the client.
	 * 
	 * @param request
	 * @param response
	 * @return true if a file was served and the request has been handled, false
	 *         otherwise.
	 * @throws IOException
	 * @throws ServletException
	 */
	private boolean serveStaticResources(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.length() <= 10) {
			return false;
		}

		int pos = request.getRequestURI().indexOf("/VAADIN/");
		
		if (pos >= 0){
			serveStaticResourcesInVAADIN(request.getRequestURI().substring(pos), request,response);
			return true;
		}
		
		return false;
	}

	/**
	 * Serve resources from VAADIN directory.
	 * 
	 * @param filename
	 *            The filename to serve. Should always start with /VAADIN/.
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void serveStaticResourcesInVAADIN(String filename,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException {

		final ServletContext sc = getServletContext();
		URL resourceUrl = sc.getResource(filename);
		if (resourceUrl == null) {
			// try if requested file is found from classloader

			// strip leading "/" otherwise stream from JAR wont work
			filename = filename.substring(1);
			resourceUrl = getClassLoader().getResource(filename);

			if (resourceUrl == null) {
				// cannot serve requested file
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// security check: do not permit navigation out of the VAADIN
			// directory
			if (!isAllowedVAADINResourceUrl(request, resourceUrl)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		// Find the modification timestamp
		long lastModifiedTime = 0;
		try {
			lastModifiedTime = resourceUrl.openConnection().getLastModified();
			// Remove milliseconds to avoid comparison problems (milliseconds
			// are not returned by the browser in the "If-Modified-Since"
			// header).
			lastModifiedTime = lastModifiedTime - lastModifiedTime % 1000;

			if (browserHasNewestVersion(request, lastModifiedTime)) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
		} catch (Exception e) {
			// Failed to find out last modified timestamp. Continue without it.
			Logger.onBookFor(this.getClass()).trace(e,
					"Failed to find out last modified timestamp. Continuing without it."
			);
		}

		// Set type mime type if we can determine it based on the filename
		final String mimetype = sc.getMimeType(filename);
		if (mimetype != null) {
			response.setContentType(mimetype);
		}

		// Provide modification timestamp to the browser if it is known.
		if (lastModifiedTime > 0) {
			response.setDateHeader("Last-Modified", lastModifiedTime);
			/*
			 * The browser is allowed to cache for 1 hour (3600 minutes) without checking if
			 * the file has changed. This forces browsers to fetch a new version
			 * when the Vaadin version is updated. This will cause more requests
			 * to the servlet than without this but for high volume sites the
			 * static files should never be served through the servlet. The
			 * cache timeout can be configured by setting the resourceCacheTime
			 * parameter in web.xml
			 */
			response.setHeader("Cache-Control", "max-age=3600");
		}

		// Write the resource to the client.
		final OutputStream os = response.getOutputStream();
		final byte buffer[] = new byte[DEFAULT_BUFFER_SIZE];
		int bytes;
		InputStream is = resourceUrl.openStream();
		while ((bytes = is.read(buffer)) >= 0) {
			os.write(buffer, 0, bytes);
		}
		is.close();
	}
	
	/**
     * Checks if the browser has an up to date cached version of requested
     * resource. Currently the check is performed using the "If-Modified-Since"
     * header. Could be expanded if needed.
     * 
     * @param request
     *            The HttpServletRequest from the browser.
     * @param resourceLastModifiedTimestamp
     *            The timestamp when the resource was last modified. 0 if the
     *            last modification time is unknown.
     * @return true if the If-Modified-Since header tells the cached version in
     *         the browser is up to date, false otherwise
     */
    private boolean browserHasNewestVersion(HttpServletRequest request,
            long resourceLastModifiedTimestamp) {
        if (resourceLastModifiedTimestamp < 1) {
            // We do not know when it was modified so the browser cannot have an
            // up-to-date version
            return false;
        }
        /*
         * The browser can request the resource conditionally using an
         * If-Modified-Since header. Check this against the last modification
         * time.
         */
        try {
            // If-Modified-Since represents the timestamp of the version cached
            // in the browser
            long headerIfModifiedSince = request
                    .getDateHeader("If-Modified-Since");

            if (headerIfModifiedSince >= resourceLastModifiedTimestamp) {
                // Browser has this an up-to-date version of the resource
                return true;
            }
        } catch (Exception e) {
            // Failed to parse header. Fail silently - the browser does not have
            // an up-to-date version in its cache.
        }
        return false;
    }

    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Application getNewApplication(HttpServletRequest request)
			throws ServletException {

		//obtain context from "secret" request attribute 
		ServletWebContext context = (ServletWebContext)request.getAttribute("mhRequestResponseWebContext");

		final Rendering<UIClient> rendering =  uiService.getUIClientRendering(UIEnvironmentType.BROWSER, context.getAttributes());

		return  (Application) rendering.getComponent();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<? extends Application> getApplicationClass()
			throws ClassNotFoundException {
		return VaadinClientApplication.class;
	}


	protected String getApplicationCSSClassName() {
		return applicationId;
	}
}
