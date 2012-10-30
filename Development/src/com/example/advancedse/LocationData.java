package com.example.advancedse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import android.location.Location;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

public class LocationData {
	private double longitude;
	private double latitude;
	private Date timestamp;
	private String uuid;
	
	private Location builtFrom;
	
	private static String domian;
	private static Properties properties;
	private static AmazonSimpleDB sdb;
	
	private static AmazonSimpleDB getDB(){
		if (sdb == null){
			BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIHPTF54IFNDMMBIA", "TRbcsPtlHf31W8EF7IqrsByu7MahneSAz724GWd9");//getProperty("accessKey"), getProperty("secretKey"));
			sdb = new AmazonSimpleDBClient(creds);
		}
		return sdb;
	}

	private static String getProperty(String propName) {
		if(properties == null){
			properties = new Properties();
			try {
				properties.load(LocationData.class.getResourceAsStream("/com.example.advancedse.MainActivity/AwsCredentials.properties"));
			} catch (IOException e) {e.printStackTrace();}
		}
		return properties.getProperty(propName);
	}
	
	public static void save(Location location, String uuid){//String uuid, double lon, double lat, Date time){
		AmazonSimpleDB db = LocationData.getDB();
		CreateDomainRequest cdr = new CreateDomainRequest(uuid);
		db.createDomain(cdr);
		String datestring = new Date().toString();
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		attributes.add(new ReplaceableAttribute().withName("longitude").withValue(location.getLongitude()+""));
		attributes.add(new ReplaceableAttribute().withName("latitude").withValue(location.getLatitude()+""));
		attributes.add(new ReplaceableAttribute().withName("timestamp").withValue(datestring));
		
		sdb.putAttributes(new PutAttributesRequest(uuid, datestring, attributes));
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Location getBuiltFrom() {
		return builtFrom;
	}

	public void setBuiltFrom(Location builtFrom) {
		this.builtFrom = builtFrom;
	}
}
