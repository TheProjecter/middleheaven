/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text.bundle;

import java.util.MissingResourceException;

import org.middleheaven.global.text.LocalizableText;


public abstract class FileTextLocalizableBundleReader {


    public abstract String findLabel(LocalizableText label) throws MissingResourceException;
        
}
