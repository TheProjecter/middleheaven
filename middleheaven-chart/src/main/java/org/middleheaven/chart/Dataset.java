package org.middleheaven.chart;

/**
 * A <code>Dataset</code> is a set of Series and the main 
 * block for building a chart. Charts are based in a single <code>Dataset</code>.
 *
 */
public interface Dataset extends SeriesListener, Iterable<Series>{
	
	/**
	 * 
	 * @return number of series in the data set
	 */
	public int size();
	
	/**
	 * Obtain a series from the data set
	 * @param index from 0 to size()-1
	 * @return a series of data
	 */
	public Series getSerie(int index);
	
	/**
	 * Add a series listener that will be informed when the data in the series changes
	 * @param listener the series listener to be informed
	 */
	public void addDatasetListener(DatasetListener listener);
	
	/**
	 * Remove a series listener 
	 * @param listener the series listener to remove
	 */
	public void removeDatasetListener(DatasetListener listener);
}
