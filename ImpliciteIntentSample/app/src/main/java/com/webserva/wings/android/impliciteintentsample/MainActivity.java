package com.webserva.wings.android.impliciteintentsample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 暗黙的インテントサンプル
 * 暗黙的インテントはアクションとURI
 * new Intent(ACTION_VIEW, URI)で生成される：画面表示
 * new Intent(ACTION_DIAL, URI)で生成される: 電話発信
 * new Intent(ACTION_SEND, URI)で生成される: メール送信などなど定数できめられている
 * <p>
 * 位置情報取得のライブラリを利用する FUsedLocationProviderClient SDK Manager > SDK Tools > Google Play Servicesにチェックを入れてインストール
 * File > Project Structure > Dependencies > Modules > app を選択 > Artiface Name > play-services-location > OK をクリック これでライブラリを依存関係に追加してダウンロードがされます。
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    // 経度と緯度のフィールド定義
    private double _longitude, _latitude = 0;

    // FusedLocationProviderClient
    private FusedLocationProviderClient _fusedLocationProviderClient;

    // LocationRequest
    private LocationRequest _locationRequest;

    // 位置情報が変更された時のそりを行うコールバックオブジェクト。
    private OnUpdateLocation _onUpDatedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FusedLocationProviderClientを取得
        _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // LocationRequestのビルダーを作成
        LocationRequest.Builder builder = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY);
        // LocationRequestオブジェクト生成
        _locationRequest = builder.build();
        // 位置情報が変更された時の処理を行うコールバックオブジェクトを生成
        _onUpDatedLocation = new OnUpdateLocation();
    }

    /**
     * 位置情報追跡の開始を記述する
     * 画面が表示された時に位置情報追跡を開始する
     * 権限許可ダイアログを出す
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        // 位置情報の追跡を開始 requestは位置情報の更新に関する設定情報が格納されたオブジェクト onUpdatedLocationは位置情報が更新された時に呼ばれるコールバックオブジェクト、 looperはコールバックオブジェクトを実行させるスレッドのLooperオブジェクト
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 許可をACCESS_FINE_LOCATIONとACCESS_COARSE_LOCATIONに設定
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            // 許可を求めるダイアログを表示。その際、リクエストコード1000に設定。
            ActivityCompat.requestPermissions(this, permissions, 1000);
            return;
        }
        _fusedLocationProviderClient.requestLocationUpdates(_locationRequest, _onUpDatedLocation, Looper.getMainLooper());
    }

    /**
     * 許可ダイアログの結果を受け取りそれに対する処理を記述する
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 位置情報のパーミッションダイアログでかつ許可を選択したなら
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // 再度許可が下りていないかどうかチェックする
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        // 位置情報追跡開始。
        _fusedLocationProviderClient.requestLocationUpdates(_locationRequest, _onUpDatedLocation, Looper.getMainLooper());
    }

    /**
     * 位置情報の追跡を停止
     * 画面が非表示になった時に位置情報追跡を停止する
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        // 位置情報の追跡を停止
        _fusedLocationProviderClient.removeLocationUpdates(_onUpDatedLocation);

    }

    /**
     * 地図検索ボタンクリック時の処理
     *
     * @param view クリックされたViewこれはボタン。onClickアノテーションで設定される。
     *             onClick属性で指定した場合、View を引数に取ることが必須
     */
    public void onMapSearchButtonClick(View view) {
        Log.d(TAG, "onMapSearchButtonClick");
        // 入力欄に入力されたキーワード文字列を取得
        EditText etSearchWord = findViewById(R.id.etSearchWord);
        String searchWord = etSearchWord.getText().toString();
        try {
            // 入力されたキーワードをURLエンコード。
            searchWord = URLEncoder.encode(searchWord, "UTF-8");
            // マップアプリと連携するURI文字列を生成 地図アプリはgeo:0,0?q=で指定、
            // URIの文字列をもとにOSが起動するアプリケーションを判断してくれる。
            String uriString = "geo:0,0?q=" + searchWord;
            // URI文字列からURIオブジェクトを生成
            Uri uri = Uri.parse(uriString);
            // Intentを生成
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // アクティビティを起動
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "検索キーワードをUTF-8エンコードに失敗", e);
        }
    }

    /**
     * 現在地を表示するボタンクリック時の処理
     *
     * @param view クリックされたViewこれはボタン。onClickアノテーションで設定される。
     */
    public void onMapShowCurrentButtonClick(View view) {
        Log.d(TAG, "onMapShowCurrentButtonClick");
        // フィールドの緯度と経度の値をもとにマップアプリと連携するURI文字列を生成。
        String uriString = "geo:" + _latitude + "," + _longitude;
        // URI文字列からURIオブジェクトを生成
        Uri uri = Uri.parse(uriString);
        // Intentを生成 暗黙的インテントはアクションとURIを指定する
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // アクティビティを起動
        startActivity(intent);
    }

    private class OnUpdateLocation extends LocationCallback {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            Log.d(TAG, "onLocationResult");
            // 直近の位置情報を取得
            Location location = locationResult.getLastLocation();
            if (location != null) {
                // locationオブジェクトから緯度経度を取得
                _latitude = location.getLatitude();
                _longitude = location.getLongitude();
                // 取得した結果をTextViewに表示
                TextView tvLatitude = findViewById(R.id.tvLatitude);
                TextView tvLongitude = findViewById(R.id.tvLongitude);
                tvLatitude.setText(Double.toString(_latitude));
                tvLongitude.setText(Double.toString(_longitude));
            }
        }
    }
}
