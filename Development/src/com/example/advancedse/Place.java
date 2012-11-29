package com.example.advancedse;

public class Place {

	String name;
	double longitude, latidude;
	int id;
	
	public Place(int id, String name, double longitude, double latitude){
		this.name = name;
		this.longitude = longitude;
		this.latidude = latitude;
		this.id = id;
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
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	
}
