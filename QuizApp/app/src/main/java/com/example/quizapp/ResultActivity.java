package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActivityResultBinding binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 正解数を受け取る
        int score = getIntent().getIntExtra("RIGHT_ANSWER_COUNT", 0);
        binding.scoreLabel.setText(getString(R.string.result_score, score));

        // 正解数によってのだ仕分け
        if (score == 5) {
            binding.starImage1.setImageResource(R.drawable.baseline_star_24);
            binding.starImage2.setImageResource(R.drawable.baseline_star_24);
            binding.starImage3.setImageResource(R.drawable.baseline_star_24);
            binding.starImage4.setImageResource(R.drawable.baseline_star_24);
            binding.starImage5.setImageResource(R.drawable.baseline_star_24);
            binding.resultMessage.setText(R.string.message_5);
        } else if (score == 4) {
            binding.starImage1.setImageResource(R.drawable.baseline_star_24);
            binding.starImage2.setImageResource(R.drawable.baseline_star_24);
            binding.starImage3.setImageResource(R.drawable.baseline_star_24);
            binding.starImage4.setImageResource(R.drawable.baseline_star_24);
            binding.resultMessage.setText(R.string.message_4);
        } else if (score == 3) {
            binding.starImage1.setImageResource(R.drawable.baseline_star_24);
            binding.starImage2.setImageResource(R.drawable.baseline_star_24);
            binding.starImage3.setImageResource(R.drawable.baseline_star_24);
            binding.resultMessage.setText(R.string.message_3);
        } else if (score == 2) {
            binding.starImage1.setImageResource(R.drawable.baseline_star_24);
            binding.starImage2.setImageResource(R.drawable.baseline_star_24);
            binding.resultMessage.setText(R.string.message_2);
        } else if (score == 1) {
            binding.starImage1.setImageResource(R.drawable.baseline_star_24);
            binding.resultMessage.setText(R.string.message_1);
        } else {
            binding.resultMessage.setText(R.string.message_0);
        }

        // もう一度ボタン
        binding.tryAgainBtn.setOnClickListener(v -> startActivity(new Intent(ResultActivity.this, MainActivity.class)));
    }
}