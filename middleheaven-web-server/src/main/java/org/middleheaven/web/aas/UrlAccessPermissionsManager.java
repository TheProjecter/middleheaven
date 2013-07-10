/**
 * 
 */
package org.middleheaven.web.aas;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.aas.AccessPermissionsManager;
import org.middleheaven.aas.Permission;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.UrlPattern;

/**
 * 
 */
public final class UrlAccessPermissionsManager implements AccessPermissionsManager<HttpUrl> {

	private List<URLPermission> permissions = new LinkedList<URLPermission>();
	
	
	public UrlAccessPermissionsManager (){}
	
	public void restrict(UrlPattern urlPattern, Permission ... permissions){
		this.permissions.add(new URLPermission(urlPattern,permissions));
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Permission[] getGuardPermission(HttpUrl url) {
		URLPermission selected = null;
		double max = 0;
		String path = url.getContexlesPathAndFileName(true);

		for (URLPermission up : this.permissions){

			double m = up.match(path);
			if (Double.compare(m, max) > 0){
				max = m;
				selected = up;
			}
		}

		if (selected != null){
			return selected.permissions;
		} else {
			return new Permission[0];
		}
	}

	private static class URLPermission {

		private Permission[] permissions;
		private UrlPattern urlPattern;

		public URLPermission(UrlPattern urlPattern, Permission ... permissions) {
			this.urlPattern = urlPattern;
			this.permissions = permissions;
		}

		public double match (String url){
			return urlPattern.match(url);
		}

	}
}
