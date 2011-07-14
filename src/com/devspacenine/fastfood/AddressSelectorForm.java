package com.devspacenine.fastfood;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddressSelectorForm extends LinearLayout {
	
	TextView addressText;
	RelativeLayout gpsButton;
	
	public AddressSelectorForm(Context context) {
		super(context);
		
		// Inflate the view from the layout resource
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.address_selector_form, this, true);
		
		// Get references to the child controls
		addressText = (TextView) findViewById(R.id.address_text);
		gpsButton = (RelativeLayout) findViewById(R.id.gps_button);
	}
	
	public AddressSelectorForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Inflate the view from the layout resource
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater) getContext().getSystemService(infService);
		li.inflate(R.layout.address_selector_form, this, true);
		
		// Get references to the child controls
		addressText = (TextView) findViewById(R.id.address_text);
		gpsButton = (RelativeLayout) findViewById(R.id.gps_button);
	}
}