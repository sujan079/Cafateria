package com.example.cafateriaclientapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.Adapter.GeneralInfoAdapter;
import com.example.cafateriaclientapp.Adapter.OrderHistoryAdapter;
import com.example.cafateriaclientapp.Database.CafateriaDatabase;
import com.example.cafateriaclientapp.Database.Models.DB_User;
import com.example.cafateriaclientapp.Executors.AppExecutor;
import com.example.cafateriaclientapp.MainActivity;
import com.example.cafateriaclientapp.Network.Api.OrderHistoryApi;
import com.example.cafateriaclientapp.Network.Api.UserApi;
import com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory.History;
import com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory.OrderHistory;
import com.example.cafateriaclientapp.Network.GSON_Models.User.User;
import com.example.cafateriaclientapp.Network.RetrofitClient;
import com.example.cafateriaclientapp.OrderHistoryActivity;
import com.example.cafateriaclientapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private String TAG="ACCOUNT_FRAGMENT";

    private CafateriaDatabase mDB;
    private Retrofit retrofit;
    private List<DB_User> users=new ArrayList<>();

    private Context context;
    private List<AuthUI.IdpConfig> providers;
    private static int MY_REQUEST_CODE = 123;
    private Button SignOut;
    private static AlertDialog errorDialog;

    private View accoutView;

    private Callbacks mCallbacks;

    private RecyclerView rvGeneralInfo;
    private RecyclerView.LayoutManager rvGeneralInfoLayoutManager;
    private GeneralInfoAdapter generalInfoAdapter;

    private RecyclerView rvOrderHistory;
    private RecyclerView.LayoutManager orderHistoryLayoutManger;
    private OrderHistoryAdapter orderHistoryAdapter;


    public interface Callbacks {
        //Callback for when button clicked.
        public void onButtonClicked();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks
        mCallbacks = (Callbacks) activity;

    }

    //handler
    private Handler showLoginSignUpHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            showSignInOptions();
        }
    };

    private Handler showErrorDialogHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            errorDialogbox((String) msg.obj);
        }
    };

    private Handler showAccountViewHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int prop= (int) msg.obj;

        accoutView.setVisibility(prop);
        }
    };

    private Handler loadUserInfoHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<String> info= (List<String>) msg.obj;
            generalInfoAdapter.setInfoList(info);
            generalInfoAdapter.notifyDataSetChanged();
        }
    };

    private Handler loadOrdersHistoryHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<History> info= (List<History>) msg.obj;
            orderHistoryAdapter.setHistories(info);
            orderHistoryAdapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();

        Button view_more=view.findViewById(R.id.view_more);

        view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOrderHistoryIntent();
            }
        });

        rvGeneralInfo=view.findViewById(R.id.rv_userInfo);
        rvGeneralInfoLayoutManager=new LinearLayoutManager(context);
        generalInfoAdapter=new GeneralInfoAdapter(new ArrayList<String>());
        rvGeneralInfo.setLayoutManager(rvGeneralInfoLayoutManager);
        rvGeneralInfo.setAdapter(generalInfoAdapter);

        rvOrderHistory=view.findViewById(R.id.rv_last_orders);
        orderHistoryLayoutManger=new LinearLayoutManager(context);
        orderHistoryAdapter=new OrderHistoryAdapter(new ArrayList<History>());
        rvOrderHistory.setAdapter(orderHistoryAdapter);
        rvOrderHistory.setLayoutManager(orderHistoryLayoutManger);

        accoutView=view.findViewById(R.id.account_view);

        mDB=CafateriaDatabase.getInstance(context);
        retrofit=new RetrofitClient().getINSTANCE();


        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        SignOut = view.findViewById(R.id.btn_signOut);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOutUser();

            }
        });

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                users=mDB.userDao().getUser();
                if(users.size()>0){
                    Message dataMsg=new Message();
                    dataMsg.obj=View.VISIBLE;

                    showAccountViewHandler.sendMessage(dataMsg);
                    loadUserInfo(users.get(0));
                    loadUserHistory(users.get(0));

                }else{
                    Message errMsg=new Message();
                    errMsg.obj="Not Sing In.Please Sign IN";
                    showErrorDialogHandler.sendMessage(errMsg);

                    showLoginSignUpHandler.sendMessage(new Message());
                }

            }
        });

    }


    public void loadUserInfo(DB_User currentUser){
        UserApi userApi=retrofit.create(UserApi.class);

        Call<User> userCall=userApi.getUserData(currentUser.getId());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()==200){
                    final User userData=response.body();
                    List<String> infos=new ArrayList<>();
                    infos.add("ID: "+userData.getId());
                    infos.add("EMAIL: "+userData.getEmail());
                    String status=userData.isActive()?("Active"):("Not Active");
                    infos.add("Status:"+status);

                    AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            users=mDB.userDao().getUser();
                            if(users.size()==0){
                                mDB.userDao().addUser(new DB_User(
                                        userData.getId(),
                                        userData.getEmail()
                                ));
                            }

                        }
                    });

                    Message userInfoMsg=new Message();
                    userInfoMsg.obj=infos;

                    loadUserInfoHandler.sendMessage(userInfoMsg);

                }else{
                    AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDB.userDao().delUser();
                        }
                    });
                    errorDialogbox("Could Not get User Data");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                errorDialogbox(t.getMessage());
            }
        });
    }

    public void loadUserHistory(DB_User currentUser){
        OrderHistoryApi orderHistoryApi=retrofit.create(OrderHistoryApi.class);
        Call<OrderHistory> orderHistoryCall=orderHistoryApi.getAllHistory(currentUser.getId(),null,new Integer(10));
        orderHistoryCall.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                if(response.code()==200){
                    OrderHistory orderHistory=response.body();
                    Message historyMsg=new Message();
                    historyMsg.obj=orderHistory.getOrderHistory();

                    loadOrdersHistoryHandler.sendMessage(historyMsg);
                }else{
                    Message errMsg=new Message();
                    errMsg.obj="Could Not get History";
                    showErrorDialogHandler.sendMessage(errMsg);

                    showLoginSignUpHandler.sendMessage(new Message());
                }
            }

            @Override
            public void onFailure(Call<OrderHistory> call, Throwable t) {
                Message errMsg=new Message();
                errMsg.obj="Could Not get History";
                showErrorDialogHandler.sendMessage(errMsg);

                showLoginSignUpHandler.sendMessage(new Message());
            }
        });
    }



    public void errorDialogbox(String error){

        accoutView.setVisibility(View.INVISIBLE);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=LayoutInflater.from(context).inflate(R.layout.error_dialog_box,null,false);

        TextView errorTextView= view.findViewById(R.id.tv_error);
        errorTextView.setText(error);
        builder.setView(view);
        errorDialog=builder.create();
        errorDialog.show();
    }

    public void signOutUser() {

        mCallbacks.onButtonClicked();
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDB.userDao().delUser();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void saveUser(FirebaseUser user){
        final String email=user.getEmail();
        final String uid=user.getUid();

        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.userDao().delUser();

                DB_User saveUser=new DB_User(uid,email);
                mDB.userDao().addUser(saveUser);
                loadUserInfo(saveUser);
                loadUserHistory(saveUser);


            }
        });

    }

    public void saveUserOnServer(final FirebaseUser user){
        final String email=user.getEmail();
        final String uid=user.getUid();

        UserApi userApi=retrofit.create(UserApi.class);
        Call<User> userCall=userApi.addUser(new User(uid,email));

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()==200){
                    saveUser(user);
                }else {
                    Message errMsg=new Message();
                    errMsg.obj="Could Not Create User";
                    showErrorDialogHandler.sendMessage(errMsg);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Message errMsg=new Message();
                errMsg.obj=t.getMessage();
                showErrorDialogHandler.sendMessage(errMsg);
            }
        });
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                saveUser(user);
                accoutView.setVisibility(View.VISIBLE);

                saveUserOnServer(user);
                errorDialog.cancel();
            }
        }
    }


    public void startOrderHistoryIntent(){
        Intent intent=new Intent(getActivity(), OrderHistoryActivity.class);
        startActivity(intent);
    }
}
