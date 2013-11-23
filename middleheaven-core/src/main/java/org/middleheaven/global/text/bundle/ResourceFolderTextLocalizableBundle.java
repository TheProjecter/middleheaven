
package org.middleheaven.global.text.bundle;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import org.middleheaven.culture.Culture;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;

/**
 * Uses a key-value property file named {@code domain_language_country.properties} as source for 
 * {@code GlobalLabel}'s localization. 
 * If this file isn't found the file {@code domain_language.properties} is used instead. 
 * If this file also ins't found the file {@code domain.properties}.
 * IF no domain file is found an exception is throwned.
 * 
 */
public final class ResourceFolderTextLocalizableBundle extends LocalizableTextBundle {

	private MessageFormat notFoundFormat = new MessageFormat("??{0}:{1}??");

	private final Map<Key,PropertyResourceBundle> bundles = new HashMap<Key,PropertyResourceBundle>();

	private final ManagedFile folder;

	public ResourceFolderTextLocalizableBundle(ManagedFile folder){  
		this.folder = folder;
	}

	/**
	 * A {@code MessageFormat} compatible message format to use when the label is not found on any bundle.
	 * The default is the pattern
	 * <pre>
	 * ??{0}:{1}??   
	 * </pre>
	 * The first argument is the label's domain and the second is the label's key.
	 * @param pattern
	 */
	public void setNotFoundPattern(String pattern){
		notFoundFormat= new MessageFormat(pattern);
	}

	@Override
	protected String findLabel(LocalizableText label, Culture culture) throws MissingResourceException {

	    Key key = new Key(label.getDomain(), culture);
		PropertyResourceBundle bundle = bundles.get(key);
		final Object[] args = {label.getDomain(),label.getMessageKey()};
		
		if (bundle == null) {
			StringBuilder filename = new StringBuilder(label.getDomain())
			.append('_')
			.append(culture.getLanguage())
			.append('_')
			.append(culture.getCountry())
			.append(".properties");
		
			while (!folder.retrive(filename.toString()).exists()){
				int pos = filename.lastIndexOf("_");
				if (pos<0){
					return notFoundFormat.format(args);
				}
				filename.delete(pos, filename.length());
				filename.append(".properties");
			}
			ManagedFile file = folder.retrive(filename.toString());

			try {
				bundle = new PropertyResourceBundle(file.getContent().getInputStream());
			} catch (IOException e) {
				throw ManagedIOException.manage(e);
			}

			bundles.put(key, bundle);
		}

		try{
			final String message = bundle.getString(label.getMessageKey());
			final Object [] messageParams = label.getMessageParams();
			if (messageParams.length >0 || message.indexOf('{') >=0){
				return new MessageFormat(message).format(messageParams);
			} else {
				return message;
			}
		} catch (MissingResourceException e){
			return notFoundFormat.format(args); 
		}
	}

	private final static class Key {

		private Culture culture;
		private String domain;

		/**
		 * Constructor.
		 * @param domain
		 * @param culture
		 */
		public Key(String domain, Culture culture) {
			this.domain = domain;
			this.culture = culture;
		}
		
		public boolean equals(Object other){
			if (other instanceof Key){
				Key k = (Key)other;
				return this.domain.equals(k.domain) && this.culture.equals(k.culture);
			}
			return false;
		}
		
		public int hashCode(){
			return domain.hashCode() ^ culture.hashCode();
		}
		
	}
}
