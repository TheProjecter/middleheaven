package org.middleheaven.process.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.global.Culture;
import org.middleheaven.util.Hash;
import org.middleheaven.util.OperatingSystemInfo;
import org.middleheaven.util.VersionReader;

/**
 * 
 */
public final class HttpProcessingUtils {

	private HttpProcessingUtils() {
	}

	// see http://nerds.palmdrive.net/useragent/code.html

	public static HttpUserAgent parse(HttpServletRequest request) {

		String userAgent = request.getHeader("User-Agent");

		BrowserRawInfo res = getBrowser(userAgent);
		BrowserInfo binfo;

		if (res==null){
			binfo = BrowserInfo.unkownBrowser();
			binfo.name = userAgent;
		} else {
			binfo = BrowserInfo.browser(res.name, res.engine,VersionReader.fromString(res.version));
			binfo.bot = res.isBot;
		}

		OSRawInfo os = getOS(userAgent);
		OperatingSystemInfo osInfo;
		if(os==null){
			osInfo = OperatingSystemInfo.unkown();
		} else {
			osInfo = OperatingSystemInfo.system(os.name, os.version, os.versionBase);
		}

		return new HttpUserAgent(binfo, osInfo);
	}

	

	/**
	 * 
	 * @param acceptLanguageHeaderValue
	 * @return
	 */
	public static List<Culture> acceptableLanguages(String acceptLanguageHeaderValue) {

		// read the aCceptLanguage header manually because request.getLocale()
		// defaults to Locale.getDefaultLocale() when the header is not present
		// this will deceive the globalization api
		
		if (acceptLanguageHeaderValue != null && !acceptLanguageHeaderValue.trim().isEmpty()) {
			// see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
			
			// example : da, en-gb;q=0.8, en;q=0.7

			if (acceptLanguageHeaderValue.contains(",")){
				String[] langs = acceptLanguageHeaderValue.split(",");
				List<PrioritizedCulture> pcultures = new ArrayList<PrioritizedCulture>(langs.length);
				
				for (String lang : langs){
					if(lang.contains(";")){
						String[] parts = lang.trim().split(";");
						PrioritizedCulture p = new PrioritizedCulture();
						p.culture = resolveCulture(parts[0].trim());
						p.priority = Double.valueOf(resolveNumber(parts[1].trim()));
						
						pcultures.add(p);
						
					} else {
						PrioritizedCulture p = new PrioritizedCulture();
						p.culture = resolveCulture(lang);
						p.priority = 1.0d;
						pcultures.add(p);
					}
				}
				
				// default order is from minor to major, we need major to minor
				Collections.sort(pcultures,Collections.reverseOrder());
				List<Culture> cultures = new ArrayList<Culture>(langs.length);
				
				for (PrioritizedCulture p : pcultures){
					cultures.add(p.culture);
				}
				
				return cultures;
			} else {
				// just one language
				List<Culture> cultures = new ArrayList<Culture>(1);
				cultures.add(resolveCulture(acceptLanguageHeaderValue.trim()));
			}
		}
		return Collections.emptyList();
	}

	private static String resolveNumber(String number) {
		StringBuilder builder = new StringBuilder();

		for (char c : number.toCharArray()){
			if ( Character.isDigit(c) || c == '.'){
				builder.append(c);
			} 
		}
		
		return builder.toString();
	}

	private static class PrioritizedCulture implements Comparable<PrioritizedCulture> {
		public Culture culture;
		public double priority;
		
		public int compareTo(PrioritizedCulture other){
			return Double.compare(priority, other.priority);
		}
		
		public boolean equals(Object other){
			return (other instanceof PrioritizedCulture) && equalsOther((PrioritizedCulture) other);
		}

		/**
		 * @param other
		 * @return
		 */
		private boolean equalsOther(PrioritizedCulture other) {
			return Double.compare(this.priority, other.priority) == 0;
		}
		
		public int hashCode(){
			return Hash.hash(priority).hashCode();
		}
	}
	
	private static Culture resolveCulture(String acceptableLanguage) {
	
		if (acceptableLanguage.contains("-")){
			String[] parts = acceptableLanguage.split("-");
			return Culture.valueOf(parts);
		} else {
			return Culture.valueOf(acceptableLanguage);
		}
	}

	private static String getVersionNumber(String userAgent, int position) {
		if (position < 0){
			return "";
		}
		StringBuilder res = new StringBuilder();
		int status = 0;

		while (position < userAgent.length()) {
			char c = userAgent.charAt(position);
			switch (status) {
			case 0: // No valid digits encountered yet
				if (c == ' ' || c == '/') {
					break;
				}
				if (c == ';' || c == ')'){
					return "";
				}
				status = 1;
				break;
			case 1: // Version number in progress
				if (c == ';' || c == '/' || c == ')' || c == '(' || c == '['){
					return res.toString().trim();
				}
				if (c == ' '){
					status = 2;
				}
				res.append(c);
				break;
			case 2: // Space encountered - Might need to end the parsing
				if ((Character.isLetter(c) && Character.isLowerCase(c))
						|| Character.isDigit(c)) {
					res.append(c);
					status = 1;
				} else {
					return res.toString().trim();
				}
				break;
			default:
			}
			position++;
		}
		return res.toString().trim();
	}

	private static String getFirstVersionNumber(String userAgent,
			int position, int numDigits) {
		String ver = getVersionNumber(userAgent, position);

		int i = 0;
		
		StringBuilder res = new StringBuilder();
	
		while (i < ver.length() && i < numDigits) {
			res.append(String.valueOf(ver.charAt(i)));
			i++;
		}
		return res.toString();
	}


	private final static String[] BOTS = { "google", "msnbot", "googlebot", "webcrawler", "inktomi", "teoma" };
	private final static  String[] BOTS_NAMES = { "Google", "MSNBot", "Google", "WebCrawler","Inktomi", "Teoma" };

	private static BrowserRawInfo getBotName(String userAgent) {
		userAgent = userAgent.toLowerCase();
		int pos = 0;

		for (int i = 0; i < BOTS.length; i++) {
			if ((pos = userAgent.indexOf(BOTS[i])) >= 0) {
				if (BOTS[i].endsWith("/")) {
					pos += BOTS[i].length();
				} else {
					pos = -1;
				}
				final BrowserRawInfo info = new BrowserRawInfo(BOTS_NAMES[i], "bot", getVersionNumber(userAgent, pos));
				info.isBot = true;
				return info;
			}
		}

		return null;

	}

	/**
	 * 
	 * @param userAgent
	 * @return [os, version, base]
	 */
	private static OSRawInfo getOS(String userAgent) {
		
		final String BOT = "Bot";
		
		if (getBotName(userAgent) != null) {
			return new OSRawInfo(BOT, BOT, BOT);
		}

		int pos;
		
		final String WINDOWS_NAME = "Windows";
		
		if ((pos = userAgent.indexOf("Windows-NT")) > -1) {
			return new OSRawInfo("Win", getVersionNumber(userAgent, pos + 8),WINDOWS_NAME);
		} else if (userAgent.indexOf("Windows NT") > -1) {
			// The different versions of Windows NT are decoded in the verbosity
			// level 2
			// ie: Windows NT 5.1 = Windows XP
			if ((pos = userAgent.indexOf("Windows NT 5.1")) > -1) {
				return new OSRawInfo("WinXP", getVersionNumber(userAgent, pos + 7),WINDOWS_NAME);
			} else if ((pos = userAgent.indexOf("Windows NT 5.0")) > -1) {
				return new OSRawInfo("Win2000", getVersionNumber(userAgent, pos + 7),WINDOWS_NAME);
			} else if ((pos = userAgent.indexOf("Windows NT 5.2")) > -1) {
				return new OSRawInfo("Win2003", getVersionNumber(userAgent, pos + 7),WINDOWS_NAME);
			} else if ((pos = userAgent.indexOf("Windows NT 4.0")) > -1) {
				return new OSRawInfo("WinNT4", getVersionNumber(userAgent, pos + 7),WINDOWS_NAME);
			}

			return new OSRawInfo("WinNT4", "NT", WINDOWS_NAME);

		} else if (userAgent.indexOf("Win") > -1) {
			if (userAgent.indexOf(WINDOWS_NAME) > -1) {
				if ((pos = userAgent.indexOf("Windows 98")) > -1
						|| (pos = userAgent.indexOf("Windows_98")) > -1) {
					return new OSRawInfo("Win98",getVersionNumber(userAgent, pos + 7), WINDOWS_NAME);
				} else if ((pos = userAgent.indexOf("Windows 2000")) > -1) {
					return new OSRawInfo("Win2000", getVersionNumber(userAgent,
							pos + 7), WINDOWS_NAME);
				} else if ((pos = userAgent.indexOf("Windows 95")) > -1) {
					return new OSRawInfo("Win95",getVersionNumber(userAgent, pos + 7), WINDOWS_NAME);
				} else if ((pos = userAgent.indexOf("Windows 9x")) > -1) {
					return new OSRawInfo("Win9x",getVersionNumber(userAgent, pos + 7), WINDOWS_NAME);
				} else if ((pos = userAgent.indexOf("Windows ME")) > -1) {
					return new OSRawInfo("WinME",getVersionNumber(userAgent, pos + 7), WINDOWS_NAME);
				} else if ((pos = userAgent.indexOf("Windows 3.1")) > -1) {
					return new OSRawInfo("Win31",getVersionNumber(userAgent, pos + 7), WINDOWS_NAME);
				}
			}

			return null;
		} else {
			final String MAC = "Mac";
			if ((pos = userAgent.indexOf("Mac OS X")) > -1) {
				return new OSRawInfo("MacOSX", getVersionNumber(userAgent, pos + 6),MAC);
			} else if ((pos = userAgent.indexOf("Mac_PowerPC")) > -1) {
				return new OSRawInfo("MacPPC", getVersionNumber(userAgent, pos + 3),MAC);
			} else {
				final String FREEBDS = "FreeBSD";
				if ((pos = userAgent.indexOf(FREEBDS)) > -1) {
					return new OSRawInfo(FREEBDS, getVersionNumber(userAgent, pos + 7),FREEBDS);
				} else {
					final String LINUX = "Linux";
					if ((pos = userAgent.indexOf(LINUX)) > -1) {
						return new OSRawInfo(LINUX, getVersionNumber(userAgent, pos + 5),LINUX);
					} else {
						final String UNIX = "Unix";
						if ((pos = userAgent.indexOf(UNIX)) > -1) {
							return new OSRawInfo(LINUX, getVersionNumber(userAgent, pos + 4),UNIX);
						} else {
							final String SUNOS = "SunOS";
							if ((pos = userAgent.indexOf(SUNOS)) > -1) {
								return new OSRawInfo(SUNOS, getVersionNumber(userAgent, pos + 5),SUNOS);
							} else {
								return null;
							}
						}
					}
				}
			}
		}

	}

	/**
	 * 
	 * @param userAgent
	 * @return [mainName, version, implementation]
	 */
	private static BrowserRawInfo getBrowser(String userAgent) {
		
		if (userAgent == null || userAgent.trim().isEmpty()){
			return null;
		}
		
		BrowserRawInfo botName = getBotName(userAgent);

		if (botName != null) {
			return botName;
		}

		int pos;
		if ((pos = userAgent.indexOf("Lotus-Notes/")) > -1) {
			return new BrowserRawInfo("LotusNotes", getVersionNumber(userAgent, pos + 12),
			"LotusNotes");
		} else if ((pos = userAgent.indexOf("Opera")) > -1) {
			return new BrowserRawInfo("Opera", getVersionNumber(userAgent, pos + 5),
			"Opera");
		} else if (userAgent.indexOf("MSIE") > -1) {
			return new BrowserRawInfo("MSIE", getVersionNumber(userAgent, pos + 4),
			"Mozilla");
		} else if ((pos = userAgent.indexOf("Gecko/")) > -1) {
			BrowserRawInfo res = new BrowserRawInfo("Gecko", getFirstVersionNumber(userAgent,pos + 5, 4), "Gecko");

			if ((pos = userAgent.indexOf("Netscape/")) > -1) {
				if ((pos = userAgent.indexOf("Netscape/6")) > -1) {
					res.engine = "NS6";
					res.version = getVersionNumber(userAgent, pos + 9);
				} else {
					if ((pos = userAgent.indexOf("Netscape/7")) > -1) {
						res.engine = "NS7";
						res.version = getVersionNumber(userAgent, pos + 9);
					} else {
						res.engine = "NS?";
						res.version += getVersionNumber(userAgent, userAgent
								.indexOf("Netscape/") + 9);
					}
				}
			} else {
				String[] subBrowsers = { "Firefox", "Camino", "Chimera", "Firebird", "Phoenix", "Galeon" };

				for (int i = 0; i < subBrowsers.length; i++) {
					if ((pos = userAgent.indexOf(subBrowsers[i])) > -1) {
						res.engine = subBrowsers[i];
						res.version = getVersionNumber(userAgent, pos
								+ subBrowsers[i].length());
						break;
					}
				}
			}

			return res;

		} else if ((pos = userAgent.indexOf("Netscape/")) > -1) {
			if ((pos = userAgent.indexOf("Netscape/4")) > -1) {
				return new BrowserRawInfo("NS", getVersionNumber(userAgent, pos + 9),"Mozilla");
			} else {
				return new BrowserRawInfo("NS", getVersionNumber(userAgent, pos + 9),"Mozilla");
			}
		} else if ((pos = userAgent.indexOf("Safari/")) > -1) {
			return new BrowserRawInfo("Safari", getVersionNumber(userAgent, pos + 6),"KHTML");
		} else if ((pos = userAgent.indexOf("Konqueror/")) > -1) {
			return new BrowserRawInfo("Konqueror", getVersionNumber(userAgent, pos + 9),"KHTML");
		} else if ((pos = userAgent.indexOf("KHTML")) > -1) {
			return new BrowserRawInfo("KHTML", getVersionNumber(userAgent, pos + 5),"KHTML");
		} else {
			// We will interpret Mozilla/4.x as Netscape Communicator is and
			// only if x
			// is not 0 or 5
			if (userAgent.indexOf("Mozilla/4.") == 0
					&& userAgent.indexOf("Mozilla/4.0") < 0
					&& userAgent.indexOf("Mozilla/4.5 ") < 0) {
				return new BrowserRawInfo("Communicator", getVersionNumber(userAgent,pos + 8), "Mozilla");
			} else {
				return null;
			}

		}

	}

	private static class BrowserRawInfo {
		public String name;
		public String engine;
		public String version;
		public boolean isBot;

		public BrowserRawInfo(String name, String engine, String version) {
			super();
			this.name = name;
			this.engine = engine;
			this.version = version;
		}

	}

	private static class OSRawInfo {
		public String name;
		public String version;
		public String versionBase;

		public OSRawInfo(String name, String version, String versionBase) {
			super();
			this.name = name;
			this.version = version;
			this.versionBase = versionBase;
		}

	}

}
