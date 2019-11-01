package com.example.cafateriaadminapp.Adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItem;
import com.example.cafateriaadminapp.Network.Retrofit.Model.RoutineItem.RoutineItems;
import com.example.cafateriaadminapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class SelectRoutineItemAdapter extends RecyclerView.Adapter<SelectRoutineItemAdapter.SelectRoutineViewHolder> {


    private List<Integer> selected = new ArrayList<>();
    private List<MenuItem> selected_menuitems = new ArrayList<>();

    private List<MenuItem> menuItems = new ArrayList<>();
    private SparseBooleanArray selectedItem = new SparseBooleanArray();


    public List<MenuItem> getSelected_menuitems() {
        return selected_menuitems;
    }

    public void setSelected(List<Integer> selected) {
        this.selected = selected;
    }


    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public SelectRoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_routine_item_list, parent, false);
        return new SelectRoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectRoutineViewHolder holder, int position) {
        holder.bind(menuItems.get(position));

        if (selected_menuitems.size() == 0) {
            for (Integer index : selected) {
                selectedItem.append(index, true);
                selected_menuitems.add(menuItems.get(index));
            }
        }


        holder.mCBSelected.setChecked(selectedItem.get(position));

    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }


    class SelectRoutineViewHolder extends RecyclerView.ViewHolder {

        private TextView mRoutineItemName, mRoutineItemCategory, mRoutineItemPrice;
        private CheckBox mCBSelected;

        public SelectRoutineViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoutineItemName = itemView.findViewById(R.id.tv_selected_routine_item_name);
            mRoutineItemCategory = itemView.findViewById(R.id.tv_selected_routine_item_categories);
            mRoutineItemPrice = itemView.findViewById(R.id.tv_selected_routine_item_price);

            mCBSelected = itemView.findViewById(R.id.cb_item_selected);


            mCBSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MenuItem current_item = menuItems.get(getAdapterPosition());
                    selectedItem.append(getAdapterPosition(), mCBSelected.isChecked());
                    boolean isSelected = selectedItem.get(getAdapterPosition());


                    if (isSelected) {
                        selected_menuitems.add(current_item);

                    } else {
                        for (int index = 0; index < selected_menuitems.size(); index++) {

                            if (selected_menuitems.get(index).getId().equals(current_item.getId())) {
                                selected_menuitems.remove(index);
                            }
                        }
                    }
                }
            });

        }


        public void bind(MenuItem menuItem) {
            mRoutineItemName.setText(menuItem.getItemName());
            mRoutineItemPrice.setText(String.valueOf(menuItem.getPrice()));
            String category = "";
            for (String cat : menuItem.getCategories()) {
                category += cat + ",";
            }
            mRoutineItemCategory.setText(category);

        }
    }
}
