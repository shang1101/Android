package com.example.frozenlistviews;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	ArrayList<Animal> animals;
	ListView listView, listview2;
	View clickSource;
	View touchSource;

	int offset = 0;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.engNameList);
		listview2 = (ListView) findViewById(R.id.listview2);

		// create some animals

		animals = new ArrayList<Animal>();

		Animal a = new Animal("kedi", 5, "deneme", "deneme", "deneme", "deneme");
		Animal b = new Animal("köpek", 6, "deneme", "deneme", "deneme",
				"deneme");
		Animal c = new Animal("goril", 7, "deneme", "deneme", "deneme",
				"deneme");
		Animal d = new Animal("ördek", 8, "deneme", "deneme", "deneme",
				"deneme");

		Animal e = new Animal("kedi", 5, "deneme", "deneme", "deneme", "deneme");
		Animal f = new Animal("köpek", 6, "deneme", "deneme", "deneme",
				"deneme");
		Animal g = new Animal("goril", 7, "deneme", "deneme", "deneme",
				"deneme");
		Animal h = new Animal("ördek", 8, "deneme", "deneme", "deneme",
				"deneme");

		Animal j = new Animal("tavşan", 5, "deneme", "deneme", "deneme", "deneme");
		Animal k = new Animal("maymun", 6, "deneme", "deneme", "deneme",
				"deneme");

		//
		animals.add(a);
		animals.add(e);
		animals.add(f);
		animals.add(g);
		animals.add(b);
		animals.add(h);
		animals.add(j);
		animals.add(k);
		animals.add(c);
		animals.add(d);

		// I created a simple custom adapter to use ViewHolder

		AnimalAdapterHolder adapter1 = new AnimalAdapterHolder(
				MainActivity.this, R.layout.row, animals, 2);
		AnimalAdapterHolder adapter2 = new AnimalAdapterHolder(
				MainActivity.this, R.layout.row, animals, 0);

		listView.setAdapter(adapter1);
		listview2.setAdapter(adapter2);

		/*
		 * ArrayAdapter<String> adapter; adapter = new
		 * ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
		 * fiilliste);
		 * 
		 * 
		 * 
		 * listView.setAdapter(adapter);
		 */

		listView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (touchSource == null)
					touchSource = v;

				if (v == touchSource) {
					listview2.dispatchTouchEvent(event);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						clickSource = v;
						touchSource = null;
					}
				}

				return false;
			}
		});

		listview2.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (touchSource == null)
					touchSource = v;

				if (v == touchSource) {
					listView.dispatchTouchEvent(event);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						clickSource = v;
						touchSource = null;
					}
				}

				return false;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (parent == clickSource) {
					view.setBackgroundColor(Color.GRAY);

				}
			}
		});

		listview2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (parent == clickSource) {
					view.setBackgroundColor(Color.GRAY);
				}
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (view == clickSource)
					listview2.setSelectionFromTop(firstVisibleItem, view
							.getChildAt(0).getTop() + offset);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});

		listview2.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (view == clickSource)
					listView.setSelectionFromTop(firstVisibleItem, view
							.getChildAt(0).getTop() + offset);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// Set listView's x, y coordinates in loc[0], loc[1]
				int[] loc = new int[2];
				listView.getLocationInWindow(loc);

				// Save listView's y and get listView2's coordinates
				int firstY = loc[1];
				listview2.getLocationInWindow(loc);

				offset = firstY - loc[1];
				// Log.v("Example", "offset: " + offset + " = " + firstY + " + "
				// + loc[1]);
			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();

		handler.sendEmptyMessageDelayed(0, 500);

	}
}
