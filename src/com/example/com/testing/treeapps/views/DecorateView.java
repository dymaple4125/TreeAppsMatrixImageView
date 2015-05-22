package com.example.com.testing.treeapps.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.EventLog.Event;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Self defined decorate view
 * 
 * @author ericding
 * 
 */
public class DecorateView extends View {

	public DecorateView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DecorateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		// this.onDraw(canvas);
	}

	public void drawDecorate(Bitmap bitmap) {
		this.bitmap = bitmap;
		mode = INIT_MODE;
		invalidate();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();

		switch (action) {

		case MotionEvent.ACTION_DOWN:

			Log.d("action", "down");
			downX = (int) event.getX();
			downY = (int) event.getY();
			mode = DRAG_MODE;
			break;

		case MotionEvent.ACTION_MOVE:

			Log.d("action", "move");

			moveX = (int) event.getX() - downX;
			moveY = (int) event.getY() - downY;
			invalidate();

			break;

		case MotionEvent.ACTION_UP:

			Log.d("action", "up");

			break;

		}

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (bitmap != null) {

			if (mode == INIT_MODE) {
				canvas.drawBitmap(bitmap, 0, 0, paint);
			} else if (mode == DRAG_MODE) {
				canvas.drawBitmap(bitmap, moveX, moveY, paint);
			}
		}
	}

	public static final int INIT_MODE = 0;
	public static final int DRAG_MODE = 1;
	public static final int MATRIX_MODE = 2;

	private Bitmap bitmap;
	private Paint paint = new Paint();
	private Matrix matrix = new Matrix();

	private int downX, downY, moveX, moveY;
	private int mode = DRAG_MODE;
}
