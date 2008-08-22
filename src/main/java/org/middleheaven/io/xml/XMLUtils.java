/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.io.xml;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.middleheaven.global.ISO8601Format;


public class XMLUtils {

    public static final Map<String,String> getParams(Element parentElement, String elementName){
        List list = parentElement.elements(elementName);
        Map<String,String> params;
        if (list.isEmpty()){
            params = Collections.emptyMap();
        } else {
            params = new HashMap<String,String>();
            for (Iterator it = list.iterator(); it.hasNext();){
                Element el = (Element)it.next();
                params.put(el.attributeValue("name"), el.getText());
            }
        }
        return params;
    }
    
    public static final boolean booleanAttribute(String attribName , Element el, boolean required, boolean defaultValue) throws XMLAttributemissingException{
        String val = el.attributeValue(attribName);
        if (val==null || val.length()==0){
            if (required){
                throw new XMLAttributemissingException( attribName , el.getName() );
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
        String val = el.attributeValue(attribName);
        if (val==null || val.length()==0){
            return null;
        }else {
            return new Boolean(booleanAttribute(attribName, el, false));
        }
    }


    public static final Date getDate(String attribName , Element el, Date defaultValue) throws XMLException, XMLAttributemissingException{
        String val = el.attributeValue(attribName);
        if (val==null || val.length()==0){
            if (defaultValue == null){
                throw new XMLAttributemissingException( attribName ,  el.getName());
            }else{
                return defaultValue;
            }
        }else {
            ISO8601Format dateFormat = new ISO8601Format();
            try {
                return (Date)dateFormat.parseObject(val);
            } catch (ParseException e){
                throw new XMLException("Format for attribute " + attribName + " in element " + el.getName() + " is illegal. Correct format is: yyyy-MM-ddThh:mm:ss");
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
    public static final String getStringAttribute(String attribName , Element el, String defaultValue) throws XMLAttributemissingException{
        String val = el.attributeValue(attribName);
        if (val==null || val.length()==0 ){
            if (defaultValue == null){
                throw new XMLAttributemissingException(attribName ,  el.getName());
            }else{
                return defaultValue;
            }
        }else {
            return val;
        }
    }

    public static final String getStringAttribute(String attribName , Element el) throws XMLAttributemissingException{
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

        String val = el.attributeValue(attribName);
        if (val==null){
            if (defaultValue== null){
                throw new XMLAttributemissingException(attribName , el.getName());
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

}