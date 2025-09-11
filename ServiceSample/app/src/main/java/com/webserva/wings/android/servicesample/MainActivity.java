package com.webserva.wings.android.servicesample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Androidにはサービスというアクティビティから独立して、バックぐ運土で処理を続ける、サービスという仕組みがある
 * この中で再生される音楽ファイルはバックグラウンドにいっても再生され続ける。
 * 通知チャネルを使用して画面を持たないサービスが実行されているのかどうかを確認できるようにする。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 未承諾の場合は通知リクエストを求める。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        // 通知からアクティビティが起動された時の処理を記述する。通知経由でMainActivity画面が表示された場合、再生ボタンを使用不可にして停止ボタンを使用可能にする。
        // Intent
        Intent intent = getIntent();
        // 通知のタップからの引継ぎデータを取得
        boolean fromNotification = intent.getBooleanExtra("fromNotification" , false);
        // 引継ぎデータを取得・存在する場合
        if (fromNotification) {
            // 再生ボタンをタップ不可に、停止ボタンをタップ可に変更。
            Button btPlay = findViewById(R.id.btPlay);
            Button btStop = findViewById(R.id.btStop);
            btPlay.setEnabled(false);
            btStop.setEnabled(true);
        }

    }

    /**
     * 再生ボタンタップ時の処理。メディア再生をサービスでするために記述
     */
    public void onPlayButtonClick(View view) {
        // インテント生成
        Intent intent = new Intent(MainActivity.this, SoundManageService.class);
        // サービス起動
        startService(intent);
        // 再生ボタンをタップ不可能に、停止ボタンをタップ可に変更。
        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);
        btPlay.setEnabled(false);
        btStop.setEnabled(true);
    }

    /**
     * 停止ボタンタップ時の処理。サービスを終了させる処理を記述
     */
    public void onStopButtonClick(View view) {
        // インテント生成
        Intent intent = new Intent(MainActivity.this, SoundManageService.class);
        // サービス停止
        stopService(intent);
        // 再生ボタンをタップ可能に、停止ボタンをタップ不可能に変更。
        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);
        btPlay.setEnabled(true);
        btStop.setEnabled(false);
    }
}
