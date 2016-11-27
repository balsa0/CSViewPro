package com.csviewpro.domain.math;

/**
 * Created by Balsa on 2016. 11. 27..
 */
public class GeoMath {

	public static double distance(double x1, double y1, double x2, double y2){
		return Math.hypot(x1-x2, y1-y2);
	}

}
