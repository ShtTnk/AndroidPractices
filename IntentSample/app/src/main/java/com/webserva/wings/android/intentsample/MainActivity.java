package com.webserva.wings.android.intentsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // listViewを取得
        ListView lvMenu = findViewById(R.id.lvMenu);
        // SimpleAdapterで使用するListオブジェクトを用意。
        List<Map<String, String>> menuList = new ArrayList<>();
        // Mapオブジェクトに登録
        for (int i = 1; i < 100; i++) {
            Map<String, String> menu = new HashMap<>();
            menu.put("name", "からあげ" + i);
            menu.put("price", i + 800 + "円");
            menuList.add(menu);
        }

        // SimpleAdapter第4引数from用データの用意
        String[] from = {"name", "price"};
        // SimpleAdapter第5引数from用データの用意
        int[] to = {android.R.id.text1, android.R.id.text2};
        // SimpleAdapterを生成。from配列のname,priceがto配列のtext1,text2にあたる。simple_list_item_2ではそのようになる。
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, menuList, android.R.layout.simple_list_item_2, from, to);
        // アダプタの登録
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(new ListItemClickListener());
        // カスタムビューバインダーの登録
        adapter.setViewBinder(new CustomViewBinder());
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            String menuName = item.get("name");
            String menuPrice = item.get("price");
            Intent intent = new Intent(MainActivity.this, MenuThanksActivity.class);
            intent.putExtra("menuName", menuName);
            intent.putExtra("menuPrice", menuPrice);
            startActivity(intent);
        }
    }
    private class CustomViewBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(R.mipmap.ic_launcher);
                return true; // カスタム
            }
            return false; // デフォルト
        }
    }
}