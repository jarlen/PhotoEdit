package cn.jarlen.photoedit.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

public class FileUtils
{

	public static String SDCARD_PAHT = Environment
			.getExternalStorageDirectory().getPath();

	public static String DCIMCamera_PATH = Environment
			.getExternalStorageDirectory() + "/DCIM/Camera/";

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用; false为不可用
	 */
	public static boolean isSDAvailable()
	{
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}
	
	public static Bitmap ResizeBitmap(Bitmap bitmap, int scale)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(1/scale, 1/scale);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	public static String getNewFileName()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());

		return formatter.format(curDate);
	}

	/**
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @param bm
	 *            要保存的bitmap
	 * 
	 * @param name
	 *            保存的名字 可为null,就根据时间自定义一个文件名
	 * 
	 * @return 以“.jpg”格式保存至相册
	 * 
	 */
	public static Boolean saveBitmapToCamera(Context context, Bitmap bm,
			String name)
	{

		File file = null;

		if (name == null || name.equals(""))
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date curDate = new Date(System.currentTimeMillis());
			name = formatter.format(curDate) + ".jpg";
		}

		file = new File(DCIMCamera_PATH, name);
		if (file.exists())
		{
			file.delete();
		}

		try
		{
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;

		} catch (IOException e)
		{

			e.printStackTrace();
			return false;
		}

//		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		Uri uri = Uri.fromFile(file);
//		intent.setData(uri);
//		context.sendBroadcast(intent);

		return true;
	}

	/**
	 * 
	 * @param bitmap
	 * @param destPath
	 * @param quality
	 */
	public static void writeImage(Bitmap bitmap, String destPath, int quality)
	{
		try
		{
			deleteFile(destPath);
			if (createFile(destPath))
			{
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
				{
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean createFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath
	 *            要删除的文件路径名
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (file.exists())
			{
				return file.delete();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除 dir目录下的所有文件，包括删除删除文件夹
	 * 
	 * @param dir
	 */
	public static void deleteDirectory(File dir)
	{
		if (dir.isDirectory())
		{
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++)
			{
				deleteDirectory(listFiles[i]);
			}
		}
		dir.delete();
	}

}
