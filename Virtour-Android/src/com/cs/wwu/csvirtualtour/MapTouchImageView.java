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
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MapTouchImageView extends TouchImageView implements OnTouchListener {

	private Paint p = new Paint();
	private Stop[] stops;
	private float borderx;
	private float bordery;
	private int mapId, oldId;
	public MapTouchImageView(Context context) {
		super(context);
		this.setZoom(1);
		//this.stops = Globals.getStops();
		this.setOnTouchListener(this);
	}
	
	public void setStops(Stop[] value)
	{
		this.stops = value;
	}
	
	public void setMap(int id)
	{
		this.mapId = id;
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
		for (int i = 0; i < stops.length; i++)
		{
			Stop s = stops[i];
			if (isInView(s.getStopPositionX(), s.getStopPositionY()))
			{
				PointF zoomedLocation = getZoomedPosition(s.getStopPositionX(),s.getStopPositionY());
				
				p.setColor(Color.BLACK);
				p.setTextSize(40 - 10 * this.getCurrentZoom());
				canvas.drawText(s.getStopName(), zoomedLocation.x, zoomedLocation.y -30, p);
				p.setColor(Color.RED);
				canvas.drawCircle(zoomedLocation.x, zoomedLocation.y, 10 * this.getCurrentZoom(), p);
			}
		}		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x, y;
		float xMargin = 40, yMargin = 40;
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
			
			if (stopPos.x + xMargin > x && stopPos.x - xMargin < x && stopPos.y + yMargin > y && stopPos.y - yMargin < y)
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
