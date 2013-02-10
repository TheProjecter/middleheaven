/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.middleheaven.util.Hash;
import org.middleheaven.util.collections.CollectionUtils;

/**
 * 
 */
public class ProfilesBag implements Iterable<String>{

	private final Collection<String> profiles = new HashSet<String>(); 
	
	public void add(String ... profile){
		profiles.addAll(Arrays.asList(profile));
	}
	
	public void add(Collection<String> profiles){
		profiles.addAll(profiles);
	}
	
	public void remove(Collection<String> profiles){
		profiles.removeAll(profiles);
	}
	
	public void remove(String ... profile){
		profiles.removeAll(Arrays.asList(profile));
	}
	
	public boolean containsProfile(String profile){
		return profiles.contains(profile);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<String> iterator() {
		return profiles.iterator();
	}
	
	public boolean isEmpty(){
		return profiles.isEmpty();
	}
	
	public int hashCode(){
		return Hash.hash(profiles).hashCode();
	}
	
	public boolean equals(Object other){
		return (other instanceof ProfilesBag ) && CollectionUtils.equalContents(((ProfilesBag) other).profiles, this.profiles); 
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
		return this.profiles.toString();
	}


}
