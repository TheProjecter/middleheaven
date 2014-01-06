package org.middleheaven.quantity.time;

import java.io.Serializable;
import java.util.Comparator;

class TimePointComparator implements Comparator<TimePoint> , Serializable{


	private static final long serialVersionUID = -2612209648880313093L;

	@Override
	public int compare(TimePoint a, TimePoint b) {
		return a.compareTo(b);
	}

}
