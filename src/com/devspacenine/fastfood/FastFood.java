package com.devspacenine.fastfood;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FastFood extends Activity implements LocationListener {
	// Intent Request Codes
	public static final int REQUEST_CUISINE_FILTERS = 1;
	
	// Intent Extra Keys
	public static final String CURRENT_CUISINE_CHOICES = "current_choices";
	public static final String EXTRA_LOCATION = "location";
	public static final String EXTRA_ADDRESS = "address";
	public static final String EXTRA_CUISINE_FILTERS = "selected_cuisines";
	
	// Dialog ID's
	public static final int ENABLE_GPS_DIALOG = 1;
	public static final int PICK_ADDRESS_DIALOG = 2;
	public static final int SET_ADDRESS_DIALOG = 3;
	
	// Debug boolean
	private boolean debug = true;
	
	//Context variables
	private Context ctx;
	
	// Views that will be manipulated
	private TextView mAddressText;
	private TextView mCuisineSelector;
	private RelativeLayout mGPSButton;
	
	// Location variables
	private LocationManager mLocationManager;
	private Location mLocation;
	private List<Address> mAddressList;
	private Address mAddress;
	
	// Utility variables
	private String[] mSelectedCuisines;
	
	// Dialog variables
	private ProgressDialog mGPSDialog;
	
	// Event booleans
	private boolean waiting_for_gps;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Save this context in a local variable and initialize the default
        // cuisine filter
        ctx = this;
        mSelectedCuisines = new String[] {};
        
        // Get the views that will be manipulated
        mCuisineSelector = (TextView) findViewById(R.id.cuisine_input);
        mAddressText = (TextView) findViewById(R.id.address_text);
        
        // Get the device's default location manager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        // Set up the GPS button to search for current address when pressed
        mGPSButton = (RelativeLayout) findViewById(R.id.gps_button);
        mGPSButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// Make sure the GPS service is enabled
        		if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	        		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) ctx);
	        		waiting_for_gps = true;
	        		// Show a progress dialog for the location search
	        		mGPSDialog = ProgressDialog.show(ctx, "", getString(R.string.waiting_for_gps), true, true);
	        		
	        		// Set up the dialog's onDismiss listener
	        		mGPSDialog.setOnDismissListener(new OnDismissListener() {
	        			public void onDismiss(DialogInterface dialog) {
	        				if(waiting_for_gps) {
		        				// Check if any addresses were found
		        				if(mAddressList.isEmpty()) {
		        					// None were found, show a failure Toast
		        					Toast.makeText(ctx, "Could not get an address for your location", Toast.LENGTH_SHORT).show();
		        				}else if(mAddressList.size() == 1) {
		        					// Only found one, update the address input with its value
		        					mAddress = mAddressList.get(0);
		        					StringBuffer sb = new StringBuffer();
		        					for(int i=0; i < mAddress.getMaxAddressLineIndex(); i++)
		        						sb.append(mAddress.getAddressLine(i)).append(" ");
		        					mAddressText.setText(sb.toString());
		        				}else{
		        					// Found more than one address, ask the user which to use
		        					showDialog(PICK_ADDRESS_DIALOG);
		        				}
	        				}
	        			}
	        		});
	        		
	        		// Set up the dialog's onCancel listener
	        		mGPSDialog.setOnCancelListener(new OnCancelListener() {
	        			public void onCancel(DialogInterface dialog) {
	        				mLocationManager.removeUpdates((LocationListener) ctx);
	        				waiting_for_gps = false;
	        			}
	        		});
        		}else{
        			// GPS is disabled, show a dialog requesting to enable it
        			showDialog(ENABLE_GPS_DIALOG);
        		}
        	}
        });
        
        // Set up the Cuisine Selector to start a ListActivity when clicked
        mCuisineSelector.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// Construct the intent for the ListActivity
        		Intent intent = new Intent(ctx, CuisineList.class);
        		
        		// Figure out what the current selection is and pass it to the ListActivity
        		// as an array
        		String current_value = (String) mCuisineSelector.getText();
        		// If the text value is the default then the current choice is "Any"
        		if(current_value.equals("")) {
        			mSelectedCuisines = new String[] {};
        		}else{
        			// Split the string into an array of choices
        			mSelectedCuisines = current_value.split(", ");
        		}
        		// Attach the array to the intent and start the activity
        		intent.putExtra(CURRENT_CUISINE_CHOICES, mSelectedCuisines);
        		startActivityForResult(intent, REQUEST_CUISINE_FILTERS);
        	}
        });
    }
    
    /**
     * Called when an Activity returns with a result
     * 
     * @requestCode - The code that was used to start the activity
     * @resultCode - The code that the activity returned
     * @data - An Intent object with any returned data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    	case REQUEST_CUISINE_FILTERS:
    		switch(resultCode) {
    		case RESULT_OK:
    			// The user saved their cuisine filters successfully. Update the global
    			// variable and the form value
    			mSelectedCuisines = data.getStringArrayExtra(CURRENT_CUISINE_CHOICES);
    			if(debug) Log.d("DSN", mSelectedCuisines.toString());
    			StringBuffer sb = new StringBuffer();
    			for (int i=0; i < mSelectedCuisines.length; i++) {
    		        if (i != 0) sb.append(", ");
    		  	    sb.append(mSelectedCuisines[i]);
    		  	}
    			if(mSelectedCuisines.length == 0) {
    				mCuisineSelector.setText("");
    			}else{
    				mCuisineSelector.setText(sb.toString());
    			}
    			break;
    		case RESULT_CANCELED:
    			// The user hit either the cancel button or the back button. Don't make
    			// any changes to the cuisine filter
    			break;
    		default:
    			break;
    		}
    		break;
    	default:
    		break;
    	}
    }
    
    /**
     * Called when the Sign In button is clicked. Starts the AccountPortal activity.
     */
    public void signIn(View view) {
    	Intent intent = new Intent(ctx, AccountPortal.class);
    	startActivity(intent);
    }
    
    public void search(View view) {
    	if(mAddressText.getText().equals("")) {
    		showDialog(SET_ADDRESS_DIALOG);
    	}else{
    		Intent intent = new Intent(ctx, RestaurantSearchResults.class);
    		intent.putExtra(EXTRA_ADDRESS, mAddress);
    		intent.putExtra(EXTRA_LOCATION, mLocation);
    		intent.putExtra(EXTRA_CUISINE_FILTERS, mSelectedCuisines);
    		startActivity(intent);
    	}
    }
    
    /**
     * Determines which dialog to construct when showDialog is called in this activity
     * 
     * @param id - The ID sent to showDialog
     */
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog;
    	switch(id) {
    	case ENABLE_GPS_DIALOG:
    		// Set the dialog's message, positive button, and negative button
    		AlertDialog.Builder enable_gps_builder = new AlertDialog.Builder(this);
    		enable_gps_builder.setMessage("GPS is disabled. Would you like to enable it?");
    		enable_gps_builder.setCancelable(false);
    		enable_gps_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int item) {
    				// The player chose to enable GPS
    				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    				startActivityForResult(intent, 0);
    				dialog.dismiss();
    			}
    		});
    		enable_gps_builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// The player chose not to enable GPS
					dialog.cancel();
    			}
    		});
    		dialog = enable_gps_builder.create();
    		break;
    	case PICK_ADDRESS_DIALOG:
    		// Build an array from the list of addresses
    		String[] tmp_choices = new String[mAddressList.size()];
    		StringBuffer sb;
    		for(int i=0;i < mAddressList.size(); ++i) {
    			sb = new StringBuffer();
    			for(int j=0;j < mAddressList.get(i).getMaxAddressLineIndex(); j++)
    				sb.append(mAddressList.get(i).getAddressLine(j)).append(" ");
    			tmp_choices[i] = sb.toString();
    		}
    		final String[] address_choices = tmp_choices;
    		
    		// Set the dialog's title, choices, and click listener
    		AlertDialog.Builder pick_address_builder = new AlertDialog.Builder(this);
    		pick_address_builder.setTitle("Choose an Address");
    		pick_address_builder.setItems(address_choices, new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int item) {
    				mAddressText.setText(address_choices[item]);
    				dialog.dismiss();
    			}
    		});
    		dialog = pick_address_builder.create();
    		break;
    	case SET_ADDRESS_DIALOG:
    		// Set the dialog's message and ok button
    		AlertDialog.Builder set_address_builder = new AlertDialog.Builder(this);
    		set_address_builder.setMessage("You must set your address before you can search for restaurants.");
    		set_address_builder.setCancelable(false);
    		set_address_builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int item) {
    				dialog.dismiss();
    			}
    		});
    		dialog = set_address_builder.create();
    		break;
    	default:
    		dialog = null;
    	}
    	return dialog;
    }
    
    // LocationListener interface methods
    /**
     * Called when the gps location has been updated
     */
    public void onLocationChanged(Location location) {
    	mLocation = location;
    	GetLocationThread get_location = new GetLocationThread(location.getLatitude(), location.getLongitude(), 5);
    	get_location.start();
    	mLocationManager.removeUpdates((LocationListener) ctx);
	}
	
    /**
     * Called when the status of the provider changes
     */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if(status == LocationProvider.OUT_OF_SERVICE) {
			Toast.makeText(ctx, "Could not get an accurate GPS location", Toast.LENGTH_SHORT).show();
			mGPSDialog.cancel();
		}
	}
	
	/**
	 * Called when the provider is enabled
	 */
	public void onProviderEnabled(String provider) {}
	
	/**
	 * Called when the provider is disabled
	 */
	public void onProviderDisabled(String provider) {
		Toast.makeText(ctx, "GPS was just disabled. Canceling address search", Toast.LENGTH_SHORT).show();
		mGPSDialog.cancel();
	}
	
	// A thread to try and search for the device's current location by address
	private class GetLocationThread extends Thread {
		private double mmLatitude;
		private double mmLongitude;
		private int mmMaxResults;
		private List<Address> mmAddressResult;
		
		public GetLocationThread(double latitude, double longitude, int maxResults) {
			mmLatitude = latitude;
			mmLongitude = longitude;
			mmMaxResults = maxResults;
		}
		
		public void run() {
			Geocoder geocoder = new Geocoder(ctx);
			try{
				mmAddressResult = geocoder.getFromLocation(mmLatitude, mmLongitude, mmMaxResults);
				if(waiting_for_gps) {
					setAddress(mmAddressResult);
					mGPSDialog.dismiss();
				}
			}catch(IOException e){
				if(waiting_for_gps) {
					Log.d("DSN Debug", "Could not search for address with Geocoder - " + e.getMessage());
					Toast.makeText(ctx, "Could not get an address because of network errors", Toast.LENGTH_SHORT).show();
					mGPSDialog.cancel();
				}
			}
		}
	}
	
	// Set the address variable synchronously
	private synchronized void setAddress(List<Address> addressList) {
		mAddressList = addressList;
	}
}