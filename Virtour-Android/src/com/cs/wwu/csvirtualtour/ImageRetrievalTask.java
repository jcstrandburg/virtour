package com.cs.wwu.csvirtualtour;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ImageRetrievalTask extends AsyncTask<String, Void, Bitmap> {
	ImageView Image;
	OnContentLoaded contentloader;
	public ImageRetrievalTask(ImageView theImage, OnContentLoaded loader) {
		this.Image = theImage;
		this.contentloader = loader;
	}
	@Override
	protected Bitmap doInBackground(String... urls) {
		
		String url = urls[0];
		Bitmap retrievedImage = null;
		
		try {
			InputStream is = new URL(url).openStream();
			retrievedImage = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			Log.e("Image Retrieval",e.getMessage());
			e.printStackTrace();
		}
		return retrievedImage;
	}
	
	protected void onPostExecute(Bitmap result) {
		Image.setAlpha(1f);
		Image.setImageBitmap(ImageProcessor.decodeSampledBitmapFromResource(result, Image.getWidth(), Image.getHeight()));
		Image.setVisibility(View.VISIBLE);
		if (this.contentloader != null)
		{
			this.contentloader.onContentLoaded();
		}
	}

}
