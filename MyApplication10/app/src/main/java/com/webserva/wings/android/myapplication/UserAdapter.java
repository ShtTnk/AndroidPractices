package com.webserva.wings.android.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ユーザーリスト表示用 Adapter。
 * 削除モード対応で、背景色変更・ゴミ箱アイコン表示。
 */
public class UserAdapter extends BaseAdapter {

    private List<User> users;
    private LayoutInflater inflater;
    private boolean isDeleteMode = false;

    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDeleteMode(boolean deleteMode) {
        this.isDeleteMode = deleteMode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() { return users.size(); }

    @Override
    public Object getItem(int position) { return users.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public int getViewTypeCount() { return 3; }

    @Override
    public int getItemViewType(int position) {
        return users.get(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        User user = users.get(position);

        ViewHolderBase holder = null;

        if (convertView == null) {
            switch (type) {
                case User.TYPE_ADMIN:
                    convertView = inflater.inflate(R.layout.item_admin, parent, false);
                    holder = new AdminViewHolder();
                    ((AdminViewHolder) holder).name = convertView.findViewById(R.id.txtAdminName);
                    ((AdminViewHolder) holder).expiry = convertView.findViewById(R.id.txtAdminExpiry);
                    ((AdminViewHolder) holder).icon = convertView.findViewById(R.id.imgIcon);
                    convertView.setTag(holder);
                    break;
                case User.TYPE_NORMAL:
                    convertView = inflater.inflate(R.layout.item_normal, parent, false);
                    holder = new NormalViewHolder();
                    ((NormalViewHolder) holder).name = convertView.findViewById(R.id.txtNormalName);
                    ((NormalViewHolder) holder).expiry = convertView.findViewById(R.id.txtNormalExpiry);
                    ((NormalViewHolder) holder).icon = convertView.findViewById(R.id.imgIcon);
                    convertView.setTag(holder);
                    break;
                case User.TYPE_GUEST:
                    convertView = inflater.inflate(R.layout.item_guest, parent, false);
                    holder = new GuestViewHolder();
                    ((GuestViewHolder) holder).name = convertView.findViewById(R.id.txtGuestName);
                    ((GuestViewHolder) holder).expiry = convertView.findViewById(R.id.txtGuestExpiry);
                    ((GuestViewHolder) holder).icon = convertView.findViewById(R.id.imgIcon);
                    convertView.setTag(holder);
                    break;
            }
        } else {
            holder = (ViewHolderBase) convertView.getTag();
        }

        // データセット
        holder.setData(user);

        // 削除モード UI
        if (isDeleteMode) {
            convertView.setBackgroundColor(Color.parseColor("#FFCDD2")); // 赤っぽい背景
            holder.showDeleteIcon(true);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            holder.showDeleteIcon(false);
        }

        return convertView;
    }

    /** 共通の ViewHolder 基底クラス */
    static abstract class ViewHolderBase {
        abstract void setData(User user);
        abstract void showDeleteIcon(boolean visible);
    }

    static class AdminViewHolder extends ViewHolderBase {
        TextView name, expiry;
        ImageView icon;

        @Override
        void setData(User user) {
            name.setText(user.getName());
            expiry.setText(user.getExpiry());
        }

        @Override
        void showDeleteIcon(boolean visible) {
            icon.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    static class NormalViewHolder extends ViewHolderBase {
        TextView name, expiry;
        ImageView icon;

        @Override
        void setData(User user) {
            name.setText(user.getName());
            expiry.setText(user.getExpiry());
        }

        @Override
        void showDeleteIcon(boolean visible) {
            icon.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    static class GuestViewHolder extends ViewHolderBase {
        TextView name, expiry;
        ImageView icon;

        @Override
        void setData(User user) {
            name.setText(user.getName());
            expiry.setText(user.getExpiry());
        }

        @Override
        void showDeleteIcon(boolean visible) {
            icon.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}
