package com.devspacenine.fastfood;

import android.location.Address;
import android.location.Location;

public class Restaurant {
	private int mmId;
	private String mmName;
	private String mmDescription;
	private String[] mmTags;
	private float mmRating;
	private float mmDistance;
	private float mmDeliveryFee;
	private float mmDeliveryMinimum;
	private boolean mmOpen;
	private boolean mmDeliveryOpen;
	private boolean mmPhoneOpen;
	
	// Future variables
	private Location mmLocation;
	private Address mmAddress;
	private String mmPhoneNumber;
	private String mmUrl;
	
	public Restaurant(int id, String name, String description, String[] tags, float rating, float distance,
			float fee, float minimum, boolean open, boolean openDelivery, boolean openPhone) {
		mmId = id;
		mmName = name;
		mmDescription = description;
		mmTags = tags;
		mmRating = rating;
		mmDistance = distance;
		mmDeliveryFee = fee;
		mmDeliveryMinimum = minimum;
		mmOpen = open;
		mmDeliveryOpen = openDelivery;
		mmPhoneOpen = openPhone;
	}
	
	public int getId() {
		return mmId;
	}
	
	public String getName() {
		return mmName;
	}
	
	public String getDescription() {
		return mmDescription;
	}
	
	public String[] getTags() {
		return mmTags;
	}
	
	public String getTagsString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i < mmTags.length; i++) {
			if (i != 0) sb.append(", ");
			sb.append(mmTags[i]);
		}
		return sb.toString();
	}
	
	public String getTag(int index) {
		return mmTags[index];
	}
	
	public boolean hasTag(String tag) {
		for(int i=0;i < mmTags.length; i++)
			if(mmTags[i].equals(tag)) return true;
		return false;
	}
	
	public boolean hasTagIn(String[] tags) {
		if(tags.length==0) return true;
		for(int i=0;i < tags.length; i++)
			for(int j=0;j < mmTags.length; j++)
				if(tags[i].equals(mmTags[j])) return true;
		return false;
	}
	
	public String getPhoneNumber() {
		return mmPhoneNumber;
	}
	
	public String getUrl() {
		return mmUrl;
	}
	
	public float getRating() {
		return mmRating;
	}
	
	public Location getLocation() {
		return mmLocation;
	}
	
	public Address getAddress() {
		return mmAddress;
	}
	
	public float getDistance() {
		return mmDistance;
	}
	
	public float getDeliveryFee() {
		return mmDeliveryFee;
	}
	
	public float getDeliveryMinimum() {
		return mmDeliveryMinimum;
	}
	
	public boolean isOpen() {
		return mmOpen;
	}
	
	public boolean isDeliveryOpen() {
		return mmDeliveryOpen;
	}
	
	public boolean isPhoneOpen() {
		return mmPhoneOpen;
	}
};