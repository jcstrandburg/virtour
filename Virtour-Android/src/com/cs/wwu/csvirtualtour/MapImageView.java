package com.cs.wwu.csvirtualtour;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

public class MapImageView extends ImageView {

	private Paint p = new Paint();
	private  ArrayList<Float> mapMarks;
	private float x = -1f, y = -1f;
	//Paint p = new Paint();
	
	public void setMapMarks(ArrayList<Float> value)
	{
		this.mapMarks = value;
	}
	
	public void setSelectedMark(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public MapImageView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		p.setColor(Color.GRAY);
		p.setStrokeWidth(2);
		Matrix matrix = this.getImageMatrix();
		float[] values = new float[9];
		matrix.getValues(values);
		float x = values[2];
		float y = values[5];
		for (int i = 0; i < mapMarks.size(); i = i + 2)
		{
			float markX = mapMarks.get(i);
			float markY = mapMarks.get(i+1);
			
			if (markX == this.x && markY == this.y)
			{
				p.setColor(Color.RED);
			}
			else
			{
				p.setColor(Color.GRAY);
			}
			canvas.drawCircle(x + this.getMeasuredWidth() * markX,y + this.getMeasuredHeight() * markY, (this.getMeasuredHeight() / 50), p);
		}
		
		
	}

}
