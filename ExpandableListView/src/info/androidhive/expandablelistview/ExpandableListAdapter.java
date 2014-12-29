package info.androidhive.expandablelistview;

import info.androidhive.expandablelistview.PinnedHeaderExpListView.PinnedHeaderAdapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	View[] views;
	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	int countLocked = 0;

	public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, int count) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;

		countLocked = count;

		views = new View[5];
	}

	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

		TextView textView = new TextView(_context);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(36, 0, 0, 0);
		return textView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		views[0] = lblListHeader;
		TextView lblListHeader1 = (TextView) convertView.findViewById(R.id.lblListHeader1);
		views[1] = lblListHeader1;
		TextView lblListHeader2 = (TextView) convertView.findViewById(R.id.lblListHeader2);
		views[2] = lblListHeader2;
		TextView lblListHeader3 = (TextView) convertView.findViewById(R.id.lblListHeader3);
		views[3] = lblListHeader3;
		TextView lblListHeader4 = (TextView) convertView.findViewById(R.id.lblListHeader4);
		views[4] = lblListHeader4;

		// store the holder with the view.
		for (int i = views.length - 1; i >= countLocked; i--)
			views[i].setVisibility(View.INVISIBLE);

		lblListHeader4.setTypeface(null, Typeface.BOLD);
		lblListHeader4.setText(headerTitle);

		lblListHeader3.setTypeface(null, Typeface.BOLD);
		lblListHeader3.setText(headerTitle);

		lblListHeader2.setTypeface(null, Typeface.BOLD);
		lblListHeader2.setText(headerTitle);

		lblListHeader1.setTypeface(null, Typeface.BOLD);
		lblListHeader1.setText(headerTitle);

		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
