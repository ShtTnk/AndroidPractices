package com.webserva.wings.android.fragmentsample;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

/**
 * フラグメントで注文完了を表現する
 */
public class MenuThanksFragment extends Fragment {

    public MenuThanksFragment() {
        super(R.layout.fragment_menu_thanks);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvMenu = view.findViewById(R.id.lvMenu);

        // このフラグメントに埋め込まれた引継ぎデータを取得
        Bundle extras = getArguments();
        // 注文した定食名と金額変数を用意
        String menuName = "";
        String menuPrice = "";
        if (extras != null) {
            menuName = extras.getString("menuName");
            menuPrice = extras.getString("menuPrice");
            Log.i("menuPrice", menuPrice);
        }
        // TextViewを取得
        TextView tvMenuName = view.findViewById(R.id.tvMenuName);
        TextView tvMenuPrice = view.findViewById(R.id.tvMenuPrice);
        tvMenuName.setText(menuName);
        tvMenuPrice.setText(menuPrice);

        // 戻るを取得
        Button btBackButton = view.findViewById(R.id.btThxBack);
        // 戻るにリスナ登録
        btBackButton.setOnClickListener(v -> {
//            // フラグメントマネジャー取得
//            FragmentManager manager = getParentFragmentManager();
//            // バックスタックのひとつ前の状態に戻る
//            manager.popBackStack();
        FragmentManager manager = getParentFragmentManager();
        Activity parentActivity = getActivity();
        View fragmentMainContainer = parentActivity.findViewById(R.id.fragmentMainContainer);
        View fragmentThanksContainer = parentActivity.findViewById(R.id.fragmentThanksContainer);
        if(fragmentMainContainer != null) {
            manager.popBackStack();
        } else if (fragmentThanksContainer != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.remove(MenuThanksFragment.this);
            transaction.commit();
        }
    });

    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // タップされた行のデータを取得
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            // 定食名と金額を取得
            String menuName = item.get("name");
            String menuPrice = item.get("price");

            // 引継ぎデータをまとめて格納 Bundleオブジェクト
            Bundle bundle = new Bundle();
            // Bundleに引継ぎデータを格納
            bundle.putString("menuName", menuName);
            bundle.putString("menuPrice", menuPrice);

            // フラグメントマネジャー取得
            FragmentManager manager = getParentFragmentManager();
            // フラグメントトランザクションの開始
            FragmentTransaction transaction = manager.beginTransaction();
            // フラグメントトランザクションが正しく動作するように設定
            transaction.setReorderingAllowed(true);
            // 現在の表示内容をバックスタックに追加
            transaction.addToBackStack("Only List");
            // fragmentMainContainerのフラグメントを注文完了フラグに書き換え。
            transaction.replace(R.id.fragmentMainContainer, MenuThanksFragment.class, bundle);
            // フラグメントトランザクションのコミット
            transaction.commit();
        }
    }
}
