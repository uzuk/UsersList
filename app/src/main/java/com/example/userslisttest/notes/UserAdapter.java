package com.example.userslisttest.notes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.example.userslisttest.R;
import com.example.userslisttest.db.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;


public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {
    public OnItemClickListener listener;
    Context mContext;

    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getAge().equals(newItem.getAge()) &&
                    oldItem.getFavColor().equals(newItem.getFavColor());
        }
    };

    //////////////
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = getItem(position);
        holder.textViewNameRv.setText("Name: " + currentUser.getName());
        holder.textViewAgeRv.setText("Age: " + currentUser.getAge().toString());
        holder.textViewColorRv.setText("Fav Color: " + currentUser.getFavColor());
        String uri = currentUser.getImageUriString();
        try {
            Uri uri1 = Uri.parse(uri);
            Picasso.get()
                    .load(new File(uri1.getPath()))
                    .fit()
                    .into(holder.userAvatarRv);
        }catch (NullPointerException e){
            holder.userAvatarRv.setImageResource(R.drawable.ic_person_person);
        }
    }

    //////////////////

    public User getUserAt(int position) {
        return getItem(position);
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameRv;
        private TextView textViewAgeRv;
        private TextView textViewColorRv;
        private ImageView userAvatarRv;

        public UserHolder(View itemView) {
            super(itemView);
            textViewNameRv = itemView.findViewById(R.id.user_name_rv);
            textViewAgeRv = itemView.findViewById(R.id.user_age_rv);
            textViewColorRv = itemView.findViewById(R.id.user_color_rv);
            userAvatarRv = itemView.findViewById(R.id.user_avatar_rv);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User User);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

