package com.webserva.wings.android.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * ユーザー一覧画面。
 * 削除モード ON/OFF 切替、単体削除、全削除に対応。
 */
public class MainActivity extends Activity {

    private ListView listView;
    private Button deleteModeButton;
    private Button deleteAllButton;
    private UserAdapter adapter;
    private List<User> users;

    /** 削除モードフラグ */
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        deleteModeButton = findViewById(R.id.btnDeleteMode);
        deleteAllButton = findViewById(R.id.btnDeleteAll);

        // 初期ユーザー取得
        users = getAllUsersFromAutomotiveSettings();

        // Adapterセット
        adapter = new UserAdapter(this, users);
        listView.setAdapter(adapter);

        // 削除モード切替ボタン
        deleteModeButton.setOnClickListener(v -> toggleDeleteMode());

        // 全削除ボタン
        deleteAllButton.setOnClickListener(v -> {
            users.clear();  // 実際に削除
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "全ユーザーを削除しました", Toast.LENGTH_SHORT).show();
        });

        // リストアイテムクリック
        listView.setOnItemClickListener((parent, view, position, id) -> {
            User clickedUser = users.get(position);
            if (isDeleteMode) {
                users.remove(position); // 実際に削除
                adapter.notifyDataSetChanged();
                Toast.makeText(this, clickedUser.getName() + " を削除しました", Toast.LENGTH_SHORT).show();
            } else {
                openUserSettings(clickedUser);
            }
        });
    }

    /** 削除モード切替 */
    private void toggleDeleteMode() {
        isDeleteMode = !isDeleteMode;
        deleteAllButton.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
        adapter.setDeleteMode(isDeleteMode); // Adapterに反映
        adapter.notifyDataSetChanged();
        Toast.makeText(this, isDeleteMode ? "削除モード ON" : "通常モード", Toast.LENGTH_SHORT).show();
    }

    /** 設定画面を開く（通常モード時） */
    private void openUserSettings(User user) {
        Toast.makeText(this, user.getName() + " の設定画面を開く", Toast.LENGTH_SHORT).show();
    }

    /**
     * AutomotiveSettings から全ユーザー取得（仮実装）
     */
    private List<User> getAllUsersFromAutomotiveSettings() {
        List<User> users = new ArrayList<>();

        // 仮に固定で作ってるだけやけど、ここで実際の AutomotiveSettings API を使う
        users.add(new User(User.TYPE_ADMIN ,"Admin1", "2025/12/31"));

        for(int i=1; i<=4; i++)
            users.add(new User(User.TYPE_NORMAL, "Normal" + i, "2025/12/31"));

        for(int i=1; i<=8; i++)
            users.add(new User(User.TYPE_GUEST, "Normal" + i, "2025/12/31"));

        return users;
    }

    /**
     * SharedPreferences から全ユーザー取得（仮実装）
     * @return
     */
    private List<User> getAllUsersFromStorage() {
        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);
        int count = prefs.getInt("user_count", 0);

        List<User> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int type = prefs.getInt("user_" + i + "_type", User.TYPE_NORMAL);
            String name = prefs.getString("user_" + i + "_name", "Unknown");
            String expiry = prefs.getString("user_" + i + "_expiry", "----/--/--");
            list.add(new User(type, name, expiry));
        }
        return list;
    }
}
