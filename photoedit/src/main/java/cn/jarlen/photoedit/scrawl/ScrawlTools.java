package cn.jarlen.photoedit.scrawl;

import android.content.Context;
import android.graphics.Bitmap;

import cn.jarlen.photoedit.utils.FileUtils;

public class ScrawlTools
{
	private DrawingBoardView drawView;
	private Context context;
	
	private int mBrushColor;
	

	
	public ScrawlTools(Context context, DrawingBoardView drawView,
			Bitmap bitmap)
	{
		this.drawView = drawView;
		this.context = context;
		drawView.setBackgroundBitmap(bitmap);
	}

	/**
	 * 创建画笔
	 * 
	 * @param drawStatus
	 * 绘画类型
	 * 
	 * @param paintBitmap
	 * 绘画画笔图片
	 * 
	 * @param color
	 * 画笔颜色
	 * 
	 */
	public void creatDrawPainter(DrawAttribute.DrawStatus drawStatus,
			Bitmap paintBitmap, int color)
	{
		drawView.setBrushBitmap(drawStatus, paintBitmap, color);
	}

	
	/**
	 * 创建画笔
	 * 
	 * @param drawStatus
	 * 绘画类型
	 * 
	 * @param paintBrush
	 * 画笔类型
	 * (包括画笔图片，画笔颜色，画笔粗细，画笔粗细种类)
	 * 
	 */
	public void creatDrawPainter(DrawAttribute.DrawStatus drawStatus,
			PaintBrush paintBrush)
	{
		int color = paintBrush.getPaintColor();
		int size = paintBrush.getPaintSize();
		int num = paintBrush.getPaintSizeTypeNo();
		Bitmap bitmap = paintBrush.getPaintBitmap();

		Bitmap paintBitmap = FileUtils.ResizeBitmap(bitmap, num - (size - 1));
		drawView.setBrushBitmap(drawStatus, paintBitmap, color);
	}
	
	
	public void creatStampPainter(DrawAttribute.DrawStatus drawStatus,int[] res,int color)
	{
		drawView.setStampBitmaps(drawStatus, res, color);
	}
	

	/**
	 * 得到绘画图片
	 * @return
	 */
	public Bitmap getBitmap()
	{
		return drawView.getDrawBitmap();
	}

	public int getBrushColor()
	{
		return mBrushColor;
	}

	/**
	 * 设置画刷颜色
	 * 
	 * @param mBrushColor
	 * 画刷颜色（无用）
	 * 
	 */
	
	public void setBrushColor(int mBrushColor)
	{
		this.mBrushColor = mBrushColor;
	}
}
