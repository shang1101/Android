package com.example.scrollview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ScrollViewListener {

	// more efficient than HashMap for mapping integers to objects
	SparseArray<Group> groups = new SparseArray<Group>();

	ScrollView myScroll;
	ObservableScrollView myScroll2;
	VerticalSeekBar verticalSeekBar;

	ListView listv;
	ExpandableListView exListv;
	MyExpandableListAdapter exAdapter;
	int progressBar = 0;

	Button btnClick;
	Button btnClickBody;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView view = (TextView) findViewById(R.id.TextView02);
		TextView view1 = (TextView) findViewById(R.id.TextView03);

		myScroll = (ScrollView) findViewById(R.id.scrollView1);
		// myScroll2 = (ObservableScrollView) findViewById(R.id.myScroll2);

		// listview and expandible listview

		listv = (ListView) findViewById(R.id.markalarListView);

		exListv = (ExpandableListView) findViewById(R.id.attributesExpandableListView);

		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
				"OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
				"Android", "iPhone", "WindowsMobile" };

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listv.setAdapter(adapter);

		

		btnClick = (Button) findViewById(R.id.btndeneme);
		btnClickBody=(Button) findViewById(R.id.btndeneme2);
		
		
		btnClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				createData(5,5);
				exAdapter = new MyExpandableListAdapter(MainActivity.this, groups);
				exListv.setAdapter(exAdapter);
			}
		});
		
		
		btnClickBody.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				createData(361,15);
				exAdapter = new MyExpandableListAdapter(MainActivity.this, groups);
				exListv.setAdapter(exAdapter);
			}
		});
		
		
		

		// exListv.setAdapter(exAdapter);

		/*
		 * String s = "", d = ""; for (int i = 0; i < 1000; i++) { s +=
		 * "vogella.com "; d += "demba ba"; } view.setText(s); view1.setText(d);
		 */

		// myScroll.setScrollViewListener(this);

		// myScroll2.setScrollViewListener(this);

		// set to vertical side bar

		verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar1);

		verticalSeekBar.setScrollView(myScroll);

		listv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Log.v(TAG,"CHILD TOUCH");
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		exListv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Log.v(TAG,"CHILD TOUCH");

				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					v.getParent().requestDisallowInterceptTouchEvent(true);

					return false;

				case MotionEvent.ACTION_MOVE:

				case MotionEvent.ACTION_UP:

					v.getParent().requestDisallowInterceptTouchEvent(false);

					return false;

				case MotionEvent.ACTION_CANCEL:

					break;

				}

				return true;

			}
		});

	}

	void fixVertical(ObservableScrollView scrollView) {

		int height = scrollView.getMeasuredHeight();
		int totalHeight = scrollView.getChildAt(0).getHeight();

		double oran = (double) (totalHeight - height)
				/ verticalSeekBar.getMax();

		verticalSeekBar
				.setProgress(verticalSeekBar.getMax()
						- (int) (verticalSeekBar.getMax()
								* verticalSeekBar.getY() / verticalSeekBar
									.getHeight()));
		scrollView
				.setScrollY((int) ((verticalSeekBar.getMax() - (int) (verticalSeekBar
						.getMax() * (1 - verticalSeekBar.getY()
						/ verticalSeekBar.getHeight()))) * oran));

		verticalSeekBar.onSizeChanged(verticalSeekBar.getWidth(),
				verticalSeekBar.getHeight(), 0, 0);

	}

	/*
	 * @Override public void onScrollChanged(ObservableScrollView scrollView,
	 * int x, int y, int oldx, int oldy) { if (myScroll == myScroll2) {
	 * 
	 * // myScroll2.scrollTo(x, y); } else if (scrollView == myScroll2) { //
	 * myScroll.scrollTo(x, y); // fixVertical(scrollView); } else if
	 * (scrollView == myScroll) { // myScroll2.scrollTo(x, y); }
	 * 
	 * }
	 */
	private void createData(int k,int l) {
		for (int j = 0; j < k; j++) {
			Group group = new Group("Test " + j);
			for (int i = 0; i < l; i++) {
				group.children.add("Sub Item" + i);
			}
			groups.append(j, group);

		}
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	} // end toast

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		toast("deneme");

	}

}
