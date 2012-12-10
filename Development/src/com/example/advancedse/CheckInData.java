package com.example.advancedse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import android.location.Location;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

public class CheckInData {
	
//	private static String domian;
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
				properties.load(CheckInData.class.getResourceAsStream("/com.example.advancedse.MainActivity/AwsCredentials.properties"));
			} catch (IOException e) {e.printStackTrace();}
		}
		return properties.getProperty(propName);
	}
	
	public static void save(String locationId, String email, String review){//String uuid, double lon, double lat, Date time){
		email = Util.encodeEmail(email);
		AmazonSimpleDB db = CheckInData.getDB();
		CreateDomainRequest cdr = new CreateDomainRequest("CheckIn");
		db.createDomain(cdr);
		String datestring= new Date().getTime() +"";
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		attributes.add(new ReplaceableAttribute().withName("locationId").withValue(locationId));
		attributes.add(new ReplaceableAttribute().withName("email").withValue(email));
		attributes.add(new ReplaceableAttribute().withName("timestamp").withValue(datestring));
		attributes.add(new ReplaceableAttribute().withName("review").withValue(review));
		
		sdb.putAttributes(new PutAttributesRequest("CheckIn", datestring, attributes));
	}
	
	public static void save(String locationId, String email){
		save(locationId, email, "");
	}
	
	public static ArrayList<User> load(String locationId){

		AmazonSimpleDB sdbClient = CheckInData.getDB();
		sdbClient.setEndpoint("sdb.amazonaws.com");
		String nextToken = null;
		SelectRequest selectRequest = new SelectRequest( "SELECT * FROM CheckIn WHERE locationId='"+locationId+"'" ).withConsistentRead( true );
		selectRequest.setNextToken( nextToken );        
		SelectResult response = sdbClient.select( selectRequest );
		ArrayList<User> checkIn = new ArrayList<User>();
		List<Item> users = response.getItems();
		if(users.size() > 0){ //some locations exist in the database
			for(Item i : users){
				String name = null;
				String email = null;
				String review = null;
				String datetime = null;
				List<Attribute> attributes = i.getAttributes();
				Iterator<Attribute> attIter = attributes.iterator();
				while(attIter.hasNext()){
					Attribute att = attIter.next();
					if(att.getName().equals("email")){
						name = att.getValue();
					}else if(att.getName().equals("name")){
						email = att.getValue();
					}else if(att.getName().equals("review")){
						review = att.getValue();
					}else if(att.getName().equals("datetime")){
						datetime = att.getValue();
					}
				}
				if(name != null && email != null && review != null && datetime != null){
					checkIn.add(new User(email, name, review, datetime));
				}
			}
		}
		return checkIn;
	}
}

	
