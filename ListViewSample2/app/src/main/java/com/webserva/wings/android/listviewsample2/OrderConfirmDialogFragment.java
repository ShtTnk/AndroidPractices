package com.webserva.wings.android.listviewsample2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * Androidでダイアログを表示する方法。
 * 1. DialogFragmentを継承
 * 2. onCreateDialogでビルダーを使用してDialogオブジェクトをリターン
 * 3. アクティビティでオブジェクトを生成し。showメソッドを使用
 */
public class OrderConfirmDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance) {
        // ビルダー生成を生成してリターンする。
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_msg)
                .setPositiveButton(R.string.dialog_btn_ok,new DialogButtonClickListener())
                .setNegativeButton(R.string.dialog_btn_ng,new DialogButtonClickListener())
                .setNeutralButton(R.string.dialog_btn_nu,new DialogButtonClickListener())
                .create();
    }
    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // トースト用変数
            String msg = "";
            // タップされたアクションボタンで分岐
            switch (which){
                // positiveならば
                case DialogInterface.BUTTON_POSITIVE:
                    msg = getString(R.string.dialog_ok_toast);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    msg = getString(R.string.dialog_ng_toast);
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    msg = getString(R.string.dialog_nu_toast);
                    break;
            }
            // getActivityで呼び出されたときのアクティビティを取得する。このメソッドはどこで呼び出されるのかわからないため、このように記述することができる。
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }
}
