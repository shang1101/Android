package com.example.frozenlistviews;

public class Animal {
	String name;
	int age;
	String speed;
	String location;
	String type;
	String hunting;
	
	
	public Animal(String name, int age, String speed, String location,
			String type, String hunting) {
		super();
		this.name = name;
		this.age = age;
		this.speed = speed;
		this.location = location;
		this.type = type;
		this.hunting = hunting;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getSpeed() {
		return speed;
	}


	public void setSpeed(String speed) {
		this.speed = speed;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getHunting() {
		return hunting;
	}


	public void setHunting(String hunting) {
		this.hunting = hunting;
	}
	
	
	
	
}
