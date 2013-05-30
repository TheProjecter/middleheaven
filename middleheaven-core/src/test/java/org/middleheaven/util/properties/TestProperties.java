/**
 * 
 */
package org.middleheaven.util.properties;

import static org.junit.Assert.assertEquals;

import javax.swing.JLabel;

import org.junit.Test;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.property.BindedProperty;
import org.middleheaven.util.property.ComposedProperty;
import org.middleheaven.util.property.Property;

/**
 * 
 */
public class TestProperties {

	@Test
	public void testJavaBeanBinding() {
		
		// JLabel is a JavaBean
		JLabel label = new JLabel();
		
		Property<String> p = BindedProperty.bind("text" , label);
		Property<String> other = ComposedProperty.bind("other",  new Function<String, Property[]> (){

			@Override
			public String apply(Property[] all) {
				return (String)all[0].get();
			}
			
		}, p);
		
		p.set("Teste");
		
		assertEquals("Teste", label.getText());
		assertEquals("Teste", other.get());
		
		label.setText("Changed");
		
		assertEquals("Changed" , p.get());
		assertEquals("Changed", other.get());
		
	}
	
	@Test
	public void testBinding() {
		
		// TextHolder is not a JavaBean
		TextHolder label = new TextHolder();
		
		Property<String> p = BindedProperty.bind("text" , label);
		Property<String> other = ComposedProperty.bind("other",  new Function<String, Property[]> (){

			@Override
			public String apply(Property[] all) {
				return (String)all[0].get();
			}
			
		}, p);
		
		p.set("Teste");
		
		assertEquals("Teste", label.getText());
		assertEquals("Teste", other.get());
		
		label.setText("Changed");
		
		assertEquals("Changed" , p.get());
		assertEquals("Teste", other.get()); // not changed beacause label is not javabean
		
	}

	
	public static class TextHolder {
	
		private String text;

		/**
		 * Obtains {@link String}.
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * Atributes {@link String}.
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}
		
	}
}
