package com.example.handsonreview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("HOME");
    }

    public void launchSettings(View v) {
        // launch a new activity
        Log.d("launchSettings", "Executed");
        Intent i = new Intent(this, SettingsActivity.class);
        String message = ((EditText)findViewById(R.id.editText)).getText().toString();
        // key value を渡すことができる
        i.putExtra("COOL", message);
        startActivity(i);
    }

}