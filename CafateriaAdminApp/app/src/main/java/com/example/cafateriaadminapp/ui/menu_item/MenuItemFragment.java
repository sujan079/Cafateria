package com.example.cafateriaadminapp.ui.menu_item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Session2Command;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Adapter.MenuItemsAdapter;
import com.example.cafateriaadminapp.AddUpdateMenuItem;
import com.example.cafateriaadminapp.Network.Retrofit.Api.MenuItemsApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItem;
import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItems;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.User;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.Users;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;
import com.example.cafateriaadminapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuItemFragment extends Fragment {

    private Retrofit retrofitClient;
    private MenuItemsApiClient menuItemsApiClient;

    private Context context;
    private AlertDialog errorDialog, fetchingData;

    private RecyclerView rvMenuItems;
    private MenuItemsAdapter menuItemsAdapter;
    private RecyclerView.LayoutManager menuItemsLayoutManager;

    private FloatingActionButton addMenuItem;

    public int ADD_MENU_ITEM_REQUEST_CODE = 123;
    public int UPDATE_MENU_ITEM_REQUEST_CODE = 234;


    private MenuItemsAdapter.OnMenuItemClickListner onMenuItemClickListner = new MenuItemsAdapter.OnMenuItemClickListner() {
        @Override
        public void removeMenuItem(MenuItem menuItem) {
            Call<MenuItem> menuItemCall = menuItemsApiClient.deleteMenuItem(menuItem.getId());
            menuItemCall.enqueue(new Callback<MenuItem>() {
                @Override
                public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                    if (response.code() == 200) {
                        loadMenuData();
                    } else {
                        errorDialogbox("Not Deleted,Try Again");
                    }
                }

                @Override
                public void onFailure(Call<MenuItem> call, Throwable t) {
                    errorDialogbox(t.getMessage());
                }
            });
        }

        @Override
        public void updateMenuItem(MenuItem menuItem) {
            Intent addUpdateActivity = new Intent(context, AddUpdateMenuItem.class);
            addUpdateActivity.putExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_ID,menuItem.getId());
            addUpdateActivity.putExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_NAME,menuItem.getItemName());
            addUpdateActivity.putExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_Price,menuItem.getPrice());

            String category="";
            for (String cat: menuItem.getCategories()) {
                category+=cat+",";
            }
            addUpdateActivity.putExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_Categories,category);

            startActivityForResult(addUpdateActivity, UPDATE_MENU_ITEM_REQUEST_CODE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_item, container, false);
        context = rootView.getContext();

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofitClient = RetrofitClient.getINSTANCE();
        menuItemsApiClient = retrofitClient.create(MenuItemsApiClient.class);

        rvMenuItems = view.findViewById(R.id.rv_MenuItems);
        menuItemsAdapter = new MenuItemsAdapter();
        menuItemsLayoutManager = new GridLayoutManager(context, 2);

        menuItemsAdapter.setOnMenuItemClickListner(onMenuItemClickListner);


        rvMenuItems.setAdapter(menuItemsAdapter);
        rvMenuItems.setLayoutManager(menuItemsLayoutManager);
        rvMenuItems.setHasFixedSize(true);

        addMenuItem = view.findViewById(R.id.fab_add_menu_item);

        addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUpdateActivity = new Intent(context, AddUpdateMenuItem.class);

                startActivityForResult(addUpdateActivity, ADD_MENU_ITEM_REQUEST_CODE);
            }
        });


        loadMenuData();

    }


    public void loadMenuData() {
        showLoadingDialogBox();


        Call<MenuItems> menuItemsCall = menuItemsApiClient.getAllMenuItems();

        menuItemsCall.enqueue(new Callback<MenuItems>() {
            @Override
            public void onResponse(Call<MenuItems> call, Response<MenuItems> response) {
                fetchingData.cancel();

                if (response.code() == 200) {
                    MenuItems menuItems = response.body();

                    menuItemsAdapter.setMenuItems(menuItems.getMenuItems());
                    menuItemsAdapter.notifyDataSetChanged();

                } else {
                    errorDialogbox("Try Again,Failed To Get MenuItems");

                }
            }

            @Override
            public void onFailure(Call<MenuItems> call, Throwable t) {
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

    public void addUpdateMenuItem(MenuItem menuItem) {

        Call<MenuItem> menuItemCall;
        if (menuItem.getId()==null) {
            menuItemCall = menuItemsApiClient.insertMenuItem(menuItem);

        } else {
            menuItemCall = menuItemsApiClient.updateMenuItem(menuItem.getId(),menuItem);
        }

        menuItemCall.enqueue(new Callback<MenuItem>() {
            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                if (response.code() == 200) {
                    loadMenuData();
                } else {
                    errorDialogbox("Try Again,Insert Failed");

                }
            }

            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) {
                errorDialogbox(t.getMessage());

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_MENU_ITEM_REQUEST_CODE) {

            String name = data.getStringExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_NAME);
            Double price = data.getDoubleExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_Price, 0.0);

            String category = data.getStringExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_Categories);
            List<String> categories = Arrays.asList(category.split(","));

            addUpdateMenuItem(new MenuItem(
                    name,
                    price,
                    categories
            ));
        } else if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_MENU_ITEM_REQUEST_CODE) {


            String id = data.getStringExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_ID);

            String name = data.getStringExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_NAME);
            Double price = data.getDoubleExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_Price, 0.0);

            String category = data.getStringExtra(AddUpdateMenuItem.EXTRA_MENU_ITEM_Categories);
            List<String> categories = Arrays.asList(category.split(","));

            MenuItem menuItem = new MenuItem(
                    name,
                    price,
                    categories);

            menuItem.setId(id);

            addUpdateMenuItem(menuItem);
        } else {
            Toast.makeText(context, "Adding MenuItem Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
