/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.jarlen.photoedit.scrawl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author jarlen
 */
public class DrawingBoardView extends View
{

	/** 画板背景 **/
	private Bitmap backgroundBitmap = null;

	/** 绘画图层 **/
	private Bitmap paintBitmap = null;

	/** 绘画板 **/
	private Canvas paintCanvas = null;

	/** 手势监听 **/
	private GestureDetector brushGestureDetector = null;
	private BrushGestureListener brushGestureListener = null;

	/** 绘画类型 **/
	private DrawAttribute.DrawStatus mDrawStatus;
	
	private int mBrushColor;

	/** 上下文 **/
	private Context context;

	public DrawingBoardView(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		this.context = context;
		brushGestureListener = new BrushGestureListener();
		brushGestureDetector = new GestureDetector(context,
				brushGestureListener);

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		if(backgroundBitmap != null && !backgroundBitmap.isRecycled()){
			canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		}

		if(paintBitmap != null && !paintBitmap.isRecycled()){
			canvas.drawBitmap(paintBitmap, 0, 0, null);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		brushGestureDetector.onTouchEvent(event);
		postInvalidate();
		return true;
	}

	/**
	 * 设置绘制的背景图片 创建绘画画板Canvas
	 * 
	 * @param bitmap
	 *            背景图片
	 * 
	 */
	public void setBackgroundBitmap(Bitmap bitmap)
	{

		backgroundBitmap = bitmap;
		paintBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		paintCanvas = new Canvas(paintBitmap);

	}

	
	/**
	 * 设置绘画类型
	 * 
	 * @param drawStatus
	 * 
	 * 绘画类型
	 * 
	 */
	public void setDrawStatus(DrawAttribute.DrawStatus drawStatus)
	{
		this.mDrawStatus = drawStatus;
	}
	
	/**
	 * 设置画笔颜色
	 * 
	 * @param color
	 * 
	 * 画笔颜色
	 * 
	 */
	public void setPaintColor(int color)
	{
		this.mBrushColor = color;
	}
	
	

	/**
	 * 设置画笔
	 * 
	 * @param drawStatus
	 *            绘画类型
	 * 
	 * @param brushBitmap
	 *            画笔
	 * 
	 * @param brushColor
	 *            画笔颜色
	 * 
	 */
	public void setBrushBitmap(DrawAttribute.DrawStatus drawStatus,
			Bitmap brushBitmap, int brushColor)
	{
		this.mDrawStatus = drawStatus;
		this.mBrushColor = brushColor;
		
		Bitmap tempBrush = null;
		int brushDistance = 0;
		Paint brushPaint = null;

		switch (mDrawStatus)
		{
			case PEN_WATER :

				brushDistance = 1;
				tempBrush = casualStroke(brushBitmap, brushColor);
				brushPaint = null;

				break;

			case PEN_CRAYON :

				brushDistance = brushBitmap.getWidth() / 2;
				tempBrush = casualStroke(brushBitmap, brushColor);
				brushPaint = null;

				break;

			case PEN_COLOR_BIG :
				tempBrush = casualStroke(brushBitmap, brushColor);
				brushDistance = 2;
				brushPaint = null;

				break;

			case PEN_ERASER :
				brushPaint = new Paint();
				brushPaint.setFilterBitmap(true);
				brushPaint.setXfermode(new PorterDuffXfermode(
						PorterDuff.Mode.DST_OUT));

				tempBrush = brushBitmap;
				brushDistance = brushBitmap.getWidth() / 4;

				break;

			default :
				break;
		}

		brushGestureListener.setBrush(tempBrush, brushDistance, brushPaint);
	}

	/**
	 * 设置邮票数据
	 * 
	 * @param drawStatus
	 *            绘画类型
	 * 
	 * @param res
	 *            资源ID数组(大小为4)
	 * 
	 * @param color
	 *            颜色
	 * 
	 */
	public void setStampBitmaps(DrawAttribute.DrawStatus drawStatus, int[] res,
			int color)
	{
		Bitmap[] brushBitmaps = new Bitmap[4];
		brushBitmaps[0] = casualStroke(res[0], color);
		brushBitmaps[1] = casualStroke(res[1], color);
		brushBitmaps[2] = casualStroke(res[2], color);
		brushBitmaps[3] = casualStroke(res[3], color);

		brushGestureListener.setStampBrush(brushBitmaps);
		this.mDrawStatus = drawStatus;

	}

	class BrushGestureListener extends GestureDetector.SimpleOnGestureListener
	{

		private Bitmap brushBitmap = null;
		private int brushDistance;

		/** 半个画刷宽度 **/
		private int halfBrushBitmapWidth;

		/** 画刷 **/
		private Paint brushPaint = null;

		/** 邮票 **/
		private Bitmap[] stampBrushBitmaps = null;

		/** 是否是邮票 **/
		private boolean isStamp = false;

		public BrushGestureListener()
		{
			super();
			isStamp = false;
		}

		@Override
		public boolean onDown(MotionEvent e)
		{

			switch (mDrawStatus)
			{
				case PEN_WATER :
				case PEN_CRAYON :
				case PEN_COLOR_BIG :
				case PEN_ERASER :
				{
					isStamp = false;
					break;
				}
				case PEN_STAMP :
				{
					isStamp = true;
					break;
				}
				default :
					isStamp = false;
					break;
			}

			if (isStamp)
			{
				paintSingleStamp(e.getX(), e.getY());
			} else
			{
				paintCanvas.drawBitmap(brushBitmap, e.getX()
						- halfBrushBitmapWidth,
						e.getY() - halfBrushBitmapWidth, brushPaint);
			}

			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY)
		{

			if (isStamp)
			{
				//paintSingleStamp(e2.getX(), e2.getY());
			} else
			{
				float beginX = e2.getX();
				float beginY = e2.getY();
				float distance = (float) Math.sqrt(distanceX * distanceX
						+ distanceY * distanceY);
				float x = distanceX / distance, x_ = 0;
				float y = distanceY / distance, y_ = 0;
				while (Math.abs(x_) <= Math.abs(distanceX)
						&& Math.abs(y_) <= Math.abs(distanceY))
				{
					x_ += x * brushDistance;
					y_ += y * brushDistance;
					paintCanvas.save();
					paintCanvas.rotate((float) (Math.random() * 10000), beginX
							+ x_, beginY + y_);
					paintCanvas.drawBitmap(brushBitmap, beginX + x_
							- halfBrushBitmapWidth, beginY + y_
							- halfBrushBitmapWidth, brushPaint);
					paintCanvas.restore();
				}
			}

			return true;
		}

		public void setBrush(Bitmap brushBitmap, int brushDistance,
				Paint brushPaint)
		{
			this.brushBitmap = brushBitmap;
			this.brushDistance = brushDistance;
			this.halfBrushBitmapWidth = brushBitmap.getWidth() / 2;
			this.brushPaint = brushPaint;
		}

		public void setStampBrush(Bitmap[] brushBitmaps)
		{
			this.stampBrushBitmaps = brushBitmaps;
			halfBrushBitmapWidth = brushBitmaps[0].getWidth() / 2;
		}

		private void paintSingleStamp(float x, float y)
		{
			
			if (Math.random() > 0.1)
			{
				paintCanvas.drawBitmap(stampBrushBitmaps[0], x
						- halfBrushBitmapWidth, y - halfBrushBitmapWidth, null);
			}
			
			for (int i = 1; i < stampBrushBitmaps.length; i++)
			{
				if (Math.random() > 0.3)
				{
					paintCanvas.drawBitmap(stampBrushBitmaps[i], x
							- halfBrushBitmapWidth, y - halfBrushBitmapWidth,
							null);
				}
			}

		}

	}
	
	
	
	/**
	 * 将图片涂色
	 * 
	 * @param paintBit
	 *            画笔
	 * 
	 * @param color
	 *            画笔颜色
	 * 
	 * @return 将画笔(图片)涂色
	 * 
	 */
	private Bitmap casualStroke(Bitmap paintBit, int color)
	{
		Bitmap bitmap = paintBit.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		Paint paintUnder = new Paint();
		paintUnder.setColor(color);
		canvas.drawPaint(paintUnder);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		canvas.drawBitmap(paintBit, 0, 0, paint);
		paintBit.recycle();
		return bitmap;
	}

	/**
	 * 将图片涂色
	 * 
	 * @param drawableId
	 *            图片
	 * 
	 * @param color
	 *            颜色
	 * 
	 * @return
	 */
	private Bitmap casualStroke(int drawableId, int color)
	{
		Bitmap mode = ((BitmapDrawable) this.getResources().getDrawable(
				drawableId)).getBitmap();
		Bitmap bitmap = mode.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		Paint paintUnder = new Paint();
		paintUnder.setColor(color);
		canvas.drawPaint(paintUnder);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		canvas.drawBitmap(mode, 0, 0, paint);
		return bitmap;
	}
	
	/**
	 * 得到绘画图片
	 * 
	 * @return
	 */
	public Bitmap getDrawBitmap()
	{
		Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		canvas.drawBitmap(paintBitmap, 0, 0, null);
		canvas.save();

		return bitmap;
	}
}
