package com.example.advancedse;

public class Place {

	String name;
	double longitude, latidude;
	
	public Place(String name, double longitude, double latitude){
		this.name = name;
		this.longitude = longitude;
		this.latidude = latitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatidude() {
		return latidude;
	}

	public void setLatidude(double latidude) {
		this.latidude = latidude;
	}
	
	
}
