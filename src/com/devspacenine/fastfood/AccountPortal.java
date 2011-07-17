package com.devspacenine.fastfood;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class AccountPortal extends Activity {
	// Views that will be manipulated
	private TextView subheader;
	private EditText email_input;
	private EditText password_input;
	private ViewFlipper flipper;
	
	// Animations
	private Animation inFromLeftAnimation;
	private Animation outToRightAnimation;
	private Animation inFromRightAnimation;
	private Animation outToLeftAnimation;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_portal);
		
		// Get the animations from xml
		inFromLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
		outToRightAnimation = AnimationUtils.loadAnimation(this, R.anim.out_to_right);
		inFromRightAnimation = AnimationUtils.loadAnimation(this, R.anim.in_from_right);
		outToLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.out_to_left);
		
		// Get the views that will be manipulated
		subheader = (TextView) findViewById(R.id.subheader);
		email_input = (EditText) findViewById(R.id.email_input);
		password_input = (EditText) findViewById(R.id.password_input);
		flipper = (ViewFlipper) findViewById(R.id.view_flipper);
	}
	
	/**
	 * Called when the user starts to create a new account
	 * 
	 * @param view - The button that was pressed
	 */
	public void newAccount(View view) {
		flipper.setInAnimation(inFromRightAnimation);
		flipper.setOutAnimation(outToLeftAnimation);
		flipper.showNext();
		subheader.setText("Create Account");
	}
	
	/**
	 * Called when the user has submitted the create account form
	 * 
	 * @param view - The button that was pressed
	 */
	public void createAccount(View view) {
		flipper.setInAnimation(inFromLeftAnimation);
		flipper.setOutAnimation(outToRightAnimation);
		flipper.showPrevious();
		subheader.setText("Sign In");
	}
}