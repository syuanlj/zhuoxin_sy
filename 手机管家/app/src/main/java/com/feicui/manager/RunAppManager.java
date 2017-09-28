package com.feicui.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Debug.MemoryInfo;

import com.feicui.bean.RunningAppInfo;
import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

/**
 * 可以查找当前设备上运行的进程信息，杀死进程的方法
 * 
 * @author Administrator
 * 
 */
public class RunAppManager {

	// 存储系统进程
	private static List<RunningAppInfo> sysList;
	private static List<RunningAppInfo> userList;
	private static RunAppManager runAppManager;
	// ActivityManager
	private static ActivityManager am;
	// packageManager
	private static PackageManager pm;

	// 封装构造方法，实现单列模式
	private RunAppManager(Context context) {
		sysList = new ArrayList<RunningAppInfo>();
		userList = new ArrayList<RunningAppInfo>();

		// 实例化am
		am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		// 实例化pm
		pm = context.getPackageManager();

	}

	// 提供共有方法，获取单列对象
	public static RunAppManager getRnAppManager(Context context) {

		if (runAppManager == null) {// 如果对象不为null
			runAppManager = new RunAppManager(context);
		}
		// 返回对象
		return runAppManager;
	}

	/**
	 * 获取进程信息，并分类添加到集合中
	 */
	public static void getRunApp() {
		if (sysList != null) {
			sysList.clear();
		}
		if (userList != null) {
			userList.clear();
		}
		if(Build.VERSION.SDK_INT<21){//手机版本小于5.0
			forblow21();
		}else{
			forbigger21();
		}
		
		
	}

	/**
	 * 手机系统大于21（5.0）的获取进程信息方法
	 */
	private static void forbigger21() {
		//获取系统所有进程(和5.0之下唯一的区别)
		List<AndroidAppProcess> raps = ProcessManager.getRunningAppProcesses();
		for (AndroidAppProcess androidAppProcess : raps) {
			//实例化实体类对象
			RunningAppInfo info = new RunningAppInfo();
			//得到包名设置包名（进程名称）
			String packageName=androidAppProcess.name;
			info.setPackageName(packageName);
			
			MemoryInfo[] memory = am.getProcessMemoryInfo(new int[]{androidAppProcess.pid});
			//获取进程内存
			int size = memory[0].getTotalPrivateDirty()*1024;
			info.setSize(size);
			
			try {
				ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
				//得到应用图标
				Drawable icon = appInfo.loadIcon(pm);
				info.setIcon(icon);
				//得到应用名称（lableNmae）
				String lableName = (String) appInfo.loadLabel(pm);
				info.setLableName(lableName);
				
				if((appInfo.flags&ApplicationInfo.FLAG_SYSTEM)==1){
					//如果是系统进程
					info.setSystem(true);
					info.setClear(false);
					sysList.add(info);
				}else{
					info.setSystem(false);
					info.setClear(true);
					userList.add(info);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * 手机系统小于21（5.0）的获取进程信息方法
	 */
	private static void forblow21() {
		// 获取系统所有的进程
		List<RunningAppProcessInfo> raps = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : raps) {
			//实例化实体类对象
			RunningAppInfo info = new RunningAppInfo();

			int pid = runningAppProcessInfo.pid;
			MemoryInfo[] memory = am.getProcessMemoryInfo(new int[] { pid });
			// 得到的进程大小单位为kb，需要转为b

			int size = memory[0].getTotalPrivateDirty() * 1024;
			info.setSize(size);
			// 得到包名
			String packageName = runningAppProcessInfo.processName;
			info.setPackageName(packageName);
			try {

				ApplicationInfo appInfo = pm.getApplicationInfo(packageName,
						PackageManager.GET_META_DATA
								| PackageManager.GET_SHARED_LIBRARY_FILES
								| PackageManager.GET_UNINSTALLED_PACKAGES);
				// 获取应用图标
				Drawable icon = appInfo.loadIcon(pm);
				info.setIcon(icon);
				// 获取应用名称
				String lableName = (String) appInfo.loadLabel(pm);
				info.setLableName(lableName);

				if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {// 如果是系统进程
					info.setClear(false);
					info.setSystem(true);
					sysList.add(info);
				} else {// 用户进程
					info.setClear(true);
					info.setSystem(false);
					userList.add(info);
				}

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static List<RunningAppInfo> getSysList() {
		return sysList;
	}

	public static List<RunningAppInfo> getUserList() {
		return userList;
	}

	/**
	 * 杀死单个进程
	 * @param context
	 * @param packageName
	 */
	public static void killProcess(Context context, String packageName) {
		if (am == null) {
			// 只有获得本类对象之后，类中全局变量才能被实例化
			getRnAppManager(context);
		}
		am.killBackgroundProcesses(packageName);
	}

	/**
	 * 中界面中一键加速
	 */
	public static void killAllUser(Context context){
		//获取进程信息前，必须要保证调用了构造方法（必须要保证这个类实例化）
		getRnAppManager(context).getRunApp();
		List<RunningAppInfo> list = getUserList();
		for (RunningAppInfo runningAppInfo : list) {
			am.killBackgroundProcesses(runningAppInfo.getPackageName());
		}
	}
}
