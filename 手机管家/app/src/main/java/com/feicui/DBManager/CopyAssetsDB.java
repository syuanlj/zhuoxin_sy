package com.feicui.DBManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 只负责数据库文件的复制
 * 
 * @author Administrator
 * 
 */
public class CopyAssetsDB {

	public static void copyDB(Context context) {
		try {
			// 先得到一个文件路径
			// 1复制文件到data/data/包名/数据库名.db
			String path = Environment.getDataDirectory() + "/data/"
					+ context.getPackageName() + "/commonnum.db";
			// 根据路径去创建数据库文件
			File file = new File(path);
			if (!file.exists()) {// 如果不存在
				// 新建文件
				file.createNewFile();
				// 得到assets文件下的数据库文件输入流
				InputStream is = context.getAssets().open("db/commonnum.db");
				// 拿到数据库文件的输出流
				FileOutputStream fos = new FileOutputStream(file);

				byte[] b = new byte[1024];

				int len = 0;
				while ((len = is.read(b)) != -1) {
					fos.write(b, 0, len);
				}
				fos.close();
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 复制垃圾清理数据库
	 * @param context
	 */
	public static void copyClearDB(Context context) {
		// 1复制文件到data/data/包名/数据库名.db
		String path = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/clearpath.db";
		try {
			//读取assets文件夹内的数据库文件,获取一个输入流
			InputStream is = context.getAssets().open("db/clearpath.db");
			//根据路径创建文件File对象
			File file=new File(path);
			if(!file.exists()){//如果文件不存在
				//新建文件
				file.createNewFile();
				//得到文件输出流，往文件中写入内容
				FileOutputStream fos=new FileOutputStream(file);
				
				//开始读写操作
				//创建一个byte[]数组用来保存每次读取到的内容
				byte[] b=new byte[1024];
				//一个int对象，用来保存读取到的字节长度
				int len=0;
				//循环读取
				while ((len=is.read(b))!=-1) {
					//将每次读取到的内容，写入文件
					fos.write(b, 0, len);
				}
				//读写操作完毕，关闭相关流
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
