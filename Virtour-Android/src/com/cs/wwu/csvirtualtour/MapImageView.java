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
	//Paint p = new Paint();
	
	public void setMapMarks(ArrayList<Float> value)
	{
		this.mapMarks = value;
	}
	
	public MapImageView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		p.setColor(Color.RED);
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
			canvas.drawCircle(x + this.getMeasuredWidth() * markX,y + this.getMeasuredHeight() * markY, (this.getMeasuredHeight() / 50), p);
		}
		
		
	}

}
