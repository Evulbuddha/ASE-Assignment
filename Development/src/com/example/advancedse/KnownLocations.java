package com.example.advancedse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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
import com.example.advancedse.exceptions.IncorrectPasswordException;
import com.example.advancedse.exceptions.InvalidEmailException;

public class KnownLocations {
	
//	private static String domian;
	private static Properties properties;
	private static AmazonSimpleDB sdb;
	
	private static Map map;
	
	
	
	private static AmazonSimpleDB getDB(){
		if (sdb == null){
			BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIHPTF54IFNDMMBIA", "TRbcsPtlHf31W8EF7IqrsByu7MahneSAz724GWd9");//getProperty("accessKey"), getProperty("secretKey"));
			sdb = new AmazonSimpleDBClient(creds);
		}
		return sdb;
	}
	
	public static void setMap(Map m){
		map = m;
	}

	private static String getProperty(String propName) {
		if(properties == null){
			properties = new Properties();
			try {
				properties.load(KnownLocations.class.getResourceAsStream("/com.example.advancedse.MainActivity/AwsCredentials.properties"));
			} catch (IOException e) {e.printStackTrace();}
		}
		return properties.getProperty(propName);
	}
	
	public static void newLocation(String name, double lon, double lat){//String uuid, double lon, double lat, Date time){
		AmazonSimpleDB db = KnownLocations.getDB();
		CreateDomainRequest cdr = new CreateDomainRequest("KnownLocations");
		int id = getHighestKnownLocationID(db);
		id++;
		String idString = String.format("%05d", id);//pads it with 5 0s
		db.createDomain(cdr);
		List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
		attributes.add(new ReplaceableAttribute().withName("Location Name").withValue(name));
		attributes.add(new ReplaceableAttribute().withName("id").withValue(idString));
		attributes.add(new ReplaceableAttribute().withName("longitude").withValue(lon+""));
		attributes.add(new ReplaceableAttribute().withName("latitude").withValue(lat+""));
		
		sdb.putAttributes(new PutAttributesRequest("KnownLocations", id+"", attributes));
		map.addPlace(new Place(id, name, lon, lat));
	}
	
	//public static void save(Location location, String name){
	//	newLocation(location, name);
	//}
	
	private static int getHighestKnownLocationID(AmazonSimpleDB sdbClient) {
		sdbClient.setEndpoint("sdb.amazonaws.com");
		String nextToken = null;
		SelectRequest selectRequest = new SelectRequest( "SELECT id FROM KnownLocations WHERE id is not null ORDER BY id DESC LIMIT 1" ).withConsistentRead( true );
		selectRequest.setNextToken( nextToken );        
		SelectResult response = sdbClient.select( selectRequest );
		try{
			nextToken = response.getItems().get(0).getName();//should only be one entry
			return Integer.parseInt(nextToken);
		}catch(IndexOutOfBoundsException iobEX){
			return -1;
		}
	}
 
	public static ArrayList<Place> loadAll(){
		AmazonSimpleDB sdbClient = KnownLocations.getDB();
		sdbClient.setEndpoint("sdb.amazonaws.com");
		String nextToken = null;
		SelectRequest selectRequest = new SelectRequest( "SELECT * FROM KnownLocations" ).withConsistentRead( true );
		selectRequest.setNextToken( nextToken );        
		SelectResult response = sdbClient.select( selectRequest );
		ArrayList<Place> places = new ArrayList<Place>();
		List<Item> locations = response.getItems();
		if(locations.size() > 0){ //some locations exist in the database
			for(Item i : locations){
				String name = null;
				double longitude = 0, latitude = 0;
				int id = Integer.parseInt(i.getName());
				List<Attribute> attributes = i.getAttributes();
				Iterator<Attribute> attIter = attributes.iterator();
				while(attIter.hasNext()){
					Attribute att = attIter.next();
					if(att.getName().equals("Location Name")){
						name = att.getValue();
					}else if(att.getName().equals("longitude")){
						longitude = Double.parseDouble(att.getValue());
					}else if(att.getName().equals("latitude")){
						latitude = Double.parseDouble(att.getValue());
					}
				}
				if(name != null && longitude != 0 && latitude != 0){
					places.add(new Place(id, name, longitude, latitude));
				}
			}
		}
		return places;
	}
}

	
