package com.example.android.swipedismiss;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomList extends ArrayAdapter<Integer> {

	private final Context context;
	private final ArrayList<Integer> imageId;
	private final static String TAG = "CustomList";

	public CustomList(Context c, int resource, int img,
			ArrayList<Integer> imageId) {

		super(c, resource, img, imageId);

		this.context = c;
		this.imageId = imageId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_row, null, false);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(viewHolder);

		} else {
			// we call the view created before to not create a view in each time
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final int imgId = imageId.get(position);

		viewHolder.img.setImageResource(imgId);

		viewHolder.img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(context, "Clicked " + imgId, Toast.LENGTH_SHORT)
						.show();
			}
		});

		// Create a generic swipe-to-dismiss touch listener.
		viewHolder.img.setOnTouchListener(new SwipeDismissTouchListener(
				viewHolder.img, null,
				new SwipeDismissTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(Object token) {
						return true;
					}

					@Override
					public void onDismiss(View view, Object token) {

						Log.d(TAG, "Image ıd" + imgId);

						
						imageId.remove(position);
						remove(position);

						notifyDataSetChanged();
					}
				}));

		return convertView;
	}

	public class ViewHolder {

		ImageView img;

		public ImageView getImg() {
			return img;
		}

		public void setImg(ImageView img) {
			this.img = img;
		}

	}

}
