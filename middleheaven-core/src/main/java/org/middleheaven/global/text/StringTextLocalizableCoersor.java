/**
 * 
 */
package org.middleheaven.global.text;

import org.middleheaven.util.coersion.AbstractTypeCoersor;
import org.middleheaven.util.coersion.TypeCoercing;


/**
 * Coercor for {@link String} and {@link LocalizableText} types.
 */
public class StringTextLocalizableCoersor extends AbstractTypeCoersor<String, LocalizableText> {

	static {
		TypeCoercing.addCoersor(String.class, LocalizableText.class , new StringTextLocalizableCoersor());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends LocalizableText> T coerceForward(String value,Class<T> type) {
		return type.cast(LocalizableText.valueOf(value));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends String> T coerceReverse(LocalizableText value,
			Class<T> type) {
		return type.cast(value.toString());
	}



}
