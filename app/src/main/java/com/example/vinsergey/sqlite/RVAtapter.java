package com.example.vinsergey.sqlite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.UserViewHolder> {

    private List<User> users;

    RVAdapter(List<User> users){
        this.users = users;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userEmail;
        TextView userId;
        UserViewHolder(View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.idUser);
            userName = itemView.findViewById(R.id.nameUser);
            userEmail = itemView.findViewById(R.id.emailUser);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        userViewHolder.userId.setText(String.valueOf(users.get(position).getId()));
        userViewHolder.userName.setText(users.get(position).getName());
        userViewHolder.userEmail.setText(users.get(position).getEmail());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        return new UserViewHolder(view);
    }

//    @Override
//    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}