package info.androidhive.expandablelistview;

import info.androidhive.expandablelistview.PinnedHeaderExpListView.PinnedHeaderAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class MyExpandableListAdapter extends BaseExpandableListAdapter implements PinnedHeaderAdapter, OnScrollListener {
	// Sample data set. children[i] contains the children (String[]) for
	// groups[i].
	private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };
	private String[][] children = { { "Arnold", "Barry", "Chuck", "David", "Stas", "Oleg", "Max", "Alex", "Romeo", "Adolf" },
			{ "Ace", "Bandit", "Cha-Cha", "Deuce", "Nokki", "Baron", "Sharik", "Toshka", "SObaka", "Belka", "Strelka", "Zhuchka" },
			{ "Fluffy", "Snuggles", "Cate", "Yasha", "Bars" }, { "Goldy", "Bubbles", "Fluffy", "Snuggles", "Guffy", "Snoopy" } };

	private Context context=null;

	private int mPinnedHeaderBackgroundColor;
	private int mPinnedHeaderTextColor;
	
	public MyExpandableListAdapter(Context context) {
		super();
		this.context = context;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return children[groupPosition][childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return children[groupPosition].length;
	}

	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(36, 0, 0, 0);
		return textView;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textView = getGenericView();
		textView.setText(getChild(groupPosition, childPosition).toString());
		return textView;
	}

	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	public int getGroupCount() {
		return groups.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.header, parent, false);
		textView.setText(getGroup(groupPosition).toString());
		return textView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void configurePinnedHeader(View v, int position, int alpha) {
		TextView header = (TextView) v;
		final String title = (String) getGroup(position);

		header.setText(title);
		if (alpha == 255) {
			header.setBackgroundColor(mPinnedHeaderBackgroundColor);
			header.setTextColor(mPinnedHeaderTextColor);
		} else {
			header.setBackgroundColor(Color.argb(alpha, Color.red(mPinnedHeaderBackgroundColor), Color.green(mPinnedHeaderBackgroundColor),
					Color.blue(mPinnedHeaderBackgroundColor)));
			header.setTextColor(Color.argb(alpha, Color.red(mPinnedHeaderTextColor), Color.green(mPinnedHeaderTextColor),
					Color.blue(mPinnedHeaderTextColor)));
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderExpListView) {
			((PinnedHeaderExpListView) view).configureHeaderView(firstVisibleItem);
		}

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

}