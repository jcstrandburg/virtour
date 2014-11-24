package com.cs.wwu.csvirtualtour;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

public class MapImageView extends ImageView {

	float markX, markY;
	Paint p = new Paint();
	//Paint p = new Paint();
	public MapImageView(Context context,float markx, float marky) {
		super(context);
		this.markX = markx;
		this.markY = marky;
		// TODO Auto-generated constructor stub
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
		canvas.drawCircle(x + this.getMeasuredWidth() * markX,y + this.getMeasuredHeight() * markY, 10, p);
		
		
	}

}
