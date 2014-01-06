/*
 * Created on 2007/01/06
 *
 */
package org.middleheaven.quantity.money;

import java.math.RoundingMode;
import java.util.Currency;

class CurrencyMoneyContext extends MoneyContext<Currency>{

    CurrencyMoneyContext(Currency currency){
        this.fractionDigits = currency.getDefaultFractionDigits();
        this.roundingMode = RoundingMode.HALF_EVEN;
    }
}
