/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.culture.Culture;

/**
 * An utility class to help write strutureed HTML files 
 */
public final class HtmlDocument {

	private ByteArrayOutputStream headOut = new ByteArrayOutputStream();
	private ByteArrayOutputStream bodyOut = new ByteArrayOutputStream();
	
	private Writer head = new PrintWriter(new StringWriter());
	private Writer body = new StringWriter();
	
	
	private Set<HtmlScript> scripts = new HashSet<HtmlScript>();
	private Set<String> cssFiles = new HashSet<String>();
	
	private String context;
	private Culture culture;

	public static HtmlDocument newInstance(String context, Culture culture) throws UnsupportedEncodingException{
		return new HtmlDocument(context, culture);
	}
	
	private HtmlDocument (String context, Culture culture) throws UnsupportedEncodingException{
		this.context = context;
		this.culture = culture;
		
		head = new OutputStreamWriter(headOut, "UTF-8");
		body = new OutputStreamWriter(bodyOut, "UTF-8");
		 
		
	}
	
	public Writer getHeadWriter(){
		return head;
	}

	public Writer getBodyWriter(){
		return body;
	}
	
	public void addScript(HtmlScript script) throws IOException{
		if (scripts.add(script)){
			if (script.isSource()) {
				head.append("<script type=\"text/javascript\" src=\"").append(this.context + "/" + script.getRelativeSource()).append("\" ></script>");
			} else {
				head.append("<script type=\"text/javascript\" >\n")
				.append(script.toString())
				.append("\n</script>");
			}
		}
	}
	
	public void addMeta(){
		
	}
	
	
	
	public void writeToResponse(Writer responseWriter) throws IOException{
		head.close();
		body.close();
		responseWriter.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
		
		.append("<html lang=\"").append(this.culture.getLanguage().toString()).append("\">")
		.append("<head>")
		.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">")
		.append(new String(headOut.toByteArray(), "UTF-8"))
		.append("</head>")
		.append("<body>")
		.append(new String(bodyOut.toByteArray(), "UTF-8"))
		.append("</body>")
		.append("</html>");
	}

	/**
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	public void addRelativeStylesheet(String cssfile) throws IOException {
		if (cssFiles.add(cssfile)){
			
			head.append("<link rel='stylesheet' type='text/css' href='").append(this.context + "/" + cssfile).append("' />");

		}
	}

	/**
	 * @return
	 */
	public Culture getCulture() {
		return this.culture;
	}

	/**
	 * @return
	 */
	public String getContextPath() {
		return this.context;
	}
	

}
