package com.example.cafateriacounterapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriacounterapp.ActionListeners.CategoriesItemActionListner;
import com.example.cafateriacounterapp.Database.Models.DB_Category;
import com.example.cafateriacounterapp.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    private List<DB_Category> categories;
    private CategoriesItemActionListner categoriesItemActionListner;
    private static Integer SELECTED=0;

    public CategoriesAdapter(List<DB_Category> categories, CategoriesItemActionListner categoriesItemActionListner) {
        this.categories = categories;
        this.categoriesItemActionListner = categoriesItemActionListner;
    }

    public void setCategories(List<DB_Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position==SELECTED){
            holder.itemView.setBackgroundResource(R.drawable.selected_background);

        }else{
            holder.itemView.setBackgroundResource(R.drawable.round_background);
        }
        holder.bindData(categories.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTvCategoryItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvCategoryItem=itemView.findViewById(R.id.tv_category_item);

            itemView.setOnClickListener(this);
        }

        public void bindData(String category){
            mTvCategoryItem.setText(category);
        }

        @Override
        public void onClick(View view) {
            categoriesItemActionListner.onCategoryItemClicked(
                    categories.get(getAdapterPosition()));

            SELECTED=getAdapterPosition();
            notifyDataSetChanged();
        }
    }
}
