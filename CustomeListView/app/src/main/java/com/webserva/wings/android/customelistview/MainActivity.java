package com.webserva.wings.android.customelistview;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /**
     * Delete Mode フラグ
     */
    boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // メイン画面のレイアウト

        ListView lvMenu = findViewById(R.id.lvMenu);

        List<Map<String, Object>> menuList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> menu = new HashMap<>();
            menu.put("name", "からあげ" + i);
            menu.put("price", (i + 800) + "円");
            menu.put("icon", R.mipmap.ic_launcher);
            menuList.add(menu);
        }

        String[] from = {"icon", "name", "price"};
        int[] to = {R.id.ivIcon, R.id.tvName, R.id.tvPrice};

        SimpleAdapter adapter = new SimpleAdapter(this, menuList, R.layout.row_menu, from, to);

        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Integer) {
                    ImageView iv = (ImageView) view;
                    if (deleteMode) {
                        iv.setImageResource(R.mipmap.ic_launcher_round);
                    } else {
                        iv.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                    return true;
                }
                return false;
            }
        });

        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(((parent, view, position, id) -> {
            Map<String, Object> item = menuList.get(position);
            if (deleteMode) {
                //
                new AlertDialog.Builder(this)
                        .setTitle("削除")
                        .setMessage(item.get("name") + "を削除する？")
                        .setPositiveButton("はい", (dialog, which) -> {
                            menuList.remove(position);
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("NO", null)
                        .show();
            } else {
                Intent intent = new Intent(this, AnotherActivity.class);
                intent.putExtra("name", (String)item.get("name"));
                intent.putExtra("price", (String)item.get("price"));
                startActivity(intent);
            }
        }));

        Button btnDeleteMode = findViewById(R.id.btnDeleteMode);
        btnDeleteMode.setOnClickListener(v -> {
            deleteMode = !deleteMode;
            if (deleteMode) {
                btnDeleteMode.setAlpha(0.5f);
            } else {
                btnDeleteMode.setAlpha(1.0f);
            }
            adapter.notifyDataSetChanged();
        });
    }
}