package com.example.cafateriaadminapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.User;
import com.example.cafateriaadminapp.R;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder>{


    private OnUserItemClickListner userItemClickListner;

    public void setUserItemClickListner(OnUserItemClickListner userItemClickListner) {
        this.userItemClickListner = userItemClickListner;
    }

    private List<User> users=new ArrayList<>();

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvUserID,mTvUserEmail;
        private Button btnSeeOrders,btnDelUser;
        private Switch statusSwitch;


        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvUserID=itemView.findViewById(R.id.tv_user_id);
            mTvUserEmail=itemView.findViewById(R.id.tv_user_email);
            btnSeeOrders=itemView.findViewById(R.id.view_orders_btn);
            btnDelUser=itemView.findViewById(R.id.delete_user);
            statusSwitch=itemView.findViewById(R.id.user_status);

            btnSeeOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User currentUser=users.get(getAdapterPosition());
                    userItemClickListner.seeOrders(currentUser.get_id());
                }
            });

            btnDelUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User currentUser=users.get(getAdapterPosition());
                    userItemClickListner.deleteUser(currentUser.get_id());
                }
            });


            statusSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User currentUser=users.get(getAdapterPosition());
                    currentUser.setActive(!currentUser.isActive());
                    userItemClickListner.updateStatus(currentUser);
                }
            });

        }

        public void bind(User user){
            mTvUserID.setText(user.get_id());
            mTvUserEmail.setText(user.getEmail());
            statusSwitch.setChecked(user.isActive());
        }
    }

    public interface OnUserItemClickListner{
        void deleteUser(String id);
        void seeOrders(String id);
        void updateStatus(User user);

    }
}
