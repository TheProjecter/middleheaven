package org.middleheaven.global.atlas.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.culture.CountryIsoCode;
import org.middleheaven.culture.IsoCode;
import org.middleheaven.global.atlas.Atlas;
import org.middleheaven.global.atlas.AtlasContext;
import org.middleheaven.global.atlas.AtlasLocaleInfo;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.CountryDivisionInfo;
import org.middleheaven.global.atlas.CountryDivisionType;
import org.middleheaven.global.atlas.CountryInfo;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.DateHolder;

public class ISOFileAtlasModule extends AtlasModule {

	String path = "data";

	public ISOFileAtlasModule() {

		String atlaspath = System.getProperty("middleheaven.atlas.data.path");
		if (atlaspath!=null){

			try {
				File file = new File(atlaspath.replaceAll("file:/", "")).getCanonicalFile();
				path = file.getAbsolutePath();
			} catch (IOException e) {

			}

		} 

	}

	public InputStream locate(String filename) {
		try {
			File f = new File( path  + File.separator + filename);
			return new FileInputStream (f);
		} catch (FileNotFoundException e) {
			String name = "/" + Atlas.class.getPackage().getName().replaceAll("\\.","/") + "/data/" + filename;
			return this.getClass().getResourceAsStream(name);
		}
	}


	@Override
	public void loadAtlas(AtlasContext context) {
		InputStream in = locate("iso3166.txt");
		if (in!=null){
			try {

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				int year = Integer.parseInt(reader.readLine());
				DateHolder date = CalendarDate.date(year,1,1);
				String line;
				while((line=reader.readLine())!=null){
					if(line.trim().length()>0){
						String[] nameCode = line.split(";");
						CountryInfo info = new CountryInfo(new CountryIsoCode(nameCode[1]),date);
						info.setName(nameCode[0]);
						info.addLanguage(this.findLocaleForCountry(info.getIsoCode()));
						
						context.addCountryInfo(loadCountry(info));

					}
				}
				reader.close();

			} catch (NumberFormatException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new AtlasNotFoundException();
		}
	}



	private CountryInfo loadCountry(CountryInfo countryInfo) throws IOException{

	
		// read countryDivisions
		Map<IsoCode ,CountryDivisionInfo > divisions = new HashMap<IsoCode ,CountryDivisionInfo>();
		InputStream in = locate("iso3166-2-"  + countryInfo.getIsoCode() + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					final CountryIsoCode isoCode = new CountryIsoCode(nameCode[1]);
					
					CountryDivisionInfo div = new CountryDivisionInfo (isoCode,countryInfo);
					div.setName(nameCode[2]);
					div.setType(CountryDivisionType.fromText(nameCode[3]));
					countryInfo.addAtlasLocale(div);
					divisions.put(div.getIsoCode(), div);
				}
			}
			reader.close();
		} 

		// read cities

		in = locate("unlocode-"  + countryInfo.getIsoCode() + ".csv");
		if (in!=null){

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(";");
					if (nameCode.length>4){

						if (divisions.isEmpty()) {
							countryInfo.addAtlasLocale(new AtlasLocaleInfo(new CountryIsoCode(nameCode[1]),countryInfo).setName(nameCode[2]));
						}  else if (nameCode[4].trim().length()>0 ){ // division is defined
							CountryDivisionInfo division = divisions.get(nameCode[4]);
							if (division!=null){
								division.addAtlasLocale(new AtlasLocaleInfo(new CountryIsoCode(nameCode[1]),division).setName(nameCode[2]));
							} 
							/*
							else {
								// division not found
								division = new CountryDivisionInfo (nameCode[4],countryInfo);
								division.setName(nameCode[2]);
								division.setType(CountryDivisionType.fromText(nameCode[3]));

								countryInfo.addAtlasLocale(division);
								divisions.put(division.getIsoCode(), division);
								division.addAtlasLocale(new AtlasLocaleInfo(nameCode[1],division).setName(nameCode[2]));

							}
*/

						} //else {
							// Cannot add a non division to a devided country 
							// no op
						//}
					}

				}
			}
			reader.close();
		} 

		return countryInfo;
	}





}
