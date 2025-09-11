package com.webserva.wings.android.mediasample;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;

/**
 * MediaPlayer再生クラス
 * res/rawフォルダを作成し音声ファイルを追加
 */
public class MainActivity extends AppCompatActivity {

    // メディアプレーヤーフィールド
    private MediaPlayer _player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // フィールドのメディアプレーヤーオブジェクトを生成
        _player = new MediaPlayer();
        // 音声ファイルのURI文字列を作成。
        String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.birdhamming;
        // 音声ファイルのURI文字列をもとにURIオブジェクトを生成
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        try {
            // メディアプレーヤーに音声ファイルを指定。
            _player.setDataSource(MainActivity.this, mediaFileUri);
            //非同期でのメディア再生準備が完了した際のリスナを設定。
            _player.setOnPreparedListener(new PlayerPreparedListener());
            // メディア再生が終了した際のリスナを設定。
            _player.setOnCompletionListener(new PlayerCompletionListener());
            // 非同期でメディア再生を準備
            _player.prepareAsync();
        } catch (IOException e) {
            Log.e("MediaSample", "メディアプレーヤー準備時の例外発生", e);
        }

        // スイッチを取得
        SwitchMaterial loopSwitch = findViewById(R.id.swLoop);
        // スイッチにリスナ設定をする
        loopSwitch.setOnCheckedChangeListener(new LoopSwitchChangedListener());
    }

    /**
     * アクテビティ終了時の処理
     */
    @Override
    protected void onStop() {
        // プレーヤーが再生中ならば
        if (_player.isPlaying()) {
            // プレーヤーを停止。
            _player.stop();
        }
        // 開放
        _player.release();
        // 親呼び出し
        super.onStop();
    }

    /**
     * プレーヤーの再生準備が整ったときのリスナクラス。
     */
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // 各ボタンをタップ可能に設定
            Button btPlay = findViewById(R.id.btPlay);
            btPlay.setEnabled(true);
            Button btBack = findViewById(R.id.btBack);
            btBack.setEnabled(true);
            Button btnForward = findViewById(R.id.btForward);
            btnForward.setEnabled(true);
        }
    }

    /**
     * 再生が終了したときのリスナクラス。
     */
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (!_player.isPlaying()){
                // 再生ボタンのラベルを「再生」に設定
                Button btPlay = findViewById(R.id.btPlay);
                btPlay.setText(R.string.bt_play_play);
            }
        }
    }

    /**
     * 非推奨だが、xmlのonClick属性で定義している
     * 再生ボタンタップの処理をするメソッド
     */
    public void onPlayButtonClick(View view) {
        // 再生ボタンを取得
        Button btPlay = findViewById(R.id.btPlay);
        // プレーヤーが再生中ならば
        if (_player.isPlaying()) {
            // プレーヤーを一時停止。
            _player.pause();
            // 再生ボタンのラベルを再生に設定
            btPlay.setText(R.string.bt_play_play);
        }
        // プレーヤーが再生中でなければ
        else {
            // プレーヤーを再生
            _player.start();
            // 再生ボタンのラベルを「一時停止に変更」
            btPlay.setText(R.string.bt_play_pause);
        }
    }

    /**
     * 戻るボタンをクリックしたときの処理
     *
     */
    public void onBackButtonClick(View view) {
        // 再生位置を先頭に変更。
        _player.seekTo(0);
    }

    /**
     * 進むボタンをクリックしたときの処理
     * @param view
     */
    public void onForwardButtonClick(View view) {
        // 現在再生中のメディアファイルの長さを取得
        int duration = _player.getDuration();
        // 再生位置を終端に変更。
        _player.seekTo(duration);
        // 再生中でなければ、
        if (!_player.isPlaying()) {
            // 再生ボタンのラベルを変更「一時停止」
            Button btPlay = findViewById(R.id.btPlay);
            btPlay.setText(R.string.bt_play_pause);
            // 再生を開始。
            _player.start();
        }
    }

    /**
     * リピート再生のコードを記述
     * onCheckChangedはtrue/falseがボタンの状態によって切り替わる。
     */
    private class LoopSwitchChangedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // ループするかどうかの設定 true/falseが入っている
            _player.setLooping(isChecked);
        }
    }
}

//下記はChatGptの提案。Viewは使わないのであればボタンクリック処理系統のメソッドの記述は改良可能
//public void onPlayButtonClick(View view) {
//    Button btPlay = (Button)view; // もしXMLのボタンならこれでOK
//}
