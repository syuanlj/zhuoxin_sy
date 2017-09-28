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
 * ���Բ��ҵ�ǰ�豸�����еĽ�����Ϣ��ɱ�����̵ķ���
 * 
 * @author Administrator
 * 
 */
public class RunAppManager {

	// �洢ϵͳ����
	private static List<RunningAppInfo> sysList;
	private static List<RunningAppInfo> userList;
	private static RunAppManager runAppManager;
	// ActivityManager
	private static ActivityManager am;
	// packageManager
	private static PackageManager pm;

	// ��װ���췽����ʵ�ֵ���ģʽ
	private RunAppManager(Context context) {
		sysList = new ArrayList<RunningAppInfo>();
		userList = new ArrayList<RunningAppInfo>();

		// ʵ����am
		am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		// ʵ����pm
		pm = context.getPackageManager();

	}

	// �ṩ���з�������ȡ���ж���
	public static RunAppManager getRnAppManager(Context context) {

		if (runAppManager == null) {// �������Ϊnull
			runAppManager = new RunAppManager(context);
		}
		// ���ض���
		return runAppManager;
	}

	/**
	 * ��ȡ������Ϣ����������ӵ�������
	 */
	public static void getRunApp() {
		if (sysList != null) {
			sysList.clear();
		}
		if (userList != null) {
			userList.clear();
		}
		if(Build.VERSION.SDK_INT<21){//�ֻ��汾С��5.0
			forblow21();
		}else{
			forbigger21();
		}
		
		
	}

	/**
	 * �ֻ�ϵͳ����21��5.0���Ļ�ȡ������Ϣ����
	 */
	private static void forbigger21() {
		//��ȡϵͳ���н���(��5.0֮��Ψһ������)
		List<AndroidAppProcess> raps = ProcessManager.getRunningAppProcesses();
		for (AndroidAppProcess androidAppProcess : raps) {
			//ʵ����ʵ�������
			RunningAppInfo info = new RunningAppInfo();
			//�õ��������ð������������ƣ�
			String packageName=androidAppProcess.name;
			info.setPackageName(packageName);
			
			MemoryInfo[] memory = am.getProcessMemoryInfo(new int[]{androidAppProcess.pid});
			//��ȡ�����ڴ�
			int size = memory[0].getTotalPrivateDirty()*1024;
			info.setSize(size);
			
			try {
				ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
				//�õ�Ӧ��ͼ��
				Drawable icon = appInfo.loadIcon(pm);
				info.setIcon(icon);
				//�õ�Ӧ�����ƣ�lableNmae��
				String lableName = (String) appInfo.loadLabel(pm);
				info.setLableName(lableName);
				
				if((appInfo.flags&ApplicationInfo.FLAG_SYSTEM)==1){
					//�����ϵͳ����
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
	 * �ֻ�ϵͳС��21��5.0���Ļ�ȡ������Ϣ����
	 */
	private static void forblow21() {
		// ��ȡϵͳ���еĽ���
		List<RunningAppProcessInfo> raps = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : raps) {
			//ʵ����ʵ�������
			RunningAppInfo info = new RunningAppInfo();

			int pid = runningAppProcessInfo.pid;
			MemoryInfo[] memory = am.getProcessMemoryInfo(new int[] { pid });
			// �õ��Ľ��̴�С��λΪkb����ҪתΪb

			int size = memory[0].getTotalPrivateDirty() * 1024;
			info.setSize(size);
			// �õ�����
			String packageName = runningAppProcessInfo.processName;
			info.setPackageName(packageName);
			try {

				ApplicationInfo appInfo = pm.getApplicationInfo(packageName,
						PackageManager.GET_META_DATA
								| PackageManager.GET_SHARED_LIBRARY_FILES
								| PackageManager.GET_UNINSTALLED_PACKAGES);
				// ��ȡӦ��ͼ��
				Drawable icon = appInfo.loadIcon(pm);
				info.setIcon(icon);
				// ��ȡӦ������
				String lableName = (String) appInfo.loadLabel(pm);
				info.setLableName(lableName);

				if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {// �����ϵͳ����
					info.setClear(false);
					info.setSystem(true);
					sysList.add(info);
				} else {// �û�����
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
	 * ɱ����������
	 * @param context
	 * @param packageName
	 */
	public static void killProcess(Context context, String packageName) {
		if (am == null) {
			// ֻ�л�ñ������֮������ȫ�ֱ������ܱ�ʵ����
			getRnAppManager(context);
		}
		am.killBackgroundProcesses(packageName);
	}

	/**
	 * �н�����һ������
	 */
	public static void killAllUser(Context context){
		//��ȡ������Ϣǰ������Ҫ��֤�����˹��췽��������Ҫ��֤�����ʵ������
		getRnAppManager(context).getRunApp();
		List<RunningAppInfo> list = getUserList();
		for (RunningAppInfo runningAppInfo : list) {
			am.killBackgroundProcesses(runningAppInfo.getPackageName());
		}
	}
}
