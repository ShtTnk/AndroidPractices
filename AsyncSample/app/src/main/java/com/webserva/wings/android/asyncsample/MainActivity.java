package com.webserva.wings.android.asyncsample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ListMenuPresenter;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    // ログに記載するタグ文字列
    private static final String DEBUG_TAG = "AsyncSample";
    // お天気情報のURL
    private static final String WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja";
    // お天気APIにアクセスするためのAPIキー
    private static final String APP_ID = "fead9cf4ea5c27f7a977a750afdace15";
    // りすとびゅーに表示させるデータ
    private List<Map<String, String>> _list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _list = createPrefectureList();

        ListView lvCityList = findViewById(R.id.lvCityList);
        String[] from = {"name"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, _list, android.R.layout.simple_list_item_1, from, to);
        lvCityList.setAdapter(adapter);
        lvCityList.setOnItemClickListener(new ListItemClickListener());
    }

    // リストビューに表示させる転記ポイントリストデータを生成するメソッド。
    private List<Map<String, String>> createPrefectureList() {
        List<Map<String, String>> list = new ArrayList<>();

        String[] japaneseNames = {"北海道", "青森", "岩手", "宮城", "秋田", "山形", "福島",
                "茨城", "栃木", "群馬", "埼玉", "千葉", "東京", "神奈川",
                "新潟", "富山", "石川", "福井", "山梨", "長野", "岐阜",
                "静岡", "愛知", "三重", "滋賀", "京都", "大阪", "兵庫",
                "奈良", "和歌山", "鳥取", "島根", "岡山", "広島", "山口",
                "徳島", "香川", "愛媛", "高知", "福岡", "佐賀", "長崎",
                "熊本", "大分", "宮崎", "鹿児島", "沖縄"};

        String[] englishNames = {"Hokkaido", "Aomori", "Iwate", "Miyagi", "Akita", "Yamagata", "Fukushima",
                "Ibaraki", "Tochigi", "Gunma", "Saitama", "Chiba", "Tokyo", "Kanagawa",
                "Niigata", "Toyama", "Ishikawa", "Fukui", "Yamanashi", "Nagano", "Gifu",
                "Shizuoka", "Aichi", "Mie", "Shiga", "Kyoto", "Osaka", "Hyogo",
                "Nara", "Wakayama", "Tottori", "Shimane", "Okayama", "Hiroshima", "Yamaguchi",
                "Tokushima", "Kagawa", "Ehime", "Kochi", "Fukuoka", "Saga", "Nagasaki",
                "Kumamoto", "Oita", "Miyazaki", "Kagoshima", "Okinawa"};

        for (int i = 0; i < japaneseNames.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", japaneseNames[i]);
            map.put("q", englishNames[i]);
            list.add(map);
        }
        return list;
    }

    // お天気情報の取得処理を行うメソッド

    private void receiveWeatherInfo(final String urlFull) {
        // ここに非同期でお天気情報を取得するメソッドを記述
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            String result = "";
            try {
                result = new WeahterInfoBackgroundReceiver(urlFull).call();
            }
            catch (ExecutionException ex) {
                Log.w(DEBUG_TAG, "非同期処理で例外発生", ex);
            }
            catch(InterruptedException ex) {
                Log.w(DEBUG_TAG, "非同期処理結果の取得で例外発生", ex);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            final String finalResult = result;
            runOnUiThread(() -> showWeatherInfo(finalResult));
        });
    }

    // リストがタップされたときの処理が記述されたリスナクラス
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> item = _list.get(position);
            String q = item.get("q");
            String urlFull = WEATHERINFO_URL + "&q=" + q + "&appid=" + APP_ID;

            receiveWeatherInfo(urlFull);
        }
    }

    // 非同期でお天気情報APIにアクセスするためのクラス
    @WorkerThread
    private class WeahterInfoBackgroundReceiver implements Callable<String> {
        // URL
        private final String _urlFull;
        // コンストラクタ
        public WeahterInfoBackgroundReceiver(String urlFull) {
            _urlFull = urlFull;
        }
        @Override
        public String call() throws Exception {
            String result = "";
            // HTTP接続オブジェクト
            HttpURLConnection con = null;
            // HTTP接続のレスポンスデータとして取得するInputStream
            InputStream is = null;
            try {
                // URLオブジェクト
                URL url = new URL(_urlFull);
                // URLオブジェクトからHttpURLConnectionを取得
                con = (HttpURLConnection) url.openConnection();
                // 接続に使っていい時間を指定
                con.setConnectTimeout(1000);
                // データの取得に使ってもよい時間を指定
                con.setReadTimeout(1000);
                // HTTP接続メソッドをGETに
                con.setRequestMethod("GET");
                // 接続
                con.connect();
                // HttpURLConnectionからレスポンスデータを取得
                is = con.getInputStream();
                // レスポンスデータをInputStreamオブジェクトに変換
                result = is2String(is);
            } catch (MalformedURLException ex) {
                Log.e(DEBUG_TAG, "URL変換失敗");
            } catch (SocketTimeoutException ex) {
                Log.e(DEBUG_TAG, "タイムアウト", ex);
            } catch (IOException ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            }finally {
                if (con != null) {
                    con.disconnect();
                }
                if(is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Log.e(DEBUG_TAG, "InputStreamかいほう失敗", ex);
                    }
                }
            }
            return result;
        }
    }

    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        char[] b = new  char[1024];
        int line;
        while (0 <= (line = reader.read(b))){
            sb.append(b, 0, line);
        }
        return sb.toString();
    }

    //
    @UiThread
    private void showWeatherInfo(String result) {
        // 都市名
        String cityName = "";
        // 天気
        String weather = "";
        // 緯度
        String latitude = "";
        // 軽度
        String longitude = "";
        try {
            // ルートJsonを作成
            JSONObject rootJSON = new JSONObject(result);
            // 都市名文字列を取得
            cityName = rootJSON.getString("name");
            // 緯度
            JSONObject coordJSON = rootJSON.getJSONObject("coord");
            latitude = coordJSON.getString("lat");
            //軽度
            longitude = coordJSON.getString("lon");
            // 転記Json配列
            JSONArray weatherJSONArray = rootJSON.getJSONArray("weather");
            JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
            weather = weatherJSON.getString("description");
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "JSON解析失敗");
        }
        // 画面に表示する文字列を生成
        String telop = cityName + "の天気";
        // 天気の詳細情報を表示する文字列を生成
        String desc = "現在は" + weather + "です。 \n 緯度は" + latitude + "度で軽度は" + longitude + "度です。";
        // 天気情報を表示するTextVIewを取得
        TextView tvWeatherTelop = findViewById(R.id.tvWeatherTelop);
        TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);
        // 天気情報を表示
        tvWeatherTelop.setText(telop);
        tvWeatherDesc.setText(desc);
    }
}