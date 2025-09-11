package com.webserva.wings.android.listviewsample2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * リストビューとリストデータを結びつけるアダプタクラス。下記の通りセットアップする。
 * 1. リストデータを用意
 * 2. リストデータをもとにアダプタオブジェクトを生成
 * 3. ListViewにアダプタオブジェクトをセット
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1.ListViewオブジェクトを取得
        ListView lvMenu = findViewById(R.id.lvMenu);
        // ListViewに表示するリストデータ用Listオブジェクトを作成。
        List<String> menuList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            menuList.add("定食" + i);
        }
        // 2.アダプタオブジェクトを生成
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, menuList);
        // 3.ListViewにアダプタオブジェクトを設定。
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(new ListItemClickListener());
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 注文確認ダイアログフラグメントオブジェクトを生成
            OrderConfirmDialogFragment dialogFragment = new OrderConfirmDialogFragment();
            // ダイアログを表示 getSupportFragmentManager()でFragmentManagerを取得している
            dialogFragment.show(getSupportFragmentManager(), "OrderConfirmDialogFragment");
        }
    }

}