/*
 * Created on 13/12/2005
 */
package org.middleheaven.util;

import java.io.Serializable;

import org.middleheaven.util.coersion.TypeCoercing;


/**
 * Represents a version number. A version number as 4 parts The major version number, the minor version number, the revision number and the build version number
 */
public final class Version implements Comparable<Version>, Serializable{

	private static final long serialVersionUID = -4584274753011801993L;
	
	private int major;
    private int minor;
    private int revision;
    private int build;
    private String tag;
    
    private boolean unkown = false;
    
	public static Version valueOf(int major, int minor, int revision) {
		return valueOf(major, minor, revision, 0, "");
	}
	
	public static Version valueOf(int major, int minor) {
		return valueOf(major, minor, 0, 0, "");
	}
	
	public static Version valueOf(int major, int minor, int revision, int build, CharSequence tag) {
		return new Version(major, minor, revision, build, tag.toString().trim());
	}

	/**
	 * @param number
	 * @return
	 */
	public static Version valueOf(String number) {
		if (StringUtils.isEmptyOrBlank(number)) {
			throw new IllegalArgumentException("Argument is mandatory");
		}
		String[] pars = Splitter.on(".").split(number).asArray();
		
		String tag = null;
		if (pars.length > 4){
			tag = pars[4];
		}
		
		Integer[] res = TypeCoercing.coerceArray(pars, Integer.class, 4);
		

		switch (pars.length){
		case 0:
			return new Version(true);
		case 1:
			return new Version(res[0], 0, 0, 0, tag);
		case 2:
			return new Version(res[0],res[1], 0, 0, tag);
		case 3:
			return new Version(res[0],res[1], res[2], 0, tag);
		case 4:
		default:
			return new Version(res[0],res[1], res[2], res[3], tag);
		}
	
	}
	
	public static Version unknown() {
		return new Version(true);
	}
	
	Version(boolean unkown){
		this.unkown = unkown;
	}
	
    Version(int major, int minor, int revision, int build, String tag) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.build = build;
        this.tag = StringUtils.isEmptyOrBlank(tag) ? null : tag;
    }

    public boolean isUnknown(){
    	return this.unkown;
    }
    
    public final int getBuild() {
        return build;
    }
    
    public final void setBuild(int build) {
        this.build = build;
    }
    
    public final int getMajor() {
        return major;
    }
  
    public final void setMajor(int major) {
        this.major = major;
    }
  
    public final int getMinor() {
        return minor;
    }
    
    public final void setMinor(int minor) {
        this.minor = minor;
    }

    public final int getRevision() {
        return revision;
    }

    public final void setRevision(int revision) {
        this.revision = revision;
    }

    
    
    public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String toString(){
    	if (this.isUnknown()){
    		return "UNKNOWN";
    	}
        return major + "." + minor + "." + revision + (build>0 ? "." + build : "") + (tag != null ? "-" + tag : "");
    }

    public boolean exists(){
        return major + minor + revision + build >0;
    }

	@Override
	public int compareTo(Version other) {
		int d = this.major - other.major;
		if (d==0){
			d = this.minor - other.minor;
			if (d==0){
				d = this.revision - other.revision;
				if (d==0){
					d = this.build - other.build;
				}
			}
		} 
		return d;
	}
	
	public boolean equals(Object other){
		return other instanceof Version && equalsOther((Version)other);
	}
	
	private boolean equalsOther(Version other){
		return this.major == other.major && 
		this.minor == other.minor && 
		this.revision == other.revision &&
		this.build == other.build &&
		(this.tag == null ? other.tag == null : this.tag.equals(other.tag));
	}

    public int hashCode(){
    	return Hash.hash(major).hash(revision).hashCode();
    }


}
