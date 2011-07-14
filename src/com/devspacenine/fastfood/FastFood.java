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
	
	// Intent Extra Keys
	public static final String CURRENT_CUISINE_CHOICES = "current_choices";
	
	private boolean debug = true;
	
	private Context ctx;
	private TextView cuisineSelector;
	private String[] selectedCuisines;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Save this context in a local variable and initialize the default
        // cuisine filter
        ctx = this;
        selectedCuisines = new String[] {"Any"};
        
        // Set up the Cuisine Selector to start a ListActivity when clicked
        cuisineSelector = (TextView) findViewById(R.id.cuisine);
        cuisineSelector.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// Construct the intent for the ListActivity
        		Intent intent = new Intent(ctx, CuisineListView.class);
        		
        		// Figure out what the current selection is and pass it to the ListActivity
        		// as an array
        		String current_value = (String) cuisineSelector.getText();
        		// If the text value is the default then the current choice is "Any"
        		if(current_value.equals(getString(R.string.default_cuisine))) {
        			selectedCuisines = new String[] {"Any"};
        		}else{
        			// Split the string into an array of choices
        			selectedCuisines = current_value.split(", ");
        		}
        		// Attach the array to the intent and start the activity
        		intent.putExtra(CURRENT_CUISINE_CHOICES, selectedCuisines);
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
    			selectedCuisines = data.getStringArrayExtra(CURRENT_CUISINE_CHOICES);
    			StringBuffer sb = new StringBuffer();
    			for (int i=0; i < selectedCuisines.length; i++) {
    		        if (i != 0) sb.append(", ");
    		  	    sb.append(selectedCuisines[i]);
    		  	}
    			cuisineSelector.setText(sb.toString());
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
}