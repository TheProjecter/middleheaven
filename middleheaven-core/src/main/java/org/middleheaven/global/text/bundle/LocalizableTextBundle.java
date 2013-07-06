/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text.bundle;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.middleheaven.collections.ConcurrentKeyMap;
import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.util.function.Function;

/**
 * Translates {@link LocalizableText} labels to strings.
 */
public abstract class LocalizableTextBundle {

	private Map<TextKey, String> bundles = new ConcurrentKeyMap<TextKey, String>( new Function<String, TextKey>(){

		@Override
		public String apply(TextKey key) {
			return  findLabel(key.label, key.culture);
		}
		
	});
	
	
	public LocalizableTextBundle (){
		
	}
	
    /**
     * Translate {@link LocalizableText} label to string for a given {@link Culture}.
     * @param label the label to translate
     * @param culture the culture to translate with
     * @return the translated text.
     * @throws MissingResourceException
     */
    public final String localizeLabel(LocalizableText label, Culture culture) throws MissingResourceException{
    	
    	return bundles.get(new TextKey(label,culture));

    }
    
    protected abstract  String findLabel(LocalizableText label, Culture culture) throws MissingResourceException;
    
    private static class TextKey {
    	
    	private LocalizableText label;
    	private Culture culture;
    	
    	public TextKey(LocalizableText label, Culture culture) {
			super();
			this.label = label;
			this.culture = culture;
		}

    	public boolean equals (Object other){
			return other instanceof TextKey && equalsOther((TextKey)other);
    	}
		
		public boolean equalsOther (TextKey other){
			return this.label.equals(other.label) && this.culture.equals(other.culture);
    	}
		
		public int hashCode(){
			return label.hashCode();
		}
    }
    
 
    
   
}
