package org.middleheaven.ui.desktop.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class SLayoutRender extends SwingUIRender {

	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return component.getFamily().equals("tabs") || component.getFamily().equals("innerframes");
	}
	
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {
		SLayout s = new SLayout();
		
		String[] famillyParams =  component.getFamily().split(":");
		if (famillyParams[0].equals("tabs")){
	
			s.setLayout(new BorderLayout());
			
			JTabbedPane tabs = new JTabbedPane();
			s.add(tabs, BorderLayout.CENTER);
			
			RenderKit renderKit = context.getRenderKit();
			for (UIComponent comp : component.getChildrenComponents()){

				UIComponent renderedComponent = renderKit.renderComponent(context, s, comp);

				if (renderedComponent instanceof UIView){
					tabs.add(SDisplayUtils.localize(((UIView)renderedComponent).getTitleProperty().get()), (JComponent)renderedComponent);
				} else {
					tabs.add("", (JComponent)renderedComponent);
				}
				
				s.addComponent(renderedComponent);
			}
			
		
		} else if (famillyParams[0].equals("innerframes")){
			s.setLayout(new BorderLayout());
			
			JDesktopPane desktopPane = new JDesktopPane();
			s.add(desktopPane, BorderLayout.CENTER);
			
			RenderKit renderKit = context.getRenderKit();
			UIRender render = renderKit.getRender(UIView.class, "innerframe");
			
			for (UIComponent comp : component.getChildrenComponents()){

				comp.setFamily("innerframe");
				
				UIComponent renderedComponent = renderKit.renderComponent(render,context, s, comp);

				JInternalFrame iframe = (JInternalFrame)renderedComponent;
				
				iframe.setBounds(0, 0, 200, 200); // TODO how to change this size ? 

				iframe.setVisible(true);
				
				desktopPane.add(iframe);
			}
			
		} else if (famillyParams[0].equals("box")){
		
			int orientation = BoxLayout.Y_AXIS;
			if (famillyParams.length>1){
				orientation = famillyParams[1].equals("vertical") ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS;
			}
			s.setLayout(new BoxLayout(s,orientation));
			
		} else if (famillyParams[0].equals("flow")){
		
			int orientation = FlowLayout.CENTER;
			if (famillyParams.length>1){
				if (famillyParams[1].equals("center")){
					orientation = FlowLayout.CENTER;
				} else if (famillyParams[1].equals("left")){
					orientation = FlowLayout.LEFT;
				} else if (famillyParams[1].equals("right")){
					orientation = FlowLayout.RIGHT;
				}
			}
			s.setLayout(new FlowLayout(orientation));
			
		} else {
			
			s.setBackground(Color.GREEN);
		
		}
		
		return s;
		
		
	}

}
