package com.webserva.wings.android.servicesample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

/**
 * Serviceクラス。Wizardを使って作成。
 * 通知の実装。通知ドロワーへの表示。また通知ドロワーをタップしたらアクティビティを起動するようにする。
 */
public class SoundManageService extends Service {

    // メディアプレーヤーフィールド
    private MediaPlayer _player;

    // 通知チャンネルID文字列定数
    private static final String CHANNEL_ID = "soundmanagerservice_notification_channel";

    // 追加
    @Override
    public void onCreate() {
        super.onCreate();
        // フィールドのメディアプレーヤーオブジェクトを生成。
        _player = new MediaPlayer();
        // 通知チャネル名をstrings.xmlから取得
        String name = getString(R.string.msg_notification_channel_name);
        // 通知チャネルの重要度を標準に設定
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        // 通知チャネルを生成。
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        // NotificationManagerオブジェクトを取得
        NotificationManager manager = getSystemService(NotificationManager.class);
        // 通知チャネルの設定
        manager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        // 再生中ならば
        if(_player.isPlaying()) {
            // プレーヤーを停止
            _player.stop();
        }
        // プレーヤーを開放
        _player.release();
        super.onDestroy();
    }

    /**
     * この中に基本的にサービスで実行したい処理を、バックグラウンドで行う処理を記述する。
     * @param intent The Intent supplied to {@link android.content.Context#startService},
     * as given.  This may be null if the service is being restarted after
     * its process has gone away, and it had previously returned anything
     * except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to
     * start.  Use with {@link #stopSelfResult(int)}.
     *
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 音声ファイルURI文字列を生成
        String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.birdhamming;
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        // URIオブジェクトを生成
        try {
            // 音声ファイルを指定
            _player.setDataSource(SoundManageService.this, mediaFileUri);
            // 非同期でのメディア再生準備が完了した際のリスナ設定。
            _player.setOnPreparedListener(new PlayerPreparedListener());
            // メディア再生が終了した際のリスナを設定。
            _player.setOnCompletionListener(new PlayerCompletionListener());
            // 非同期でメディア再生
            _player.prepareAsync();
        } catch (IOException ex) {
            Log.e("ServiceSample", "メディアプレーヤー準備時のれいがい発生", ex);
        }
        // 定数を返す = 1
        return  START_STICKY;
    }

    /**
     * コンストラクタは削除したが、こちらはウィザードが作成してくれたものをそのままのこしている 。
     * @param intent The Intent that was used to bind to this service,
     * as given to {@link android.content.Context#bindService
     * Context.bindService}.  Note that any extras that were included with
     * the Intent at that point will <em>not</em> be seen here.
     *
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * メディア再生準備が完了したときのリスナクラス
     */
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // メディアを再生
            mp.start();

            // Notificationを作成するBuilderクラス生成。
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this, CHANNEL_ID);
            // 通知エリアに表示されるアイコンを設定。
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            // 通知ドロワーでの表示タイトルを設定。
            builder.setContentTitle(getString(R.string.msg_notification_title_start));
            // 通知ドロワーでの表示メッセージを設定。
            builder.setContentText(getString(R.string.msg_notification_text_start));
            // 起動先Activityクラスを指定したIntentオブジェクトを生成。
            Intent intent = new Intent(SoundManageService.this, MainActivity.class);
            // 起動先アクティビティに引継ぎデータを格納
            intent.putExtra("fromNotification", true);
            // PendingIntentオブジェクトを取得。
            PendingIntent stopServiceIntent = PendingIntent.getActivity(SoundManageService.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            // PendingIntentオブジェクトをビルダーに設定
            builder.setContentIntent(stopServiceIntent);
            // タップされた通知を自動的に消去するように設定。
            builder.setAutoCancel(true);
            // BuilderからNotificationオブジェクトを生成。
            Notification notification = builder.build();
            // Notificationオブジェクトをもとにサービスをフォアグラウンド化。マニフェストファイルに2か所追記が必要だった。
            startForeground(200, notification);
        }
    }


    /**
     * メディア再生が終了したときのリスナクラス。
     */
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // Notificationを作成するBuilderクラス生成。
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this, CHANNEL_ID);
            // 通知エリアに表示されるアイコンを設定。
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            // 通知ドロワーでの表示タイトルを設定。
            builder.setContentTitle(getString(R.string.msg_notification_title_finish));
            // 通知ドロワーでの表示メッセージを設定。
            builder.setContentText(getString(R.string.msg_notification_text_finish));
            // BuilderからNotificationオブジェクトを生成。
            Notification notification = builder.build();
            // NotificationManagerCompatオブジェクトを取得。
            NotificationManagerCompat manager = NotificationManagerCompat.from(SoundManageService.this);
            // 通知。下記if文を追加しないと、近年のアンドロイドアプリは通知の権限の操作すらできなかった。。
            if (ActivityCompat.checkSelfPermission(SoundManageService.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.notify(100, notification);

            // メディアプレーヤーを狩猟
            stopSelf();
        }
    }
}
