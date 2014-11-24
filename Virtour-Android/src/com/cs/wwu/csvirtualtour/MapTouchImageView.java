package com.cs.wwu.csvirtualtour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class MapTouchImageView extends TouchImageView {

	Paint p = new Paint();
	float[] cords;
	public MapTouchImageView(Context context, float[] coordinates) {
		super(context);
		cords = coordinates;
	}
	
	private Boolean isInView(float xratio, float yratio)
	{
		float xpos, ypos;
		float imageWidth, imageHeight;
		float zoomedtop, zoomedleft, zoomedbottom, zoomedright;
		
		//get offsets of actual image in ImageView
		Matrix imageMatrix = this.getImageMatrix();
		float[] values = new float[9];
		imageMatrix.getValues(values);
		float xOffset = values[2];
		float yOffset = values[5];
		
		//get image width and height
		imageWidth = (this.getWidth() - (2 * xOffset));
		imageHeight = (this.getHeight() - (2 *yOffset));
		
		//Get the unzoomed dot position
		xpos = (imageWidth * xratio);
		ypos = (imageHeight * yratio);
		
		//cacluate current image bounds
		RectF bounds = this.getZoomedRect();
		zoomedleft = bounds.left * imageWidth;
		zoomedright = bounds.right * imageWidth;
		zoomedtop = bounds.top * imageHeight;
		zoomedbottom = bounds.bottom * imageHeight;
		
		if (xpos > zoomedleft && xpos < zoomedright && ypos > zoomedtop && ypos < zoomedbottom)
		{
			return true;
		}
		return false;
	}
	
	private float getZoomedPosition(float xratio, float yratio)
	{
		float xpos, ypos;
		float imageWidth, imageHeight;
		float zoomedtop, zoomedleft, zoomedbottom, zoomedright;
		
		//get offsets of actual image in ImageView
		Matrix imageMatrix = this.getImageMatrix();
		float[] values = new float[9];
		imageMatrix.getValues(values);
		float xOffset = values[2];
		float yOffset = values[5];
		
		//get image width and height
		imageWidth = (this.getWidth() - (2 * xOffset));
		imageHeight = (this.getHeight() - (2 *yOffset));
		
		//Get the unzoomed dot position
		xpos = (imageWidth * xratio);
		ypos = (imageHeight * yratio);
		
		//cacluate current image bounds
		RectF bounds = this.getZoomedRect();
		return 0.0f;
		
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		p.setColor(Color.RED);
		p.setStrokeWidth(2);
		Rect bounds = this.getDrawable().getBounds();
		Matrix matrix = this.getImageMatrix();
		float[] values = new float[9];
		matrix.getValues(values);
		float x = values[2];
		float y = values[5];
		for (int i = 0; i < cords.length; i = i + 2)
		{
			if (isInView(cords[i], cords[i+1]))
			{
				canvas.drawCircle(x + ((this.getWidth() - (2 * x)) * cords[i]),y + (this.getHeight() - (2 * y))* cords[i+1],10 * this.getCurrentZoom() , p);
			}
		}
		
		isInView(cords[0],cords[1]);
		
		
	}


}
