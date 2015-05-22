package com.example.com.testing.treeapps;

import com.example.com.testing.treeapps.util.Utility;
import com.example.com.testing.treeapps.views.DecorateView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imgLayout = (LinearLayout) findViewById(R.id.imgs_layout);
		decorateView = (DecorateView) findViewById(R.id.decorate_view);

		/**
		 * Add initial image views on layout
		 */
		final int imageSize = (int) getResources().getDimension(
				R.dimen.h_scroll_height);
		final int margin = (int) getResources().getDimension(
				R.dimen.margin_medium);
		for (int resId : resIds) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					imageSize, imageSize);
			params.setMargins(margin, margin, margin, margin);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			final Bitmap bitmap = Utility.decodeResourceImg(this, resId, imageSize,
					imageSize);
			imageView.setImageBitmap(bitmap);
			imageView.setTag(bitmap);
			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					decorateView.drawDecorate((Bitmap)view.getTag());
				}
				
			});
			imgLayout.addView(imageView);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public LinearLayout imgLayout;
	public DecorateView decorateView;
	public static final int resIds[] = { R.drawable.pic1, R.drawable.pic2,
			R.drawable.pic3, R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
			R.drawable.pic7, R.drawable.pic8, R.drawable.pic9 };
}
