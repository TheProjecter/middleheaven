package org.middleheaven.core.reflection.inspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.collections.TransformedCollection;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.io.IO;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Maybe;
import org.middleheaven.util.function.Predicate;

public class PackageIntrospector extends Introspector {

	private Package typePackage;

	public static PackageIntrospector of (Package typePackage){
		return new PackageIntrospector(typePackage);
	}
	
	
	PackageIntrospector(Package typePackage) {
		this.typePackage = typePackage;
	}

	public String getName(){
		return typePackage.getName();
	}

	public Enumerable<PackageIntrospector> getSubpackages(){
		return CollectionUtils.asEnumerable(Package.getPackages())
				.filter(new PackageIntrospectorPredicate(this.getName()))
				.map(mapper);
		
	}
	

	private static class PackageIntrospectorPredicate implements Predicate<Package> {

		
		private String name;

		public PackageIntrospectorPredicate (String name){
			this.name = name;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(Package p) {
			return p.getName().startsWith(name) && p.getName().length() != name.length();
		}
		
	}
	private static final PackageIntrospectorMapper mapper = new PackageIntrospectorMapper();

	private static class PackageIntrospectorMapper implements  Mapper<PackageIntrospector, Package>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public PackageIntrospector apply(Package p) {
			return PackageIntrospector.of(p);
		}
		
	}
	
	private static class IntrospectorMapper implements  Mapper<ClassIntrospector, Class<?>>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public ClassIntrospector apply(Class<?> obj) {
			return Introspector.of(obj);
		}
		
	}
	
	private static final IntrospectorMapper introspectorMapper = new IntrospectorMapper();

	public Enumerable<ClassIntrospector> getClassesIntrospectors(){
		return CollectionUtils.asEnumerable(
			TransformedCollection.transform( getPackageClasses(typePackage) , introspectorMapper)
		);
	}

	public Enumerable<Class<?>> getClasses(){
		return CollectionUtils.asEnumerable(getPackageClasses(typePackage));
	}
	
	@Override
	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		return Maybe.of(this.typePackage.getAnnotation(annotationClass));
	}

	@Override
	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return this.typePackage.isAnnotationPresent(annotationClass);
	}
	
	
	/**
	 * Obtains all classes in the package.
	 * 
	 * @param classPackage the package where to look.
	 * @return the collection of all packages.
	 */
	public static Set<Class<?>> getPackageClasses(Package classPackage) {

		Set<Class<?>> classes = new HashSet<Class<?>>();
		try {
			ClassLoader cl = classPackage.getClass().getClassLoader();
			if (cl == null) {
				// no class loader specified -> use thread context class loader
				cl = Thread.currentThread().getContextClassLoader();
			}
			String packageUrl = classPackage.getName().replaceAll("\\.", "/");
			Enumeration<URL> packageLocations = cl.getResources(packageUrl);

			while (packageLocations.hasMoreElements()){
				URL url = packageLocations.nextElement();
				process(classes,url,classPackage.getName());
			}

			return classes;
		} catch (IOException e) {
			throw new ReflectionException(e);
		}
	}

    private static void process(Set<Class<?>> classes , URL url , String packageName) throws IOException {
        if (url.getProtocol().equals("file")) {
            try {
                File folder = new File(url.toURI());

                File[] files = folder.listFiles(new FilenameFilter() {

                    public boolean accept(File file, String name) {
                        return name.indexOf('$') < 0 && name.endsWith(".class");
                    }

                });

                for (File f : files) {
                    String name = packageName
                    + "."
                    + f.getName().substring(0, f.getName().indexOf('.'));
                    classes.add(ClassIntrospector.loadFrom(name).getIntrospected());
                }
            } catch (URISyntaxException e) {
                throw new ReflectionException(e);
            }
        } else if (url.getProtocol().equals("jar")) {

            int pos = url.getFile().indexOf('!');

            extractClasseInJarPackage(
                    classes ,
                    url.getFile().substring(0, pos),
                    packageName);
        } else {
            throw new ReflectionException(
                    "Cannot find classes in package using protocol " + url.getProtocol()
            );
        }
    }

    private static void extractClasseInJarPackage(final Set<Class<?>> classes, String jarName, final String aPackageName)
        throws IOException {

    	final String packageName = aPackageName.replaceAll("\\." , "/");
        if (jarName.startsWith("file:")) {
            jarName = jarName.substring("file:".length());
        }

        IO.using(new JarInputStream(new FileInputStream(new File(jarName))), new Block<JarInputStream>(){

			@Override
			public void apply(JarInputStream jarFile) {
				try {   
		        JarEntry jarEntry;

		        while (true) {
		            jarEntry = jarFile.getNextJarEntry();
		            if (jarEntry == null) {
		                break;
		            }
		            
		           
		            final String name = jarEntry.getName();
		            
		   
					if (name.startsWith(packageName)
		                    && name.endsWith(".class") && !isFromSubPackage(name, packageName)) {

		                String className = name.replaceAll("/", "\\.");
		                className = className.substring(0, className.length() - ".class".length());

		                try {
		                    classes.add(Class.forName(className));
		                } catch (ClassNotFoundException e) {
		                    // no-op
		                    continue;
		                }
		            }
		        }

				} catch (IOException e){
					throw ManagedIOException.manage(e);
				}
			}
			
		});
    }

	/**
	 * @param name
	 * @param packageName
	 * @return
	 */
	private static boolean isFromSubPackage(String name, String packageName) {
		return Character.isLowerCase(name.replaceAll(packageName, "").charAt(1));
	}


	/**
	 * @return
	 */
	public Package getIntrospected() {
		return this.typePackage;
	}
}
