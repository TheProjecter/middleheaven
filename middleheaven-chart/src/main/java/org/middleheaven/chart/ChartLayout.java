package org.middleheaven.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartLayout {

	private ChartType type;
	private String title;
	private List<String> axisNames = new ArrayList<String>();
	private ChartOrientation orientation = ChartOrientation.VERTICAL; 
	private boolean is3D = true;


	public ChartOrientation getOrientation() {
		return orientation;
	}

	public void setOrientation(ChartOrientation orientation) {
		this.orientation = orientation;
	}

	public void setType(ChartType type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static ChartLayout layout(ChartType type, String title){
		return new ChartLayout(type,title);
	}

	public ChartLayout(ChartType type,String title) {
		this.title = title;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public Object getType() {
		return type;
	}

	public String getAxisName(int index){
		try {
			String s = axisNames.get(index);
			if (s==null){
				return "";
			}
			return s;
		} catch (IndexOutOfBoundsException e){
			return "";
		}
	}

	public boolean is3D() {
		return is3D;
	}

	public void set3D(boolean is3D) {
		this.is3D = is3D;
	}

}
