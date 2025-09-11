package com.example.tutorial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        TextView textView = findViewById(R.id.hello);
    }

    // レイアウトファイルのボタンにonClickを記述するとこのようにかけるが、拡張性、保守性、可読性を高めるという点においては非推奨
    public void toggle(View v){
        // toggle
    }
    public void disable(View v) {
        View myView = findViewById(R.id.button4);
        myView.setEnabled(false);
        Button button = (Button) myView;
        button.setText("New Disabled");

        // disable code
        /*
        v.setEnabled(false);
        Log.d("success", "Button Disabled");
        // ボタンを受け取って、ボタンテキスト表示を変更する
        Button button = (Button) v;
        button.setText("Disabled");
        */
    }

    public void handleText(View v) {

    }
}
