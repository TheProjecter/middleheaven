package org.middleheaven.process.web;

import org.middleheaven.util.Version;

public final class BrowserInfo {

	public static enum CommonBrowsers {
		UNKOWN,
		MSIE,
		FIREFOX,
		CROME,
		OPERA,
		NAVIGATOR,
		SAFARI
		;
	}
	
	String name;
	String baseEngine;
	Version version;
	boolean bot;
	private boolean isUnkown;
	
	public static BrowserInfo unkownBrowser(){
		return new BrowserInfo("","",Version.unknown(),false, true);
	}
	
	/**
	 * 
	 * @param acceptableCultures acceptable display cultures in preference order
	 * @param name
	 * @param baseEngine
	 * @param version
	 * @return
	 */
	public static BrowserInfo browser(String name, String baseEngine, Version version){
		return new BrowserInfo(name,baseEngine,version, false, false);
	}
	
	private BrowserInfo(String name, String baseEngine, Version version, boolean isBot, boolean isUnkown) {
		super();
		this.name = name;
		this.baseEngine = baseEngine;
		this.version = version;
		this.bot = isBot;
		this.isUnkown =isUnkown;
	}

	public boolean isUnkown(){
		return isUnkown;
	}
	
	public boolean isBot(){
		return bot;
	}
	
	public String getName() {
		return name;
	}

	public String getBaseEngine() {
		return baseEngine;
	}

	public Version getVersion() {
		return version;
	}
	
	public boolean is(CommonBrowsers browser){
		return this.name.equalsIgnoreCase(browser.name());
	}
	
	public boolean isMSIE(){
		return is(CommonBrowsers.MSIE);
	}
	
	public boolean isFirefox(){
		return is(CommonBrowsers.FIREFOX);
	}
}
