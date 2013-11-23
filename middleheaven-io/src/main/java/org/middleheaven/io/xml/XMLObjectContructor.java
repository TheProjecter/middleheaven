/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.middleheaven.io.FileNotFoundManagedException;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class XMLObjectContructor<T> {

	private T object;
	protected URL url;

	public T getConstructedObject(){
		return object;
	}

	protected void setConstructedObject(T object){
		this.object = object;
	}

	protected void constructFrom(CharSequence text) throws ManagedIOException , XMLException {
		if (text ==null || text.toString().trim().length()==0){
			throw new IllegalArgumentException("Parameter 'text' cannot be null nor empty");
		}
		constructFrom(new ByteArrayInputStream(text.toString().getBytes()));
	}

	protected void constructFrom(File file) throws FileNotFoundManagedException, ManagedIOException , XMLException {
		try {
			constructFrom(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new FileNotFoundManagedException(file);
		}
	}

	protected void constructFrom(URL sourceURL) throws ManagedIOException , XMLException{
		try {
			url = sourceURL;
			constructFrom(sourceURL.openStream());
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}

	protected void constructFrom(ManagedFile file){
		try {
			url = file.getURI().toURL();
			constructFrom(file.getContent().getInputStream());
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}

	}

	protected void constructFrom(InputStream sourceStream) throws ManagedIOException , XMLException{

		// Create a builder factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		factory.setExpandEntityReferences(false);

		// Create the builder and parse the file
		try {
			constructFrom(factory.newDocumentBuilder().parse(sourceStream));
		} catch (SAXException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		} catch (ParserConfigurationException e) {
			throw new XMLException(e);
		}

	}

	protected abstract void constructFrom(Document document) throws ManagedIOException , XMLException;
}
