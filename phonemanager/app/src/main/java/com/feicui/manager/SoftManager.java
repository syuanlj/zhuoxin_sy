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

	// ����ģʽ
	public static SoftManager getSoftManager(Context context) {
		if (softManager == null) {
			softManager = new SoftManager(context);
		}
		return softManager;
	}

	/**
	 * ��ȡ���ð�װ�������Ϣ
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
		// �õ���װ�����
		List<PackageInfo> packageInfo = pm
				.getInstalledPackages(PackageManager.GET_ACTIVITIES
						| PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo packageInfo2 : packageInfo) {
			// ʵ�������
			SoftList softList = new SoftList();
			// ��ð���
			String packageName = packageInfo2.packageName;
			softList.setPackageName(packageName);
			// �õ���һ�ΰ�װʱ��
			long longTime = packageInfo2.firstInstallTime;
			String time = CommonUtil.getStrTime(longTime);
			softList.setTime(time);
			// �õ�Ӧ��ͼ��
			Drawable icon = packageInfo2.applicationInfo.loadIcon(pm);
			softList.setIcon(icon);
			// �õ�Ӧ������
			String name = (String) packageInfo2.applicationInfo.loadLabel(pm);
			softList.setName(name);
			// ѡ��״̬Ϊfalse
			softList.setChear(false);
			// ��ӵ����м�����
			allList.add(softList);

			// �ж���ϵͳ�����û���
			if ((packageInfo2.applicationInfo.flags & 
					ApplicationInfo.FLAG_SYSTEM) == 1) {
				//�����ϵͳ�ģ���ӵ�ϵͳ
				sysList.add(softList);
			}else{
				userList.add(softList);
			}

		}

	}

}
