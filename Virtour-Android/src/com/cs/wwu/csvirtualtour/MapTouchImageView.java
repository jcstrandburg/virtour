package com.cs.wwu.csvirtualtour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class MapTouchImageView extends TouchImageView {

	Paint p = new Paint();
	float[] cords;
	float borderx;
	float bordery;
	public MapTouchImageView(Context context, float[] coordinates) {
		super(context);
		cords = coordinates;
		this.setZoom(1);
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
	
	private PointF getZoomedPosition(float xratio, float yratio)
	{
		PointF returned = new PointF(0,0);
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
		imageWidth = (this.getMeasuredWidth());
		imageHeight = (this.getMeasuredHeight());
		
		if (this.getCurrentZoom() == 1)
		{
			borderx = xOffset * 2;
			bordery = yOffset * 2;
		}
		
		//Get the unzoomed dot position
		xpos = ((imageWidth - borderx) * xratio);
		ypos = ((imageHeight - bordery) * yratio);
		

		returned.x = (xpos) * this.getCurrentZoom() + xOffset;
		returned.y = (ypos) * this.getCurrentZoom() + yOffset;
		
		return returned;
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		p.setColor(Color.RED);
		p.setStrokeWidth(2);
		for (int i = 0; i < cords.length; i = i + 2)
		{
			if (isInView(cords[i], cords[i+1]))
			{
				PointF zoomedLocation = getZoomedPosition(cords[i],cords[i+1]);
				
				canvas.drawCircle(zoomedLocation.x, zoomedLocation.y, 10 * this.getCurrentZoom(), p);
			}
		}		
	}


}
