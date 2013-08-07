package org.middleheaven.aas;

import java.util.Iterator;


/**
 * Condition with with the ResourceAccessor must comply 
 * in order to access a specific resource.
 */
public final class ResourcePermission implements Permission {


	private static final long serialVersionUID = -8169569097407381165L;
	
	PermissionLevel permissionLevel;
    String resourceName;

    /**
     * Create a {@link ResourcePermission} for a given resource name and level.
     * @param resourceName the resource name
     * @param permissionLevel the permission level.
     * @return the resulting {@link Permission}.
     */
    public static Permission getInstance(String resourceName, PermissionLevel permissionLevel){
        return new ResourcePermission( resourceName, permissionLevel );
    }

    /**
     * Create a {@link ResourcePermission} for a given resource name with <code>PermissionLevel.READ</code> level.
     * @param resourceName the resource name
     * @return the resulting {@link Permission}.
     */
    public static Permission getInstance(String resourceName){
        return new ResourcePermission( resourceName, PermissionLevel.READ );
    }

    private ResourcePermission(String resourceName, PermissionLevel permissionLevel){
    	this.resourceName = resourceName;
    	this.permissionLevel = permissionLevel;
    }
    
    /**
     *
     * @return the resource name
     */
	public String getResourceName() {
		return resourceName;
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
    public boolean equals(Object other){
        return other instanceof ResourcePermission &&
            ((ResourcePermission)other).permissionLevel == this.permissionLevel &&(
                    this.resourceName == null && ((ResourcePermission)other).resourceName==null ||
                    ((ResourcePermission)other).resourceName.equals(this.resourceName)
            );
    }

    /**
     * 
     * {@inheritDoc}
     */
    public int hashCode(){
        return permissionLevel.hashCode() ^ resourceName.hashCode();
    }

    /**
     * 
     * @return the this {@link PermissionLevel} 
     */
    public PermissionLevel getLevel(){
        return this.permissionLevel;
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
            PermissionSet set = (PermissionSet) threshold;
            if (set.size()>1){
                return false; // a unique permission cannot contain more than one
            } else {
                for (Iterator<ResourcePermission> it = set.iterator();it.hasNext();){
                    ResourcePermission r = it.next();
                    if (r.resourceName.equals(this.resourceName)&& PermissionLevel.levelIncludes(this.permissionLevel, r.permissionLevel)){
                           return true;
                    }
                }
                return false;
            }
        } else if (threshold instanceof ResourcePermission){

           ResourcePermission other = (ResourcePermission)threshold;
           if (other.permissionLevel == PermissionLevel.NONE){
               return true; // no level is required, accept it
           }
           return other.resourceName.equals(this.resourceName) && PermissionLevel.levelIncludes(this.permissionLevel, other.permissionLevel);
        } else {
        	return false;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    public String toString(){
        return this.resourceName + "=>" + this.permissionLevel;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean isLenient() {
        return this.permissionLevel == PermissionLevel.NONE;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public boolean isStrict() {
        return "resource.null".equals(resourceName);
    }
}

