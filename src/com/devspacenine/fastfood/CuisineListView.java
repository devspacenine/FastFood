package com.devspacenine.fastfood;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

public class CuisineListView extends ListActivity {
	private ArrayList<String> selectedCuisines;
	private String[] previouslySelectedCuisines;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cuisine_list);
		
		// Instantiate the selectedCuisines ArrayList
		selectedCuisines = new ArrayList<String>();
		
		// Get the list of cuisine options, attach them to a ListAdapter and set it as
		// this view's ListAdapter
		String[] cuisine_list = getResources().getStringArray(R.array.cuisine_options);
		ArrayAdapter<String> cuisines = new ArrayAdapter<String>(this,
				R.layout.cuisine_list_item, cuisine_list);
		setListAdapter(cuisines);
		
		// Get the ListView, make it filterable and set the choiceMode to Multiple
		ListView lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setTextFilterEnabled(true);
		
		// Check if any list items have already been selected, save them in a local list
		// and set them to checked in the local ListAdapter
		previouslySelectedCuisines = getIntent().getStringArrayExtra(
				FastFood.CURRENT_CUISINE_CHOICES);
		CheckedTextView tmp = new CheckedTextView(this);
		for(String option : previouslySelectedCuisines) {
			cuisines.getView(cuisines.getPosition(option), tmp, lv);
			tmp.setChecked(true);
		}
		
		// Set the OnItemClickListener for each list item
		lv.setOnItemClickListener(new OnItemClickListener() {
			// Called every time a list item is clicked
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// Get the RelativeLayout and toggle its selection
				CheckedTextView selection = (CheckedTextView) view;
				Log.d("DSN Debug", selection.toString());
				if(selection.isChecked()) {
					selectedCuisines.remove((String) selection.getText());
					selection.setChecked(false);
				}else{
					selectedCuisines.add((String) selection.getText());
					selection.setChecked(true);
				}
			}
		});
	}
	
	/**
	 * This is called when the user pushes the "Cancel" button. The activity will
	 * finish and the cuisine filter will remain in the same state it was when this
	 * activity was started.
	 * 
	 * @param view - The button that called this onClick method
	 */
	public void cancelFilter(View view) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * This is called when the user pushes the "Set Filter" button. The activity will
	 * finish and the cuisine filter will be updated and returned to the parent
	 * activity.
	 * 
	 * @param view - The button that called this onClick method
	 */
	public void saveFilter(View view) {
		Intent data = new Intent();
		// Convert the selectedCuisines ArrayList to an array and sort it alphabetically
		String[] selected_cuisines_array = (String[]) selectedCuisines.toArray();
		Arrays.sort(selected_cuisines_array);
		data.putExtra(FastFood.CURRENT_CUISINE_CHOICES, selected_cuisines_array);
		setResult(RESULT_OK, data);
		finish();
	}
}