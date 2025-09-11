package com.webserva.wings.android.customelistview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AnotherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // メイン画面のレイアウト

        // インテント取得
        Intent intent = getIntent();
        String menu = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");

        // TextVIewに表示
        TextView tvMenu = findViewById(R.id.tvMenuName);
        TextView tvPrice = findViewById(R.id.tvMenuPrice);
        tvMenu.setText(menu);
        tvPrice.setText(price);

    }
    public void onBackButtonClick(){
        finish();
    }
}
