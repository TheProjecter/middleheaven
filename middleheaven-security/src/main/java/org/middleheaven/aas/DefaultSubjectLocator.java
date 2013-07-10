package org.middleheaven.aas;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Creates a new Subject just based on passed information.
 */
public class DefaultSubjectLocator implements SubjectLocator {

	@Override
	public Subject load(Set<Credential> credentials, Set<Role> roles) {
		String name ="";
		
		for (Credential c  : credentials){
			if (c instanceof NamedCredential){
				name = ((NamedCredential)c).getName();
				break;
			}
		}
		
		return new DefaultSubject(name, roles);
	}
	
	private static class DefaultSubject implements Subject {

		private Map<String, Role > roles = new HashMap<String,Role> ();
		private String name;
		
		public DefaultSubject (String name,  Set<Role> roles){
			for (Role r : roles){
				this.roles.put(r.getName(), r);
			}
			
			this.name = name;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isInRole(String roleName) {
			return roles.containsKey(roleName);
		}
		
	}

}
