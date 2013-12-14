/**
 * 
 */
package org.middleheaven.ui.binding;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.lang.reflect.Field;

import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.reflection.ClassSet;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.reflection.inspection.ClassIntrospector;
import org.middleheaven.reflection.inspection.Introspector;
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

				for (ReflectedField field : introspector.inspect().fields().annotatedWith(UIBind.class).retriveAll()){
	
					Maybe<UIBind> fieldBind = field.getAnnotation(UIBind.class);

					UIComponent fieldComponent = UISearch.absolute(client).search(fieldBind.get().value()).first();

					field.set(driver, fieldComponent);
	
				}
				
				for (ReflectedMethod method : introspector.inspect().methods().notInheritFromObject().annotatedWith(UIActionBind.class).retriveAll()){
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
