/*
 * Created on 2007/01/06
 *
 */
package org.middleheaven.quantity.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Currency;


public class MoneyContext<C> {

    
    public static MoneyContext getCurrencyMoneyContext(Currency currency){
        return new CurrencyMoneyContext(currency);
    }
    
    public static <C> MoneyContext<C> getMoneyContext(Class<C> currencyClass ,int fraction, RoundingMode mode){ 
        return new MoneyContext<C>(fraction, mode);
    }
    
    public static <C> MoneyContext<C> getMoneyContext(Class<C> currencyClass ,int fraction){ 
        return new MoneyContext<C>(fraction,RoundingMode.HALF_EVEN);
    }
    
    protected int fractionDigits;
    protected RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    
    protected MoneyContext(){}
    
    protected MoneyContext(int fractionDigits, RoundingMode roundingMode){
        this.fractionDigits = fractionDigits;
        this.roundingMode = roundingMode;
    }
    
    
    
    public int getFractionDigits(){
        return fractionDigits;
    }
    
    public RoundingMode getRoundingMode(){
        return roundingMode;
    }
    
    
    long toAmount(Number number){
        return Math.round(number.doubleValue() * getPower(this.fractionDigits));
    }
    
    long toAmount(long number){
        return number * getPower(this.fractionDigits);
    }
    
    BigDecimal fromAmount(long amount){
        return BigDecimal.valueOf(amount, this.fractionDigits);
    }
    
    private static int getPower(int n){
        if (powers.length<=n){
           int[] newpowers = Arrays.copyOf(powers, n+1);
           for (int i = powers.length; i < newpowers.length; i++){
               newpowers[i] = (int)Math.pow(10, i);
           }
           powers = newpowers;
        }
        return powers[n];
    }
    
    private static int[] powers = new int[]{1,10,100,1000};
}
