package com.feicui.DBManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.feicui.bean.ClearInfo;
import com.feicui.manager.MemoryManager;

/**
 * 获取各种软件的缓存文件
 * 
 * @author Administrator
 * 
 */
public class SelectForClear {
	public static long allSize = 0;

	public static List<ClearInfo> getClearList(Context context)
			 {

		// 声明一个接口对象，用来保存从数据库中获取到的实体类对象
		List<ClearInfo> infoList = new ArrayList<>();
		// 调用拷贝数据库的方法，确保数据库文件已经拷贝，避免创建一个空的数据库
		CopyAssetsDB.copyClearDB(context);
		// 1复制文件到data/data/包名/数据库名.db
		String path = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/clearpath.db";
		// 打开或者新建一个数据库，（如果指定路径下数据库不存在，就创建一个新的）
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
		// 查询语句：查询表中所有的记录
		String sql = "select * from softdetail";
		// 查询数据库的记录
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			do {
				// 得到不同列的对应值
				String softChinesename = c.getString(c
						.getColumnIndex("softChinesename"));
				String apkname = c.getString(c.getColumnIndex("apkname"));
				String filepath = c.getString(c.getColumnIndex("filepath"));
				// 获得不同软件存储文件的路径
				String filePath = MemoryManager.getPhoneInSDCardPath()
						+ filepath;
				// 根据路径找到文件File对象
				File file = new File(filePath);

				if (file.exists()) {// 如果文件存在
					// 获取文件（文件夹）的总大小
					long fileSize = getFileSize(file);
					allSize += fileSize;
					// 应用图标
					Drawable icon = null;
					try {
						icon = context.getPackageManager()
								.getApplicationIcon(apkname);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					boolean isChecked = false;
					if (fileSize > 0) {
						ClearInfo info = new ClearInfo(softChinesename,
								apkname, filePath, isChecked, fileSize, icon,
								file);
						infoList.add(info);
					}

				}

			} while (c.moveToNext());
		}
		c.close();
		Log.e("infoList", infoList.toString());
		return infoList;
	}

	/**
	 * 获取文件夹总大小
	 * 
	 * @param file
	 * @return
	 */
	private static long getFileSize(File file) {
		long size = 0;
		if (file.isDirectory()) {// 如果是文件夹
			// 获得文件夹下所有文件的数组
			File[] files = file.listFiles();
			if (files != null) {// 如果数组不为空
				for (File file2 : files) {// 遍历递归
					size += getFileSize(file2);
				}
			}
		} else {
			size += file.length();
		}

		return size;

	}

	/**
	 * 删除文件夹，注意：文件夹如果不为空，没法删除
	 */
	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				deleteFile(file2);
			}
		} else {
			file.delete();
		}
	}

}
