package com.devspacenine.fastfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FastFood extends Activity {
	// Intent Request Codes
	public static final int REQUEST_CUISINE_FILTERS = 1;
	
	private boolean debug = true;
	
	private Context ctx;
	private TextView cuisineSelector;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ctx = this;
        
        // Set up the Cuisine Selector to start a ListActivity when clicked
        cuisineSelector = (TextView) findViewById(R.id.cuisine);
        cuisineSelector.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// Construct the intent for the ListActivity
        		Intent intent = new Intent(ctx, CuisineListView.class);
        		
        		// Figure out what the current selection is and pass it to the ListActivity
        		// as an array
        		String current_value = (String) cuisineSelector.getText();
        		String[] current_choices;
        		// If the text value is the default then the current choice is "Any"
        		if(current_value.equals(getString(R.string.default_cuisine))) {
        			current_choices = new String[] {"Any"};
        		}else{
        			// Split the string into an array of choices
        			current_choices = current_value.split(", ");
        		}
        		// Attach the array to the intent and start the activity
        		intent.putExtra("current_choices", current_choices);
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
    		break;
    	default:
    		break;
    	}
    }
}