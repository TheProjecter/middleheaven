package org.middleheaven.global.location;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.middleheaven.quantity.coordinate.GeoCoordinate;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measurables.Distance;
import org.middleheaven.quantity.measure.AngularMeasure;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.unit.SI;


public class CoordinateTest {

	@Test 
	public void testDistance(){

		WorldGeodeticModel model = new WGM84();
		// http://www.movable-type.co.uk/scripts/latlong-vincenty.html

		GeoCoordinate c1 = new GeoCoordinate(
				AngularMeasure.degrees(53),
				AngularMeasure.degrees(2)
		);

		GeoCoordinate c2 = new GeoCoordinate(
				AngularMeasure.degrees(52),
				AngularMeasure.degrees(0)
		);

		DecimalMeasure<Distance> distance = DecimalMeasure.exact(Real.valueOf(175572.473), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));


		c1 = new GeoCoordinate(
				AngularMeasure.degrees(0),
				AngularMeasure.degrees(0)
		);

		c2 = new GeoCoordinate(
				AngularMeasure.degrees(52),
				AngularMeasure.degrees(0)
		);

		distance = DecimalMeasure.exact(Real.valueOf(5763343.550), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));

	

		c1 = new GeoCoordinate(
				AngularMeasure.degrees(53,9,2),
				AngularMeasure.degrees(1,50,40)
		);

		c2 = new GeoCoordinate(
				AngularMeasure.degrees(52,12,19),
				AngularMeasure.degrees(0,8,33)
		);

		distance = DecimalMeasure.exact(Real.valueOf(155927.728), SI.METER); 
		assertEquals(distance, model.distance(c1, c2));

	}
}
