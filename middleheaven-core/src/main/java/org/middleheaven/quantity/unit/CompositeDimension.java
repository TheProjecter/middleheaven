package org.middleheaven.quantity.unit;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.util.Hash;

@SuppressWarnings("unchecked")
public class CompositeDimension extends Dimension {

	/*
	 * Due to limitation of the model dimension generic type can not be atributed 
	 * so the class is all marked with @SuppressWarnings("unchecked") 
	 */
	
	private static final long serialVersionUID = 963244403057787828L;


	public static Dimension multiply(Dimension a, Dimension b){
		CompositeDimension c = new  CompositeDimension();
		c.add(a, 1);
		c.add(b, 1);
		return c.simplify();
	}
	
	public static Dimension over(Dimension a, Dimension b){
		CompositeDimension c = new  CompositeDimension();
		c.add(a, 1);
		c.add(b, -1);
		return c.simplify();
	}


	private Map<Character , BaseDimension > dims = new TreeMap<Character , BaseDimension >();


	CompositeDimension(CompositeDimension other){
		// clone 
		 this.dims = new TreeMap<Character , BaseDimension>(other.dims);

	}

	
	CompositeDimension(){
		this.dims.put(Character.valueOf('1'), BaseDimension.class.cast(Dimension.DIMENTIONLESS));
	}
	
	
	private void add(Dimension<?> other , int sign){
		if (other instanceof BaseDimension){
			BaseDimension<?> baseDim = (BaseDimension<?>)other; 
    		BaseDimension<?> c = dims.get(baseDim.axis());
    		if (c!=null){
    			dims.put(baseDim.axis(), BaseDimension.class.cast(BaseDimension.base(c.axis(),c.exponent()+sign*baseDim.exponent()).simplify()));
    		} else {
    			dims.put(baseDim.axis(), BaseDimension.base(baseDim.axis(),sign*baseDim.exponent()));
    		}

    	} else {
    		// merge compositions
    		CompositeDimension c = (CompositeDimension)other;

    		for (Map.Entry<Character , BaseDimension > entry : c.dims.entrySet()){
    			add(entry.getValue(),sign );
    		}
    	
    	}
	}
	
	@Override
    public Dimension times(Dimension other){
		CompositeDimension result = new CompositeDimension(this);
		result.add(other, 1);
    	return result.simplify();
    }


    @Override
    public Dimension over(Dimension other){
    	CompositeDimension result = new CompositeDimension(this);
    	result.add(other, -1);
    	return result.simplify();
    }
    
	@Override
	protected Dimension simplify() {
		// elimina todos os dimensionless
		for (Iterator<BaseDimension> it = dims.values().iterator(); it.hasNext();){
			BaseDimension d = it.next();
			if (d.exponent()==0 || d.axis() == '1'){
				it.remove();
			}
		}
		
		// if there is only one
		if (dims.size()==1){
			return dims.values().iterator().next().simplify();
		}
		
		// else 
		return this;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder(); 
		for (Iterator<BaseDimension> it = dims.values().iterator(); it.hasNext();){
			builder.append(it.next().toString());
		}
		return builder.toString();
	}
	
	public boolean equals (Object other){
		return other instanceof Dimension && this.toString().equals(other.toString());
	}
	
    public int hashCode(){
    	return Hash.hash(dims.size()).hashCode();
    }
}
