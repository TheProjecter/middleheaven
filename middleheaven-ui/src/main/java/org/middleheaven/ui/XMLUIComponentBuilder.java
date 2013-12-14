package org.middleheaven.ui;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.annotations.Shared;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.xml.XMLException;
import org.middleheaven.io.xml.XMLObjectContructor;
import org.middleheaven.io.xml.XMLUtils;
import org.middleheaven.reflection.NoSuchClassReflectionException;
import org.middleheaven.reflection.ReflectedProperty;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.layout.UIBorderLayoutManager;
import org.middleheaven.ui.layout.UIClientLayoutManager;
import org.middleheaven.ui.property.Property;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Predicate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUIComponentBuilder extends XMLObjectContructor<UIEnvironment> implements UIComponentBuilder {

	WiringService wiringContext;
	private UIEnvironmentType targetUIEnvironmentType;
	private int nextGID = 0;
	
	public XMLUIComponentBuilder(WiringService wiringContext , UIEnvironmentType targetUIEnvironmentType){
		this.wiringContext = wiringContext;
		this.targetUIEnvironmentType = targetUIEnvironmentType;
	}

	@Override
	public UIEnvironment build() {
		return this.getConstructedObject();
	}

	public UIEnvironment buildFrom(ManagedFile file){
		constructFrom(file);
		return build();
	}

	public UIEnvironment buildFrom(File file){
		constructFrom(file);
		return build();
	}

	@Override
	protected void constructFrom(Document document) 
			throws ManagedIOException,XMLException {

		// create environment and client
		Element element = document.getDocumentElement();

		//UIEnvironmentType type = UIEnvironmentType.valueOf(XMLUtils.getStringAttribute("type", element).toUpperCase());
		//String name= XMLUtils.getStringAttribute("name", element);

		BuildedUIEnvironment env = new BuildedUIEnvironment(targetUIEnvironmentType);
		//env.setName(name);

		// set clients

		NodeList clients = element.getChildNodes();
		for (int i=0; i < clients.getLength(); i++){
			Node child = clients.item(i);
			if (child.getNodeName().equals("uic")){


				UIComponent uiclient = (build(targetUIEnvironmentType, child,null));

				env.setClient((UIClient)uiclient);

			}
		}

		this.setConstructedObject(env);
	}

	private Class<UIComponent> resolveClass(String type){
		if (type.equals("commandset")){
			type = "CommandSet";
		}
		try {
			return Introspector.of(UIComponent.class).load("org.middleheaven.ui.UI" + StringUtils.capitalizeFirst(type)).getIntrospected().getReflectedType();
		} catch (NoSuchClassReflectionException e) {
			return Introspector.of(UIComponent.class).load("org.middleheaven.ui.components.UI" + StringUtils.capitalizeFirst(type)).getIntrospected().getReflectedType();
		}
	}

	private UIComponent build (UIEnvironmentType envType, Node node , UIComponent parent){

		Maybe<String> type = XMLUtils.getStringAttribute("type", node);

		if (type.isAbsent()){
			throw new IllegalStateException("Type is mandatory");
		}

		Maybe<String> familly = XMLUtils.getStringAttribute("familly", node, "default");

		final Map<String, Maybe<String>> propertiesValues = new HashMap<String, Maybe<String>>(); 


		Maybe<String> gid = XMLUtils.getStringAttribute("id", node, "");
		Maybe<String> name = XMLUtils.getStringAttribute("name", node , "");

		propertiesValues.put("name", name);
		propertiesValues.put("title", XMLUtils.getStringAttribute("title", node, ""));
		propertiesValues.put("text", XMLUtils.getStringAttribute("text", node, ""));
		propertiesValues.put("visible", XMLUtils.getStringAttribute("visible", node, ""));
		propertiesValues.put("enable", XMLUtils.getStringAttribute("enable", node, ""));

		Maybe<String> layout = XMLUtils.getStringAttribute("layout", node, "");

		Maybe<String> layoutConstraint = XMLUtils.getStringAttribute("layoutConstraint", node, "");

		if ("default".equals(familly.get())){
			familly = Maybe.absent();
		}

		Class<UIComponent> uiTypeClass = resolveClass(type.get());

		UIComponent uiComponent =  GenericUIComponent.getInstance(uiTypeClass, familly.or(""));

		if (!uiTypeClass.isInstance(uiComponent)){
			throw new IllegalStateException("Incompatible ui types");
		}

		if (gid.isPresent()){
			uiComponent.setGID(gid.get());
		} else {
			uiComponent.setGID(Integer.toString(nextGID++));
		}

		if (uiComponent.isType(UIContainer.class)){
			UILayout myLayout;
			if (layout.isPresent()){
				myLayout =  GenericUIComponent.getInstance(UILayout.class, layout.get());


			} else if (uiComponent.isType(UIClient.class)){
				myLayout =  GenericUIComponent.getInstance(UILayout.class, "client");
				myLayout.setLayoutManager(new UIClientLayoutManager());
				((UIContainer)uiComponent).setUIContainerLayout(myLayout);
			} else {
				myLayout =  GenericUIComponent.getInstance(UILayout.class, "border");
				myLayout.setLayoutManager(new UIBorderLayoutManager());
				((UIContainer)uiComponent).setUIContainerLayout(myLayout);
			}

			((UIContainer)uiComponent).setUIContainerLayout(myLayout);
		}

		uiComponent.setUIParent(parent);
		if (parent != null && parent.isType(UIContainer.class)){
			UIContainer container = (UIContainer) parent;

			if (layoutConstraint.isPresent() ){

				UILayout parentLayout = container.getUIContainerLayout();

				container.addComponent(uiComponent, XMLLayoutConstraintsParser.parseConstraint(layoutConstraint.get(), parentLayout.getFamily()));
			} else {
				container.addComponent(uiComponent);
			}

		}


		// read properties

		for (Property prop  : getProperties(uiTypeClass, uiComponent)){
			
			final Maybe<String> maybe = propertiesValues.get(prop.getName());
			if (maybe != null && maybe.isPresent()){
				prop.set((Serializable)TypeCoercing.coerce(maybe.get() , prop.getValueType()));
			}
		}

		// children

		NodeList children = node.getChildNodes();
		for (int i=0; i < children.getLength(); i++){
			Node child = children.item(i);
			if (child.getNodeName().equals("uic")){
				build(envType,child,uiComponent);

			}
		}

		return uiComponent;
	}


	private static class ModelBinderConfiguration <T> implements BindConfiguration{
		Class<T> uiModelClass;
		T uiModel;

		public  ModelBinderConfiguration(Class<T> uiModelClass , T uiModel){
			this.uiModel = uiModel;
			this.uiModelClass = uiModelClass;
		}

		@Override
		public void configure(Binder binder) {
			binder.bind(uiModelClass).in(Shared.class).toInstance(uiModel);
		}

	}
	public static class BuildedUIEnvironment extends UIEnvironment{


		public BuildedUIEnvironment(UIEnvironmentType type) {
			super(type);
		}

		@Override
		protected boolean accept(Class<? extends UIClient> type) {
			return true; // TODO verify againts type
		}

	}

	private Enumerable<Property> getProperties(Class<UIComponent> uiTypeClass, final UIComponent component){
		return Introspector.of(uiTypeClass).inspect().properties().retriveAll()
				.filter(new PropertyHandlerPredicate())
				.map(new PropertyHandlerPredicateFunction(component));
	}

	private static class PropertyHandlerPredicate implements Predicate<ReflectedProperty>{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Boolean apply(ReflectedProperty obj) {
			return obj.getValueType().isSubTypeOf(Property.class);
		}
		
	}
	
	private static class PropertyHandlerPredicateFunction implements Function<Property, ReflectedProperty>{

		private final UIComponent component;

		
		public PropertyHandlerPredicateFunction(UIComponent component) {
			super();
			this.component = component;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public Property apply(ReflectedProperty obj) {
			Property  p = (Property)obj.getValue(component);

			if (p == null){
				throw new IllegalStateException("Property " + obj.getDeclaringClass() + "." + obj.getName() + " is null");
			}

			return p;
		}
		
	}
}
