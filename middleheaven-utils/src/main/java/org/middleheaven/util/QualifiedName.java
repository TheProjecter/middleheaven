package org.middleheaven.util;

import java.io.Serializable;

/**
 * 
 */
public class QualifiedName implements Serializable{

	private static final long serialVersionUID = 5242278443994899317L;
	
	private String qualifier;
	private String designation;
	private boolean alias;
	

	/**
	 * Create a {@link QualifiedName} from a qualifier and a designation {@link String}.
	 * @param qualifier the qualifier.
	 * @param designation the designation.
	 * @return a qualified name consisting of the given qualifier and designation
	 */
	public static QualifiedName qualify(String qualifier, String designation){
		return new QualifiedName(qualifier,designation);
	}
	
	/**
	 * Create a {@link QualifiedName} from a designation {@link String} with no qualifier
	 * @param designation the designation.
	 * @return a qualified name consisting of the given qualifier and designation
	 */
	public static QualifiedName qualify( String designation){
		return new QualifiedName(null,designation);
	}
	
	private QualifiedName(String qualifier, String designation) {
		super();
		this.qualifier = qualifier;
		this.designation = designation;
	}
	
	/**
	 * The qualified name's qualifier.
	 * @return
	 */
	public String getQualifier() {
		return qualifier;
	}
	
	/**
	 * the qualified name's designation
	 * @return
	 */
	public String getDesignation() {
		return designation;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * {@link QualifiedName}s equality is not case sensitive.
	 */
	public boolean equals(Object other) {
		return (other instanceof QualifiedName) && equalsOther((QualifiedName) other);
	}

	private boolean equalsOther(QualifiedName other) {
		return this.designation.equalsIgnoreCase(other.designation) && this.qualifier.equalsIgnoreCase(other.qualifier);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return Hash.hash(designation.toLowerCase()).hash(qualifier.toLowerCase()).hashCode();
	}

	public void setAlias(boolean alias) {
		this.alias = alias;
	}

	public boolean isAlias() {
		return this.alias;
	}
	
	public String toString(){
		return this.qualifier + "." + this.designation;
	}
}
