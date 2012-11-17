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
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.example.advancedse.exceptions.IncorrectPasswordException;
import com.example.advancedse.exceptions.InvalidEmailException;

public class LoginDB {
	
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
				properties.load(LoginDB.class.getResourceAsStream("/com.example.advancedse.MainActivity/AwsCredentials.properties"));
			} catch (IOException e) {e.printStackTrace();}
		}
		return properties.getProperty(propName);
	}
	
	public static boolean login(String email, String password) throws IncorrectPasswordException, InvalidEmailException{
		AmazonSimpleDB db = LoginDB.getDB();
		GetAttributesResult ar = db.getAttributes(new GetAttributesRequest("users", email));
		List<Attribute> attributesList = ar.getAttributes();
		if(attributesList.size() == 0){
			throw new InvalidEmailException();
		}
		for(Attribute a :attributesList){
			if(a.getName().equals("password")){
				if(a.getValue().equals(password)){ //if the stored password == the input password
					return true;
				}else{
					throw new IncorrectPasswordException();
				}
			}
		}
		return false;
	}
	
	public static boolean register(String email, String password, String displayName){
		AmazonSimpleDB db = LoginDB.getDB();
		GetAttributesResult ar = db.getAttributes(new GetAttributesRequest("users", email));
		List<Attribute> attributesList = ar.getAttributes();
		if(attributesList.size() == 0){//user doesn't already exist
			List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
			attributes.add(new ReplaceableAttribute().withName("password").withValue(password));
			attributes.add(new ReplaceableAttribute().withName("displayName").withValue(displayName));
			
			sdb.putAttributes(new PutAttributesRequest("users", email, attributes));
			return true;
		}
		return false;
	}
}

	
