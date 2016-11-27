package com.csviewpro.domain.math;

/**
 * Created by Balsa on 2016. 11. 27..
 */
public class GeoMath {

	public static double distance(double x1, double y1, double x2, double y2){
		return Math.hypot(x1-x2, y1-y2);
	}

	/**
	 * Heading angle (irányszög)
	 * @param dx x coordinat diff.
	 * @param dy y coordinat diff.
	 * @return
	 */
	public static double headingAngle(double dx, double dy){

		// delta δ
		double delta = Math.toDegrees(Math.atan(Math.abs(dy)/Math.abs(dx)));

		// probably division by zero
		if(((Double)delta).isNaN())
			delta = 0;

		// check quarter
		double heading;
		if(dy >= 0 && dx >= 0){
			// 1st quarter
			heading = delta;
		}else if(dy >= 0 && dx < 0){
			// 2nd quarter
			heading = 180.0 - delta;
		}else if(dy < 0 && dx < 0){
			// 3rd quarter
			heading = 180.0 + delta;
		}else{
			// 4th quarter
			heading = 360.0 - delta;
		}

		return heading;
	}

	public static String formatDegreeToDMS(double degrees){
		int degree = (int) degrees;
		int minute = (int) (Math.abs(degrees) * 60) % 60;
		int second = (int) (Math.abs(degrees) * 3600) % 60;
		return "" + degree + "°" + minute + "'" + second + "\"";
	}

}
