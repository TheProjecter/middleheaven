/**
 * 
 */
package org.middleheaven.process.web.server.action;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.MemberAccess;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.Logger;
import org.middleheaven.process.web.server.action.annotaions.Interceptable;
import org.middleheaven.process.web.server.action.annotaions.Presenter;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.StringUtils;
import org.middleheaven.web.annotations.Path;
import org.middleheaven.web.annotations.Paths;



/**
 * 
 */
public class AnnotationDrivenWebCommandMappingService extends BuildableWebCommandMappingService {


	/**
	 * 
	 */
	private static final class ClassBindConfiguration implements
			BindConfiguration {
		/**
		 * 
		 */
		private final Class<?> presenter;

		/**
		 * Constructor.
		 * @param presenter
		 */
		private ClassBindConfiguration(Class<?> presenter) {
			this.presenter = presenter;
		}

		@Override
		public void configure(Binder binder) {
			Class c = presenter;
			binder.bind(presenter).inSharedScope().to(c);
		}
	}

	private WiringService wiringService;

	public AnnotationDrivenWebCommandMappingService (WiringService wiringService){
		this.wiringService = wiringService;
	}

	public void setObjectPool(WiringService wiringService) {
		this.wiringService = wiringService;
	}


	public PresenterCommandMappingBuilder map(final Class<?> presenter){

		wiringService.addConfiguration(new ClassBindConfiguration(presenter));

		return super.map(presenter);
	}


	/**
	 * Adds a presenter class. 
	 * The class must be annotated with @Presenter or implement ActionPresenter
	 * @param type
	 */
	private void add(Class<?> type){

		final ClassIntrospector<?> introspector = Introspector.of(type);

		if (introspector.isAnnotadedWith(Presenter.class)){


			String rootPath = readPath(introspector);




			//			.through(menuInterceptor)
			//			.through(breadcrumsInterceptor)
			//			
			Enumerable<MethodHandler> methods = introspector.inspect().methods().notInheritFromObject().withAccess(MemberAccess.PUBLIC).retriveAll();

			for (MethodHandler m : methods){

				Maybe<Paths> mayBePaths = m.getAnnotation(Paths.class);

				Path[] all;
				if (mayBePaths.isPresent()){
					all = mayBePaths.get().value();
				} else {
					Maybe<Path> mayBePath = m.getAnnotation(Path.class);
					if (mayBePath.isPresent()){
						all = new Path[]{mayBePath.get()};
					} else {
						all = new Path[0];
					}
				}

				if (all.length == 0){
					PresenterCommandMappingBuilder builder = this.map(type);

					final URLMappingBuilder urlMappingBuilder = builder.to(resolvePath(rootPath, null));

					urlMappingBuilder.withAction(m.getName()).onSuccess().forwardTo(m.getName());

					setupInterceptors(urlMappingBuilder, introspector);

					this.addWebMapping(builder.getMapping());
				} else {

					for (Path p : all){

						PresenterCommandMappingBuilder builder = this.map(type);

						final URLMappingBuilder urlMappingBuilder = builder.to(resolvePath(rootPath, p.value()));

						String target;
						if (p.value().contains("*") || p.value().contains("{")){
							target = m.getName();
						} else {
							target = p.value();
						}

						urlMappingBuilder.withAction(m.getName()).onSuccess().forwardTo(target);

						setupInterceptors(urlMappingBuilder, introspector);


						this.addWebMapping(builder.getMapping());
					}
				}

			}

		} 
	}

	public String resolvePath(String rootPath, String path ){
		if (path == null){
			return rootPath;
		} else {
			String s = StringUtils.concatIfNotEmptyPrefix( rootPath ,path, "/");
			while (s.contains("//")){
				s = s.replaceAll("//", "/");
			}
			return s;
		}
	}

	/**
	 * @param urlMappingBuilder
	 * @param introspector
	 */
	private void setupInterceptors(URLMappingBuilder urlMappingBuilder,
			ClassIntrospector<?> introspector) {

		 Maybe<Interceptable> maybeInterceptable = introspector.getAnnotation(Interceptable.class);

		if (!maybeInterceptable.isAbsent()){
			for (Class<? extends ActionInterceptor> type : maybeInterceptable.get().value()){

				final ActionInterceptor instance = this.wiringService.getInstance(type);
				if (instance != null){
					urlMappingBuilder.through(instance);
				} else {
					Logger.onBookFor(this.getClass()).warn("Instance not found for interceptor {0}", type.getName());
				}
			}
		}



	}


	/**
	 * @param introspector
	 * @return
	 */
	private String readPath(ClassIntrospector<?> introspector) {

		 Maybe<Path> maybePath = introspector.getAnnotation(Path.class);
		 
		return maybePath.isAbsent() ? "" : maybePath.get().value();


	}

	/**
	 * @param presentersSet
	 */
	public void addPresenters(ClassSet presentersSet) {
		for (Class<?> type : presentersSet){
			add(type);
		}
	}
}
