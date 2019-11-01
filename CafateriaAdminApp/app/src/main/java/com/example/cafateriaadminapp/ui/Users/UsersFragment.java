package com.example.cafateriaadminapp.ui.Users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Adapter.UsersAdapter;
import com.example.cafateriaadminapp.Network.Retrofit.Api.UsersApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.OrderHistory;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.User;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.Users;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;
import com.example.cafateriaadminapp.OrderHistoryActivity;
import com.example.cafateriaadminapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsersFragment extends Fragment {


    private RecyclerView rvUsers;
    private UsersAdapter usersAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    private Retrofit retrofit;
    private UsersApiClient usersApiClient;

    private AlertDialog errorDialog, fetchingData;

    private Context context;

    public static int ORDER_HISTORY_REQUEST_CODE=111;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users, container, false);
        context = getContext();
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = RetrofitClient.getINSTANCE();
        usersApiClient = retrofit.create(UsersApiClient.class);

        rvUsers = view.findViewById(R.id.rv_Users);
        usersAdapter = new UsersAdapter();
        userLayoutManager = new GridLayoutManager(context, 2);

        rvUsers.setAdapter(usersAdapter);
        rvUsers.setLayoutManager(userLayoutManager);
        rvUsers.setHasFixedSize(true);

        usersAdapter.setUserItemClickListner(new UsersAdapter.OnUserItemClickListner() {
            @Override
            public void deleteUser(String id) {
                delUser(id);
            }

            @Override
            public void seeOrders(String id) {
                Intent orderHistoryIntent=new Intent(context, OrderHistoryActivity.class);
                orderHistoryIntent.putExtra(OrderHistoryActivity.EXTRA_USER_ID,id);
                startActivityForResult(orderHistoryIntent,ORDER_HISTORY_REQUEST_CODE);
            }

            @Override
            public void updateStatus(User user) {
                User updateUser=new User(null,null,user.isActive());//Retrofit automatically dosnot create null objects in json update efficency;
                showLoadingDialogBox();
                Call<User> userCall=usersApiClient.updateUser(user.get_id(),updateUser);

                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        fetchingData.cancel();
                        if(response.code()==200){
                            loadUsers();

                        }else {
                            errorDialogbox("Update Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        fetchingData.cancel();
                        errorDialogbox(t.getMessage());
                    }
                });
            }
        });

        loadUsers();

    }


    public void loadUsers() {
        showLoadingDialogBox();
        Call<Users> usersCall = usersApiClient.getAllUsers();

        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                fetchingData.cancel();
                if (response.code() == 200) {
                    List<User> users=response.body().getUsers();
                    usersAdapter.setUsers(users);
                    usersAdapter.notifyDataSetChanged();
                } else {
                    errorDialogbox("Could Not Get User Data");
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());
            }
        });
    }

    public void delUser(String user_id){
        showLoadingDialogBox();
        Call<Users> usersCall=usersApiClient.deleteUsers(user_id);
        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                fetchingData.cancel();
                if(response.code()==200){
                    loadUsers();
                }else{
                    errorDialogbox("Could Not delete User");
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());
            }
        });
    }
    public void errorDialogbox(String error) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.error_dialog_box, null, false);

        TextView errorTextView = view.findViewById(R.id.tv_error);
        errorTextView.setText(error);
        builder.setView(view);
        errorDialog = builder.create();
        errorDialog.show();
    }

    public void showLoadingDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.info_dialog_box, null, false);
        builder.setView(view);
        fetchingData = builder.create();
        fetchingData.show();

    }
}
