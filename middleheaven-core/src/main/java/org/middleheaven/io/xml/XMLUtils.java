/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

import java.text.ParseException;
import java.util.Date;

import org.middleheaven.global.text.ISO8601Format;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.function.Maybe;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLUtils {

	/*
    public static final Map<String,String> getParams(Element parentElement, String elementName){
        List list = parentElement.elements(elementName);
        Map<String,String> params;
        if (list.isEmpty()){
            params = Collections.emptyMap();
        } else {
            params = new HashMap<String,String>();
            for (Iterator it = list.iterator(); it.hasNext();){
                Element el = (Element)it.next();
                params.put(el.getAttribute("name"), el.getTextContent());
            }
        }
        return params;
    }
    */
    
    public static final boolean booleanAttribute(String attribName , Element el, boolean required, boolean defaultValue) throws XMLAttributemissingException{
        String val = el.getAttribute(attribName);
        if (val==null || val.length()==0){
            if (required){
                throw new XMLAttributemissingException( attribName , el.getLocalName() );
            }else{
                return defaultValue;
            }
        }else {
            return ("yes".equals(val) ||"on".equals(val) || "true".equals(val));
        }
    }

    public static final boolean booleanAttribute(String attribName , Element el, boolean defaultValue) throws XMLAttributemissingException{
        return booleanAttribute(attribName,el,false,defaultValue);
    }
    
    public static final boolean booleanAttribute(String attribName , Element el) throws XMLAttributemissingException{
        return booleanAttribute(attribName,el,true,false);
    }

    
    public static final Boolean getBooleanAttribute(String attribName,Element el){
        String val = el.getAttribute(attribName);
        if (val==null || val.length()==0){
        	// return null explicitly.
            return null;
        }else {
            return new Boolean(booleanAttribute(attribName, el, false));
        }
    }


    public static final Date getDate(String attribName , Element el, Date defaultValue) throws XMLException, XMLAttributemissingException{
        String val = el.getAttribute(attribName);
        if (val==null || val.length()==0){
            if (defaultValue == null){
                throw new XMLAttributemissingException( attribName ,  el.getLocalName());
            }else{
                return defaultValue;
            }
        }else {
            ISO8601Format dateFormat = new ISO8601Format();
            try {
                return (Date)dateFormat.parseObject(val);
            } catch (ParseException e){
                throw new XMLException("Format for attribute " + attribName + " in element " + el.getLocalName() + " is illegal. Correct format is: yyyy-MM-ddThh:mm:ss");
            }

        }
    }

    public static final Date getDateAttrbute(String attribName , Element el) throws XMLAttributemissingException{
        return getDate(attribName,el,null);
    }

    
    /**
     * Return a string attribute
     * @param attribName
     * @param el
     * @param defaultValue
     * @return
     * @throws XMLAttributemissingException
     */
    public static final Maybe<String> getStringAttribute(String attribName , Node el, String defaultValue) throws XMLAttributemissingException{
        Node val = el.getAttributes().getNamedItem(attribName);
        if (val==null || val.getTextContent().isEmpty()){
            if (defaultValue == null){
                throw new XMLAttributemissingException(attribName ,  el.getLocalName());
            }else{
                return StringUtils.maybe(defaultValue);
            }
        }else {
            return StringUtils.maybe(val.getTextContent());
        }
    }

    public static final Maybe<String> getStringAttribute(String attribName , Node el) throws XMLAttributemissingException{
        return getStringAttribute(attribName,el,null);
    }

    /**
     * Return an attribute as a integer
     * @param attribName
     * @param el
     * @param defaultValue
     * @return
     * @throws XMLAttributemissingException
     */
    public final int intAttribute(String attribName , Element el, Integer defaultValue) throws XMLAttributemissingException{

        String val = el.getAttribute(attribName);
        if (val==null){
            if (defaultValue== null){
                throw new XMLAttributemissingException(attribName , el.getLocalName());
            }else{
                return defaultValue;
            }
        }else {
            try{
                return Integer.parseInt(val);
            }catch(NumberFormatException e){
                throw new XMLException("Problem parsing " + val + " to a long");
            }
        }
    }


    public final int getInt(String attribName , Element el) throws XMLAttributemissingException{
        return intAttribute(attribName,el,null);
    }

	public static Node getChildNode(String name, Node node) {
		if (node ==null){
			return null;
		}
		NodeList list = node.getChildNodes();
		for (int i=0; i < list.getLength(); i++){
			Node no = list.item(i);
			if (no.getNodeName().equalsIgnoreCase(name)){
				return no;
			}
		}
		return null;
	}

}
