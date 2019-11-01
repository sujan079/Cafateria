package com.example.cafateriaadminapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItem;
import com.example.cafateriaadminapp.R;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder> {

    private List<MenuItem> menuItems=new ArrayList<>();
    private OnMenuItemClickListner onMenuItemClickListner;


    public void setOnMenuItemClickListner(OnMenuItemClickListner onMenuItemClickListner) {
        this.onMenuItemClickListner = onMenuItemClickListner;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_list,parent,false);
        return new MenuItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemsViewHolder holder, int position) {
        holder.bindData(menuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

     class MenuItemsViewHolder extends RecyclerView.ViewHolder{
         private TextView mTvItemName, mTvItemCategory, mTvItemPrice;
         private Button ivRemove;
        private Button updateBtn;

        public MenuItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvItemName=itemView.findViewById(R.id.tv_menu_item_name);
            mTvItemCategory=itemView.findViewById(R.id.tv_menu_item_categories);
            mTvItemPrice=itemView.findViewById(R.id.tv_menu_item_price);

            ivRemove=itemView.findViewById(R.id.remove_btn);
            updateBtn=itemView.findViewById(R.id.update_btn);

            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMenuItemClickListner.removeMenuItem(menuItems.get(getAdapterPosition()));
                }
            });

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMenuItemClickListner.updateMenuItem(menuItems.get(getAdapterPosition()));

                }
            });

        }

        public void bindData(MenuItem menuItem){
            mTvItemName.setText(menuItem.getItemName());
            mTvItemPrice.setText(String.valueOf(menuItem.getPrice()));

            String category="";
            for (String cat: menuItem.getCategories()) {
                category+=cat+",";
            }

            mTvItemCategory.setText(category);
        }
    }

    public interface OnMenuItemClickListner {
        void removeMenuItem(MenuItem menuItem);

        void updateMenuItem(MenuItem menuItem);
    }
}
