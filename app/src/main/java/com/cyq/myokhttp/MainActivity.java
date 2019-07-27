package com.cyq.myokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OKHttpUtils.httpGet("\n" +
                        "http://47.105.172.117:81/api/User/LoginByOtherWay?gender=&mail=&phone=&userimage=https%3A%2F%2Fgraph.facebook.com%2F151607632658702%2Fpicture%3Fheight%3D200%26width%3D200%26migrationoverrides%3D7Boctober20123Atrue7D&logintype=fb&nickname=%E6%AF%9B%E5%8D%8E&id=151607632658702&username=");
            }
        }).start();

    }
}
