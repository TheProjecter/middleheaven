/*
 * Created on 2006/10/07
 *
 */
package org.middleheaven.global.text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.Publish;
import org.middleheaven.core.services.Require;
import org.middleheaven.core.services.ServiceAtivatorContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.global.Culture;
import org.middleheaven.global.CultureModel;


/**
 *
 */
public class LocalizationServiceProvider extends ServiceActivator{

	RepositoryDomainBundle masterBundle;

	private Map<String, CultureModel> models = new HashMap<String, CultureModel>();
	LocalizationService service = new LocalizationServiceImpl();
	private BootstrapService bootstrapService;
	
	@Require
	public void setBootstrapService(BootstrapService bootstrapService) {
		this.bootstrapService = bootstrapService;
	}

	@Publish
	public LocalizationService getLocalizationService(){
		return service;
	}
	
	@Override
	public void activate(ServiceAtivatorContext context) {
		Container environment = bootstrapService.getEnvironmentBootstrap().getContainer();

		RepositoryDomainBundle masterBundle = new RepositoryDomainBundle();
		masterBundle.setRepository(environment.getEnvironmentConfigRepository());

		RepositoryDomainBundle childBundle = new RepositoryDomainBundle();
		childBundle.setRepository(environment.getAppConfigRepository());
		masterBundle.setChildBundle(childBundle);

	}


	@Override
	public void inactivate(ServiceAtivatorContext context) {
		masterBundle = null;
		service = null;
	}

	private class LocalizationServiceImpl implements LocalizationService{

		public CultureModel getApplicationCultureModel(Object applicationID) {
			return models.get(applicationID.toString());
		}

		public void registerApplicationLocaleModel(Object applicationID, CultureModel model) {
			models.put(applicationID.toString(), model);
		}

		public Locale getExecutionEnvironmentLocale() {
			return Locale.getDefault();
		}


		public String getMessage(GlobalLabel label, boolean asMnemonic) {
			return getMessage(this.getExecutionEnvironmentCulture(), label, asMnemonic); 
		}

		@Override
		public String getMessage(Culture culture, GlobalLabel localResource, boolean asMnemonic) {
			String message = masterBundle.localizeLabel(localResource, culture.toLocale());
			if (asMnemonic){
				return message.replaceAll("&", "");
			}
			return message;
		}

		@Override
		public TimestampFormatter getTimestampFormatter(Culture culture) {
			// TODO implement LocalizationService.getTimestampFormatter
			return null;
		}

		@Override
		public Culture getExecutionEnvironmentCulture() {
			return Culture.defaultValue();
		}

		

		



	}






}