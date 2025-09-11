package com.webserva.wings.android.asynchandlersample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    // ログに記載するタグ文字列
    private static final String DEBUG_TAG = "AsyncSample";
    // お天気情報のURL
    private static final String WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather?lang=ja&units=metric";
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
//        // ListViewに色をつける方法
//        lvCityList.setAdapter(new SimpleAdapter(MainActivity.this, _list, android.R.layout.simple_list_item_1, from, to){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                if (position % 2 == 0) {
//                    view.setBackgroundColor(0xFFE0E0E0); // 薄グレー
//                } else {
//                    view.setBackgroundColor(0xFFFFFFFF); // 白
//                }
//                return view;
//            }
//        });
    }

    // リストビューに表示させる転記ポイントリストデータを生成するメソッド。
    private List<Map<String, String>> createPrefectureList() {
        List<Map<String, String>> list = new ArrayList<>();

        String[] japaneseNames = {"北海道", "青森", "岩手", "宮城", "秋田", "山形", "福島", "茨城", "栃木", "群馬", "埼玉", "千葉", "東京", "神奈川", "新潟", "富山", "石川", "福井", "山梨", "長野", "岐阜", "静岡", "愛知", "三重", "滋賀", "京都", "大阪", "兵庫", "奈良", "和歌山", "鳥取", "島根", "岡山", "広島", "山口", "徳島", "香川", "愛媛", "高知", "福岡", "佐賀", "長崎", "熊本", "大分", "宮崎", "鹿児島", "沖縄"};

        String[] englishNames = {"Hokkaido", "Aomori", "Iwate", "Miyagi", "Akita", "Yamagata", "Fukushima", "Ibaraki", "Tochigi", "Gunma", "Saitama", "Chiba", "Tokyo", "Kanagawa", "Niigata", "Toyama", "Ishikawa", "Fukui", "Yamanashi", "Nagano", "Gifu", "Shizuoka", "Aichi", "Mie", "Shiga", "Kyoto", "Osaka", "Hyogo", "Nara", "Wakayama", "Tottori", "Shimane", "Okayama", "Hiroshima", "Yamaguchi", "Tokushima", "Kagawa", "Ehime", "Kochi", "Fukuoka", "Saga", "Nagasaki", "Kumamoto", "Oita", "Miyazaki", "Kagoshima", "Okinawa"};

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
        TextView tvWeatherTelop = findViewById(R.id.tvWeatherTelop);
        tvWeatherTelop.setText("");
        TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);
        tvWeatherDesc.setText("");

        Looper looper = Looper.getMainLooper();
        Handler handler = HandlerCompat.createAsync(getMainLooper());
        WeatherInfoBackGroundReceiver backGroundReceiver = new WeatherInfoBackGroundReceiver(handler, urlFull);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(backGroundReceiver);
    }

    // リストがタップされたときの処理が記述されたリスナクラス
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> item = _list.get(position);
            String q = item.get("q");
            String urlFull = WEATHERINFO_URL + "&q=" + q + "&appid=" + APP_ID;
            receiveWeatherInfo(urlFull);
            // showWeatherInfo(q);
        }
    }

    // バックグラウンド処理
    private class WeatherInfoBackGroundReceiver implements Runnable {
        private final Handler _handler;
        private final String _urlFull;

        public WeatherInfoBackGroundReceiver(Handler handler, String urlFull) {
            _handler = handler;
            _urlFull = urlFull;
        }

        @WorkerThread
        @Override
        public void run() {
            String result = "";
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                URL url = new URL(_urlFull);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(2000);
                con.setReadTimeout(2000);
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                result = is2String(is);
            } catch (Exception ex) {
                Log.e(DEBUG_TAG, "通信失敗", ex);
            } finally {
                if (con != null) con.disconnect();
                if (is != null) try {
                    is.close();
                } catch (IOException ignore) {
                }
            }

            // UIスレッドで呼び出し
            final String finalResult = result;
            _handler.post(() -> showWeatherInfo(finalResult));
        }
    }

    private String is2String(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        char[] b = new char[1024];
        int line;
        while (0 <= (line = reader.read(b))) {
            sb.append(b, 0, line);
        }
        return sb.toString();
    }

    /**
     * お天気情報をJSONで取得してパースして表示する。
     *
     * @param result
     */
    @UiThread
    private void showWeatherInfo(String result) {
        try {
            JSONObject rootJSON = new JSONObject(result);
            Log.d("JSON", rootJSON.toString());
            String cityName = rootJSON.getString("name");

            JSONObject coordJSON = rootJSON.getJSONObject("coord");
            String latitude = coordJSON.getString("lat");
            String longitude = coordJSON.getString("lon");

            JSONArray weatherJSONArray = rootJSON.getJSONArray("weather");
            JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
            String weather = weatherJSON.getString("description");

            JSONObject mainJSON = rootJSON.getJSONObject("main");
            String temp = mainJSON.getString("temp");
            String tempMin = mainJSON.getString("temp_min");
            String tempMax = mainJSON.getString("temp_max");

            // 表示用テキスト
            String telop = cityName + " の天気";
            String desc = "現在: " + weather + "\n" + "緯度: " + latitude + "\n" + "経度: " + longitude + "\n" + "気温: " + temp + "℃\n" + "最低気温: " + tempMin + "℃\n" + "最高気温: " + tempMax + "℃";

            TextView tvWeatherTelop = findViewById(R.id.tvWeatherTelop);
            TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);

            tvWeatherTelop.setText(telop);
            tvWeatherDesc.setText(desc);

        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "JSON解析失敗", e);
        }
    }

    @UiThread
    private void addMsg(String msg) {
        // tvWeatherDescのTextView
        TextView tvWeatherDesc = findViewById(R.id.tvWeatherDesc);
        String msgNow = tvWeatherDesc.getText().toString();
        if (!msgNow.equals("")) {
            msgNow += "\n";
        }
        // 引数のメッセージを追加
        msgNow += msg;
        // 追加されたメッセージをTextViewに表示
        tvWeatherDesc.setText(msgNow);
    }
}
