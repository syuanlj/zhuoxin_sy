package com.feicui.DBManager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.feicui.bean.TelList;
import com.feicui.bean.TelType;

/**
 * 通讯大全界面需要获得的信息
 * 
 * @author Administrator
 * 
 */
public class SelectForTel {

	/**
	 * 获得数据库classlist表中所有记录
	 * 
	 * @param context
	 * @return
	 */
	public static List<TelType> getTelType(Context context) {
		List<TelType> telTypes = null;
		try {
			// 为了确保数据库文件已经复制完毕，必须要先调用复制的方法
			CopyAssetsDB.copyDB(context);
			telTypes = new ArrayList<TelType>();
			// 1复制文件到data/data/包名/数据库名.db
			String path = Environment.getDataDirectory() + "/data/"
					+ context.getPackageName() + "/commonnum.db";
			// 去打开已经复制好的数据库
			SQLiteDatabase db = context.openOrCreateDatabase(path, 0, null);
			String sql = "select * from classlist";
			// 执行sql语句，获得结果集游标
			Cursor c = db.rawQuery(sql, null);

			TelType tt = null;
			if (c.moveToFirst()) {
				do {
					tt = new TelType();
					String name = c.getString(c.getColumnIndex("name"));
					int idx = c.getInt(c.getColumnIndex("idx"));
					tt.setName(name);
					tt.setIdx(idx);

					telTypes.add(tt);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return telTypes;
	}

	public static List<TelList> getTelList(Context context,TelType info) {
		List<TelList> list=new ArrayList<TelList>();
		
		// 1到data/data/包名/数据库名.db
		String path = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/commonnum.db";
		// 去打开已经复制好的数据库
		SQLiteDatabase db = context.openOrCreateDatabase(path, 0, null);
		String sql="select name,number from table"+info.getIdx();
		// 执行sql语句，获得结果集游标
		Cursor c = db.rawQuery(sql, null);
		TelList tl=null;
		if(c.moveToFirst()){
			do{
				tl=new TelList();
				String name=c.getString(c.getColumnIndex("name"));
				String number=c.getString(c.getColumnIndex("number"));
				tl.setName(name);
				tl.setNumber(number);
				list.add(tl);
			}while(c.moveToNext());
		}
		return list;

	}

}
