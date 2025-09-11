package com.webserva.wings.android.databasesample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.SQLData;

public class DataBaseHelper extends SQLiteOpenHelper {
    // DBファイルの定数名
    private static final String DATABASE_NAME = "cocktailmemo.db";
    // バージョン定数
    private static final int DATABASE_VERSION = 1;
    // コンストラクタ
    public DataBaseHelper(Context context) {
        // 親クラス
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成SQL文字列の生成。
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE cocktailmemos (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("name TEXT,");
        sb.append("note TEXT");
        sb.append(");");
        String sql = sb.toString();
        // SQLの実行
        db.execSQL(sql);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
