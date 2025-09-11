package com.webserva.wings.android.managercalllback;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private EditText editText;
    private boolean dialogShowing = false;

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

        // SensorManager取得
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // コールバック登録
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // editText
        editText = findViewById(R.id.editText);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!dialogShowing) {
            editText.setText("Accel: x=" + x + " y=" + y + " z=" + z);
            dialogShowing = !dialogShowing;
            new AlertDialog.Builder(this)
                    .setTitle("Sensor Alert")
                    .setMessage("Accel too high!")
                    .setPositiveButton("OK", (d, which) -> dialogShowing = false)
                    .show();
        } else {
            // DONOTHING;
            try {
                Thread.sleep(3000);
                dialogShowing = !dialogShowing;
            } catch (Exception e) {
                Log.d("TAG", "TAG");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Sensor", "Accuracy changed: " + accuracy);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 登録解除しないとバッテリー食う
        sensorManager.unregisterListener(this);
    }
}
