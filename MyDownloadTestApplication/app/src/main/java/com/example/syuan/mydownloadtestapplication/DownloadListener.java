package com.example.syuan.mydownloadtestapplication;

/**
 * @auther sy on 2017/7/10 09:08.
 * @Email 893110793@qq.com
 */

public interface DownloadListener {
    /*
    通知当前的下载进度
     */
    void onProgress(int progress);
    /*
    通知下载成功
     */
    void onSuccess();
    /*
   通知下载失败
    */
    void onFailed();
    /*
   通知下载暂停
    */
    void onPaused();
    /*
   通知下载取消
    */
    void onCanceled();
}
