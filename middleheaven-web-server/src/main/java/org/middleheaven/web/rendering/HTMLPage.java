package org.middleheaven.web.rendering;

import java.io.CharArrayReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public final class HTMLPage implements Page{


	private HttpServletRequest request;
	private CharArrayReader content;
	private int length;
	private String title;
	private String head;
	private String body;

	public static HTMLPage parse(char[] content){
		HTMLPage page = new HTMLPage();

		page.content = new CharArrayReader(content);
		page.length = content.length;

		Parser parser = Parser.createParser(new String(content), null);
		try{
			NodeList nodes = parser.parse(null);

			Html p = findNode(Html.class, nodes);


			HeadTag head = findNode(HeadTag.class, p.getChildren());
			TitleTag title = findNode(TitleTag.class, head.getChildren());
			BodyTag body = findNode(BodyTag.class, p.getChildren());

			page.head = removeTagName(head.toHtml(), "head");
			page.head = page.head.replaceAll(title.toHtml(), "");
			page.title = removeTagName(title.toHtml(), "title");
			page.body = removeTagName(body.toHtml(), "body");

			return page;
		} catch (ParserException e){
			throw new RuntimeException(e);
		}
	} 

	private HTMLPage (){

	}

	private static String removeTagName(String n , String tagName){
		return n.replaceAll("<" + tagName + ">" ,"").replaceAll("</" + tagName + ">", "");
	}

	private static <N extends Node> N findNode(Class<N> type , NodeList nodes ) {
		for (int i = 0; i < nodes.size(); i++){
			Node node = nodes.elementAt(i);
			if (type.isInstance(node)){
				return type.cast(node);
			}
		}
		return null;
	}

	@Override
	public int getContentLength() {
		return length;
	}

	@Override
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void writePage(PrintWriter writer) {
		writer.print(content);
	}

	public String getTitle(){
		return title;
	}

	public String getHead(){
		return head;
	}

	public String getBody(){
		return body;
	}
}
