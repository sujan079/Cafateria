package com.example.cafateriaadminapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItem;
import com.example.cafateriaadminapp.Network.Retrofit.Model.RoutineItem.RoutineItems;
import com.example.cafateriaadminapp.R;

import java.util.ArrayList;
import java.util.List;

public class RoutineItemAdapter extends RecyclerView.Adapter<RoutineItemAdapter.RoutineItemViewHolder> {

    private List<RoutineItems.RoutineItem> routineItems = new ArrayList<>();

    private OnRoutineItemClickListner onRoutineItemClickListner;

    public void setOnRoutineItemClickListner(OnRoutineItemClickListner onRoutineItemClickListner) {
        this.onRoutineItemClickListner = onRoutineItemClickListner;
    }

    public void setRoutineItems(List<RoutineItems.RoutineItem> routineItems) {
        this.routineItems = routineItems;
    }

    @NonNull
    @Override
    public RoutineItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_item_list, parent, false);
        return new RoutineItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineItemViewHolder holder, int position) {
        holder.bindData(routineItems.get(position));
    }

    @Override
    public int getItemCount() {
        return routineItems.size();
    }

    public interface OnRoutineItemClickListner {
        void removeMenuItem(RoutineItems.RoutineItem routineItem);

    }

    class RoutineItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvItemName, mTvItemCategory, mTvItemPrice;
        private Button ivRemove;

        public RoutineItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemName = itemView.findViewById(R.id.tv_routine_item_name);
            mTvItemCategory = itemView.findViewById(R.id.tv_routine_item_categories);
            mTvItemPrice = itemView.findViewById(R.id.tv_routine_item_price);

            ivRemove = itemView.findViewById(R.id.remove_btn);

            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RoutineItems.RoutineItem currentItem = routineItems.get(getAdapterPosition());
                    onRoutineItemClickListner.removeMenuItem(currentItem);
                }
            });

        }

        public void bindData(RoutineItems.RoutineItem routineItem) {
            mTvItemName.setText(routineItem.getItemName());
            mTvItemPrice.setText(String.valueOf(routineItem.getPrice()));

            String category = "";
            for (String cat : routineItem.getCategories()) {
                category += cat + ",";
            }

            mTvItemCategory.setText(category);
        }
    }
}
