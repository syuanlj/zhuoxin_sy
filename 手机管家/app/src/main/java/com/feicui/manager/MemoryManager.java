package com.feicui.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

/**
 * �ڴ���Ϣ��ȡ
 */
public class MemoryManager {

	/** ��ȡ�ֻ�����sdcard·��, Ϊnull��ʾ�� */
	public static String getPhoneInSDCardPath() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return null;
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/** ��ȡ�ֻ�����sdcard·��, Ϊnull��ʾ�� */
	public static String getPhoneOutSDCardPath() {
		Map<String, String> map = System.getenv();
		if (map.containsKey("SECONDARY_STORAGE")) {
			String paths = map.get("SECONDARY_STORAGE");
			// /storage/extSdCard
			String path[] = paths.split(":");
			if (path == null || path.length <= 0) {
				return null;
			}
			return path[0];
		}
		return null;
	}

	/** �豸���ô洢SDCardȫ����С ��λB , ��û�����ÿ�ʱ,��СΪ0 */
	public static long getPhoneOutSDCardSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long blockCount = stat.getBlockCount();
			return blockCount * blockSize;
		} catch (Exception e) {
			Toast.makeText(context, "���ô洢���쳣", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}

	/** �豸���ô洢SDCard���д�С ��λB */
	public static long getPhoneOutSDCardFreeSize(Context context) {
		try {
			File path = new File(getPhoneOutSDCardPath());
			if (path == null) {
				return 0;
			}
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availaBlock = stat.getAvailableBlocks();
			return availaBlock * blockSize;
		} catch (Exception e) {
			Toast.makeText(context, "���ô洢���쳣", Toast.LENGTH_SHORT).show();
			return 0;
		}
	}

	/** �豸����洢ȫ����С ��λB */
	public static long getPhoneSelfSize() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + cacheFileSize;
	}

	/** �豸����洢���д�С ��λB */
	public static long getPhoneSelfFreeSize() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + cacheFileSize;
	}

	/** �豸���ô洢��ȫ����С(�ֻ��Դ�32G�洢�ռ�?) ��λB */
	public static long getPhoneSelfSDCardSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		return blockCount * blockSize;
	}

	/** �豸���ô洢���ռ��С(�ֻ��Դ�32G�洢�ռ�?) ��λB */
	public static long getPhoneSelfSDCardFreeSize() {
		String sdcardState = Environment.getExternalStorageState();
		if (!sdcardState.equals(Environment.MEDIA_MOUNTED)) {
			return 0;
		}
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availaBlock = stat.getAvailableBlocks();
		return availaBlock * blockSize;
	}

	/**��ȡ�ֻ��ܴ洢��С*/
	public static long getPhoneAllSize() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getBlockCount();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long dataFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getBlockCount();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + dataFileSize + cacheFileSize;
	}
	/**��ȡ�ֻ������ô洢��С*/
	public static long getPhoneAllFreeSize() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long blockCount = stat.getAvailableBlocks();
		long rootFileSize = blockCount * blockSize;

		path = Environment.getDataDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long dataFileSize = blockCount * blockSize;

		path = Environment.getDownloadCacheDirectory();
		stat = new StatFs(path.getPath());
		blockSize = stat.getBlockSize();
		blockCount = stat.getAvailableBlocks();
		long cacheFileSize = blockCount * blockSize;

		return rootFileSize + dataFileSize + cacheFileSize;
	}

	/** �豸���������ڴ� ��λB */
	public static long getPhoneFreeRamMemory(Context context) {
		MemoryInfo info = new MemoryInfo();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(info);
		return info.availMem;
	}

	/** �豸���������ڴ� ��λB */
	public static long getPhoneTotalRamMemory() {
		try {
			FileReader fr = new FileReader("/proc/meminfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split("\\s+");
			return Long.valueOf(array[1]) * 1024; // ԭΪkb, תΪb
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
