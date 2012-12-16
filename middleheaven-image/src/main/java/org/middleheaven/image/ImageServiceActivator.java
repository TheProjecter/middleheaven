package org.middleheaven.image;

import java.awt.Image;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.annotations.Service;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;


public class ImageServiceActivator extends ServiceActivator {

	ImageService service;
	

	@Service
	public static class MapImageService implements ImageService{

		Map<String, ImageSource> sources = new TreeMap<String, ImageSource>();
		
		@Override
		public Image getImage(String key) {
			Map<String, Object> properties = Collections.emptyMap();
			return getImage(key, properties);
		}

		@Override
		public Image getImage(String key, Map<String, Object> properties) {
			
			ImageSource source = sources.get(key);
			if (source==null){
				return null;
			}
			
			TreeMap<String,Object> params = new TreeMap<String,Object>(properties);
			params.put("key", key);
			return source.getImage(params);
		}

		@Override
		public void registerImage(String key, ImageSource source) {
			sources.put(key, source);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		//no-dependencies
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(ImageService.class));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		this.service  = new MapImageService();
		
		serviceContext.register(ImageService.class, service);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		this.service = null;
	}


}
