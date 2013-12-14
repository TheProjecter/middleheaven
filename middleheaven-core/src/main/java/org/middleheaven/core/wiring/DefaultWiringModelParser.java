package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.annotations.Component;
import org.middleheaven.core.annotations.Factory;
import org.middleheaven.core.annotations.Named;
import org.middleheaven.core.annotations.Params;
import org.middleheaven.core.annotations.PostCreate;
import org.middleheaven.core.annotations.PreDestroy;
import org.middleheaven.core.annotations.Profile;
import org.middleheaven.core.annotations.Publish;
import org.middleheaven.core.annotations.Qualifier;
import org.middleheaven.core.annotations.ScopeSpecification;
import org.middleheaven.core.annotations.Wire;
import org.middleheaven.reflection.MemberAccess;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedConstructor;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.Reflector;
import org.middleheaven.reflection.inspection.ClassIntrospector;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.function.Block;


/**
 * The default MiddleHeaven wiring model parser.
 * 
 * Wiring is requested by using the @{@link Wire} annotation.
 * The type of the wiring point will be used to discover witch object to use. The object of the same type will be used by default.
 * 
 * @{@link Name} is used to differenced (to name) the object that is required.
 * 
 * @{@link Params} and @{@link Param} can be used to parameterize the wiring.
 * 
 */
public class DefaultWiringModelParser extends AbstractAnnotationBasedWiringModelParser{


	private Class<? extends Annotation>[] annotations;

	/**
	 * 
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public DefaultWiringModelParser(){
		this.annotations = new Class[]{Wire.class};
	}

	/**
	 * 
	 * Constructor.
	 * @param adicionalAnnotations other annotations that can have a semantic identity with @{@link Wire}.
	 */
	public DefaultWiringModelParser(Class<? extends Annotation> ... adicionalAnnotations){
		if (!CollectionUtils.arrayContains(adicionalAnnotations, Wire.class)){
			this.annotations = CollectionUtils.appendToArrayEnd(adicionalAnnotations, Wire.class);
		} else {
			this.annotations = adicionalAnnotations;
		}

	}


	protected void parseSpecialAnnotations(Annotation a, Map<String, Object> params ){

		if (a.annotationType().isAnnotationPresent(Params.class)){
			Params name = a.annotationType().getAnnotation(Params.class);
			String[] paramPairs = name.value();
			for (String paramPair : paramPairs){
				String[] values = paramPair.split("=");
				if(values.length != 2){
					throw new IllegalStateException("Param pair expected to be in format name=value butt found" + paramPair);
				}
				params.put(values[0], values[1]);
			}
		} else if (Named.class.isAssignableFrom(a.annotationType())){
			params.put("name", ((Named)a).value());
		}

		processQualifier(a, params);
	}


	@Override
	public <T> void readBeanModel(Class<T> classType, final BeanDependencyModel model) {

		ReflectedClass<T> type = Reflector.getReflector().reflect(classType);
		if(!type.equals(model.getBeanClass())){
			throw new IllegalArgumentException("Model type " + model.getBeanClass() + " does not must match " + type.getName());
		}
		
		final Enumerable<ReflectedClass<?>> interfaces = type.getDeclaredInterfaces();


		ProfilesBag profiles = new ProfilesBag();

		for (Annotation a : type.getAnnotations()){

			if (a.annotationType().isAnnotationPresent(ScopeSpecification.class)){

				String scopeName = WiringUtils.readScope(a);

				model.addScope(scopeName);

				if ("service".equals(scopeName)){
					if (!type.isInterface()){
						if (interfaces.isEmpty()){
							throw new IllegalStateException(
								"A @Service must be declared on an annotation or the type must implement an interface"
							);
						}
						model.addContractType(interfaces.getFirst());
					}

				}
			} 

			this.parseProfileAnnotation(a , profiles);

			this.parseSpecialAnnotations(a, model.getParams());
		}

		model.setProfiles(profiles);


		for (ReflectedClass<?> i : interfaces){
			for (Annotation a : i.getAnnotations()){

				if (a.annotationType().isAnnotationPresent(ScopeSpecification.class)){
					model.addScope(WiringUtils.readScope(a));
					model.addContractType(i);
				}

			}
		}




		// find publish points


		Enumerable<ReflectedMethod> methods =  type.inspect().methods().notInheritFromObject().beingStatic(false).searchHierarchy().retriveAll();

		for (ReflectedMethod method : methods){

			if (method.isAnnotationPresent(Publish.class)){

				Publish p = method.getAnnotation(Publish.class).get();

				// process params. this params will be added to the published bean model.
				String[] paramPairs = p.value();

				Map<String,Object> params = new HashMap<String,Object> ();

				for (String paramPair : paramPairs){
					String[] values = paramPair.split("=");
					if(values.length!=2){
						throw new IllegalStateException("Param pair expected to be in format name=value but found" + paramPair);
					}
					params.put(values[0], values[1]);
				}

				Maybe<Params> mayBeParamsAnnotation = method.getAnnotation(Params.class);
				if (mayBeParamsAnnotation.isPresent()){

					for (String paramPair : mayBeParamsAnnotation.get().value()){
						String[] values = paramPair.split("=");
						if(values.length!=2){
							throw new IllegalStateException("Param pair expected to be in format name=value but found" + paramPair);
						}
						params.put(values[0], values[1]);
					}
				}

				String scope = null;
				for (Annotation a : method.getAnnotations()){
					scope = WiringUtils.readScope(a);

					if (scope != null){
						break;
					}
				}

				if (scope == null){
					for (Annotation a : method.getReturnType().getAnnotations()){
						scope = WiringUtils.readScope(a);

						if (scope != null){
							break;
						}
					}
				}

				if (scope == null){
					scope = "default";
				}

				model.addPublishPoint(new MethodPublishPoint(method, params , scope, readParamsSpecification(method)));
			} else if (method.isAnnotationPresent(PostCreate.class)){
				model.setPostCreatePoint(new MethodCallPoint(method));
			} else if (method.isAnnotationPresent(PreDestroy.class)){
				model.setPreDestroiPoint(new MethodCallPoint(method));
			}


		}


		if(model.getScopes().isEmpty()){

			if (model.getPublishPoints().isEmpty()) {
				if (type.isAnnotationPresent(Component.class)){
					model.addScope("shared");
				}
				model.addScope("default");
			} else {
				model.addScope("shared");
			}

		}


		if (model.getProducingWiringPoint() == null && !type.isInterface()){ // only create the producing point if none is already readed.


			// read @Factory

			Enumerable<ReflectedMethod> candidates = type.inspect().methods().beingStatic(true).annotatedWith(Factory.class).retriveAll();

			if (candidates.isEmpty()) {
				readConstructorProducingPoint(type, model);
			} else if ( candidates.size() > 1){
				throw new ConfigurationException("Multiple static factory methods found for " + type + ". Annotate only one method with @" + Factory.class.getSimpleName());
			} 
		}

		// injection points

		type.inspect().fields().beingStatic(false).searchHierarchy().annotatedWith(annotations).retriveAll()
		.forEach(new Block<ReflectedField>(){

			@Override
			public void apply(ReflectedField f) {
				model.addAfterWiringPoint(new FieldAfterWiringPoint(f,readParamsSpecification(f)));
			}

		} );

		type.inspect().methods().beingStatic(false).searchHierarchy().annotatedWith(annotations).retriveAll()
		.forEach(new Block<ReflectedMethod>(){

			@Override
			public void apply(ReflectedMethod method) {
				model.addAfterWiringPoint(new MethodAfterWiringPoint(method, null, readParamsSpecification(method)));
			}

		});



	}

	/**
	 * @param a
	 * @param profiles
	 */
	private void parseProfileAnnotation(Annotation a, ProfilesBag profiles) {
		parseProfileAnnotation(a,profiles, new HashSet());
	}

	private void parseProfileAnnotation(Annotation a, ProfilesBag profiles, Set<Class<?>> visited) {

		if (a.annotationType().equals(Profile.class)){

			String profile = ((Profile) a).value();

			profiles.add(profile);


		} else if (a.annotationType().isAnnotationPresent(Profile.class)){

			Profile q = a.annotationType().getAnnotation(Profile.class);

			String profile = ((Profile) q).value();

			profiles.add(profile);


		} else if (visited.add(a.annotationType())){
			for (Annotation b : a.annotationType().getAnnotations()){
				parseProfileAnnotation(b, profiles, visited);
			}
		}
	}
	private <T> void readConstructorProducingPoint(ReflectedClass<T> type, final BeanDependencyModel model) {
		// constructor
		Enumerable<ReflectedConstructor<T>> constructors = type.inspect()
				.constructors().withAccess(MemberAccess.PUBLIC).annotathedWith(annotations).retriveAll();

		if (constructors.isEmpty()){
			// search all constructors
			constructors = type.inspect().constructors().withAccess(MemberAccess.PUBLIC).retriveAll();
			if (constructors.size()>1 && !type.isEnhanced()){
				throw new ConfigurationException("Multiple constructors found for " + type + ". Annotate only one with @" + Wire.class.getSimpleName());
			} 
		} else if (constructors.size()>1){
			throw new ConfigurationException("Only one constructor may be annotated with @" + Wire.class.getSimpleName());
		} 

		ReflectedConstructor<T> constructor  = constructors.getFirst();

		if (constructor != null){
			model.setProducingWiringPoint(new ConstructorWiringPoint(constructor,null,readParamsSpecification(constructor)));
		} 
	}


	/**
	 * @param annotationType
	 * @param params
	 */
	private void processQualifier(Annotation annotation, Map<String, Object> params) {
		processQualifier(annotation,params, new HashSet());
	}

	private void processQualifier(Annotation a, Map<String, Object> params, Set<Class<?>> visited) {

		if (a.annotationType().equals(Qualifier.class)){

			String quality = ((Qualifier) a).value();
			if (quality.isEmpty()){
				quality = a.annotationType().getName();
			}
			params.put(quality, a);

		} else if (a.annotationType().isAnnotationPresent(Qualifier.class)){

			Qualifier q = a.annotationType().getAnnotation(Qualifier.class);

			String quality = ((Qualifier) q).value();
			if (quality.isEmpty()){
				quality = a.annotationType().getName();
			}
			params.put(quality, a);

		} else if (visited.add(a.annotationType())){
			for (Annotation b : a.annotationType().getAnnotations()){
				processQualifier(b, params, visited);
			}
		}

	}

	protected WiringSpecification parseAnnotations(Enumerable<Annotation> annnnotations ,ReflectedClass<?> type){


		boolean isParamRequired = true;
		boolean isParamShareable = true;

		Map<String, Object> params = new HashMap<String, Object>();

		for (Annotation a : annnnotations){

			if (Wire.class.isAssignableFrom(a.annotationType())){
				Wire wire = (Wire)a;
				isParamRequired = wire.required();
				isParamShareable = wire.shareable();
			}


			parseSpecialAnnotations(a, params);

		}

		WiringSpecification spec = WiringSpecification.search(type.getReflectedType(),params);

		spec.setRequired(isParamRequired);
		spec.setShareable(isParamShareable);

		return spec;
	}



}
