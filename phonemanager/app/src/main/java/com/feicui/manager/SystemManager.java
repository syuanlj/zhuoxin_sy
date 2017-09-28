package com.feicui.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 系统信息监测�? (未使�?)
 */
public class SystemManager {

	public static final String basicInfos[] = { "设备型号:", "系统版本:", "手机串号:", "运营�?:", "是否ROOT:" };
	public static final String CPUInfos[] = { "CPU型号:", "CPU核心�?:", "�?高频�?:", "�?低频�?:", "当前频率:" };
	public static final String resolutionInfos[] = { "摄像头像�?:", "照片�?大尺�?:", "闪光�?:" };
	public static final String pixelInfos[] = { "屏幕分辨�?:", "像素密度:", "多点触控:" };
	public static final String WIFIInfos[] = { "WIFI连接�?:", "WIFI地址:", "WIFI连接速度:", "MAC地址:", "蓝牙状�??:" };
	private Context mContext = null;
	private TelephonyManager tm = null;
	private WifiManager wm = null;

	public SystemManager(Context context) {
		mContext = context;
		tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 监测基本信息
	 */
	public boolean BasicInfo(final String[] datas) {
		if (datas == null || datas.length < basicInfos.length) {
			return false;
		}
		datas[0] = basicInfos[0] + Build.MODEL;
		datas[1] = basicInfos[1] + Build.VERSION.RELEASE;
		datas[2] = basicInfos[2] + (tm.getDeviceId() == null ? "�?" : tm.getDeviceId());
		datas[3] = basicInfos[3] + getProvidersName();
		datas[4] = basicInfos[4] + (isRoot() ? "�?" : "�?");
		return true;
	}

	/**
	 * 监测CPU信息
	 */
	public boolean CPUInfo(final String[] datas) {
		if (datas == null || datas.length < CPUInfos.length) {
			return false;
		}
		datas[0] = CPUInfos[0] + getCpuName();
		datas[1] = CPUInfos[1] + getNumCores();
		datas[2] = CPUInfos[2] + getMaxCpuFreq() + "KHZ";
		datas[3] = CPUInfos[3] + getMinCpuFreq() + "KHZ";
		datas[4] = CPUInfos[4] + getCurCpuFreq() + "KHZ";
		return true;
	}

	/**
	 * 监测分辨率信�?
	 */
	public boolean resolutionInfo(final String[] datas) {
		if (datas == null || datas.length < resolutionInfos.length) {
			return false;
		}
		datas[0] = resolutionInfos[0] + getCameraResolution();
		datas[1] = resolutionInfos[1] + getMaxPhotoSize();
		datas[2] = resolutionInfos[2] + (getFlashMode() == null ? "�?" : getFlashMode());
		return true;
	}

	/**
	 * 监测像素信息
	 */
	public boolean pixelInfo(final String[] datas) {
		if (datas == null || datas.length < pixelInfos.length) {
			return false;
		}
		datas[0] = pixelInfos[0] + getResolution();
		datas[1] = pixelInfos[1] + getPixDensity();
		datas[2] = pixelInfos[2] + (isSupportMultiTouch() ? "支持" : "不支�?");
		return true;
	}

	/**
	 * 监测WIFI信息
	 */
	public boolean WIFIInfo(final String[] datas) {
		if (datas == null || datas.length < WIFIInfos.length) {
			return false;
		}
		WifiInfo wifiInfo = wm.getConnectionInfo();
		datas[0] = WIFIInfos[0] + (wifiInfo == null ? "未连�?" : wifiInfo.getSSID());
		datas[1] = WIFIInfos[1] + (wifiInfo == null ? "�?" : wifiInfo.getIpAddress());
		datas[2] = WIFIInfos[2] + (wifiInfo == null ? "0" : wifiInfo.getLinkSpeed());
		datas[3] = WIFIInfos[3] + (wifiInfo == null ? "�?" : wifiInfo.getMacAddress());
		datas[4] = WIFIInfos[4] + getBlueToothState();
		return true;
	}

	/**
	 * Role:Telecom service providers获取手机服务商信�? <BR>
	 * �?要加入权�?<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
	 */
	public String getProvidersName() {
		String ProvidersName = null;
		String IMSI = tm.getSubscriberId();
		if (IMSI == null) {
			return "�?";
		}
		// IMSI号前�?3�?460是国家，紧接�?后面2�?00 02是中国移动，01是中国联通，03是中国电信�??
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联�??";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		} else {
			return "�?";
		}
		return ProvidersName;
	}

	/**
	 * 判断当前手机是否有ROOT权限
	 * 
	 * @return
	 */
	public boolean isRoot() {
		boolean bool = false;

		try {
			if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {

		}
		return bool;
	}

	// 获取CPU�?大频率（单位KHZ�?
	// "/system/bin/cat" 命令�?
	// "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储�?大频率的文件的路�?
	public String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// 获取CPU�?小频率（单位KHZ�?
	public String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}

	// 实时获取CPU当前频率（单位KHZ�?
	public String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取CPU名字
	public static String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			for (int i = 0; i < array.length; i++) {
			}
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the number of cores available in this device, across all processors.
	 * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
	 * 
	 * @return The number of cores, or 1 if failed to get result
	 */
	private int getNumCores() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			// Default to return 1 core
			return 1;
		}
	}

	/**
	 * 获取手机分辨�?
	 */
	public String getResolution() {
		String resolution = "";
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		resolution = metrics.widthPixels + "*" + metrics.heightPixels;
		return resolution;
	}

	/**
	 * 获取照片�?大分辨率
	 */
	public String getMaxPhotoSize() {
		String maxSize = "";
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		List<Size> sizes = parameters.getSupportedPictureSizes();
		Size size = null;
		for (Size s : sizes) {
			if (size == null) {
				size = s;
			} else if (size.height * s.width < s.height * s.width) {
				size = s;
			}
		}
		maxSize = size.width + "*" + size.height;
		camera.release();
		return maxSize;
	}

	/**
	 * 获取相机�?大尺�?
	 */
	public String getCameraResolution() {
		String cameraResolution = "";
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		List<Size> sizes = parameters.getSupportedPictureSizes();
		Size size = null;
		for (Size s : sizes) {
			if (size == null) {
				size = s;
			} else if (size.height * s.width < s.height * s.width) {
				size = s;
			}
		}
		cameraResolution = (size.width * size.height) / 10000 + "万像�?";
		camera.release();
		return cameraResolution;
	}

	/**
	 * 获取闪光灯状�?
	 */
	public String getFlashMode() {
		String flashMode = "";
		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		flashMode = parameters.getFlashMode();
		camera.release();
		return flashMode;
	}

	/**
	 * 获取像素密度
	 */
	public float getPixDensity() {
		float density = 0;
		density = mContext.getResources().getDisplayMetrics().density;
		return density;
	}

	/**
	 * 判断设备是否支持多点触控
	 */
	public boolean isSupportMultiTouch() {
		PackageManager pm = mContext.getPackageManager();
		boolean isSupportMultiTouch = pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		return isSupportMultiTouch;
	}

	/**
	 * 获取蓝牙连接状�??
	 */
	public String getBlueToothState() {
		BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bAdapter == null) {
			return "设备不支持蓝�?";
		}
		int state = bAdapter.getState();
		switch (state) {
		case BluetoothAdapter.STATE_TURNING_OFF:
			return "蓝牙关闭�?";
		case BluetoothAdapter.STATE_TURNING_ON:
			return "蓝牙�?启中";
		case BluetoothAdapter.STATE_OFF:
			return "蓝牙关闭";
		case BluetoothAdapter.STATE_ON:
			return "蓝牙�?�?";
		}
		return "未知";
	}

	/** 设备品牌(moto?) */
	public static String getPhoneName() {
		return Build.BRAND;
	}

	/** 设备型号名称(xt910) */
	public static String getPhoneModelName() {
		// 带国家用 PRODUCT
		return Build.MODEL + " Android" + getPhoneSystemVersion();
	}

	/** 设备系统版本�? (4.1.2?) */
	public static String getPhoneSystemVersion() {
		return Build.VERSION.RELEASE;
	}
}