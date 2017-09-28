package com.feicui.manager;

import java.util.ArrayList;
import java.util.List;

import com.feicui.bean.SoftList;
import com.feicui.utils.CommonUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class SoftManager {

	private static SoftManager softManager;
	private static PackageManager pm;
	private static List<SoftList> allList;
	private static List<SoftList> sysList;
	private static List<SoftList> userList;


	public static List<SoftList> getAllList() {
		return allList;
	}

	public static List<SoftList> getSysList() {
		return sysList;
	}

	public static List<SoftList> getUserList() {
		return userList;
	}

	private SoftManager(Context context) {
		pm = context.getPackageManager();
		allList = new ArrayList<SoftList>();
		sysList = new ArrayList<SoftList>();
		userList = new ArrayList<SoftList>();
	}

	// 单列模式
	public static SoftManager getSoftManager(Context context) {
		if (softManager == null) {
			softManager = new SoftManager(context);
		}
		return softManager;
	}

	/**
	 * 获取设置安装的软件信息
	 */
	public static void getSoft() {
		if(allList!=null){
			allList.clear();
		}
		if(sysList!=null){
			sysList.clear();
		}
		if(userList!=null){
			userList.clear();
		}
		// 得到安装的软件
		List<PackageInfo> packageInfo = pm
				.getInstalledPackages(PackageManager.GET_ACTIVITIES
						| PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo packageInfo2 : packageInfo) {
			// 实体类对象
			SoftList softList = new SoftList();
			// 获得包名
			String packageName = packageInfo2.packageName;
			softList.setPackageName(packageName);
			// 得到第一次安装时间
			long longTime = packageInfo2.firstInstallTime;
			String time = CommonUtil.getStrTime(longTime);
			softList.setTime(time);
			// 得到应用图标
			Drawable icon = packageInfo2.applicationInfo.loadIcon(pm);
			softList.setIcon(icon);
			// 得到应用名称
			String name = (String) packageInfo2.applicationInfo.loadLabel(pm);
			softList.setName(name);
			// 选中状态为false
			softList.setChear(false);
			// 添加到所有集合中
			allList.add(softList);

			// 判断是系统还是用户的
			if ((packageInfo2.applicationInfo.flags & 
					ApplicationInfo.FLAG_SYSTEM) == 1) {
				//如果是系统的，添加到系统
				sysList.add(softList);
			}else{
				userList.add(softList);
			}

		}

	}

}
