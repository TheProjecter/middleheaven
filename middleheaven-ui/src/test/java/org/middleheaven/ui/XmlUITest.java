package org.middleheaven.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.testui.TestRenderKit;

/**
 * 
 */
public class XmlUITest extends MiddleHeavenTestCase {

	final UIEnvironmentType uiType = UIEnvironmentType.TEST;
	
	@Test
	public void testXMLBuilder(){
	
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService(), uiType);
		UIEnvironment root = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/ui.xml"));
		assertNotNull(root);

		assertNotNull(root.getClient());
		// one window
		assertEquals(1,root.getClient().getChildrenCount());
		// 2 views
		assertEquals(1,root.getClient().getChildrenComponents().size());
	}
	
	
	
	
//	@Test // TODO move to ui/swing
//	public void testSwingRenderKit(){
//
//		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService(),uiType);
//		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/ui.xml"));
//
//		UIClient client = env.getClient();
//		
//		
//		assertNotNull("UIModel is null",client.getUIModel());
//		
//		RenderKit kit = new TestRenderKit();
//		
//
//		RenderingContext context = new RenderingContext(kit);
//		
//		UIClient rclient = kit.renderComponent(context, null, client);
//		
//		assertNotNull(rclient);
//		assertTrue(rclient instanceof Desktop);
//		assertEquals(client.getGID(), rclient.getGID());
//		
//		UIComponent wui = rclient.getChildrenComponents().get(0);
//		assertTrue("Window is not a JFrame", wui instanceof JFrame);
//		
//		
//		UIComponent layoutui = wui.getChildrenComponents().get(0);
//		assertNotNull(layoutui);
//		assertTrue("Window children is not a JPanel", layoutui instanceof JPanel);
//		
//	}
	
	@Test
	public void textUIQuery(){

		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService(), uiType);
		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/ui.xml"));

		UIClient client = env.getClient();
		
		RenderKit kit = new TestRenderKit();
		
		RenderingContext context = new RenderingContext(kit);
		
		client = kit.renderComponent(context, null, client);
		
		UIComponent wui = client.getChildrenComponents().get(0);
	
		UIComponent layoutui = wui.getChildrenComponents().get(0);
		assertNotNull(layoutui);
	
		List<UIComponent> components = UISearch.absolute(layoutui).self().list();

		assertFalse(components.isEmpty());
		assertEquals(client, components.get(0));
		
		components = UISearch.relative(layoutui).search(":parent").list(); 
		
		assertFalse(components.isEmpty());
		assertEquals(wui, components.get(0));
		
		components = UISearch.relative(layoutui).self().list();
		
		assertFalse(components.isEmpty());
		assertEquals(layoutui, components.get(0));
	
		components = UISearch.relative(layoutui).search(":parent:parent").list(); 
		
		assertFalse(components.isEmpty());
		assertEquals(client, components.get(0));
		
		
		components = UISearch.absolute(layoutui).search("#frameA").list();
		
		assertFalse("#frameA not found" ,components.isEmpty());
		UIComponent frameA = components.get(0);
		assertFalse(components.isEmpty());
		assertEquals("frameA", frameA.getGID());
		
		
		components = UISearch.absolute(layoutui).search("#frameB").list();

		assertFalse("#frameB not found" ,components.isEmpty());
		
		UIComponent frameB = components.get(0);
		assertEquals("frameB", frameB.getGID());
	}
	
	
	@Test
	public void testPatternSearh (){
		
	
		
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService(), uiType);
		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/ui.xml"));

		UIClient client = env.getClient();
		
		RenderKit kit = new TestRenderKit();
		
		RenderingContext context = new RenderingContext(kit);
		
		client = kit.renderComponent(context, null, client);
		
		UIComponent wui = client.getChildrenComponents().get(0);
	
		UIComponent layoutui = wui.getChildrenComponents().get(0);
		assertNotNull(layoutui);
	
		
		// get UICLient type
		
		;
		
		List<UIComponent> components = UISearch.absolute(client).search("UIClient").list();
		
		assertFalse(components.isEmpty());
		assertEquals(client, components.get(0));
		
//		components = UISearch.on(layoutui).search(":parent").list();
//		
//		assertFalse(components.isEmpty());
//		assertEquals(wui, components.get(0));
		
//		components = UITreeCriteria.search(".").execute(layoutui)
//		.list();
//		
//		assertFalse(components.isEmpty());
//		assertEquals(layoutui, components.get(0));
	
//		components = UISearch.on(layoutui).search(":parent:parent").list();
//		
//		
//		assertFalse(components.isEmpty());
//		assertEquals(client, components.get(0));
		
		
		components =UISearch.absolute(layoutui).search("#frameA").list(); 
		
		assertFalse("#frameA not found" ,components.isEmpty());
		
		UIComponent frameA = components.get(0);
		assertFalse(components.isEmpty());
		assertEquals("frameA", frameA.getGID());
		
		components = UISearch.absolute(layoutui).search("#frameB").list(); 
		

		assertFalse("#frameb not found" ,components.isEmpty());
		
		UIComponent frameB = components.get(0);
		assertEquals("frameB", frameB.getGID());
	}
}
