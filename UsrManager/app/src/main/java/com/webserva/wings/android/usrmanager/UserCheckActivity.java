package com.webserva.wings.android.usrmanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.pm.PackageInfo;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

import android.os.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserCheckActivity extends Activity {

    private static final String TAG = "UserPermissionList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.your_layout); // 必要であればレイアウトを設定

        UserManager userManager = (UserManager) getSystemService(USER_SERVICE);
        if (userManager == null) {
            Log.e(TAG, "UserManager が取得できへん");
            return;
        }

        UserHandle user;
        DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        // hidden
        boolean a = dpm.is(user);
        boolean isSystemOnlyUser(UserHandle user)

        PackageInfo pi = pm.getPackageInfoAsUser("com.example.app", 0, user); // hidden
        boolean isSystemApp = pi.applicationInfo.isSystemApp(); // hidden flag

        // 全ユーザー取得
        List<UserInfo> users; // ★修正: UserManager.UserInfo -> UserInfo
        try {
            // true を渡すと、停止中のユーザーや初期化中のユーザーも含めて取得しようとします。
            // false を渡すと、現在実行中のユーザーのみを取得します。
            // アプリが適切な権限を持っていない場合、SecurityExceptionが発生するか、
            // 現在のユーザーのみ、あるいは空のリストが返されることがあります。
            users = userManager.getUsers(true);
        } catch (SecurityException e) {
            Log.e(TAG, "ユーザーリストの取得に失敗しました。必要な権限がない可能性があります。", e);
            users = new ArrayList<>(); // 例: 空のリストとして処理を継続
            // return; // あるいはここで処理を終了する
        }

        if (users.isEmpty()) {
            Log.d(TAG, "ユーザーが見つかりませんでした。");
            return;
        }

        // 個別ユーザー情報を取得
        UserManager.UserInfo info = um.getUserInfo(userId);

        // ユーザー判定
        boolean isAdmin = info.isAdmin();
        boolean isGuest = info.isGuest();
        boolean isManagedProfile = info.isManagedProfile(); // hidden API

        // 権限順にソート（Admin > NonAdmin > Guest）
        // Android Lollipop (API 21) 以降では List.sort が使える
        // Collections.sort(users, (u1, u2) -> getPermissionLevel(u2) - getPermissionLevel(u1));
        Collections.sort(users, new Comparator<UserInfo>() { // ★修正: UserManager.UserInfo -> UserInfo
            @Override
            public int compare(UserInfo u1, UserInfo u2) { // ★修正
                // 降順ソート: Admin (3) > NonAdmin (2) > Guest (1)
                return getPermissionLevel(u2) - getPermissionLevel(u1);
            }

            private int getPermissionLevel(UserInfo user) { // ★修正
                if (user.isAdmin()) return 3;
                if (user.isGuest()) return 1;
                // isPrimary() もユーザータイプを判断するのに役立つ場合があります。
                // プライマリユーザーは通常、管理者権限を持ちますが、
                // isAdmin() の方が直接的です。
                // if (user.isPrimary()) return 2; // プライマリだが管理者ではない場合など
                return 2; // NonAdmin (PrimaryでもAdminでもGuestでもない場合)
            }
        });

        // ログ出力
        Log.d(TAG, "--- ユーザーリスト (権限順) ---");
        for (UserInfo userInfo : users) { // ★修正: UserManager.UserInfo -> UserInfo
            String type;
            if (userInfo.isAdmin()) {
                type = "Admin";
            } else if (userInfo.isGuest()) {
                type = "Guest";
            } else if (userInfo.isPrimary()) {
                // isPrimary() はAPIレベル17から利用可能
                type = "Primary Non-Admin";
            } else {
                type = "Non-Admin/Non-Guest Secondary User";
            }
        }
    }
}
