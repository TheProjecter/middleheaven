/**
 * 
 */
package org.middleheaven.storage.testdomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.middleheaven.model.annotations.Entity;
import org.middleheaven.model.annotations.Id;
import org.middleheaven.model.annotations.OneToMany;
import org.middleheaven.quantity.money.CentsMoney;
import org.middleheaven.storage.annotations.Column;
import org.middleheaven.storage.annotations.Dataset;
import org.middleheaven.storage.annotations.Transient;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;

@Entity
@Dataset(hardName="tablec")
public class TestSubject {
	
	@Id(type=IntegerIdentity.class)
	private Identity identity;  
	
	private Collection<TestFamillyMember> familly = new ArrayList<TestFamillyMember>();
	
	@Transient
	private String transientField;
	
	@Column(hardName="active")
	private boolean active;
	private String name;
	
	private CentsMoney money;
	
	@Column(hardName="birth")
	private Date birthdate;

	private int number;
	
	/**
	 * Obtains {@link CentsMoney}.
	 * @return the money
	 */
	public CentsMoney getMoney() {
		return money;
	}

	/**
	 * Atributes {@link CentsMoney}.
	 * @param money the money to set
	 */
	public void setMoney(CentsMoney money) {
		this.money = money;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public TestSubject() {
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(target = TestFamillyMember.class)
	public Collection<TestFamillyMember> getTestFamillyMembers(){
		return Collections.unmodifiableCollection(this.familly);
	}
	
	// the name follwoing add is not the same as the add object class
	public void addFamillyMember(TestFamillyMember member){
		this.familly.add(member);
		member.setParent(this);
	}
	
	public void removeFamillyMember(TestFamillyMember member){
		if (member.getParent().equals(this)){
			this.familly.remove(member);
		}
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return this.number;
	}
}