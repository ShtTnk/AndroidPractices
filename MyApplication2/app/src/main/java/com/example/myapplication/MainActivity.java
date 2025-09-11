package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button button;

    public class Worker {
        public void doSomethingAsync(MyCallback callback){
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.onComplete("処理完了やで。");
            }).start();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // XMLで作った画面をセット

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Worker worker = new Worker();
            worker.doSomethingAsync(result -> runOnUiThread(() -> {
                textView.setText(result); // UIの更新はUIスレッドで
            }));
        });
    }
}


