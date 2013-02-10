/**
 * 
 */
package org.middleheaven.global.text;

import java.io.Serializable;

import org.middleheaven.util.Splitter;
import org.middleheaven.util.StringUtils;

/**
 * 
 */
public abstract class TextLocalizable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3077687035613387346L;

	/**
	 * Create a {@link TextLocalizable} from a given template.
	 * A qualified global string is of the form {@code resouceDomainName:label}.
	 * If the given string template does not contain a {@code resouceDoamineName} it will be considered the text is already translated
	 * and will be used as is.
	 * 
	 * @param template the template string.
	 * @return the corresponding {@link TextLocalizable}.
	 * 
	 * 
	 */
	public static TextLocalizable valueOf(String template){
		return valueOf(template, new Serializable[0]);
	}
	
	/**
	 * Create a {@link TextLocalizable} from a given qualified global string and parameters.
	 * A qualified global string is in the form {@code resourceDomain:label}.
	 * 
	 * @param qualifiedString the qualified global string.
	 * @param params the parameters to inclue in the translated label.
	 * 
	 * @return the corresponding {@link TextLocalizable}.
	 * @throws IllegalArgumentException if the given string is no qualified.
	 */
	public static TextLocalizable valueOf(String qualifiedString, Serializable[] params){
		if (qualifiedString.indexOf(":")<0){
			return new TransaltedTextLocalizable(qualifiedString , "", "", params);
		} else {
			String[] str = Splitter.on(":").split(qualifiedString).intoArray(new String[2]);
			
			return valueOf(str[0] , str.length > 1 ? str[1] : "", params);
		}
		
	
	}
	
	/**
	 * Create a {@link TextLocalizable} from a given resourceBundle name and label.
	 * 
	 * 
	 * @param resouceDomain the name of the resource bundle domain.
	 * @param label the label to be translated
	 * @return
	 */
	public static TextLocalizable valueOf(String resouceDomain, String label){
		return valueOf (resouceDomain, label, new Serializable[0]);
	}
	
	/**
	 * Create a {@link TextLocalizable} from a given resourceBundle name and label.
	 * 
	 * 
	 * @param resouceDomain the name of the resource bundle domain.
	 * @param label the label to be translated
	 * @param params the parameters to inclue in the translated label.
	 * @return
	 */
	public static TextLocalizable valueOf(String resorceBundle, String label , Serializable[] params){
		return new KeyTextLocalizable (resorceBundle, label, params);
	}
	
	/**
	 * The name of the resource bundle domain.
	 * 
	 * @return the resource domain name.
	 */
	public abstract String getDomain();

	/**
	 * The message to translate.
	 * 
	 * @return the message to translate.
	 */
	public abstract String getMessageKey();

	/**
	 * The message parameters to use in the translation
	 * 
	 * @return the message parameters to use in the translation
	 */
	public abstract Serializable[] getMessageParams();
	
	/**
	 * 
	 * @return <code>true</code> if this object is localized.
	 */
	public abstract boolean isLocalized();
	
	/**
	 * If this object {@link #isLocalized()} returns <code>true</code>, {@code toString} returns the final, localized text,
	 * otherwise, returns a descripton of the localizable.
	 *  
	 * @return returns the localized text, or descripton
	 */
	public abstract String toString();
	

	
}
