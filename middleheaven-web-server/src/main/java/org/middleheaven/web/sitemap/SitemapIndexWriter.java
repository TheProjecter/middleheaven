package org.middleheaven.web.sitemap;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.middleheaven.culture.text.ISO8601Format;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.quantity.time.TimeUtils;
import org.middleheaven.util.StringUtils;

public class SitemapIndexWriter {


	public static SitemapIndexWriter forDomain(String domain) throws UnsupportedEncodingException, ManagedIOException {
		return new SitemapIndexWriter(domain);
	}


	private String domain;
	private List<Sitemap> sitemaps = new LinkedList<Sitemap>();

	public SitemapIndexWriter(String domain) {
		this.domain = domain;

	}

	public SitemapIndexWriter addMap(Sitemap map) throws UnsupportedEncodingException{

		sitemaps.add(map);

		return this;

	}

	public void writeTo(ManagedFile folder) throws ManagedIOException{
		ISO8601Format dateFormat = new ISO8601Format();

		if(!folder.getType().isOnlyFolder()){
			throw new IllegalArgumentException("Argument must resolve to a folder in a file system");
		}

		PrintWriter writer=null;
		try {

			ManagedFile file = folder.retrive("sitemap.xml").createFile();

			writer = new PrintWriter(new OutputStreamWriter(file.getContent().getOutputStream(), "UTF-8"));
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			if(this.sitemaps.size() == 1){
				for (Sitemap sm : this.sitemaps){
					write(writer,sm, dateFormat);
				}
			} else {
				// index file
				writer.println("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

				for (Sitemap sm : this.sitemaps){
					writer.println("	<sitemap>");
					writer.println("		<loc>" + domain + "/" + sm.getName() + ".xml.gz" + "</loc>");
					if (sm.getLastModified() != null){
						writer.println("		<lastmod>" + dateFormat.format(TimeUtils.toDate(sm.getLastModified())) +"</lastmod>");
					}
					writer.println("	</sitemap>");  
				}


				writer.println("</sitemapindex>");    

				// sitemap files 
				for (Sitemap sm : this.sitemaps){
					ManagedFile smFile = folder.retrive(sm.getName() + ".xml.gz").createFile();
					PrintWriter smWriter = null;
					try{
						smWriter = new PrintWriter(new OutputStreamWriter(new GZIPOutputStream(smFile.getContent().getOutputStream()), "UTF-8"));
						write(smWriter,sm, dateFormat);
					} finally {
						if (smWriter != null){
							smWriter.close();
						}
					}
					
					
				}

			}
		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		} finally{
			if(writer != null){
				writer.close();
			}
		}
	}

	private void write(PrintWriter writer, Sitemap map, ISO8601Format dateFormat) throws UnsupportedEncodingException{
		writer.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

		for (SitemapEntry entry : map){
			write(writer,entry, dateFormat);
		}

		writer.println("</urlset>"); 
	}
	private void write(PrintWriter writer, SitemapEntry entry , ISO8601Format dateFormat) throws UnsupportedEncodingException{

		writer.println("<url>");
		writer.print("<loc>");
		writer.print( domain + StringUtils.ensureStartsWith( entry.getLocation(), "/"));
		writer.println("</loc>");

		if(entry.getPrioriy() != null){
			writer.print("<priority>");
			writer.print( Double.toString(entry.getPrioriy()));
			writer.println("</priority>");
		}

		if(entry.getLastModified() != null){
			writer.print("<lastmod>");
			writer.print(dateFormat.format(TimeUtils.toDate(entry.getLastModified())));
			writer.println("</lastmod>");
		}

		if(entry.getChangeFrequency() != null){
			writer.print("<changefreq>");
			writer.print(entry.getChangeFrequency().name().toLowerCase());
			writer.println("</changefreq>");
		}

		writer.println("</url>");

	}

}
