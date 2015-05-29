package com.example.com.testing.treeapps.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utility {

	/**
	 * Decode image to bitmap from resource Img
	 * 
	 * @param context
	 * @param resId
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap decodeResourceImg(Context context, int resId,
			int width, int height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, opts);
		opts.inSampleSize = computeSampleSize(opts, width, height);
		opts.inJustDecodeBounds = false;
		opts.inDither = false;

		Log.i("in sample size", String.valueOf(opts.inSampleSize));

		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resId, opts);

		return bitmap;
	}

	/**
	 * Compute origin image size and current img size ratio
	 * 
	 * @param opts
	 * @param width
	 * @param height
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options opts, int width,
			int height) {

		// origin width
		double w = opts.outWidth;
		// origin height
		double h = opts.outHeight;

		// width and height ratio
		double widthRatio = w / (double) width;
		double heightRatio = h / (double) height;

		// set ratio to the small one
		double ratio = widthRatio > heightRatio ? heightRatio : widthRatio;

		if (ratio < 1)
			return 1;

		int sample = (int) Math.ceil(ratio);

		return sample;

	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static double calcDist(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

}
