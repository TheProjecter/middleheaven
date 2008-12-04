package org.middleheaven.global.atlas.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class ParserCity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file;
		if (args.length==0){
			file = new File ("C:/workspace/eclipse 3.3/MiddleHeaven/src/main/java/org/middleheaven/global/atlas/data/2007 UNLOCODE CodeList.csv");
		} else {
			file = new File(args[0]);
		}

		try {
			InputStream in = new FileInputStream(file);

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			reader.readLine(); // first line is header
			String line;
			String currentCountry = null;
			PrintWriter out = null;
			while((line=reader.readLine())!=null){
				if(line.trim().length()>0){
					String[] nameCode = line.split(",");

					nameCode = purge(nameCode);

					String country = nameCode[1].replaceAll("\"", "");
					if (!country.equals(currentCountry)){
						if (out!=null){
							out.close();
						}

						File f = new File(file.getParentFile().getAbsoluteFile() + "/unlocode-" + country + ".csv");
						if (file.exists()){
							file.delete();
						}
						out = new PrintWriter(new FileOutputStream(f));

						currentCountry = country;
					}
					out.println(line.substring(line.indexOf(",")+1).replaceAll("\"", "").replaceAll(",", ";"));

				}
			}
			reader.close();
			System.out.println("Done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String[] purge(String[] nameCode) {

		if (nameCode.length > 6){
			String[] result = new String[nameCode.length];
			int j =-1;
			for (int i =0; i < nameCode.length;i++){
				if (i==3 && nameCode[i+2].equals(nameCode[i])){
					i++;
					continue;
				} else {
					j++;
					result[j] = nameCode[i];
				}
			}
			return result;
		} else {
			return nameCode;
		}
	}

}
