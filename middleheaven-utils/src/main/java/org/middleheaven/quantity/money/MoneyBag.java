/**
 * 
 */
package org.middleheaven.quantity.money;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.quantity.measurables.Currency;
import org.middleheaven.quantity.unit.Unit;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Joiner;

/**
 * 
 */
public final class MoneyBag implements Serializable {

	private static final long serialVersionUID = -3168834691930549570L;
	
	private Map<Unit<Currency>, Money> bag = new HashMap<Unit<Currency>, Money>();

	public static MoneyBag empty(){
		return new MoneyBag();
	}

	public static MoneyBag of(Money ... all){
		MoneyBag bag = new MoneyBag();

		for (Money money : all ) {
			bag.add(money);
		}

		return bag;
	}

	MoneyBag(){

	}

	public MoneyBag times(int multiplier){
		MoneyBag res = new MoneyBag();

		for (Map.Entry<Unit<Currency>, Money> entry : this.bag.entrySet()) {
			res.bag.put(entry.getKey(), entry.getValue().times(multiplier));	
		}

		return res;
	}

	public MoneyBag negate() {
		MoneyBag res = new MoneyBag();

		for (Map.Entry<Unit<Currency>, Money> entry : this.bag.entrySet()) {
			res.bag.put(entry.getKey(), entry.getValue().negate());	
		}

		return res;
	}


	public MoneyBag plus(MoneyBag other) {
		MoneyBag res = new MoneyBag();

		res.bag.putAll(this.bag);

		for (Map.Entry<Unit<Currency>, Money> entry : other.bag.entrySet()) {
			Money money = res.bag.get(entry.getKey());

			if (money == null){
				res.bag.put(entry.getKey(), entry.getValue());
			} else {
				res.bag.put(entry.getKey(), money.plus(entry.getValue()));
			}
		}

		return res;
	}

	public MoneyBag minus(MoneyBag other) {
		MoneyBag res = new MoneyBag();

		res.bag.putAll(this.bag);

		for (Map.Entry<Unit<Currency>, Money> entry : other.bag.entrySet()) {
			Money money = res.bag.get(entry.getKey());

			if (money == null){
				res.bag.put(entry.getKey(), entry.getValue().negate());
			} else {
				res.bag.put(entry.getKey(), money.minus(entry.getValue()));
			}
		}

		return res;
	}

	public boolean isZero() {

		for (Money money : this.bag.values()){
			if(!money.isZero()){
				return false;
			}
		}

		return true;
	}


	public MoneyBag add(Money money) {


		Money m = bag.get(money.unit());

		if (m == null){
			bag.put(money.unit(), money);
		} else {
			bag.put(money.unit(), m.plus(money));
		}
		
		return this;
	}

	public MoneyBag remove(Money money) {

		Money bagMoney = this.bag.get(money.unit());

		if (bagMoney != null){
			this.bag.put(money.unit(), bagMoney.minus(money));
		} else {
			this.bag.put(money.unit(), money.negate());
		}

		return this;
	}

	public boolean equals(Object other){
		return (other instanceof MoneyBag) && equalsOther((MoneyBag)other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(MoneyBag other) {

		MoneyBag bigger = this;
		MoneyBag smaller = other;

		if (this.bag.size() < other.bag.size()) {
			bigger = other;
			smaller = this;
		}

		for (Map.Entry<Unit<Currency>, Money> entry : bigger.bag.entrySet()) {
			Money money = smaller.bag.get(entry.getKey());
			
			if (money == null){
				if( !(entry.getValue() == null || entry.getValue().isZero())){
					return false;
				}
			} else if (money.compareTo(entry.getValue()) != 0){
				return false;
			}
		}

		return true;
	}

	public int hashCode(){
		return Hash.hash(bag.values()).hashCode();
	}

	public String toString(){
		return "[" + Joiner.with(",").join(bag.values()) + "]";
	}



}
