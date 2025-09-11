package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
// import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.example.quizapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    // private TextView countLabel, questionLabel;
    private Button answerBtn1, answerBtn2, answerBtn3, answerBtn4;
    private String rightAnswer;
    private int rightAnswerCount;
    private int quizCount = 1;

    private List<List<String>> quizArray = new ArrayList<>();

    private String[][] quizData = {
            // {"都道府県名", "正解", "選択肢１", "選択肢２", "選択肢３"}
            {"北海道", "札幌市", "長崎市", "福島市", "前橋市"},
            {"青森県", "青森市", "広島市", "甲府市", "岡山市"},
            {"岩手県", "盛岡市","大分市", "秋田市", "福岡市"},
            {"宮城県", "仙台市", "水戸市", "岐阜市", "福井市"},
            {"秋田県", "秋田市","横浜市", "鳥取市", "仙台市"},
            {"山形県", "山形市","青森市", "山口市", "奈良市"},
            {"福島県", "福島市", "盛岡市", "新宿区", "京都市"},
            {"茨城県", "水戸市", "金沢市", "名古屋市", "奈良市"},
            {"栃木県", "宇都宮市", "札幌市", "岡山市", "奈良市"},
            {"群馬県", "前橋市", "福岡市", "松江市", "福井市"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.answerBtn1.setOnClickListener(this);
        binding.answerBtn2.setOnClickListener(this);
        binding.answerBtn3.setOnClickListener(this);
        binding.answerBtn4.setOnClickListener(this);

        for (int i = 0; i < quizData.length; i++) {
            // 一時的に使用するArrayListを作成
            ArrayList<String> tmpArray = new ArrayList<>();

            // tempArrayに「問題・答え・選択肢3つを入れていく」
            tmpArray.add(quizData[i][0]);
            tmpArray.add(quizData[i][1]);
            tmpArray.add(quizData[i][2]);
            tmpArray.add(quizData[i][3]);
            tmpArray.add(quizData[i][4]);

            // tmpArrayをquiArrayに追加
            quizArray.add(tmpArray);
        }

        Collections.shuffle(quizArray);

        showNextQuiz();
    }

    // クイズを出題する
    private void showNextQuiz() {
        binding.countLabel.setText(getString(R.string.count_label, quizCount));
        
        
        // クイズを一問取り出す
        List<String> quiz = quizArray.get(0);

        binding.questionLabel.setText(quiz.get(0));

        binding.answerBtn1.setText(quiz.get(1));
        binding.answerBtn2.setText(quiz.get(2));
        binding.answerBtn3.setText(quiz.get(3));
        binding.answerBtn4.setText(quiz.get(4));

        rightAnswer = quiz.get(1);

        // クイズを削除
        quizArray.remove(0);
    }

    @Override
    public void onClick(View v) {
        Button answerBtn = findViewById(v.getId());
        String btnText = answerBtn.getText().toString();

        String dialogTitle;
        if (btnText.equals(rightAnswer)) {
            // 正解
            dialogTitle = "正解";
            rightAnswerCount++;
            Log.v("MY_LOG", "正解");
        } else {
            // 不正解
            dialogTitle = "不正解...";
            Log.v("MY_LOG", "不正解");
        }

        // ダイアログオブジェクトを作成
        DialogFragment dialogFragment = new AnswerDialogFragment();

        // ダイアログに値を渡す バンドルとはキーと値のペア、セットのことを指す。
        Bundle args = new Bundle();
        args.putString("TITLE", dialogTitle);
        args.putString("MESSAGE", rightAnswer);
        dialogFragment.setArguments(args);

        // ダイアログが閉じないようにする
        dialogFragment.setCancelable(false);

        // ダイアログの表示
        dialogFragment.show(getSupportFragmentManager(), "answer_dialog");

    }

    public void checkQuizCount() {
        // クイズ終了処理
        if (quizCount == 5) {
            // 結果画面を表示する
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
            startActivity(intent);
            Log.v("MY_LOG", "クイズ終了");
        // クイズ継続処理
        } else {
            quizCount ++;
            showNextQuiz();
        }
    }

}