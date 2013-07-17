package org.middleheaven.notification;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.middleheaven.events.EventListenersSet;


public class NotificationTest {

	
	@Test
	public void testNotificationFlux(){
		EventListenersSet<NotificationListener> listeners = EventListenersSet.newSet(NotificationListener.class);
		
		final AtomicInteger integer = new AtomicInteger(0);
		
		listeners.addListener(new NotificationListener(){

			@Override
			public void handleNotification(Notification notification) {
				integer.addAndGet(1);
			}
			
		});
		
		listeners.broadcastEvent().handleNotification(new Notification("teste"));
		assertEquals(1, integer.get());
	}
	
	@Test
	public void testNoticiationInterceptor(){
		MapNotificationService service = new MapNotificationService();
		
		final AtomicInteger integer = new AtomicInteger(0);
		
		service.addFilter(new NotificationBroadcastIntercepter(){

			@Override
			public void onIntercept(Notification notification, InterceptorChain chain) {
				integer.addAndGet(1);
				chain.doChain(notification);
			}
		});
		
		service.addFilter(new NotificationBroadcastIntercepter(){

			@Override
			public void onIntercept(Notification notification, InterceptorChain chain) {
				integer.addAndGet(1);
				chain.doChain(notification);
			}
		});
		
		service.addListener(new NotificationListener(){

			@Override
			public void handleNotification(Notification notification) {
				integer.addAndGet(1);
			}
			
		}, null);
		
		service.broadcast(new Notification("test"));
		assertEquals(3, integer.get());
	}
}
