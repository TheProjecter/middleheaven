/**
 * 
 */
package org.middleheaven.process.web.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import org.middleheaven.culture.Culture;
import org.middleheaven.io.IOTransport;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.StreamNotReadableIOException;
import org.middleheaven.io.StreamableContent;
import org.middleheaven.io.StreamableContentSource;
import org.middleheaven.process.web.HttpCookieWriter;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpProcessIOException;
import org.middleheaven.process.web.HttpStatusCode;

/**
 * 
 */
class ServletBasedHttpServerResponse implements HttpServerResponse {

	private HttpServletResponse response;
	private HttpEntry entry;

	/**
	 * Constructor.
	 * @param servletResponse
	 */
	public ServletBasedHttpServerResponse(HttpServletResponse servletResponse) {
		this.response = servletResponse;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCulture(Culture culture) {
		response.setLocale(culture.toLocale());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCommitted() {
		return response.isCommitted();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		response.reset();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendError(HttpStatusCode sc, String msg)
			throws HttpProcessIOException {
		try {
			response.sendError(sc.intValue(), msg);
		} catch (IOException e) {
			throw new HttpProcessIOException(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendError(HttpStatusCode sc) throws HttpProcessIOException {
		try {
			response.sendError(sc.intValue());
		} catch (IOException e) {
			throw new HttpProcessIOException(e);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStatus(HttpStatusCode sc) {
		response.setStatus(sc.intValue());
	}
	
	
	public void renameTo(String newName) {
		((HttpServletResponse) response).setHeader("Content-disposition", "attachment; filename=".concat(newName));
	}
	
	public class ResponseHttpEntry implements HttpEntry {

		boolean streamIsOpen = false;
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getContentEncoding() {
			return response.getCharacterEncoding();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isRepeatable() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStreaming() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isChunked() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public StreamableContent getContent() {
			return new StreamableContent(){
			
				
				@Override
				public InputStream getInputStream() {
					throw new StreamNotReadableIOException();
				}

				@Override
				public OutputStream getOutputStream()  {
					try {
						 OutputStream out = response.getOutputStream();
						 streamIsOpen = true;
						 return out;
					} catch (IOException e) {
						throw ManagedIOException.manage(e);
					} 
				}

				@Override
				public boolean setSize(long expectedSize)  {
					if (streamIsOpen){
						throw new ManagedIOException("Configuration must be made before opening streams");
					}
					response.setContentLength((int)expectedSize);
					return true;
				}

				@Override
				public boolean isContentTypeWritable() {
					return !streamIsOpen;
				}
				
				@Override
				public void setContentType(String mimeContentType){
					if (streamIsOpen){
						throw new ManagedIOException("Configuration must be made before opening streams");
					}
					response.setContentType(mimeContentType);
				}

				@Override
				public String getContentType()  {
					throw new UnsupportedOperationException("Content type cannot be read for this file type");
				}

				@Override
				public long getSize() {
					return response.getBufferSize();
				}

				@Override
				public boolean isReadable() {
					return false;
				}

				@Override
				public boolean isContentTypeReadable() {
					return false;
				}

				@Override
				public boolean isWritable() {
					return true;
				}

				
			};
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Writer getContentWriter() {
			try {
				 Writer out = response.getWriter();
				 streamIsOpen = true;
				 return out;
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			} 
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void copyTo(StreamableContentSource other) throws ManagedIOException {
			IOTransport.copy(this).to(other);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpEntry getEntry() {
		if (entry == null){
			
			entry = new ResponseHttpEntry();
		}
		
		return entry;
	}


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEntry(HttpEntry entry) {
		if (this.entry != null){
			throw new IllegalStateException("HttpEntry alread in use");
		}
		
		this.entry = entry;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpCookieWriter getHttpCookieWriter() {
		throw new UnsupportedOperationException("Not implememented yet");
	}
}
