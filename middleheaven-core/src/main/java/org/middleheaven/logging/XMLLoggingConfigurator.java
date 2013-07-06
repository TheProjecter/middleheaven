/*
 * Created on 2006/09/08
 *
 */
package org.middleheaven.logging;

import java.net.URL;

import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.w3c.dom.Document;

/**
 * TODO implement
 *
 */
public class XMLLoggingConfigurator extends XMLObjectContructor implements LoggingConfigurator {

    private URL url;

    public XMLLoggingConfigurator(URL url){
        this.url = url;
    }
    

    
    @Override
    protected void constructFrom(Document document) throws XMLException {
        // TODO Le xml e cria livros para library
        
    }



	@Override
	public void config(ConfigurableLogListener configurator, LoggingConfiguration configuration) {
		this.constructFrom(url);
	}




}
