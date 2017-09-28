package com.example.sy.mediaapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CameraAlbumActivity extends AppCompatActivity {

    @BindView(R.id.camera_bt)
    Button cameraBt;
    @BindView(R.id.camera_iv)
    ImageView cameraIv;

    private Unbinder bk;
    private Uri imageUri;//拍照后的图片的Uri
    public static final int TAKE_PHOTO = 1;//请求码，用于相机跳转
    private MediaPlayer mediaPlayer=new MediaPlayer();//实例化音频播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_album);
        bk = ButterKnife.bind(this);//绑定butterknife
        init();
    }

    private void init() {
        //判断当前Activity是否获得手机读取权限，为了适应Android4.4以前的版本
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //参数类型：                     当前Activity，所需要获取的权限,请求码
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }else {
            initMedioPlayer();
        }
    }

    //初始化mediaplayer
    private void initMedioPlayer() {
        //获取音频存放路径
        File file=new File(Environment.getExternalStorageDirectory(),"music.mp3");
        try {
            //向mediaplayer中放入音频资源
            mediaPlayer.setDataSource(file.getPath());
            //准备播放音频
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case   1:
                //判断授权结果是否为空 ，若不为空则进一步判断所获得的权限是否是所需要的权限（这里指读写权限）
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initMedioPlayer();//获得权限成功，初始化mediaplayer
                }else {
                    //失败吐司并关闭请求
                    Toast.makeText(this,"no permission",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @OnClick({R.id.camera_bt, R.id.camera_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.camera_bt:
//                创建文件夹
                File outputImage = new File(getExternalCacheDir() , "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();//判断该文件是否存在，若存在则删除重新建立
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                将file转换成Uri
                if (Build.VERSION.SDK_INT >= 24) {//判断Android版本是否低于7.0

                    imageUri = FileProvider.getUriForFile(this,
                            "com.example.cameraalbumtest.fileprovider", outputImage);
                } else {
                    //版本低于7.0
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//intent隐式跳转
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//?
                startActivityForResult(intent, TAKE_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {//判断相机是否完成拍照
                    try {
                        //获取相机拍下的照片
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        cameraIv.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bk.unbind();//解绑butterknife
    }

    @OnClick({R.id.play_bt, R.id.pause_bt, R.id.stop_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_bt:
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();//开始播放
                break;
            case R.id.pause_bt:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();//暂停
                break;
            case R.id.stop_bt:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();//停止
                break;
        }
    }
}
