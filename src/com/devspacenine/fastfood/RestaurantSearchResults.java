package com.devspacenine.fastfood;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantSearchResults extends ListActivity {
	// Context variables
	private Context ctx;
	
	// Views that will be manipulated
	private ListView filterList;
	
	// Restaurant manager variables
	private RestaurantManager mRestaurantManager;
	
	// Custom ListAdapter for restaurant search results
	private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		private ArrayList<Restaurant> items;
		
		public RestaurantAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.restaurant_list_item, null);
			}
			Restaurant r = items.get(position);
			if(r != null) {
				// Set the rating
				TextView rating = (TextView) v.findViewById(R.id.rating);
				rating.setText(String.format("%.1f/5.0", r.getRating()));
				// Set the distance
				TextView distance = (TextView) v.findViewById(R.id.distance);
				distance.setText(String.format("%.1f miles away", r.getDistance()));
				// Set the restaurant name
				TextView name = (TextView) v.findViewById(R.id.name);
				name.setText(r.getName());
				// Set the tags
				TextView tags = (TextView) v.findViewById(R.id.tags);
				tags.setText(r.getTagsString());
				// Set the minimum delivery amount
				TextView minimum = (TextView) v.findViewById(R.id.minimum);
				minimum.setText(String.format("Minimum $%.2f", r.getDeliveryMinimum()));
				// Set the delivery fee
				TextView delivery_fee = (TextView) v.findViewById(R.id.delivery_fee);
				delivery_fee.setText(String.format("Delivery Fee $%.2f", r.getDeliveryFee()));
				// Set the status images
				ImageView phone_open = (ImageView) v.findViewById(R.id.phone_open);
				if(r.isPhoneOpen()) phone_open.setBackgroundResource(R.color.button_pressed_dark);
				ImageView delivery_open = (ImageView) v.findViewById(R.id.delivery_open);
				if(r.isDeliveryOpen()) delivery_open.setBackgroundResource(R.color.button_pressed_dark);
				ImageView open = (ImageView) v.findViewById(R.id.open);
				if(r.isOpen()) open.setBackgroundResource(R.color.button_pressed_dark);
			}
			return v;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}
	
	// Custom ListAdapter for cuisine filters
	private class CuisineFilterAdapter extends ArrayAdapter<String> {
		private String[] items;
		
		public CuisineFilterAdapter(Context context, int textViewResourceId, String[] items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.filter_list_item, null);
			}
			String cuisine = items[position];
			if(cuisine != null) {
				TextView filter_text = (TextView) v.findViewById(R.id.cuisine_filter);
				filter_text.setText(cuisine);
			}
			return v;
		}
	}
	
	// ListAdapter variables
	private RestaurantAdapter mRestaurantAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_list);
		
		// Save a reference to this activity context
		ctx = this;
		filterList = (ListView) findViewById(R.id.filter_list);
		
		mRestaurantManager = new RestaurantManager(this,
				(Location) getIntent().getParcelableExtra(FastFood.EXTRA_LOCATION),
				(Address) getIntent().getParcelableExtra(FastFood.EXTRA_ADDRESS),
				getIntent().getStringArrayExtra(FastFood.EXTRA_CUISINE_FILTERS));
		mRestaurantManager.loadRestaurants();
		mRestaurantManager.updateRestaurantList();
		
		mRestaurantAdapter = new RestaurantAdapter(this, R.layout.restaurant_list_item,
				mRestaurantManager.getRestaurantList());
		setListAdapter(mRestaurantAdapter);
		filterList.setAdapter(new CuisineFilterAdapter(this, R.layout.filter_list_item,
				getIntent().getStringArrayExtra(FastFood.EXTRA_CUISINE_FILTERS)));
	}
	
	/**
     * Called when the Sign In button is clicked. Starts the AccountPortal activity.
     */
    public void signIn(View view) {
    	Intent intent = new Intent(ctx, AccountPortal.class);
    	startActivity(intent);
    }
}