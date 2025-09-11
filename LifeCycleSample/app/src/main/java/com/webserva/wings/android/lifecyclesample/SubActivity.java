package com.webserva.wings.android.lifecyclesample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LifeCycleSample", "Sub onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        setContentView(R.layout.activity_sub);
    }

    @Override
    protected void onStart() {
        Log.i("LifeCycleSample", "Sub onStart() called");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("LifeCycleSample", "Sub onRestart() called");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("LifeCycleSample", "Sub onResume() called");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("LifeCycleSample", "Sub onResume() called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("LifeCycleSample", "Sub onStop() called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("LifeCycleSample", "Sub onDestroy() called");
        super.onDestroy();
    }

    public void onButtonClick(View view) {
        finish();
    }
}