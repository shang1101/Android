package com.example.frozenlistviews;

import java.util.ArrayList;
import java.util.List;

import com.example.frozenlistviews.Animal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class AnimalAdapterHolder extends ArrayAdapter<Animal> {

	Context context = null;
	ArrayList<Animal> animals;
	int countColumnLocked = 0;
	View[] views;

	public AnimalAdapterHolder(Context context, int resource,
			List<Animal> objects, int count) {
		super(context, resource, objects);
		this.context = context;
		countColumnLocked = count;
		animals = (ArrayList<Animal>) objects;
		views = new View[7];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder; // create a view holder holding the
		// view(custom row)

		/* Purpose is the not creating /inflating a row each time */

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row, parent, false);

			viewHolder = new ViewHolder();

			// well set up the ViewHolder

			viewHolder.name = (TextView) convertView
					.findViewById(R.id.textView1);
			views[0] = viewHolder.name;

			viewHolder.age = (TextView) convertView
					.findViewById(R.id.textView2);
			views[1] = viewHolder.age;

			// store the holder with the view.
			viewHolder.hunting = (TextView) convertView
					.findViewById(R.id.textView3);
			views[2] = viewHolder.hunting;

			viewHolder.location = (TextView) convertView
					.findViewById(R.id.textView4);
			views[3] = viewHolder.location;
			// store the holder with the view.
			viewHolder.speed = (TextView) convertView
					.findViewById(R.id.textView5);
			views[4] = viewHolder.speed;

			viewHolder.type = (TextView) convertView
					.findViewById(R.id.textView6);
			views[5] = viewHolder.type;
			// store the holder with the view.

			viewHolder.trial = (Button) convertView.findViewById(R.id.button1);
			views[6] = viewHolder.trial;

			// store the holder with the view.
			for (int i = views.length-1; i >= countColumnLocked; i--)
				views[i].setVisibility(View.GONE);

			convertView.setTag(viewHolder);
		} else {

			// we call the view created before to not create a view in each item
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Animal a = animals.get(position);

		if (a != null) {
			viewHolder.name.setText(a.getName());
			viewHolder.age.setText("" + a.getAge());
			viewHolder.speed.setText(a.getSpeed());
			viewHolder.location.setText("" + a.getLocation());
			viewHolder.hunting.setText(a.getHunting());
			viewHolder.type.setText("" + a.getType());

		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return animals.size();
	}

	public class ViewHolder {

		TextView name;
		TextView age;
		TextView speed;
		TextView location;
		TextView type;
		TextView hunting;
		Button trial;

		public TextView getName() {
			return name;
		}

		public void setName(TextView name) {
			this.name = name;
		}

		public TextView getAge() {
			return age;
		}

		public void setAge(TextView age) {
			this.age = age;
		}

		public TextView getSpeed() {
			return speed;
		}

		public void setSpeed(TextView speed) {
			this.speed = speed;
		}

		public TextView getLocation() {
			return location;
		}

		public void setLocation(TextView location) {
			this.location = location;
		}

		public TextView getType() {
			return type;
		}

		public void setType(TextView type) {
			this.type = type;
		}

		public TextView getHunting() {
			return hunting;
		}

		public void setHunting(TextView hunting) {
			this.hunting = hunting;
		}

		public Button getTrial() {
			return trial;
		}

		public void setTrial(Button trial) {
			this.trial = trial;
		}

	}

}
