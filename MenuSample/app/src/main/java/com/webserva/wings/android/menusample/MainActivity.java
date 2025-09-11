package com.webserva.wings.android.menusample;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // リストビューをあらわすフィールド
    private ListView _lvMenu;
    // リストビューに表示するデータリスト
    private List<Map<String, Object>> _menuList;
    // SimpleAdapterの第四引数で使用するフィールド
    private static final String[] FROM = {"name", "price"};
    // SimpleAdapterの第五引数で使用するフィールド
    private static final int[] TO = {R.id.tvMenuNameRow, R.id.tvMenuPriceRow};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // リストビューを取得
        _lvMenu = findViewById(R.id.lvMenu);
        // 定食メニューリストオブジェクトをprivateメソッドを利用して用意し、フィールドに格納
        _menuList = createTeishokuList();
        // SimpleAdapterを生成
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _menuList, R.layout.row, FROM, TO);
        // アダプターの登録
        _lvMenu.setAdapter(adapter);
        // リストタップのリスナクラス登録
        _lvMenu.setOnItemClickListener(new OnItemClickListener());

        // ここは本と違う部分だった
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // コンテキストメニュー登録に追加
        registerForContextMenu(_lvMenu);
    }

    // メニューリストを作成する
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_menu_list, menu);
        return true;
    }

    // コンテキストメニューを作成する
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // 親クラス同名メソッド呼び出し
        super.onCreateContextMenu(menu, v, menuInfo);
        // メニューインフレーターの取得
        MenuInflater inflater = getMenuInflater();
        // コンテキストメニュー用.xmlファイルをインフレート
        inflater.inflate(R.menu.menu_context_menu_list, menu);
        // コンテキストメニューのヘッダタイトル設定
        menu.setHeaderTitle(R.string.menu_list_context_header);
    }

    // 定食リストをを生成するメソッド
    private List<Map<String, Object>> createTeishokuList() {
        // 定食メニューリスト用のListオブジェクトを用意
        List<Map<String, Object>> menuList = new ArrayList<>();
        // からあげ定食のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        Map<String, Object> menu = new HashMap<>();
        menu.put("name", "からあげ定食");
        menu.put("price", 800);
        menu.put("desc", "若鳥のから揚げにサラダ、ごはんとお味噌汁がつきます。");
        menuList.add(menu);
        // ハンバーグ定食のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        menu = new HashMap<>();
        menu.put("name", "ハンバーグ定食");
        menu.put("price", 850);
        menu.put("desc", "ハンバーグの定食です。");
        menuList.add(menu);
        // 大量生成
        for (int i = 2; i < 100; i++) {
            menu = new HashMap<>();
            menu.put("name", "ハンバーグ定食" + i);
            menu.put("price", 850 + i);
            menu.put("desc", "ハンバーグ定食です。" + i);
            menuList.add(menu);
        }
        return menuList;
    }

    // 定食リストをを生成するメソッド
    private List<Map<String, Object>> createCurrayList() {
        // カレーリスト用のListオブジェクトを用意
        List<Map<String, Object>> menuList = new ArrayList<>();
        // ビーフカレーのデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        Map<String, Object> menu = new HashMap<>();
        menu.put("name", "ビーフカレー");
        menu.put("price", 800);
        menu.put("desc", "特選スパイスをきかせたカレーライス");
        menuList.add(menu);
        // ポークカレー用
        menu = new HashMap<>();
        menu.put("name", "ポークカレー");
        menu.put("price", 400);
        menu.put("desc", "ポークカレー定食です。");
        menuList.add(menu);
        return menuList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();
        if (itemId == R.id.menuListOptionTeishoku) {
            _menuList = createTeishokuList();
        } else if (itemId == R.id.menuListOptionCurry) {
            _menuList = createCurrayList();
        } else {
            returnVal = super.onOptionsItemSelected(item);
        }

        SimpleAdapter adapter2 = new SimpleAdapter(
                MainActivity.this, _menuList, R.layout.row, FROM, TO
        );
        _lvMenu.setAdapter(adapter2);
        return returnVal;
    }

    // リストアイテムのクリックリスナ
    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // タップされた行のデータを取得
            Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
            // 定食名と金額を取得。Mapの値部分がObjectなのでキャスト必要。
            String menuName = (String) item.get("name");
            Integer menuPrice = (Integer) item.get("price");

            // インテントオブジェクト生成
            Intent intent = new Intent(MainActivity.this, MenuThxActivity.class);
            intent.putExtra("menuName", menuName);
            intent.putExtra("menuPrice", menuPrice + "円");
            startActivity(intent);
        }
    }

    // コンテキストメニューからの選択処理を実装する
    private void order(Map<String, Object> menu) {
        // 名前と金額を取得
        String menuName = (String) menu.get("name");
        Integer menuPrice = (Integer) menu.get("price");
        // インテント生成
        Intent intent = new Intent(MainActivity.this, MenuThxActivity.class);
        intent.putExtra("menuName", menuName);
        intent.putExtra("menuPrice", menuPrice + "円");
        startActivity(intent);
    }

    // コンテキストメニュー選択時処理メソッドを追加
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // 戻り値用
        boolean ret = true;
        // 長押しされたviewに関する情報が格納されたオブジェクトを取得
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // 長押しされたリストのポジションを取得
        int listPosition = info.position;
        // ポジションから長押しされたメニュー情報Mapオブジェクトを取得
        Map<String, Object> menu = _menuList.get(listPosition);

        // 選択されたメニューIDを取得
        int itemId = item.getItemId();
        // IDのR値による処理の分岐
        if (itemId == R.id.menuListContextDesc) {
            // 説明文字列を取得してトーストを表示
            String desc = (String) menu.get("desc");
            Toast.makeText(MainActivity.this, desc, Toast.LENGTH_SHORT).show();
            return super.onContextItemSelected(item);
        } else if (itemId == R.id.menuListContextOrder) {
            order(menu);
            return super.onContextItemSelected(item);
        } else {
            Log.wtf("ERROR", "ERROR");
            return ret;
        }
    }
}