package com.webserva.wings.android.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button deleteModeButton;
    private Button deleteAllButton;
    private UserAdapter adapter;
    private List<User> users;
    private boolean isDeleteMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        deleteModeButton = findViewById(R.id.btnDeleteMode);
        deleteAllButton = findViewById(R.id.btnDeleteAll);

        users = getDummyUsers(); // 実際には AutomotiveSettings API などから取得

        adapter = new UserAdapter(users, (user, position, deleteMode) -> {
            if (deleteMode) {
                users.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(this, user.getName() + " を削除しました", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, user.getName() + " の設定画面を開く", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        deleteModeButton.setOnClickListener(v -> toggleDeleteMode());
        deleteAllButton.setOnClickListener(v -> {
            users.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "全ユーザーを削除しました", Toast.LENGTH_SHORT).show();
        });
    }

    private void toggleDeleteMode() {
        isDeleteMode = !isDeleteMode;
        adapter.setDeleteMode(isDeleteMode);
        deleteAllButton.setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);
        Toast.makeText(this, isDeleteMode ? "削除モード ON" : "通常モード", Toast.LENGTH_SHORT).show();
    }

    private List<User> getDummyUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User(User.TYPE_ADMIN,"Admin1","2025/12/31"));
        for(int i=1;i<=4;i++) list.add(new User(User.TYPE_NORMAL,"Normal"+i,"2025/12/31"));
        for(int i=1;i<=8;i++) list.add(new User(User.TYPE_GUEST,"Guest"+i,"2025/12/31"));
        return list;
    }
}
