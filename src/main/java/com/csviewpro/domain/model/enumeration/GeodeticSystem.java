package com.csviewpro.domain.model.enumeration;

/**
 * This enumeration contains all supported geodetic systems and holds some meta-data as well.
 */
public enum GeodeticSystem {

	// EOV - Egységes országos vetület (EPSG:23700)
	// http://spatialreference.org/ref/epsg/23700/
	EOV(52597.3726, 422251.0071, 368889.2583, 950242.2793),
	// World Geodetic System 84' (EPSG:4326)
	// http://spatialreference.org/ref/epsg/wgs-84/
	WGS84(-180.0000, -90.0000, 180.0000, 90.0000);

	// bounds
	final double xMin, yMin, xMax, yMax;

	GeodeticSystem(double xMin, double yMin, double xMax, double yMax) {

		this.xMin = xMin;
		this.yMin = yMin;
		this.xMax = xMax;
		this.yMax = yMax;

	}

	public double getxMin() {
		return xMin;
	}

	public double getxMax() {
		return xMax;
	}

	public double getyMin() {
		return yMin;
	}

	public double getyMax() {
		return yMax;
	}

}
