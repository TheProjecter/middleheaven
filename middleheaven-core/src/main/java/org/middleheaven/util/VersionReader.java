package org.middleheaven.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class VersionReader {
	
	private VersionReader(){};
	

	 /**
     * Reads the version from a string in the format *-M.m.r.b-* where * is any number of characters
     * M is the major number, m the minor , r the revision and b the build number
     * @return the correct Version
     */
    public static Version fromFileName(String versionStr){
        String[] parts =versionStr.split("-");
        if (parts.length >0){
            for (int i =0;i<parts.length;i++){
                if (Character.isDigit(parts[i].charAt(0)) && Character.isDigit(parts[i].charAt(2))){
                    return fromString(parts[i]);
                }
            }
        }
        return Version.unknown();

    }

    public static Version fromJarFile(File file) throws IOException{
        JarFile jarFile = new JarFile(file);

        Enumeration<JarEntry> e = jarFile.entries();
        JarEntry entry =null;
        
        while (e.hasMoreElements()){
            entry = e.nextElement();
            if (entry.getName().endsWith("version.properties")){
                break;
            }
        }

        if (entry==null){
        	return null;
        }

        return fromResource(jarFile.getInputStream(entry));
    }

    public static Version fromString(String versionStr){
        String[] parts = Splitter.on('.').trim().split(versionStr.trim()).intoArray(new String[0]);;
        Version version = new Version(false);
        try{
            if (parts.length>0){

                version.setMajor(Integer.parseInt(parts[0]));

            }
            if (parts.length>1){

                version.setMinor(Integer.parseInt(parts[1]));

            }
            if (parts.length>2){

                version.setRevision(Integer.parseInt(parts[2]));

            }
            if (parts.length>3){

                version.setBuild(Integer.parseInt(parts[3]));
            }
        } catch (NumberFormatException e){
            return version;
        }
        return version;
    }

    public static Version fromResource(String name) throws IOException{
        return fromResource(Version.class.getResourceAsStream(name));
    }

    public static Version fromResource(InputStream s) throws IOException{
        if (s==null){
            throw new FileNotFoundException();
        }
        Properties p = new Properties();
        p.load(s);
        return fromProperties(p);
    }

    public static Version fromProperties(Properties p) throws IOException{

        Version version = new Version(false);
        try{
            version.setMajor(Integer.parseInt(p.getProperty("version.major")));
        } catch (NumberFormatException e){}
        try{
            version.setMinor(Integer.parseInt(p.getProperty("version.minor")));
        } catch (NumberFormatException e){}
        try{
            version.setRevision(Integer.parseInt(p.getProperty("version.revision")));
        } catch (NumberFormatException e){}
        try{
            version.setBuild(Integer.parseInt(p.getProperty("version.build")));
        } catch (NumberFormatException e){}
        return version;
    }

    
}
