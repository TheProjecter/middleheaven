package org.middleheaven.aas;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A {@link Permission} that is a composition of other {@link Permission}s.
 */
public class PermissionSet implements Permission {


	private static final long serialVersionUID = -4980482150733773169L;
	
	private Map<String,ResourcePermission> permissions;
    
    public PermissionSet(){
    	permissions = new HashMap<String,ResourcePermission>(); 
    }

    /**
     * Add a {@link ResourcePermission} to this set.
     * @param p the permission to add
     */
    public void add(ResourcePermission p){
        permissions.put(p.resourceName,p);
    }

    /**
     * 
     * @return
     */
    public int size(){
        return permissions.size();
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean equals(Object other){
        return other instanceof PermissionSet && ((PermissionSet)other).permissions.equals(this.permissions);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public int hashCode(){
        return permissions.hashCode();
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean implies(Permission threshold) {
        if (threshold.isLenient()){
            return true;
        } else if (threshold.isStrict()){
            return false;
        } else if (threshold instanceof PermissionSet){

        	// verify all resources on the threshold are included in the 
        	// resources of this one
        	
            for (Iterator<ResourcePermission> it = ((PermissionSet)threshold).iterator();it.hasNext();){
                ResourcePermission requiredPermission = (ResourcePermission) it.next();
                ResourcePermission userPermission = (ResourcePermission)this.permissions.get(requiredPermission.resourceName);
                if (userPermission==null){
                	if (requiredPermission.permissionLevel != PermissionLevel.NONE){
                		// a required resource is messing
                        return false;
                	}
                } else {
                	if (!PermissionLevel.levelIncludes(userPermission.permissionLevel , requiredPermission.permissionLevel)){
                        return false;
                    }
                }
                
            }
            return true;
        } else if (threshold instanceof ResourcePermission){
            if (((ResourcePermission)threshold).permissionLevel == PermissionLevel.NONE){
                return true; // no level is required. accept it
            }
            
            for (ResourcePermission r : permissions.values()){
                if (r.implies(threshold)){
                       return true;
                }
            }
            return false;
        } else {
        	throw new IllegalArgumentException(threshold.getClass() + " is not a recognized Permission");
        }
    }

    Iterator<ResourcePermission> iterator(){
       return permissions.values().iterator();
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean isLenient() {
        return false;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public String toString (){
        StringBuilder buffer = new StringBuilder('[');
        for (Permission p :  permissions.values()){
            buffer.append(p.toString());
            buffer.append(';');
        }
        return buffer.append(']').toString();

    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean isStrict() {
        return false;
    }

}
