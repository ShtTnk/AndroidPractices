package com.webserva.wings.android.intentsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuThanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_thanks);
        // インテントオブジェクトを取得
        Intent intent = getIntent();
        // リストが面から渡されたデータを取得
        String menuName = intent.getStringExtra("menuName");
        String menuPrice = intent.getStringExtra("menuPrice");

        // 定食名と金額を表示させるTextViewを取得
        TextView tvMenuName = findViewById(R.id.tvMenuName);
        TextView tvMenuPrice = findViewById(R.id.tvMenuPrice);

        // TextViewに定食名と金額を表示
        tvMenuName.setText(menuName);
        tvMenuPrice.setText(menuPrice);
    }

    // 戻るボタンをタップした時の処理。
    public void onBackButtonClick(View view) {
        finish();
    }
}