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

	// ��ͬ�����ļ����ļ���С
	public  long TYPE_TXT_SIZE = 0;
	public  long TYPE_AUDIO_SIZE = 0;
	public  long TYPE_VIDEO_SIZE = 0;
	public  long TYPE_IMAGE_SIZE = 0;

	public  long TYPE_ZIP_SIZE = 0;
	public  long TYPE_APK_SIZE = 0;
	public  long TYPE_OTHER_SIZE = 0;

	public  long TYPE_ALL_SIZE = 0;

	// �����ڵ�Ԫ��Ϊ��ͬ�����ļ��ļ���
	public static List<List<FileInfo>> list;
	// ��ǰ�ļ������ļ���λ��
	public  int currentPostion;
	// ��ǰ�ļ��Ĵ�С
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
	 * ��UI�߳������У�������ִ��doInBackground��������֮ǰ�� ���Խ���һЩ���ݳ�ʼ������
	 */
	@Override
	protected void onPreExecute() {
		//��ʼ���߳�֮ǰ��������ϣ���Ȼ�����ظ���ӣ�
		if(list!=null){
			for(int i=0;i<7;i++){
				list.get(i).clear();
			}
		}
	}

	/**
	 * �������̣߳�����������
	 */
	@Override
	protected String doInBackground(File... params) {
		File file = params[0];
		searchFile(file);
		return "ok";
	}

	/**
	 * ������UI�̣߳�������ִ��doInBackground()����֮�󣬱����á�
	 * doInBackground()�����ķ���ֵ�����Զ����ݵ��÷����Ĳ�����
	 */
	@Override
	protected void onPostExecute(String result) {
		if(progressListener!=null){
			progressListener.onResultListener(result);
		}
	}

	/**
	 * ������UI�߳��� ֻ�е�����publishProgress()���÷����Żᱻִ�С�
	 * 
	 */
	@Override
	protected void onProgressUpdate(Long... values) {

		if (progressListener != null) {
			progressListener.onProgressListener(values[0].longValue(), values[1].longValue(),
					values[2].intValue());
		}
	}

	// ʵ�����ӿڶ���ķ���
	public void setProgressListener(ProgressListener progressListener) {
		this.progressListener = progressListener;
	}

	// ����һ���ӿڶ���
	private ProgressListener progressListener;

	public interface ProgressListener {
		/**
		 * �������ݵ�ǰ����
		 * 
		 * @param progress
		 */
		void onProgressListener(long oneTypeSize, long allTyepSize,
				int position);

		/**
		 * �������ݵ�ǰ����
		 * 
		 * @param progress
		 */
		void onResultListener(String result);
	}

	public  void clear() {
		// ��ͬ�����ļ����ļ���С
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
	 * �ݹ��ѯ�ļ��ķ���
	 * @param file  �ļ�
	 */
	private void searchFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {// ������ļ���
				File[] files = file.listFiles();
				for (File file2 : files) {
					searchFile(file2);
				}
			} else {// ������ļ�
				FileInfo info=new FileInfo();
				// �õ��ļ�����
				String fileName = file.getName();
				info.setFileName(fileName);
				// �õ��ļ�ͼ�������
				String icon_name = FileTypeUtil.getFileIconAndTypeName(file)[0];
				info.setIconID(icon_name);
				// ����ļ�����
				String fileType = FileTypeUtil.getFileIconAndTypeName(file)[1];
				
				// ����ļ�ʱ��
				String lastTime = CommonUtil.getStrTime(file.lastModified());
				info.setLastTime(lastTime);
				// �ļ�MIME����
				String mime = FileTypeUtil.getMIMEType(file);
				
				// �ļ���С
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
				//��ʵ���������ӵ�������
				list.get(currentPostion).add(info);
				// ���ϵĸ��½������ݣ�ÿһ���ļ����ܴ�С
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
