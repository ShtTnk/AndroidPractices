package com.example.quizapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AnswerDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // 値を受け取る
        String title = requireArguments().getString("TITLE");
        String message = requireArguments().getString("MESSAGE");

        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(title)
                .setMessage("答え：" + message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ボタン押下時に動作する内容
                        ((MainActivity) requireActivity()).checkQuizCount();
                    }
                })
                .create();
    }
}
