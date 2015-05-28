package com.example.com.testing.treeapps.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
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
			if (destRect.contains(downX, downY))
				mode = DRAG_MODE;
			else if (scaleRect.contains(downX, downY)) {
				tempX = (float) downX;
				mode = SCALE_MODE;
			} else if (rotateRect.contains(downX, downY)) {
				mode = ROTATE_MODE;
			} else
				mode = NONE_MODE;
			break;

		case MotionEvent.ACTION_MOVE:

			moveX = event.getX();
			moveY = event.getY();

			Log.d("action", "move");
			if (mode == DRAG_MODE) {
				matrix.set(finalMatrix);
				matrix.postTranslate(moveX - downX, moveY - downY);

				invalidate();
			} else if (mode == SCALE_MODE) {

				float ratio = event.getX() / downX;

				matrix.set(finalMatrix);
				matrix.postScale(ratio, ratio, destRect.centerX(),
						destRect.centerY());
				invalidate();

				// tempX = event.getX();
			} else if (mode == ROTATE_MODE) {

				float tanValue = (float) Math.atan2(destRect.centerY() - moveY,
						destRect.centerX() - moveX);

				int angle = (int) Math.toDegrees(tanValue);

				Log.i("angle", String.valueOf(angle));

				matrix.set(finalMatrix);
				matrix.postRotate(angle, destRect.centerX(), destRect.centerY());

				invalidate();
			}

			break;

		case MotionEvent.ACTION_UP:
			Log.d("action", "up");
			if (mode == DRAG_MODE) {
				// matrix.mapRect(area);

				finalMatrix.set(matrix);
			} else if (mode == SCALE_MODE) {
				finalMatrix.set(matrix);
			} else if (mode == ROTATE_MODE) {
				finalMatrix.set(matrix);
			}

			break;

		}

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);

		if (bitmap != null) {

			if (mode == INIT_MODE) {
				canvas.drawBitmap(bitmap, matrix, paint);
				srcRect.left = 0;
				srcRect.right = bitmap.getWidth();
				srcRect.top = 0;
				srcRect.bottom = bitmap.getHeight();
				destRect = srcRect;
				paint.setColor(Color.WHITE);
				paint.setStyle(Style.STROKE);
				canvas.drawRect(srcRect, paint);
			} else if (mode == DRAG_MODE) {

				canvas.drawBitmap(bitmap, matrix, paint);
				drawBoundary(canvas);
			} else if (mode == SCALE_MODE) {

				canvas.drawBitmap(bitmap, matrix, paint);
				drawBoundary(canvas);
			} else if (mode == ROTATE_MODE) {
				canvas.drawBitmap(bitmap, matrix, paint);
				drawBoundary(canvas);
			}

		}
	}

	private void drawBoundary(Canvas canvas) {

		Paint mPaint = new Paint();
		RectF temp = new RectF();
		RectF scale = new RectF();
		RectF rotate = new RectF();
		// map the rect to matrix
		matrix.mapRect(temp, srcRect);

		// draw boundary
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.STROKE);
		canvas.drawRect(temp, mPaint);

		// draw scale and rotate icons
		// draw scale button
		mPaint.setColor(Color.YELLOW);
		mPaint.setStyle(Style.FILL);

		scale.left = temp.right - 10;
		scale.right = temp.right + 10;
		scale.bottom = temp.bottom + 10;
		scale.top = temp.bottom - 10;
		canvas.drawRect(scale, mPaint);

		// draw rotate button
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.FILL);
		rotate.left = temp.left - 10;
		rotate.right = temp.left + 10;
		rotate.top = temp.top - 10;
		rotate.bottom = temp.top + 10;
		canvas.drawRect(rotate, mPaint);

		// set dest Rect
		destRect = temp;
		// set scale rect
		scaleRect = scale;
		// set rotate rect
		rotateRect = rotate;

	}

	public static final int NONE_MODE = -1;
	public static final int INIT_MODE = 0;
	public static final int DRAG_MODE = 1;
	public static final int SCALE_MODE = 2;
	public static final int ROTATE_MODE = 3;

	private Bitmap bitmap;
	private Paint paint = new Paint();
	private Matrix matrix = new Matrix();
	private Matrix finalMatrix = new Matrix();

	private RectF srcRect = new RectF();
	private RectF destRect = new RectF();

	private RectF scaleRect = new RectF();
	private RectF rotateRect = new RectF();

	private float tempX;
	private int startX, startY;
	private float downX, downY, moveX, moveY;
	private int mode = INIT_MODE;
}
