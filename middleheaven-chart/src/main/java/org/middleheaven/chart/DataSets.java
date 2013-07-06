package org.middleheaven.chart;

import java.util.Iterator;

import org.middleheaven.collections.CollectionUtils;

public class DataSets {

	public static Dataset singleSerie(Series data) {
		return new SingleDataset(data);
	}

	
	private static class SingleDataset extends AbstractDataset{

		Series data;
		
		public SingleDataset(Series data) {
			super();
			this.data = data;
			this.data.addSeriesListener(this);
		}

		
		@Override
		public Series getSerie(int index) {
			return data;
		}

		
		@Override
		public int size() {
			return 1;
		}


		@Override
		public Iterator<Series> iterator() {
			return CollectionUtils.singleIterator(data);
		}

	
	}
}
