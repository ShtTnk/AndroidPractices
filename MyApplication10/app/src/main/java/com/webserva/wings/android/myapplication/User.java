package com.webserva.wings.android.myapplication;

/**
 * ユーザー情報クラス
 * Admin / Normal / Guest 共通で name と expiry のみ保持
 */
public class User {
    public static final int TYPE_ADMIN = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_GUEST = 2;

    private int type;
    private String name;    // ユーザー名
    private String expiry;  // 設定期限

    public User(int type, String name, String expiry) {
        this.type = type;
        this.name = name;
        this.expiry = expiry;
    }

    public int getType() { return type; }
    public String getName() { return name; }
    public String getExpiry() { return expiry; }
}
