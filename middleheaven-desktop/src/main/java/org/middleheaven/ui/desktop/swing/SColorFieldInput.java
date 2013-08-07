package org.middleheaven.ui.desktop.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import org.middleheaven.ui.UIColor;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIColorField;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.util.SafeCastUtils;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Maybe;


public class SColorFieldInput extends SBaseFieldInput implements UIColorField {

	private static final long serialVersionUID = -8779476270509974866L;
	
	final JButton button = new JButton();
	Color color = Color.WHITE;
	
	public SColorFieldInput(){
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		
		button.setIcon(new ColorIcon(new Dimension(20,20)));
		this.add(button, BorderLayout.CENTER);
		
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				color = JColorChooser.showDialog(SColorFieldInput.this, "Select color", color);
				
				getValueProperty().set(new UIColor(color.getRGB()));
			}
			
		});
		
		getValueProperty().onChange(new Block<Serializable>(){

			@Override
			public void apply(Serializable value) {
				Maybe<UIColor> uiColor =  SafeCastUtils.safeCast(value, UIColor.class);
				if (uiColor.isAbsent()){
					color = Color.WHITE;
				} else {
					color = new Color(uiColor.get().getRGB(), true);
				}
				button.repaint();
			}
			
		});
	}
	
	public class ColorIcon implements Icon{

		public ColorIcon(Dimension dim) {
			super();
			this.dim = dim;

		}

		private static final int border = 10;
		Dimension dim;
	
		@Override
		public int getIconHeight() {
			return dim.height-border;
		}

		@Override
		public int getIconWidth() {
			return dim.width-border;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int width, int height) {
			dim = c.getSize();
			g.setColor(color);
			g.fillRect(border/2, border/2, getIconWidth(),getIconHeight());
			g.setColor(Color.BLACK);
			g.drawRect(border/2,border/2,getIconWidth(), getIconHeight());
		}
		
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIColorField.class;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIDataContainer(UIDataContainer container) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	

}
