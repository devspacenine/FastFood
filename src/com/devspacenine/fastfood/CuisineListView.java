package com.devspacenine.fastfood;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CuisineListView extends ListActivity {
	private ArrayList<String> selectedCuisines;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Instantiate the selectedCuisines ArrayList
		selectedCuisines = new ArrayList<String>();
		
		// Get the list of cuisine options and attach them to a ListAdapter
		String[] cuisines = getResources().getStringArray(R.array.cuisine_options);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.cuisine_list_item, cuisines));
		
		// Get the ListView, make it filterable, then set the OnItemClickListener
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			// Called every time a list item is clicked
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Get the RelativeLayout and toggle its selection
				RelativeLayout selected_layout = (RelativeLayout) view;
				if(selected_layout.isSelected()) {
					selectedCuisines.remove(position);
					selected_layout.setSelected(false);
				}else{
					TextView selected_text = (TextView) selected_layout.getChildAt(0);
					selectedCuisines.add(position, (String) selected_text.getText());
					selected_layout.setSelected(true);
				}
			}
		});
	}
}