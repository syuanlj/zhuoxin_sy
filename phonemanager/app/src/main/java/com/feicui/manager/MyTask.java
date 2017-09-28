package com.feicui.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.feicui.bean.FileInfo;
import com.feicui.utils.CommonUtil;
import com.feicui.utils.FileTypeUtil;

public class MyTask extends AsyncTask<File, Long, String> {

	public static final String TYPE_TXT = "txt";
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_VIDEO = "video";
	public static final String TYPE_AUDIO = "audio";
	public static final String TYPE_ZIP = "zip";
	public static final String TYPE_APK = "apk";
	public static final String OTHER = "";

	// 不同类型文件的文件大小
	public  long TYPE_TXT_SIZE = 0;
	public  long TYPE_AUDIO_SIZE = 0;
	public  long TYPE_VIDEO_SIZE = 0;
	public  long TYPE_IMAGE_SIZE = 0;

	public  long TYPE_ZIP_SIZE = 0;
	public  long TYPE_APK_SIZE = 0;
	public  long TYPE_OTHER_SIZE = 0;

	public  long TYPE_ALL_SIZE = 0;

	// 集合内的元素为不同类型文件的集合
	public static List<List<FileInfo>> list;
	// 当前文件所属的集合位置
	public  int currentPostion;
	// 当前文件的大小
	public  long currentFileSize;


	public MyTask() {
	
		if (list == null) {
			list = new ArrayList<List<FileInfo>>();
		}
		for (int i = 0; i < 7; i++) {
			list.add(new ArrayList());
		}
	}

	/**
	 * 在UI线程中运行，而且在执行doInBackground（）方法之前。 可以进行一些数据初始化工作
	 */
	@Override
	protected void onPreExecute() {
		//开始子线程之前，情况集合（不然数据重复添加）
		if(list!=null){
			for(int i=0;i<7;i++){
				list.get(i).clear();
			}
		}
	}

	/**
	 * 开启子线程，进行任务处理
	 */
	@Override
	protected String doInBackground(File... params) {
		File file = params[0];
		searchFile(file);
		return "ok";
	}

	/**
	 * 运行在UI线程，而且是执行doInBackground()方法之后，被调用。
	 * doInBackground()方法的返回值，会自动传递到该方法的参数中
	 */
	@Override
	protected void onPostExecute(String result) {
		if(progressListener!=null){
			progressListener.onResultListener(result);
		}
	}

	/**
	 * 运行在UI线程中 只有调用了publishProgress()，该方法才会被执行。
	 * 
	 */
	@Override
	protected void onProgressUpdate(Long... values) {

		if (progressListener != null) {
			progressListener.onProgressListener(values[0].longValue(), values[1].longValue(),
					values[2].intValue());
		}
	}

	// 实例化接口对象的方法
	public void setProgressListener(ProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	// 声明一个接口对象
	private ProgressListener progressListener;

	public interface ProgressListener {
		/**
		 * 用来传递当前进度
		 * 
		 * @param progress
		 */
		void onProgressListener(long oneTypeSize, long allTyepSize,
				int position);

		/**
		 * 用来传递当前进度
		 * 
		 * @param progress
		 */
		void onResultListener(String result);
	}

	public  void clear() {
		// 不同类型文件的文件大小
		TYPE_TXT_SIZE = 0;
		TYPE_AUDIO_SIZE = 0;
		TYPE_VIDEO_SIZE = 0;
		TYPE_IMAGE_SIZE = 0;

		TYPE_ZIP_SIZE = 0;
		TYPE_APK_SIZE = 0;
		TYPE_OTHER_SIZE = 0;

		TYPE_ALL_SIZE = 0;

		currentFileSize = 0;
		currentPostion = 0;
		for (int i = 0; i < 7; i++) {
			list.get(i).clear();
		}
	}
	
	
	/**
	 * 递归查询文件的方法
	 * @param file  文件
	 */
	private void searchFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {// 如果是文件夹
				File[] files = file.listFiles();
				for (File file2 : files) {
					searchFile(file2);
				}
			} else {// 如果是文件
				FileInfo info=new FileInfo();
				// 得到文件名称
				String fileName = file.getName();
				info.setFileName(fileName);
				// 得到文件图标的名称
				String icon_name = FileTypeUtil.getFileIconAndTypeName(file)[0];
				info.setIconID(icon_name);
				// 获得文件类型
				String fileType = FileTypeUtil.getFileIconAndTypeName(file)[1];
				
				// 获得文件时间
				String lastTime = CommonUtil.getStrTime(file.lastModified());
				info.setLastTime(lastTime);
				// 文件MIME类型
				String mime = FileTypeUtil.getMIMEType(file);
				
				// 文件大小
				String fileSize = CommonUtil.getFileSize(file.length());
				info.setFileSize(fileSize);
				
				info.setFile(file);
				
				switch (fileType) {
				case TYPE_TXT:
					TYPE_TXT_SIZE += file.length();
					currentFileSize = TYPE_TXT_SIZE;
					currentPostion = 0;

					break;
				case TYPE_IMAGE:
					TYPE_IMAGE_SIZE += file.length();
					currentFileSize = TYPE_IMAGE_SIZE;
					currentPostion = 1;

					break;
				case TYPE_VIDEO:
					TYPE_VIDEO_SIZE += file.length();
					currentFileSize = TYPE_VIDEO_SIZE;
					currentPostion = 2;

					break;
				case TYPE_AUDIO:
					TYPE_AUDIO_SIZE += file.length();
					currentFileSize = TYPE_AUDIO_SIZE;
					currentPostion = 3;

					break;

				case TYPE_ZIP:
					TYPE_ZIP_SIZE += file.length();
					currentFileSize = TYPE_ZIP_SIZE;
					currentPostion = 4;

					break;
				case TYPE_APK:
					TYPE_APK_SIZE += file.length();
					currentFileSize = TYPE_APK_SIZE;
					currentPostion = 5;

					break;
				default:
					TYPE_OTHER_SIZE += file.length();
					currentFileSize = TYPE_OTHER_SIZE;
					currentPostion = 6;

					break;
				}
				TYPE_ALL_SIZE = TYPE_OTHER_SIZE + TYPE_APK_SIZE + TYPE_ZIP_SIZE
						+ TYPE_AUDIO_SIZE + TYPE_VIDEO_SIZE + TYPE_IMAGE_SIZE
						+ TYPE_TXT_SIZE;
				//将实体类对象添加到集合中
				list.get(currentPostion).add(info);
				// 不断的更新界面数据，每一种文件的总大小
				String allTyepSize = CommonUtil.getFileSize(TYPE_ALL_SIZE);
				String oneTypeSize = CommonUtil.getFileSize(currentFileSize);
				publishProgress(new Long(currentFileSize), new Long(TYPE_ALL_SIZE),
						new Long(currentPostion));
			}
		}
	}

	@Override
	protected void onCancelled(String result) {
		super.onCancelled(result);
		if(progressListener!=null){
			progressListener.onResultListener(result);
		}
		
		
	}
	
	
}
