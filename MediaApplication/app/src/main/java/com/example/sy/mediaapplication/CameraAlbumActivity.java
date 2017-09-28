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
    private Uri imageUri;//���պ��ͼƬ��Uri
    public static final int TAKE_PHOTO = 1;//�����룬���������ת
    private MediaPlayer mediaPlayer=new MediaPlayer();//ʵ������Ƶ����

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_album);
        bk = ButterKnife.bind(this);//��butterknife
        init();
    }

    private void init() {
        //�жϵ�ǰActivity�Ƿ����ֻ���ȡȨ�ޣ�Ϊ����ӦAndroid4.4��ǰ�İ汾
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            //�������ͣ�                     ��ǰActivity������Ҫ��ȡ��Ȩ��,������
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }else {
            initMedioPlayer();
        }
    }

    //��ʼ��mediaplayer
    private void initMedioPlayer() {
        //��ȡ��Ƶ���·��
        File file=new File(Environment.getExternalStorageDirectory(),"music.mp3");
        try {
            //��mediaplayer�з�����Ƶ��Դ
            mediaPlayer.setDataSource(file.getPath());
            //׼��������Ƶ
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case   1:
                //�ж���Ȩ����Ƿ�Ϊ�� ������Ϊ�����һ���ж�����õ�Ȩ���Ƿ�������Ҫ��Ȩ�ޣ�����ָ��дȨ�ޣ�
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initMedioPlayer();//���Ȩ�޳ɹ�����ʼ��mediaplayer
                }else {
                    //ʧ����˾���ر�����
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
//                �����ļ���
                File outputImage = new File(getExternalCacheDir() , "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();//�жϸ��ļ��Ƿ���ڣ���������ɾ�����½���
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                ��fileת����Uri
                if (Build.VERSION.SDK_INT >= 24) {//�ж�Android�汾�Ƿ����7.0

                    imageUri = FileProvider.getUriForFile(this,
                            "com.example.cameraalbumtest.fileprovider", outputImage);
                } else {
                    //�汾����7.0
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//intent��ʽ��ת
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//?
                startActivityForResult(intent, TAKE_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {//�ж�����Ƿ��������
                    try {
                        //��ȡ������µ���Ƭ
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
        bk.unbind();//���butterknife
    }

    @OnClick({R.id.play_bt, R.id.pause_bt, R.id.stop_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_bt:
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();//��ʼ����
                break;
            case R.id.pause_bt:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();//��ͣ
                break;
            case R.id.stop_bt:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();//ֹͣ
                break;
        }
    }
}
