package com.example.advancedse;

public class User {

	private String email, name, review, datetime;
	
	public User(String email, String name, String review, String datetime)
	{
		this.email=email;
		this.name=name;
		this.review=review;
		this.datetime=datetime;
	}
	
	String getEmail()
	{
		return email;
	}
	
	String getName()
	{
		return name;
	}
	
	String getReview()
	{
		return review;
	}
	
	String getDatetime()
	{
		return datetime;
	}
	
}
