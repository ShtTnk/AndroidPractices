package com.example.counterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

/**
 * MVVCモデルで設計するカウントアプリケーション
 * model = データ
 * View = UI
 * ViewModeL ＝ 仲介約
 * ボタンを押したら数字が１ずつ増えるアプリケーション
 * 数字はViewModelで管理
 * LiveDataで画面に反映させる
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView textCount = findViewById(R.id.textCount);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getCount().observe(this, count -> {
            textCount.setText(String.valueOf(count));
        });
        buttonAdd.setOnClickListener(v -> viewModel.increment());
    }

}