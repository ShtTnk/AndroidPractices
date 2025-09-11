package com.webserva.wings.android.menusample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class MenuThxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_thx);
        // インテントオブジェクトを取得
        Intent intent = getIntent();
        // リストが面から渡されたデータを取得
        String menuName = intent.getStringExtra("menuName");
        String menuPrice = intent.getStringExtra("menuPrice");

        // 定食名と金額を表示させるTextViewを取得
        TextView tvMenuName = findViewById(R.id.tvMenuName);
        TextView tvMenuPrice = findViewById(R.id.tvMenuPrice);
        Button backBtn = findViewById(R.id.btThxBack);
        backBtn.setOnClickListener(v -> {
            finish();
        });
        // TextViewに定食名と金額を表示
        tvMenuName.setText(menuName);
        tvMenuPrice.setText(menuPrice);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーnullチェック
        if (actionBar != null) {
            Log.d("actionBar", actionBar.toString());
            // アクションバーの戻るメニューを有効に設定
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 戻るメニュー選択時の処理を記述する
     *
     * @param item The menu item that was selected.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 戻り値用変数
        boolean returnVal = true;
        // 選択されたメニューのIDを取得
        int itemId = item.getItemId();
        // 選択されたメニューが[戻る]ならば終了
        if (itemId == android.R.id.home) {
            finish();
        } else {
            // 親クラスのド名メソッドを予備だs、その戻り値をreturnValとする。
            returnVal = super.onOptionsItemSelected(item);
        }
        return returnVal;
    }
}
