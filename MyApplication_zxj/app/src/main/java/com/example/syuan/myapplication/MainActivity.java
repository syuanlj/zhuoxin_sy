package com.example.syuan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et;
    private GridView gv;
    private Button bt;
    private HashMap<String ,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        gv = (GridView) findViewById(R.id.gv);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(this);

        map = new HashMap<String, Object>();
        map.put("image1",getResources().getDrawable(R.mipmap.pic1));
        map.put("image2",getResources().getDrawable(R.mipmap.pic2));
    }

    @Override
    public void onClick(View view) {
        String etText = et.getText().toString();

        submit();
    }

    private void submit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://120.26.96.138:8083/")
                .build();

        Log.e("999", "retrofi");
        retrofit.create(Api.class).postDedicateRelease("username", "cityname",
                "company", "throughline",
                "transitline", "contacts",
                "phonenumber", "planenumber",
                "faxnumber", "warehouseaddress",
                "moreinfo", "profile", map).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e("999", "成功");
                    Toast.makeText(MainActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("999", t.getMessage());
            }
        });
    }
    interface Api {
        @POST("hwy/index.php/Home/App/zxxxfb")
        Call<String> postDedicateRelease(@Query("username") String username,
                                         @Query("cityname") String cityname,
                                         @Query("companyname") String companyname,
                                         @Query("throughline") String throughline,
                                         @Query("transitline") String transitline,
                                         @Query("contacts") String contacts,
                                         @Query("telnumber") String telphone,
                                         @Query("planenumber") String planenumber,
                                         @Query("faxnumber") String faxnumber,
                                         @Query("warehouseaddress") String warehouseaddress,
                                         @Query("moreinfo") String moreinfo,
                                         @Query("profile") String profile,
                                         @PartMap HashMap<String, Object> picturelist);

    }
}