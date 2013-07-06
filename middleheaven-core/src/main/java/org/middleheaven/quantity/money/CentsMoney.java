package org.middleheaven.quantity.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.Hash;

/**
 * Represents an amount of money that only represents quantity to the currency maximum fraction digits.
 * 
 * Example, for USD the minimum representation possible is 0.01 USD. Any operation that results in less that this value will result in an exception.
 * 
 */
public class CentsMoney extends Money  {


	private static final long serialVersionUID = -609821160213501702L;
	
	private static final int TEN = 10;
	private static final int ONE_HUNDRED = 100;
	private static final int TEN_CUBED= 1000;
	
	/**
	 * Power of 10 has the value at position n in the arrays equals 10 to nth power. 
	 * This is for internal use only.
	 */
	private static int[] powers = new int[]{1,TEN,ONE_HUNDRED,TEN_CUBED};

	private static int getPower(int n){
		if (powers.length<=n){
			// if the needed power does not yet exist, calculate it and add it to 
			// the powers array. This code is a safety it will rarely be runned
			int[] newpowers = new int[n+1];
			System.arraycopy(powers , 0 , newpowers , 0 , 0 );

			for (int i = powers.length; i < newpowers.length; i++){
				newpowers[i] = (int)Math.pow(TEN, i);
			}
			powers = newpowers;
		}
		return powers[n];
	}


	long amount;
	
	public static CentsMoney valueOf (Real value, Currency currency){
		return new CentsMoney (value , currency);
	}
	
	public static CentsMoney valueOf (String value, Currency currency){
		return new CentsMoney (Real.valueOf(value) , currency);
	}

	public static CentsMoney valueOf (String value, String isoCode){
		return new CentsMoney (Real.valueOf(value) , Currency.currency(isoCode));
	}

	public static CentsMoney valueOf (java.lang.Number value, String isoCode){
		return new CentsMoney (Real.valueOf(value) , Currency.currency(isoCode));
	}

	/**
	 * Internal utility method to transform numbers to internal representation
	 * Amounts are expressed as decimal numbers, but this is not the best way to 
	 * represent amount values. The best way is to use a long as it enhances accuracy. 
	 * However ,each currency has its own set of fraction digits so this operation depends on the currency used.
	 * 
	 * @param number
	 * @return a long representing the amount
	 */
	private long toInternalAmount(Real number){
		if (number.isInteger()){
			return number.asNumber().longValue();
		} else {
			return number.times(getPower(currency.getDefaultFractionDigits())).asNumber().longValue();
		}
		
	}  

	protected CentsMoney(Real amount, Currency currency) {
		super(currency);
		this.amount = toInternalAmount(amount);
	}

	protected CentsMoney (CentsMoney other , long amount ){
		super(other.currency);
		this.amount = amount;
	}

	CentsMoney(long amount, Currency currency) {
		super(currency);
		this.amount = amount;
	}

	public Money negate() {
		return new CentsMoney (-amount,this.currency);
	}
	
	/**
	 * @param i
	 * @return
	 */
	public CentsMoney times(int multiplier) {
		return new CentsMoney (amount * multiplier, this.currency);
	}
	
	public Money times(Real other) {
		if (other.isInteger()){
			return new CentsMoney(this.amount().times(other), currency);
		} else {
			return new PrecisionMoney(this.amount().times(other), currency);
		}
	}

	public Money over(Real other) {
		final Real result = this.amount().over(other);
		if (result.isInteger()){
			return new CentsMoney(result, currency);
		} else {
			return new PrecisionMoney(result, currency);
		}
		
	}

	public Real amount() {
		return Real.valueOf(BigDecimal.valueOf(amount, this.currency.getDefaultFractionDigits()));
	}

	public Unit<org.middleheaven.quantity.measurables.Currency> unit() {
		return currency;
	}

	public String toString(){
		return amount().toString() + " " + currency.toString();
	}

    public int hashCode(){
    	return Hash.hash(amount).hashCode();
    }
    
	/**
	 * Allocate <code>this</code> money amount in n installments.
	 * All money is divided among the installments. If the devision remainder is not zero it is added to the first amount
	 * 
	 * @param percentages an int arrays containing the percentages. Example int[]{30,30,40}  
	 * @param installments the quantity of required installments
	 * @return an arrays of Money each of the same currency as <code>this</code> and containing the percentage of the amount
	 */
	public CentsMoney[] distribute(int installments){

		BigInteger[] result = new BigInteger(Long.toString(amount))
		.divideAndRemainder(new BigInteger(Integer.toString(installments)));


		final long even = result[0].longValue();
		long  r = result[1].longValue();

		CentsMoney[] moneys = new CentsMoney[installments]; 
		Arrays.fill(moneys,new CentsMoney(this, even));

		int i=0;
		while (r>0){
			moneys[i] = new CentsMoney(this, even + 1);
			r--;
			i++;
			if (i==moneys.length){
				i=0;
			}
		}

		return moneys;
	}

	/**
	 * Allocate <code>this</code> money amount in determinate number of installments.
	 * All money is distributed among the installments. If the devision remainder is not zero it is added to the 
	 * informed installment
	 * 
	 * @param installments the number of instalments to distribute into.
	 * @param incrementedInstallment The instalment to increment. 1 is the first, 2 is the second, etc...
	 * @return an arrays of Money each of the same currency as <code>this</code> and containing the percentage of the amount
	 * 
	 */
	public CentsMoney[] distribute(int installments , int incrementedInstallment){
		CentsMoney[] qr = divideAndRemainder(installments);

		CentsMoney[] result = new CentsMoney[installments]; 
		Arrays.fill(result,qr[0]);

		if (qr[1].amount!=0){
			result[incrementedInstallment-1] = (CentsMoney)result[incrementedInstallment-1].plus(qr[1]);
		}

		return result;
	}

	/**
	 * Allocate <code>this</code> money amount according to a percentage distribution
	 * All money is divided among the installments. If the devision remainder is not zero it is distributed among the first
	 * installments
	 * 
	 * @param percentages an variable array of int, each representing a  percentages. allocate(30,30,40). The sum must be 100  
	 * @return an arrays of Money each of the same currency as <code>this</code> and containing the percentage of the amount
	 * @throws IllegalArgumentException if percentages is empty or it does not sum 100.
	 */
	public CentsMoney[] allocate(Integer firstPercentage , Integer ... percentages){
		
		percentages = CollectionUtils.appendToArrayBegining(percentages, firstPercentage);
		
		// the sum of all percentages must be 100
		int total = 0;
		for (int i = 0; i < percentages.length ; i++){
			total += percentages[i];
		}
		if (total != ONE_HUNDRED){
			throw new IllegalArgumentException ("Percentages does not sum 100");
		}

		CentsMoney[] results = new CentsMoney[percentages.length];
		long remainder = this.amount;
		for (int i = 0; i < results.length; i++) {
			results[i] = new CentsMoney(this,this.amount * percentages[i] / this.currency.getDefaultFractionDigits()); // integer division
			remainder -= results[i].amount;
		}
		for (int i = 0; i < remainder; i++) {
			results[i].amount++;
		}
		return results;
	}

	/**
	 * Divides the amount equally between <code>n</code> installments
	 * So 100 divided by 3 will return 33,33 for a 2 fraction digits currency. The remainder is ignored.
	 * @param n number of installments
	 * @return the operation result <code>Money</code>
	 * @see #divideAndRemainder(int,int)
	 */
	public CentsMoney divide (int n) {
		return divideAndRemainder(n)[0]; //return the quotient
	}
	/**
	 * Divides the amount equally between n installments
	 * returning the quotient and rest of that division.
	 * 
	 * @param n number of installments
	 * @return the operation result <code>Money</code>
	 * @see #divide(int) 
	 * @see java.math.BigInteger#divideAndRemainder(java.math.BigInteger);
	 */
	public CentsMoney[] divideAndRemainder(int n){
		BigInteger[] result = new BigInteger(Long.toString(amount))
		.divideAndRemainder(new BigInteger(Integer.toString(n)));

		return new CentsMoney[] {  
				new CentsMoney(this, result[0].longValue())  , 
				new CentsMoney(this, result[1].longValue()) 
		}; 

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZero() {
		return this.amount == 0L;
	}



}
