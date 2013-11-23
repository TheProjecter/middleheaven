package org.middleheaven.chart;

/**
 * A indexable, multi-dimensional set of values 
 * 
 *
 */
public interface Series {
	
	/**
	 * 
	 * @return name of the series
	 */
	public String getName(); 
	
	/**
	 * 
	 * @return number of elements in the series
	 */
	public int size();
	
	/**
	 * Quantity of dimensions in the series, i.e. the number of axis in the series
	 * A X-Y series will have dimension 2 as a Histogram series , but a X-Y-Z series will have 
	 * dimension 3. 
	 * The series must have at least two axis and the axis of index 0 is considered the main
	 * abscissa  
	 * @return quantity of axis in the series
	 */
	public int getDimensions();

	
	/**
	 * Obtain a value  for a specified axis
	 * @param <C> values must be comparable
	 * @param dimension 0 to getDimensions()-1
	 * @param index 0 to size()-1
	 * @return the value associated to an index an axis in the series. 
	 */
	public <C extends Comparable<C>> C getValue(int dimension, int index);
	
	/**
	 * Add a series listener that will be informed when the data in the series changes
	 * @param listener the series listener to be informed
	 */
	public void addSeriesListener(SeriesListener listener);
	
	/**
	 * Remove a series listener 
	 * @param listener the series listener to remove
	 */
	public void removeSeriesListener(SeriesListener listener);
}
