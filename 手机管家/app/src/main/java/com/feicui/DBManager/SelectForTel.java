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
 * ͨѶ��ȫ������Ҫ��õ���Ϣ
 * 
 * @author Administrator
 * 
 */
public class SelectForTel {

	/**
	 * ������ݿ�classlist�������м�¼
	 * 
	 * @param context
	 * @return
	 */
	public static List<TelType> getTelType(Context context) {
		List<TelType> telTypes = null;
		try {
			// Ϊ��ȷ�����ݿ��ļ��Ѿ�������ϣ�����Ҫ�ȵ��ø��Ƶķ���
			CopyAssetsDB.copyDB(context);
			telTypes = new ArrayList<TelType>();
			// 1�����ļ���data/data/����/���ݿ���.db
			String path = Environment.getDataDirectory() + "/data/"
					+ context.getPackageName() + "/commonnum.db";
			// ȥ���Ѿ����ƺõ����ݿ�
			SQLiteDatabase db = context.openOrCreateDatabase(path, 0, null);
			String sql = "select * from classlist";
			// ִ��sql��䣬��ý�����α�
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
		
		// 1��data/data/����/���ݿ���.db
		String path = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/commonnum.db";
		// ȥ���Ѿ����ƺõ����ݿ�
		SQLiteDatabase db = context.openOrCreateDatabase(path, 0, null);
		String sql="select name,number from table"+info.getIdx();
		// ִ��sql��䣬��ý�����α�
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
