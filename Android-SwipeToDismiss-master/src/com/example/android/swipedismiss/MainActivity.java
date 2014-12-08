/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.swipedismiss;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {
	//ArrayAdapter<String> mAdapter;
	CustomList mAdapter;
	ListView list;
	Integer[] images = { R.drawable.image1, R.drawable.image2,
			R.drawable.image3, R.drawable.image4, R.drawable.image5,
			R.drawable.image6,

	};
	
	ArrayList<Integer> imageIds=null;

	
	public ArrayList<Integer> addToArrayList(ArrayList<Integer> ids,Integer[] imgIds){
		ArrayList<Integer> result=new ArrayList<Integer>();
		
		for (int i = 0; i < imgIds.length; i++) {
			result.add(imgIds[i]);
		}
		
		return result;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		imageIds=new ArrayList<Integer>();
		imageIds=addToArrayList(imageIds, images);
		
		final CustomList adapter = new CustomList(MainActivity.this,
				R.layout.list_row,R.id.img,imageIds);
		list = (ListView) findViewById(R.id.listview1);

		list.setAdapter(adapter);

		/*
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						"Click ListItem Number " + position, Toast.LENGTH_LONG)
						.show();
				adapter.remove(position);
				adapter.notifyDataSetChanged();
			}
		});*/

		// Set up ListView example
		/*
		 * String[] items = new String[20]; for (int i = 0; i < items.length;
		 * i++) { items[i] = "Item " + (i + 1); }
		 * 
		 * mAdapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, android.R.id.text1, new
		 * ArrayList<String>(Arrays.asList(items)));
		 *  setListAdapter(mAdapter);
			ListView listView = getListView();
		 */

		
		
		
		// Create a ListView-specific touch listener. ListViews are given
		// special treatment because
		// by default they handle touches for their list items... i.e. they're
		// in charge of drawing
		// the pressed state (the list selector), handling list item clicks,
		// etc.
		
		
		
		
		/*
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				list,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							mAdapter.remove(mAdapter.getItem(position));
						}
						mAdapter.notifyDataSetChanged();
					}
				});
		list.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		list.setOnScrollListener(touchListener.makeScrollListener());

		*/
		
		
		
		
		// Set up normal ViewGroup example
		final ViewGroup dismissableContainer = (ViewGroup) findViewById(R.id.dismissable_container);
		for (int i = 0; i <list.getCount(); i++) {
			
			
			//create an imageView object 
			final ImageView dismissableImageView=new ImageView(this);
			
			dismissableImageView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			dismissableImageView.setImageResource(imageIds.get(i));  //set the resource of it
			
			dismissableImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Toast.makeText(MainActivity.this,
							"Clicked " + ((Button) view).getText(),
							Toast.LENGTH_SHORT).show();
				}
			});
			
			// Create a generic swipe-to-dismiss touch listener.
			dismissableImageView.setOnTouchListener(new SwipeDismissTouchListener(
					dismissableImageView, null,
					new SwipeDismissTouchListener.DismissCallbacks() {
						@Override
						public boolean canDismiss(Object token) {
							return true;
						}

						@Override
						public void onDismiss(View view, Object token) {
							dismissableContainer.removeView(dismissableImageView);  //remove the image which was touch in the onDismiss method
						}
					}));
			
			dismissableContainer.addView(dismissableImageView);
			
			
			
			
			
			
			
		
		}
	}

	
	

}
