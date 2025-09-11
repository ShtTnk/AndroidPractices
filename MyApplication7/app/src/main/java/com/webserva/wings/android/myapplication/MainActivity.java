package com.webserva.wings.android.myapplication;

import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // その１ 現在プロセスのユーザーを取得
        UserHandle myUser = android.os.Process.myUserHandle();
        Log.v("UserHandle myUser", myUser.toString());
        // その2 ユーザー情報を取得
        UserManager um = getSystemService(UserManager.class);
        List<UserHandle> profiles = um.getUserProfiles();
        Log.v("getUserProfiles", profiles.toString());
        boolean bool = um.isAdminUser();
        Log.v("isAdminUser", "isAdminUser" + bool);
//        int num = um.getUserCount();
//        Log.v(TAG, "getUserCount" + num);



//        if(um != null) {
//            Log.v("isAdminUser", "isAdminUse" + um.isAdminUser());
//        }
//        Log.v("um.getUserCount()", "um.getUserCount()" + um.getUserCount());
    }
}