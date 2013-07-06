/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.util.Hash;

/**
 * 
 */
public class ProfilesBag implements Iterable<String>{

	private final Collection<String> profilesSet = new HashSet<String>(); 
	
	public void add(String ... profile){
		this.profilesSet.addAll(Arrays.asList(profile));
	}
	
	public void add(Collection<String> profiles){
		this.profilesSet.addAll(profiles);
	}
	
	public void remove(Collection<String> profiles){
		this.profilesSet.removeAll(profiles);
	}
	
	public void remove(String ... profile){
		this.profilesSet.removeAll(Arrays.asList(profile));
	}
	
	public boolean containsProfile(String profile){
		return profilesSet.contains(profile);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<String> iterator() {
		return profilesSet.iterator();
	}
	
	public boolean isEmpty(){
		return profilesSet.isEmpty();
	}
	
	public int hashCode(){
		return Hash.hash(profilesSet).hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof ProfilesBag ) && CollectionUtils.equalContents(((ProfilesBag) other).profilesSet, this.profilesSet); 
	}

	/**
	 * @param activeProfiles
	 * @return
	 */
	public boolean accepts(ProfilesBag other) {
		
		if (other.isEmpty() || this.isEmpty()) {
			return true;
		}
		
		// TODO test
		for (String profile : other){
			if (this.containsProfile(profile)){
				return true;
			}
		}
		
		return false;
	}
	
	public String toString(){
		return this.profilesSet.toString();
	}


}
