package com.example.cafateriacounterapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriacounterapp.ActionListeners.MenuItemsActionListner;
import com.example.cafateriacounterapp.Database.Models.DB_MenuItem;
import com.example.cafateriacounterapp.R;

import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.ViewHolder> {

    List<DB_MenuItem> menuItemList;
    MenuItemsActionListner menuItemsActionListner;

    public MenuItemsAdapter(List<DB_MenuItem> menuItemList,MenuItemsActionListner actionListner) {
        this.menuItemList = menuItemList;
        this.menuItemsActionListner=actionListner;
    }

    public void setMenuItemList(List<DB_MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(menuItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTvItemName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemName=itemView.findViewById(R.id.tv_menu_item);
            itemView.setOnClickListener(this);
        }

        public void bindData(DB_MenuItem item){
            mTvItemName.setText(item.getItemName());
        }

        @Override
        public void onClick(View view) {
            menuItemsActionListner.onMenuItemClick(menuItemList.get(getAdapterPosition()));
        }
    }
}
