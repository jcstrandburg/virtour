package com.cs.wwu.csvirtualtour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MapTouchImageView extends TouchImageView implements OnTouchListener {

	private Paint p = new Paint();
	private Stop[] stops;
	private float borderx;
	private float bordery;
	private int mapId;
	private Rect bounds;
	
	public MapTouchImageView(Context context, int mapId) {
		super(context);
		this.setZoom(1);
		this.setMaxZoom(5);
		//this.stops = Globals.getStops();
		this.mapId = mapId;
		this.bounds = new Rect();
		this.setOnTouchListener(this);
	}
	
	public void setStops(Stop[] value)
	{
		this.stops = value;
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
		for (int i = 0; i < stops.length; i++)
		{
			Stop s = stops[i];
			if (this.mapId == s.getStopMapID() && isInView(s.getStopPositionX(), s.getStopPositionY()))
			{
				p.setStyle(Paint.Style.FILL_AND_STROKE);
				PointF zoomedLocation = getZoomedPosition(s.getStopPositionX(),s.getStopPositionY());
				p.setTextSize((this.getMeasuredHeight() / 50) + 10 * this.getCurrentZoom());
				p.getTextBounds(s.getStopRooNumber(),0,s.getStopRooNumber().length(), bounds);
				float length = p.measureText(s.getStopRooNumber());
				float height = bounds.bottom - bounds.top;
				bounds.top = (int)(zoomedLocation.y - height - ((this.getMeasuredHeight() /100) * this.getCurrentZoom()));
				bounds.bottom = bounds.top + (int)height + (int)(3 * this.getCurrentZoom());
				bounds.left = (int)(zoomedLocation.x - length /2 - (int)(3 * this.getCurrentZoom()));
				bounds.right = (int)(bounds.left + length + (int)(3 * this.getCurrentZoom()));
				//Draw background rectangle
				p.setColor(Color.WHITE);
				canvas.drawRect(bounds,p);
				p.setColor(Color.BLACK);
				p.setStyle(Paint.Style.STROKE);
				canvas.drawRect(bounds,p);
				//Draw Text
				p.setStyle(Paint.Style.FILL_AND_STROKE);
				p.setColor(Color.BLACK);
				canvas.drawText(s.getStopRooNumber(), zoomedLocation.x - length/2, zoomedLocation.y - ((this.getMeasuredHeight() /100) * this.getCurrentZoom()), p);
				p.setColor(Color.RED);
				canvas.drawCircle(zoomedLocation.x, zoomedLocation.y, (this.getMeasuredHeight() / 100) * this.getCurrentZoom(), p);
			}
		}		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x, y;
		float xMargin = this.getMeasuredWidth() / 50, yMargin = this.getMeasuredHeight() / 50;
		//Maybe put up a little preview
		//Open appropriate stop
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			x = event.getX();
			y = event.getY();
//			Toast T =  Toast.makeText(this.getContext(),String.format("You touched me at %f,%f",event.getX(),event.getY()), Toast.LENGTH_LONG);
//			T.show();
			
		for (int i = 0; i < stops.length; i++) 
		{
			
			PointF stopPos = this.getZoomedPosition(stops[i].getStopPositionX(), stops[i].getStopPositionY());
			
			if (event.getPointerCount() == 1 && stopPos.x + xMargin > x && stopPos.x - xMargin < x && stopPos.y + yMargin > y && stopPos.y - yMargin < y)
			{
//				Toast To = Toast.makeText(this.getContext(), "YAY", Toast.LENGTH_LONG);
//				To.show();
				
				Intent intent = new Intent(this.getContext(),StopActivity.class);
				intent.putExtra("StopID", stops[i].getStopID());
				this.getContext().startActivity(intent);
			}
			
		}
		
		}
		
		
		return super.onTouchEvent(event);
		
	}


}
