package com.devspacenine.fastfood;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.location.Address;
import android.location.Location;

public class RestaurantManager {
	// Parent activity information
	private Context mContext;
	
	// Active location information
	private Location mLocation;
	private Address mAddress;
	
	// ListView variables
	private ArrayList<Restaurant> mRestaurantList;
	private ArrayList<Restaurant> mFilteredRestaurantList;
	private Restaurant mSelectedRestaurant;
	private String[] mCuisineFilters;
	
	// Constructors
	public RestaurantManager(Context context, Location location, Address address, String[] cuisineFilters) {
		mContext = context;
		mLocation = location;
		mAddress = address;
		mCuisineFilters = cuisineFilters;
		mRestaurantList = new ArrayList<Restaurant>();
		mFilteredRestaurantList = new ArrayList<Restaurant>();
	}
	
	/**
	 * A temporary test function that initializes some psuedo restaurants
	 */
	public synchronized void loadRestaurants() {
		// Temporary test restaurants
		mRestaurantList.add(new Restaurant(1, "Papa John's", "Lorem ipsum dolor sit amet," +
				" consectetur adipiscing elit. In leo quam, convallis et pretium vitae, volutpat nec" +
				" nisi. Phasellus nec diam sapien.", new String[] {"Pizza", "Italian", "Pasta", "Vegetarian"},
				4.0f, 2.5f, 0.0f, 15.0f, true, true, true));
		mRestaurantList.add(new Restaurant(2, "Gumbys", "Lorem ipsum dolor sit amet," +
				" consectetur adipiscing elit. In leo quam, convallis et pretium vitae, volutpat nec" +
				" nisi. Phasellus nec diam sapien.", new String[] {"Pizza", "Italian", "Pasta", "Vegetarian"},
				4.5f, 5.0f, 0.0f, 15.0f, true, true, false));
		mRestaurantList.add(new Restaurant(3, "Potato Shack", "Lorem ipsum dolor sit amet," +
				" consectetur adipiscing elit. In leo quam, convallis et pretium vitae, volutpat nec" +
				" nisi. Phasellus nec diam sapien.", new String[] {"American", "Italian", "BBQ", "Vegetarian",
				"Chicken", "Steak"}, 3.5f, 4.5f, 2.5f, 10.0f, true, true, true));
		mRestaurantList.add(new Restaurant(4, "Burger Boy", "Lorem ipsum dolor sit amet," +
				" consectetur adipiscing elit. In leo quam, convallis et pretium vitae, volutpat nec" +
				" nisi. Phasellus nec diam sapien.", new String[] {"America", "BBQ", "Hamburgers", "Sandwiches"},
				4.0f, 3.0f, 2.0f, 12.0f, true, true, true));
		mRestaurantList.add(new Restaurant(5, "Chao's Chinese", "Lorem ipsum dolor sit amet," +
				" consectetur adipiscing elit. In leo quam, convallis et pretium vitae, volutpat nec" +
				" nisi. Phasellus nec diam sapien.", new String[] {"Asian", "Chinese", "Vietnamese", "Japanese",
				"Korean", "Indian", "Soup"}, 3.5f, 4.5f, 2.5f, 15.0f, true, true, true));
		mRestaurantList.add(new Restaurant(6, "The Pita Pit", "Lorem ipsum dolor sit amet," +
				" consectetur adipiscing elit. In leo quam, convallis et pretium vitae, volutpat nec" +
				" nisi. Phasellus nec diam sapien.", new String[] {"Greek", "Deli", "Healthy", "Mediterranean",
				"Vegetarian", "Wraps"}, 4.0f, 2.5f, 3.0f, 12.0f, true, true, true));
	}
	
	public synchronized void updateRestaurantList() {
		// Remove restaurants that don't match the filter
		ArrayList<Restaurant> list_copy = (ArrayList<Restaurant>) mRestaurantList.clone();
		for(Iterator<Restaurant> itr = list_copy.iterator(); itr.hasNext();) {
			Restaurant r = (Restaurant) itr.next();
			if(!r.hasTagIn(mCuisineFilters)) {
				mRestaurantList.remove(r);
				mFilteredRestaurantList.add(r);
			}
		}
		// Add previously filtered restaurants that do match the filter
		ArrayList<Restaurant> filtered_copy = (ArrayList<Restaurant>) mFilteredRestaurantList.clone();
		for(Iterator<Restaurant> itr = filtered_copy.iterator(); itr.hasNext();) {
			Restaurant r = (Restaurant) itr.next();
			if(r.hasTagIn(mCuisineFilters)) {
				mRestaurantList.add(r);
				mFilteredRestaurantList.remove(r);
			}
		}
	}
	
	public synchronized ArrayList<Restaurant> getRestaurantList() {
			return mRestaurantList;
	}
}