package com.example.advancedse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

public class LocationData {
	private double longitude;
	private double latitude;
	private Date timestamp;
	private String uuid;
	
	private static String domian;
	private static Properties properties;
	private static AmazonSimpleDB sdb;
	
	private static AmazonSimpleDB getDB(){
		if (sdb == null){
			BasicAWSCredentials creds = new BasicAWSCredentials(getProperty("accessKey"), getProperty("secretKey"));
		}
		return sdb;
	}

	private static String getProperty(String propName) {
		if(properties == null){
			properties = new Properties();
			try {
				properties.load(LocationData.class.getResourceAsStream("AwsCredentials.properties"));
			} catch (IOException e) {e.printStackTrace();}
		}
		return properties.getProperty(propName);
	}
	
	public void save(){//String uuid, double lon, double lat, Date time){
		LocationData.getDB().createDomain(new CreateDomainRequest(LocationData.getProperty("domain")));
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		attributes.add(new ReplaceableAttribute().withName("longitude").withValue(longitude+""));
		attributes.add(new ReplaceableAttribute().withName("latitude").withValue(latitude+""));
		attributes.add(new ReplaceableAttribute().withName("timestamp").withValue(timestamp.toString()));
		attributes.add(new ReplaceableAttribute().withName("uuid").withValue("FAKEUUID"));
		
		sdb.putAttributes(new PutAttributesRequest(LocationData.getProperty("domain"), uuid, attributes));
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
}
