package com.example.com.testing.treeapps.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.com.testing.treeapps.util.Utility;

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

		MyIcon myIcon = new MyIcon(bitmap);
		myIcons.add(myIcon);

		// no mode
		mode = NONE_MODE;

		// re draw the view
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();

		switch (action) {

		case MotionEvent.ACTION_DOWN:

			Log.d("action", "down");
			downX = event.getX();
			downY = event.getY();

			mode = NONE_MODE;

			activeIcon = null;
			// initial
			for (MyIcon icon : myIcons) {
				icon.isActive = false;
			}

			// check inverse
			for (int i = myIcons.size() - 1; i >= 0; i--) {

				// if has this one
				if (myIcons.get(i).mRegion.contains((int) downX, (int) downY)
						|| myIcons.get(i).mRotateRect.contains(downX, downY)) {
					activeIcon = myIcons.get(i);
					activeIcon.isActive = true;
					matrix.set(activeIcon.mMatrix);

					if (activeIcon.mRotateRect.contains(downX, downY)) {
						mode = ROTATE_MODE;
					} else {
						mode = DRAG_MODE;
					}

					break;
				}
			}

			// if exists delete and
			if (activeIcon != null) {
				myIcons.remove(activeIcon);
				myIcons.add(activeIcon);
			}

			invalidate();

			break;

		case MotionEvent.ACTION_MOVE:

			if (activeIcon != null) {
				moveX = event.getX();
				moveY = event.getY();

				Log.d("action", "move");
				if (mode == DRAG_MODE) {
					matrix.set(activeIcon.mMatrix);
					matrix.postTranslate(moveX - downX, moveY - downY);
				} else if (mode == ROTATE_MODE) {

					/**
					 * Calculate scale value and rotate value
					 */
					float downTanValue = (float) Math.atan2(activeIcon.centerY
							- downY, activeIcon.centerX - downX);
					float moveTanValue = (float) Math.atan2(activeIcon.centerY
							- moveY, activeIcon.centerX - moveX);
					double currentDist = Utility.calcDist(moveX, moveY,
							activeIcon.centerX, activeIcon.centerY);
					double cornerDist = Utility.calcDist(downX, downY,
							activeIcon.centerX, activeIcon.centerY);

					float ratio = (float) (currentDist / cornerDist);

					int downAngle = (int) Math.toDegrees(downTanValue);
					int moveAngle = (int) Math.toDegrees(moveTanValue);

					int angle = moveAngle - downAngle;

					if (angle < 0)
						angle = 360 + angle;
					Log.i("angle", String.valueOf(angle));

					matrix.set(activeIcon.mMatrix);
					matrix.postRotate(angle, activeIcon.centerX,
							activeIcon.centerY);
					matrix.postScale(ratio, ratio, activeIcon.centerX,
							activeIcon.centerY);

				}

				invalidate();
			}
			break;

		case MotionEvent.ACTION_UP:
			Log.d("action", "up");
			if (activeIcon != null) {
				// to get the bodary
				minX = Float.MAX_VALUE;
				minY = Float.MAX_VALUE;
				maxX = 0;
				maxY = 0;

				for (int i = 0; i < 8; i += 2) {
					if (destPoints[i] < minX)
						minX = destPoints[i];
					if (destPoints[i] > maxX)
						maxX = destPoints[i];
					if (destPoints[i + 1] < minY)
						minY = destPoints[i + 1];
					if (destPoints[i + 1] > maxY)
						maxY = destPoints[i + 1];
				}

				// reset the activate region
				activeIcon.mRegion.set((int) minX, (int) minY, (int) maxX,
						(int) maxY);
				activeIcon.mRotateRect.set(destPoints[0] - ROTATE_BTN_MARGIN,
						destPoints[1] - ROTATE_BTN_MARGIN, destPoints[0]
								+ ROTATE_BTN_MARGIN, destPoints[1]
								+ ROTATE_BTN_MARGIN);
				activeIcon.centerX = (maxX + minX) / 2;
				activeIcon.centerY = (maxY + minY) / 2;

				Log.i("min and max",
						String.valueOf(minX) + "and" + String.valueOf(minY)
								+ "and" + String.valueOf(maxX) + "and"
								+ String.valueOf(maxY) + "AND"
								+ String.valueOf(centerX) + "and"
								+ String.valueOf(centerY));
				activeIcon.mMatrix.set(matrix);
			}

			break;

		}

		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);

		// draw all icons
		if (myIcons.size() != 0) {

			for (int i = 0; i < myIcons.size(); i++) {

				final MyIcon myIcon = myIcons.get(i);

				if (!myIcon.isActive) {
					canvas.drawBitmap(myIcon.mBitmap, myIcon.mMatrix, paint);
				} else {
					canvas.drawBitmap(myIcon.mBitmap, matrix, paint);
					drawBoundary(canvas, myIcon);
				}
			}
		}
	}

	/**
	 * Draw activate icon boundary
	 * 
	 * @param canvas
	 * @param myIcon
	 */
	private void drawBoundary(Canvas canvas, MyIcon myIcon) {

		Paint mPaint = new Paint();
		RectF rotate = new RectF();
		// map the rect to matrix

		matrix.mapPoints(destPoints, myIcon.mSrcPoints);

		// draw boundary
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.STROKE);
		// draw three lines
		for (int i = 0; i < 3; i++) {
			canvas.drawLine(destPoints[i * 2], destPoints[i * 2 + 1],
					destPoints[(i + 1) * 2], destPoints[(i + 1) * 2 + 1],
					mPaint);
		}

		// draw last line
		canvas.drawLine(destPoints[0], destPoints[1], destPoints[6],
				destPoints[7], mPaint);

		// draw rotate button
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.FILL);
		rotate.left = destPoints[0] - ROTATE_BTN_MARGIN;
		rotate.right = destPoints[0] + ROTATE_BTN_MARGIN;
		rotate.top = destPoints[1] - ROTATE_BTN_MARGIN;
		rotate.bottom = destPoints[1] + ROTATE_BTN_MARGIN;
		canvas.drawRect(rotate, mPaint);

	}

	class MyIcon {

		public MyIcon(Bitmap bitmap) {
			mBitmap = bitmap;
			mMatrix = new Matrix();
			mRotateRect = new RectF();
			mSrcPoints = new float[8];
			mSrcPoints[0] = 0;
			mSrcPoints[1] = 0;
			mSrcPoints[2] = bitmap.getWidth();
			mSrcPoints[3] = 0;
			mSrcPoints[4] = bitmap.getWidth();
			mSrcPoints[5] = bitmap.getHeight();
			mSrcPoints[6] = 0;
			mSrcPoints[7] = bitmap.getHeight();
			mRegion = new Region(0, 0, bitmap.getWidth(), bitmap.getHeight());
			centerX = bitmap.getWidth() / 2;
			centerY = bitmap.getHeight() / 2;
			isActive = false;
			mRotateRect.set(-ROTATE_BTN_MARGIN, -ROTATE_BTN_MARGIN,
					ROTATE_BTN_MARGIN, ROTATE_BTN_MARGIN);
		}

		public Matrix mMatrix;
		public Bitmap mBitmap;
		public float[] mSrcPoints;
		public float centerX, centerY;
		public Region mRegion;
		public RectF mRotateRect;
		public boolean isActive;

	}

	public static final int NONE_MODE = 0;
	public static final int DRAG_MODE = 1;
	public static final int ROTATE_MODE = 2;

	public static final int ROTATE_BTN_MARGIN = 15;

	private MyIcon activeIcon; // current activate icon
	private Paint paint = new Paint(); // view paint
	private Matrix matrix = new Matrix(); // current working matrix

	private float[] destPoints = new float[8]; // destinate points

	private float minX, minY, maxX, maxY, centerX, centerY; // all temp boundary
															// points value
	private ArrayList<MyIcon> myIcons = new ArrayList<MyIcon>(); // a list of
																	// Icons
	private float downX, downY, moveX, moveY; // touch points
	private int mode = NONE_MODE; // working mode
}
