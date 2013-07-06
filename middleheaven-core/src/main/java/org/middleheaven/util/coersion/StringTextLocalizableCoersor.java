/**
 * 
 */
package org.middleheaven.util.coersion;

import org.middleheaven.global.text.LocalizableText;

/**
 * Coercor for {@link String} and {@link LocalizableText} types.
 */
public class StringTextLocalizableCoersor extends AbstractTypeCoersor<String, LocalizableText> {


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
