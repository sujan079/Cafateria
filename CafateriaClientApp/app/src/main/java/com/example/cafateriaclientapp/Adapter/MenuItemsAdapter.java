package com.example.cafateriaclientapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.ActionListener.AddOrderActionListener;
import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.MenuItem;
import com.example.cafateriaclientapp.R;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.ViewHolder> {
    private List<MenuItem> menuItems;
    private AddOrderActionListener addOrderActionListener;

    public MenuItemsAdapter(List<MenuItem> menuItems, AddOrderActionListener addOrderActionListener) {
        this.menuItems = menuItems;
        this.addOrderActionListener = addOrderActionListener;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_list_sample, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(menuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvMenuItemName, mTvMenuItemPrice;
        private Button mOrderBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMenuItemName = itemView.findViewById(R.id.tv_menu_ItemName);
            mTvMenuItemPrice = itemView.findViewById(R.id.tv_menu_item_price);

            mOrderBtn = itemView.findViewById(R.id.btn_order);

            mOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addOrderActionListener.onOrderClick(menuItems.get(getAdapterPosition()));
                }
            });
        }

        private void bindData(MenuItem menuItem) {
            mTvMenuItemName.setText(menuItem.getItemName());
            mTvMenuItemPrice.setText(menuItem.getPrice().toString());
        }

    }
}
