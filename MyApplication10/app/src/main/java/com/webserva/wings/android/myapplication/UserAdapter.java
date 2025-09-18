package com.webserva.wings.android.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView 用ユーザーリスト Adapter
 * 削除モード対応
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private boolean isDeleteMode = false;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user, int position, boolean isDeleteMode);
    }

    public UserAdapter(List<User> users, OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case User.TYPE_ADMIN:
                layoutRes = R.layout.item_admin;
                break;
            case User.TYPE_NORMAL:
                layoutRes = R.layout.item_normal;
                break;
            case User.TYPE_GUEST:
                layoutRes = R.layout.item_guest;
                break;
            default:
                layoutRes = R.layout.item_normal;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.expiry.setText(user.getExpiry());

        if (isDeleteMode) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2"));
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.icon.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onUserClick(user, position, isDeleteMode));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return users.get(position).getType();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, expiry;
        ImageView icon;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            expiry = itemView.findViewById(R.id.user_expiry);
            icon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
