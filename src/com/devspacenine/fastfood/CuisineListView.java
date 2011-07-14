package com.devspacenine.fastfood;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

public class CuisineListView extends ListActivity {
	private ArrayList<String> selectedCuisines;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cuisine_list);
		
		// Instantiate the selectedCuisines ArrayList
		selectedCuisines = new ArrayList<String>();
		
		// Get the list of cuisine options and attach them to a ListAdapter
		String[] cuisines = getResources().getStringArray(R.array.cuisine_options);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.cuisine_list_item, cuisines));
		
		// Get the ListView, make it filterable, then set the OnItemClickListener
		ListView lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			// Called every time a list item is clicked
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
}