package info.androidhive.expandablelistview;

import info.androidhive.expandablelistview.PinnedHeaderExpListView.PinnedHeaderAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ExpandableListAdapter listAdapter, listAdapter2;
	ExpandableListView expListView;
	ExpandableListView expListView2;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	View clickSource;
	View touchSource;

	int offset = 0;
	Handler handler;

	public void toast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	} // end toast

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		expListView2 = (ExpandableListView) findViewById(R.id.list);

		
		
		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, 2);
		listAdapter2 = new ExpandableListAdapter(this, listDataHeader, listDataChild, 5);

		// setting list adapter
		expListView.setAdapter(listAdapter);
		expListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
		expListView2.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

		// set the header here

		View view = View.inflate(this, R.layout.header, null);
		expListView2.addHeaderView(view, null, false);
		expListView2.setAdapter(listAdapter2);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// ***************for second one

		// Listview Group click listener
		expListView2.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

				return false;
			}
		});

		// *****************************

		// // Listview Group expanded listener
		// expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(int groupPosition) {
		// Toast.makeText(getApplicationContext(),
		// listDataHeader.get(groupPosition) + " Expanded",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		// *************** for second
		// Listview Group expanded listener
		// expListView2.setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(int groupPosition) {
		// Toast.makeText(getApplicationContext(),
		// listDataHeader.get(groupPosition) + " Expanded",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		// ********************************

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				toast(listDataHeader.get(groupPosition) + " setOnGroupCollapseListener");
			}
		});

		// for second****************************

		// Listview Group collasped listener
		expListView2.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				toast(listDataHeader.get(groupPosition) + " setOnGroupCollapseListener");

			}
		});
		// *********************************

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// *************************************** for second
		// Listview on child click listener
		expListView2.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		// ********************************

		expListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				toast("selammmmmmm setOnTouchListener  exp1");
				if (touchSource == null)
					touchSource = v;

				if (v == touchSource) {
					expListView2.dispatchTouchEvent(event);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						clickSource = v;
						touchSource = null;
					}
				}

				return false;
			}
		});

		expListView2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				toast("selammmmmmm setOnTouchListener  exp2");
				if (touchSource == null)
					touchSource = v;

				if (v == touchSource) {
					expListView.dispatchTouchEvent(event);
					if (event.getAction() == MotionEvent.ACTION_UP) {
						clickSource = v;
						touchSource = null;
					}
				}

				return false;
			}
		});

		expListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view == clickSource)
					expListView2.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});

		expListView2.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view == clickSource)
					expListView.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});

	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Top 250");
		listDataHeader.add("Now Showing");
		listDataHeader.add("Coming Soon..");

		// Adding child data
		List<String> top250 = new ArrayList<String>();
		top250.add("The Shawshank Redemption");
		top250.add("The Godfather");
		top250.add("The Godfather: Part II");
		top250.add("Pulp Fiction");
		top250.add("The Good, the Bad and the Ugly");
		top250.add("The Dark Knight");
		top250.add("12 Angry Men");
		top250.add("The Shawshank Redemption");
		top250.add("The Godfather");

		List<String> nowShowing = new ArrayList<String>();
		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		nowShowing.add("The Conjuring");
		nowShowing.add("Despicable Me 2");
		nowShowing.add("Turbo");
		nowShowing.add("Grown Ups 2");
		nowShowing.add("Red 2");
		nowShowing.add("The Wolverine");

		List<String> comingSoon = new ArrayList<String>();
		comingSoon.add("2 Guns");
		comingSoon.add("The Smurfs 2");
		comingSoon.add("The Spectacular Now");
		comingSoon.add("The Canyons");
		comingSoon.add("Europa Report");

		listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
		listDataChild.put(listDataHeader.get(1), nowShowing);
		listDataChild.put(listDataHeader.get(2), comingSoon);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// Set listView's x, y coordinates in loc[0], loc[1]
				int[] loc = new int[2];
				expListView.getLocationInWindow(loc);

				// Save listView's y and get listView2's coordinates
				int firstY = loc[1];
				expListView2.getLocationInWindow(loc);

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
