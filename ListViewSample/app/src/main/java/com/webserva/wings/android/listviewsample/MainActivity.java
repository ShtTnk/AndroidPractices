package com.webserva.wings.android.listviewsample;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

/**
 * ListViewを使用してリストにリスナーをセットする。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ListViewをオブジェクトを取得
        ListView lvMenu = findViewById(R.id.lvMenu);
        lvMenu.setOnItemClickListener(new ListItemClickListener());
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // タップされた定食名を取得
            String item = (String) parent.getItemAtPosition(position);
            // トーストで表示する文字列を取得
            String show = "You Chose： " + item;
            Toast.makeText(MainActivity.this, show, Toast.LENGTH_LONG).show();
        }
    }
}