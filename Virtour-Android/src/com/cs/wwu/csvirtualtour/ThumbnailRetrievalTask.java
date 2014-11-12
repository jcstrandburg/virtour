package com.cs.wwu.csvirtualtour;

import wseemann.media.FFmpegMediaMetadataRetriever;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ThumbnailRetrievalTask extends AsyncTask<String, Void, Bitmap> {

	ImageView Image;
	
	public ThumbnailRetrievalTask(ImageView theImage) {
		this.Image = theImage;
	}
	@Override
	protected Bitmap doInBackground(String... urls) {
		
		String url = urls[0];
		Bitmap retrievedImage = null;
		
		FFmpegMediaMetadataRetriever mr = new FFmpegMediaMetadataRetriever();
		mr.setDataSource(url);
		retrievedImage = mr.getFrameAtTime(20000,FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
		
		return retrievedImage;
	}
	
	protected void onPostExecute(Bitmap result) {
		Image.setImageBitmap(result);
	}

}
