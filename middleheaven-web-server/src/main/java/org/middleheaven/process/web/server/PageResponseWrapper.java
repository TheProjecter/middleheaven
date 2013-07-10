package org.middleheaven.process.web.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.middleheaven.process.web.RoutablePrintWriter;
import org.middleheaven.web.rendering.HTMLPage;
import org.middleheaven.web.rendering.Page;
import org.middleheaven.web.rendering.PageBuffer;

public final class PageResponseWrapper extends HttpServletResponseWrapper  {

	private final RoutablePrintWriter routablePrintWriter;
	private final RoutableServletOutputStream routableServletOutputStream;

	private PageBuffer buffer;
	private boolean aborted = false;
	private boolean parseablePage = false;

	public PageResponseWrapper(final HttpServletResponse  response) {
		super(response);
		
		ResponseDestinationFactory factory = new ResponseDestinationFactory(response);
		routablePrintWriter = new RoutablePrintWriter(factory);
		routableServletOutputStream = new RoutableServletOutputStream(factory);
	}
	
	private static class ResponseDestinationFactory implements RoutablePrintWriter.DestinationFactory , RoutableServletOutputStream.DestinationFactory{

		HttpServletResponse  response;
		public ResponseDestinationFactory (HttpServletResponse  response){
			this.response = response;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public PrintWriter activateDestination() throws IOException {
			return response.getWriter();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ServletOutputStream create() throws IOException {
			return response.getOutputStream();
		}
		
	}

	
	 public void setContentType(String  type) {
		 super.setContentType(type);

		 if (type != null) {
			 // this is the content type + charset. eg: text/html;charset=UTF-
			 int offset = type.lastIndexOf("charset=");
			 String  encoding = null;
			 if (offset != -1){
				 encoding = extractContentTypeValue(type, offset + 8);
			 }
			 String  contentType = extractContentTypeValue(type, 0);

			// if (factory.shouldParsePage(contentType)) {
				 activate(contentType, encoding);
			// }
		 }

	 }

	 private void activate(String  contentType, String  encoding) {
		 if (parseablePage) {
			 return; // already activated
		 }
		 parseablePage = true;
		 buffer = new PageBuffer( contentType, encoding);
		 
		 routablePrintWriter.updateDestination(new RoutablePrintWriter.DestinationFactory() {
			 public PrintWriter  activateDestination() {
				 return buffer.getWriter();
			 }
		 });
		 routableServletOutputStream.updateDestination(new RoutableServletOutputStream.DestinationFactory() {
			 public ServletOutputStream  create() {
				 return buffer.getOutputStream();
			 }
		 });
	 }

	 private String  extractContentTypeValue(String  type, int startIndex) {
		 if (startIndex < 0 ){
			 return null;
		 }

		 // Skip over any leading spaces
		 while (startIndex < type.length() && type.charAt(startIndex) == ' '){			 
			 startIndex++;
		 }

		 if (startIndex >= type.length()){
			 return null;
		 }

		 int endIndex = startIndex;

		 if (type.charAt(startIndex) == '"') {
			 startIndex++;
			 endIndex = type.indexOf('"', startIndex);
			 if (endIndex == -1){
				 endIndex = type.length();
			 }
		 } else {
			 // Scan through until we hit either the end of the string or a
			 // special character (as defined in RFC-). Note that we ignore '/'
			 // since we want to capture it as part of the value.
			 char ch;
			 while (endIndex < type.length() && (ch = type.charAt(endIndex)) != ' ' && ch != ';'
				 && ch != '(' && ch != ')' && ch != '[' && ch != ']' && ch != '<' && ch != '>'
					 && ch != ':' && ch != ',' && ch != '=' && ch != '?' && ch != '@' && ch!= '"'
						 && ch !='\\'){
				 endIndex++;
			 }
		 }
		 return type.substring(startIndex, endIndex);
	 }

	 /** Prevent content-length being set if page is parseable. */
	 public void setContentLength(int contentLength) {
		 if (!parseablePage){
			 super.setContentLength(contentLength);
		 }
	 }

	 /** Prevent content-length being set if page is parseable. */
	 public void setHeader(String  name, String  value) {
		 if (name.equalsIgnoreCase("content-type")) { // ensure ContentType is always set through setContentType()
			 setContentType(value);
		 } else if (!parseablePage || !name.equalsIgnoreCase("content-length")) {
			 super.setHeader(name, value);
		 }
	 }

	 /** Prevent content-length being set if page is parseable. */
	 public void addHeader(String  name, String  value) {
		 if (name.equalsIgnoreCase("content-type")) { // ensure ContentType is always set through setContentType()
			 setContentType(value);
		 } else if (!parseablePage || !name.equalsIgnoreCase("content-length")) {
			 super.addHeader(name, value);
		 }
	 }

	 /**
	  * Prevent 'not modified' () HTTP status from being sent if page is parseable
	  * (so web-server/browser doesn't cache contents).
	  */
	 public void setStatus(int sc) {
		 if (!parseablePage || sc != HttpServletResponse.SC_NOT_MODIFIED) {
			 super.setStatus(sc);
		 }
	 }

	 public ServletOutputStream  getOutputStream() {
		 return routableServletOutputStream;
	 }

	 public PrintWriter  getWriter() {
		 return routablePrintWriter;
	 }

	 public Page getPage() throws IOException  {
		 if (aborted || !parseablePage) {
			 return null;
		 } else {
			 return HTMLPage.parse(buffer.asString());
		 }
	 }

	 public void sendError(int sc) throws IOException  {
		 aborted = true;
		 super.sendError(sc);
	 }

	 public void sendError(int sc, String  msg) throws IOException  {
		 aborted = true;
		 super.sendError(sc, msg);
	 }

	 public void sendRedirect(String  location) throws IOException  {
		 aborted = true;
		 super.sendRedirect(location);
	 }

	 public boolean isUsingStream() {
		 return buffer != null && buffer.isUsingStream();
	 }
}

