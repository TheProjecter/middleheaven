/**
 * 
 */
package org.middleheaven.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.function.Function;

/**
 * 
 */
public class UISearch {

	public static class UISearchExecutor {


		private UIComponent pivot;

		public UISearchExecutor (UIComponent pivot){
			this.pivot = pivot;
		}
		
		static final Function<List<UIComponent>, UIComponent> PARENT = new Function<List<UIComponent>, UIComponent>(){

			@Override
			public List<UIComponent> apply(UIComponent c) {
				return Collections.singletonList(c.getUIParent());
			}
			
		};
		
		static final Function<List<UIComponent>, UIComponent> CHILDREN = new Function<List<UIComponent>, UIComponent>(){

			@Override
			public List<UIComponent> apply(UIComponent c) {
				return c.getChildrenComponents();
			}
			
		};
		
		public UIQuery search(String pattern){


			StringBuilder builder = new StringBuilder();

			Data data = new Data();
			State state = State.TYPE;

			for (int i = 0; i < pattern.length(); i++){

				char c = pattern.charAt(i);

				switch (c){
				case '.':
					data.set(state, builder.toString());
					builder.delete(0, builder.length());
					state = State.FAMILLY;
					break;
				case '#':
					data.set(state, builder.toString());
					builder.delete(0, builder.length());
					state = State.ID;
					break;
				case ':':
					data.set(state, builder.toString());
					builder.delete(0, builder.length());
					state = State.SPECIAL;
					break;
				case '[':
					data.set(state, builder.toString());
					builder.delete(0, builder.length());

					// find properties

					int pos = pattern.indexOf(']', i);

					if (pos < -1){
						throw new IllegalArgumentException("Wrong propeties sintax. ] not found");
					}

					String properties = pattern.substring(i+1, pos);

					String[] all = StringUtils.split(properties, ",");

					for (String s : all){

						String[] parts = StringUtils.split(s, "=");

						data.properties.put(parts[0].trim(), parts[1].trim());
					}
					i = pos;
					state = State.TYPE; // default
					break;
				default:
					builder.append(c);
				}
			}

			data.set(state, builder.toString());

			LinkedList<UIComponent> matches = new LinkedList<UIComponent>();
			LinkedList<UIComponent> candidades = new LinkedList<UIComponent>();
			candidades.add(this.pivot);

			doSearch(data, candidades, matches);

			return new ListUIQuery(matches);
		}

		/**
		 * @param gid
		 * @param components
		 */
		private void doSearch(Data data, LinkedList<UIComponent> candidades, List<UIComponent> matches) {


			while (!candidades.isEmpty()) {

				UIComponent c = candidades.removeFirst();

				if (data.matches(c)){
					applySpecial(data, c , matches);
					
					if( data.isUnique()){
						return;
					}
				} 

				if (c.getChildrenCount() > 0){
					for (UIComponent uic : c.getChildrenComponents()){
						candidades.addLast(uic);
					}
				}

			}

		}
		
		private void applySpecial(Data data, UIComponent c, List<UIComponent> matches) {
			if (data.specials.isEmpty()){
				matches.add(c);
				return;
			}
			
			List<UIComponent> src = new LinkedList<UIComponent>();
			src.add(c);
			
			List<UIComponent> res = new LinkedList<UIComponent>();

			for (Function<List<UIComponent>, UIComponent> op : data.specials){
				 for (UIComponent comp : src) {
					 res.addAll(op.apply(comp));
				 }
				
				 src = res;
				 res = new LinkedList<UIComponent>();
			}
			
			matches.addAll(src);
		}
		
		private static  enum State{

			TYPE,
			FAMILLY,
			ID,
			PROPERTIES,
			SPECIAL

		}

		private static class Data {

			public String type;
			public String familly;
			public String id;
			public Map<String, String> properties = new HashMap<String,String>();
			private LinkedList<Function<List<UIComponent>, UIComponent>> specials = new LinkedList<Function<List<UIComponent>, UIComponent>>();

			public boolean isUnique(){
				return !StringUtils.isEmptyOrBlank(id) || "UIClient".equals(type);
			}
			
			public void set(State state, String value){
				switch(state){
				case TYPE:
					this.type = value;
					break;
				case ID:
					this.id = value;
					break;
				case FAMILLY:
					this.familly = value;
					break;
				case SPECIAL:
					if( value.equalsIgnoreCase("parent")){
						specials.add(PARENT);
					} else if( value.equalsIgnoreCase("children")){
						specials.add(CHILDREN);
					}
				}
			}

			/**
			 * @param c
			 * @return
			 */
			public boolean matches(UIComponent c) {
				return this.matchType(c) && this.matchId(c) && this.matchFamilly(c);
			}

			/**
			 * @param c
			 * @return
			 */
			private boolean matchId(UIComponent c) {
				if(!StringUtils.isEmptyOrBlank(id)){
					return c.getGID().equals(id);
				}
				
				return true;
			}

			/**
			 * @param c
			 * @return
			 */
			@SuppressWarnings("rawtypes")
			private boolean matchType(UIComponent c) {
				if(!StringUtils.isEmptyOrBlank(type)){
					Class t = ClassIntrospector.loadFrom( UIClient.class.getPackage().getName() + "." + type).getIntrospected();
					return c.isType(t);
				}
				
				return true;
			}
			
	
			private boolean matchFamilly(UIComponent c) {
				if(!StringUtils.isEmptyOrBlank(familly)){
					return c.getFamily().equals(this.familly);
				}
				
				return true;
			}
		}

		public UIQuery self() {
			return new ListUIQuery(Collections.singletonList(pivot));
		}

	}

	public static class ListUIQuery implements UIQuery{

		private List<UIComponent> list;

		public ListUIQuery(List<UIComponent> list) {
			super();
			this.list = Collections.unmodifiableList(list);
		}

		@Override
		public UIComponent find(int index) {
			final List<UIComponent> list = list();
			return list.isEmpty() ? null : list.get(index);
		}

		@Override
		public <T extends UIComponent> T find(int index, Class<T> type) {
			return type.cast(list().get(index));
		}

		@Override
		public UIComponent first() {
			return find(0);
		}

		@Override
		public <T extends UIComponent> T first(Class<T> type) {
			return type.cast(first());
		}

		@Override
		public List<UIComponent> list() {
			return list;
		}

	}

	public static UISearchExecutor absolute(UIComponent c){
		return new UISearchExecutor(searchClient(c));
	}
	
	public static UISearchExecutor relative(UIComponent c){
		return new UISearchExecutor(c);
	}

	public interface UISearchFilter {

		public boolean accept(UIComponent c);

		public boolean canContinueSearch(UIComponent c);
	}


	private static UIClient searchClient(UIComponent other) {


		List<UIComponent> list = new ArrayList<UIComponent>(1);

		searchUp ( other, new UISearchFilter() {

			@Override
			public boolean canContinueSearch(UIComponent c) {
				return c instanceof UIClient || c.getUIParent() != null;
			}

			@Override
			public boolean accept(UIComponent c) {
				return c instanceof UIClient;
			}
		}, list);

		return (UIClient) (list.isEmpty() ? null : list.get(0));

	}


	/**
	 * Applies a {@link UISearchFilter} to the {@code bottom} component and up to all parents until the root component,
	 *  the filter returns false from {@link UISearchFilter#canContinueSearch(UIComponent)} or a match is found.
	 * 
	 * @param bottom the start component.
	 * @param filter the filter to apply
	 * @return the found component that matches the filter
	 */
	public static UIComponent searchFirstUp(UIComponent bottom,UISearchFilter filter) {

		UIComponent candidate = bottom;

		while (candidate != null && filter.canContinueSearch(candidate)){

			if (filter.accept(candidate)){
				return candidate;
			}

			candidate = candidate.getUIParent();
		}

		return null;

	}


	/**
	 * Applies a {@link UISearchFilter} to the {@code bottom} component and up to all parents until the root component
	 * of the filter returns false from {@link UISearchFilter#canContinueSearch(UIComponent)}.
	 * All components that are accepted by the filter are added to the provided {@code result} collection.
	 * 
	 * The filter is applied to the bottom component as well.
	 * 
	 * @param bottom the start component.
	 * @param filter the filter to apply
	 * @param result the result collection
	 */
	private static void searchUp(UIComponent bottom,UISearchFilter filter, Collection<UIComponent> result) {

		UIComponent candidate = bottom;

		while (candidate != null && filter.canContinueSearch(candidate)){

			if (filter.accept(candidate)){
				result.add(candidate);
			}

			candidate = candidate.getUIParent();
		}


	}

	/**
	 * @param gid
	 * @param components
	 */
	private static UIComponent doSearch(String gid, LinkedList<UIComponent> components) {


		while (!components.isEmpty()) {

			UIComponent c = components.removeFirst();

			if (c.getGID().equals(gid)){
				return c;
			} else {
				if (c.getChildrenCount() > 0){
					for (UIComponent uic : c.getChildrenComponents()){
						components.addLast(uic);
					}
				}

			}

		}

		return null;
	}
}
