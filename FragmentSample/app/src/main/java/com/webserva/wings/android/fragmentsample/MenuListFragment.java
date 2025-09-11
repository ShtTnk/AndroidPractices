package com.webserva.wings.android.fragmentsample;

import android.app.Activity;
import android.media.MediaRouter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuListFragment extends Fragment {

    // リストビューをあらわすフィールド
    private ListView _lvMenu;
    // リストビューに表示するデータリスト
    private List<Map<String, Object>> _menuList;
    // SimpleAdapterの第四引数で使用するフィールド
    private static final String[] FROM = {"name", "price"};
    // SimpleAdapterの第五引数で使用するフィールド
    private static final int[] TO = {android.R.id.text1, android.R.id.text2};

    /**
     * コンストラクタ
     */
    public MenuListFragment() {
        super(R.layout.fragment_menu_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance){
        super.onViewCreated(view, savedInstance);
        // 画面部品ListViewを取得する
        ListView lvMenu = view.findViewById(R.id.lvMenu);
        // 定食メニューリストオブジェクトをprivateメソッドを利用して用意し、フィールドに格納
        _menuList = createTeishokuList();

        // このフラグメントが所属するアクテビティオブジェクトを取得
        Activity parentActivity = getActivity();
        // SimpleAdapter第４・5引数from用データの用意

        // SimpleAdapterの生成
        SimpleAdapter adapter = new SimpleAdapter(parentActivity, _menuList, android.R.layout.simple_list_item_2, FROM, TO);
        // アダプターの登録
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(new ListItemClickListener());
    }

    // 定食リストをを生成するメソッド
    private List<Map<String, Object>> createTeishokuList() {
        // 定食メニューリスト用のListオブジェクトを用意
        List<Map<String, Object>> menuList = new ArrayList<>();
        // からあげ定食のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        Map<String, Object> menu = new HashMap<>();
        menu.put("name", "からあげ定食");
        menu.put("price", "¥" + 800);  // 文字列で格納
        menu.put("desc", "若鳥のから揚げにサラダ、ごはんとお味噌汁がつきます。");
        menuList.add(menu);
        // ハンバーグ定食のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録
        menu = new HashMap<>();
        menu.put("name", "ハンバーグ定食");
        menu.put("price", "¥" + 850);
        menu.put("desc", "ハンバーグの定食です。");
        menuList.add(menu);
        // 大量生成
        for (int i = 2; i < 100; i++) {
            menu = new HashMap<>();
            menu.put("name", "ハンバーグ定食" + i);
            menu.put("price", "¥" + (850 + i));
            menu.put("desc", "ハンバーグ定食です。" + i);
            menuList.add(menu);
        }
        return menuList;
    }
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // タップされた行のデータを取得
            Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
            // 定食名と金額を取得
            String menuName = (String) item.get("name");
            String menuPrice = (String) item.get("price");
            // 引継ぎデータをまとめて格納 Bundleオブジェクト
            Bundle bundle = new Bundle();
            // Bundleに引継ぎデータを格納
            bundle.putString("menuName", menuName != null ? menuName : "");
            bundle.putString("menuPrice", menuPrice != null ? menuPrice: "");

            // フラグメントマネジャー取得
            FragmentManager manager = getParentFragmentManager();
            // フラグメントトランザクションの開始
            FragmentTransaction transaction = manager.beginTransaction();
            // フラグメントトランザクションが正しく動作するように設定
            transaction.setReorderingAllowed(true);
//            // 現在の表示内容をバックスタックに追加
//            transaction.addToBackStack("Only List");
//            // fragmentMainContainerのフラグメントを注文完了フラグに書き換え。
//            transaction.replace(R.id.fragmentMainContainer, MenuThanksFragment.class, bundle);
//            // フラグメントトランザクションのコミット
//            transaction.commit();
            // このフラグメントが所属するアクテビティを取得
            Activity parentActivity = getActivity();
            // 自分が所属するアクテビティからfragmentMainContainerを取得
            View fragmentMainContainer = parentActivity.findViewById(R.id.fragmentMainContainer);
            // 自分が所属するアクテビティからfragmentThxContainerを取得
            View fragmentThanksContainer = parentActivity.findViewById(R.id.fragmentThanksContainer);
            // fragmentContainerが存在するなら
            if(fragmentMainContainer != null) {
                transaction.addToBackStack("only List");
                transaction.replace(R.id.fragmentMainContainer, MenuThanksFragment.class, bundle);
            } else if (fragmentThanksContainer != null) {
                // Fragment MainContainerが存在するなら
                transaction.replace(R.id.fragmentThanksContainer, MenuThanksFragment.class, bundle);
            }
            transaction.commit();
        }
    }
}