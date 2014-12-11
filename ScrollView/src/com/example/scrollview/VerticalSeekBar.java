package com.example.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {

	private ScrollView scrollView;

	public ScrollView getScrollView() {

		return scrollView;

	}

	public void setScrollView(ScrollView scrollView) {

		this.scrollView = scrollView;

	}

	public VerticalSeekBar(Context context) {

		super(context);

	}

	public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

	}

	public VerticalSeekBar(Context context, AttributeSet attrs) {

		super(context, attrs);
		
		setProgress(200);

	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(h, w, oldh, oldw);
		

	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {

		super.onMeasure(heightMeasureSpec, widthMeasureSpec);

		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());

	}

	protected void onDraw(Canvas c) {

		c.rotate(-90);

		c.translate(-getHeight(), 0);
		
		super.onDraw(c);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isEnabled()) {

			return false;

		}

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

		case MotionEvent.ACTION_MOVE:

		case MotionEvent.ACTION_UP:

			setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
			
			int height= this.scrollView.getMeasuredHeight();
			int totalHeight = this.scrollView.getChildAt(0).getHeight();
		
			double oran = (double) (totalHeight - height)/getMax();

			this.scrollView.setScrollY((int)((getMax() - (int) (getMax() * (1 - event.getY() / getHeight()))) * oran));

			onSizeChanged(getWidth(), getHeight(), 0, 0);

			break;

		case MotionEvent.ACTION_CANCEL:

			break;

		}

		return true;

	}

}
