package com.webserva.wings.android.databasesample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // 選択されたカクテルの主キーを表すフィールド。
    private int _cocktailId = -1;
    // 選択されたカクテル名を表すフィールド
    private String _cocktailName = "";
    // データベースヘルパーオブジェクト
    private DataBaseHelper _helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // カクテルリスト用ListViewを取得とリスナ登録
        ListView lvCocktail = findViewById(R.id.lvCocktail);
        lvCocktail.setOnItemClickListener(new ListItemClickOnListener());
        // DBヘルパーオブジェクト生成
        _helper = new DataBaseHelper(MainActivity.this);
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onSaveButtonClick);
    }

    @Override
    protected void onDestroy() {
        // DEヘルパーオブジェクトの開放
        _helper.close();
        super.onDestroy();
    }

    // リストがタップされたときんお処理が記述されたメンバクラス
    private class ListItemClickOnListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // タップされた行番号をフィールドに代入
            _cocktailId = position;
            Log.d("DBTAG", _cocktailId + "<cocktail ID" + position + "<position");
            // タップされた行のデータ取得。これがカクテル名になるので、フィールドに代入。
            _cocktailName = (String) parent.getItemAtPosition(position);
            Log.d("DBTAG", _cocktailName);
            // カクテル名を表示するTextViewに表示カクテル名を設定
            TextView tvCocktailName = findViewById(R.id.tvCocktailName);
            tvCocktailName.setText(_cocktailName);
            // 保存ボタンをタップできるように設定
            Button btnSave = findViewById(R.id.btnSave);
            btnSave.setEnabled(true);

            // db接続
            SQLiteDatabase db = _helper.getWritableDatabase();
            // 主キー検索
            String sql = "SELECT * FROM cocktailmemos WHERE _id = " + _cocktailId;
            Log.d("SELECT STATEMENT", sql);
            // SQLの実行
            Cursor cursor = db.rawQuery(sql, null);
            // DBからの値を取得するための変数 null に備える
            String note = "";
            while (cursor.moveToNext()) {
                // からむいんでっくすの取得
                int idxNote = cursor.getColumnIndex("note");
                // カラムのインデックスをもとに実際のデータを取得
                note = cursor.getString(idxNote);
                Log.d("NOTECURSOR", note);
            }
            // 感想のEditTextの各画面部品を取得しデータベースの値を反映
            EditText etNote = findViewById(R.id.editTextTextMultiLine);
            etNote.setText(note);
        }
    }

    private final View.OnClickListener onSaveButtonClick = v -> {
        //感想欄を取得
        EditText etNote = findViewById(R.id.editTextTextMultiLine);
        String note = etNote.getText().toString();

        // DBヘルパーオブジェクトから接続オブジェクトを取得
        SQLiteDatabase db = _helper.getWritableDatabase();

        // まずリストで選択されたカクテルのメモデータを削除し、そのあとインサートを行う。
        // 削除用SSQLを用意
        String sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?";
        // SQL文字列をもとにプレペアドステートメントを取得
        SQLiteStatement stmt = db.compileStatement(sqlDelete);
        // 変数のバインド
        stmt.bindLong(1, _cocktailId);
        // 削除のSQL実行
        stmt.executeUpdateDelete();

        // インサート用のSQL
        String sqlInsert = "INSERT INTO cocktailmemos (_id, name, note) VALUES (?, ?, ?)";
        Log.d("SQLINSERT", sqlInsert);
        // プリペアードステートメント
        stmt = db.compileStatement(sqlInsert);
        stmt.bindLong(1, _cocktailId);
        stmt.bindString(2, _cocktailName);
        stmt.bindString(3, note);
        // 実行
        stmt.executeInsert();

        // 回答欄の入力値を消去
        etNote.setText("");

        // 感想欄の入力値を消去
        etNote.setText("");
        // カクテル名未選択に
        TextView tvCocktailName = findViewById(R.id.tvCocktailName);
        tvCocktailName.setText(getString(R.string.btn_save));
    };
}