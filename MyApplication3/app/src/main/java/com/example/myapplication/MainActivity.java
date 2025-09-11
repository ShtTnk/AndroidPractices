package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    public void handleText(View v) {
        // 入力フォームを取得
        EditText t = findViewById(R.id.source);
        // 文字列型に変換
        String input =  t.getText().toString();
        // output文字列に出力
        ((TextView)findViewById(R.id.output)).setText(input);
        // トースト表示をする
        Toast.makeText(this, input, Toast.LENGTH_LONG).show();
        Log.d("handleText", input);
    }


}