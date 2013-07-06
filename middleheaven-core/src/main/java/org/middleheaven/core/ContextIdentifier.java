/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.core;

import java.io.Serializable;

import org.middleheaven.util.Hash;

public class ContextIdentifier implements Serializable {

	private static final long serialVersionUID = -5069580111097238795L;
	
	private final String id;
	
    protected ContextIdentifier(String id){
        this.id = id;
    }
    
    public static ContextIdentifier getInstance(CharSequence id) {
        return new ContextIdentifier(id.toString());
        
    }
    
    public String toString(){
        return id;
    }
    
    public boolean equals(Object other){
        return other instanceof ContextIdentifier && id !=null && id.equals(other.toString());
    }
    
    public int hashCode(){
    	return Hash.hash(id).hashCode();
    }
}
