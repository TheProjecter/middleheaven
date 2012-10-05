/**
 * 
 */
package org.middleheaven.storage.testdomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.middleheaven.model.annotations.Id;
import org.middleheaven.model.annotations.OneToMany;
import org.middleheaven.model.annotations.Transient;
import org.middleheaven.model.annotations.mapping.Column;
import org.middleheaven.model.annotations.mapping.Dataset;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IntegerIdentity;

@Dataset(name="tablec")
public class TestSubject {
	
	@Id(type=IntegerIdentity.class)
	private Identity identity;  
	
	private Collection<TestFamillyMember> familly = new ArrayList<TestFamillyMember>();

	@Transient
	private String transientField;
	
	@Column(name="active")
	private boolean active;
	private String name;
	
	@Column(name="birth")
	private Date birthdate;

	private int number;
	
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