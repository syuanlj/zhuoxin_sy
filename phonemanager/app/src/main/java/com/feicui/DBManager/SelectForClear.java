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
 * ��ȡ��������Ļ����ļ�
 * 
 * @author Administrator
 * 
 */
public class SelectForClear {
	public static long allSize = 0;

	public static List<ClearInfo> getClearList(Context context)
			 {

		// ����һ���ӿڶ���������������ݿ��л�ȡ����ʵ�������
		List<ClearInfo> infoList = new ArrayList<>();
		// ���ÿ������ݿ�ķ�����ȷ�����ݿ��ļ��Ѿ����������ⴴ��һ���յ����ݿ�
		CopyAssetsDB.copyClearDB(context);
		// 1�����ļ���data/data/����/���ݿ���.db
		String path = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/clearpath.db";
		// �򿪻����½�һ�����ݿ⣬�����ָ��·�������ݿⲻ���ڣ��ʹ���һ���µģ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
		// ��ѯ��䣺��ѯ�������еļ�¼
		String sql = "select * from softdetail";
		// ��ѯ���ݿ�ļ�¼
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			do {
				// �õ���ͬ�еĶ�Ӧֵ
				String softChinesename = c.getString(c
						.getColumnIndex("softChinesename"));
				String apkname = c.getString(c.getColumnIndex("apkname"));
				String filepath = c.getString(c.getColumnIndex("filepath"));
				// ��ò�ͬ����洢�ļ���·��
				String filePath = MemoryManager.getPhoneInSDCardPath()
						+ filepath;
				// ����·���ҵ��ļ�File����
				File file = new File(filePath);

				if (file.exists()) {// ����ļ�����
					// ��ȡ�ļ����ļ��У����ܴ�С
					long fileSize = getFileSize(file);
					allSize += fileSize;
					// Ӧ��ͼ��
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
	 * ��ȡ�ļ����ܴ�С
	 * 
	 * @param file
	 * @return
	 */
	private static long getFileSize(File file) {
		long size = 0;
		if (file.isDirectory()) {// ������ļ���
			// ����ļ����������ļ�������
			File[] files = file.listFiles();
			if (files != null) {// ������鲻Ϊ��
				for (File file2 : files) {// �����ݹ�
					size += getFileSize(file2);
				}
			}
		} else {
			size += file.length();
		}

		return size;

	}

	/**
	 * ɾ���ļ��У�ע�⣺�ļ��������Ϊ�գ�û��ɾ��
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
