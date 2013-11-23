/**
 * 
 */
package org.middleheaven.ui.binding;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.lang.reflect.Field;

import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.annotations.UIActionBind;
import org.middleheaven.ui.annotations.UIBind;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.util.Maybe;

/**
 * 
 */
public final class UIBinder {


	public static UIBinder newInstance(ClassSet drivers, WiringService wiring){
		return new UIBinder(drivers, wiring);
	}

	private ClassSet drivers;
	private WiringService wiring;

	private UIBinder (ClassSet drivers, WiringService wiring){
		this.drivers = drivers;
		this.wiring = wiring;
	}
	
	public void bind(final UIClient client){

		for (Class<?> c : drivers){
			final ClassIntrospector<?> introspector = Introspector.of(c);
			Maybe<UIBind> bind = introspector.getAnnotation(UIBind.class);

			if (bind.isPresent()){

				final Object driver = wiring.getInstance(c);

				for (Field field : introspector.inspect().fields().annotatedWith(UIBind.class).retriveAll()){
					field.setAccessible(true);
					UIBind fieldBind = field.getAnnotation(UIBind.class);

					UIComponent fieldComponent = UISearch.absolute(client).search(fieldBind.value()).first();

					try {
						field.set(driver, fieldComponent);
					} catch (IllegalArgumentException e) {
						throw ReflectionException.manage(e, field.getDeclaringClass());
					} catch (IllegalAccessException e) {
						throw ReflectionException.manage(e, field.getDeclaringClass());
					}
				}
				
				for (MethodHandler method : introspector.inspect().methods().notInheritFromObject().annotatedWith(UIActionBind.class).retriveAll()){
					UIActionBind methodBind = method.getAnnotation(UIActionBind.class).get();

					UIComponent fieldComponent = UISearch.absolute(client).search(methodBind.value()).first();

					if (fieldComponent.isType(UICommand.class)){
						UICommand command = safeCast(fieldComponent, UICommand.class).get();
						
						command.addCommandListener(new MethodCommandListener(method , driver ));
					}
				}

			}
		}
	}
}
