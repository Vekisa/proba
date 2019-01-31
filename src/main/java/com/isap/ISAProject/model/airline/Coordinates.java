package com.isap.ISAProject.model.airline;

public class Coordinates {

	private Double lon;
	private Double lat;
	
	public Double getLon() { return lon; }
	
	public void setLon(double lon) { this.lon = lon; }
	
	public Double getLat() { return lat; }
	
	public void setLat(double lat) { this.lat = lat; }

	public boolean hasValues() { return this.lon != null && this.lat != null;}
	
}
