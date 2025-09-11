package com.webserva.wings.android.hellosample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn = findViewById(R.id.btClick);
        Button btn2 = findViewById(R.id.btClear);
        HelloListener helloListener = new HelloListener();
        btn.setOnClickListener(helloListener);
        btn.setOnClickListener(new HelloListener());
        btn2.setOnClickListener(helloListener);
    }

    /**
     * ボタンをクリックしたときのリスナクラス
     * EditTextから入力された名前を取得してTextViewに表示させる
     */
    private class HelloListener implements View.OnClickListener {
        // 名前入力欄EditTextオブジェクトを取得
        EditText input = findViewById(R.id.etName);
        // メッセージを表示するTextViewオブジェクトを取得。
        TextView output = findViewById(R.id.tv_Output);

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Log.d("onClick", "GET ID >> " + id);
            if (id == R.id.btClick) {
                // 入力された名前文字列を取得。
                String inputStr = input.getText().toString();
                // メッセージを表示する。
                if (inputStr != null && !inputStr.isEmpty()) {
                    output.setText(inputStr + "さんこんにちは");
                } else {
                    CharSequence message = "なんかセットしんかい";
                    output.setText(message);
                }
            } else if (id == R.id.btClear) {
                input.setText("");
                output.setText("");
            } else {
                Log.d("onclick", "error" + v.getId());
            }
        }
    }
}