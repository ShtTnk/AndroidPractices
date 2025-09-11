package com.webserva.wings.android.toolbarsample;

import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * ScrollViewを使用したサンプルアプリケーション。
 * Androidではマテリアルテーマによって3次元がと配色についてはすでに設定が済んでいる状態。
 * マテリアルデザインの画面部品に動きを付ける
 * アクションバーよりも柔軟に使用できるツールバーの設定コード
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar取得
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Toolbarにロゴを設定
        toolbar.setLogo(R.mipmap.ic_launcher);
        // ツールバーにタイトル文字列を設定
        toolbar.setTitle(R.string.toolbar_title);
        // ツールバーのタイトル文字色を設定｡
        toolbar.setTitleTextColor(Color.WHITE);
        // ツールバーのサブタイトル文字列を設定。
        toolbar.setSubtitle(R.string.toolbar_subtitle);
        // ツールバーのサブタイトル文字色を設定
        toolbar.setSubtitleTextColor(Color.LTGRAY);
        // アクションバーにツールバーを設定
        setSupportActionBar(toolbar);

    }
}
