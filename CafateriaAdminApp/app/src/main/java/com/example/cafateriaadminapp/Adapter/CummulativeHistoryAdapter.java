package com.example.cafateriaadminapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.R;
import com.example.cafateriaadminapp.ui.order_history.OrderHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class CummulativeHistoryAdapter extends RecyclerView.Adapter<CummulativeHistoryAdapter.CummulativeHistoryViewHolder>{

    private List<OrderHistoryFragment.CummulativeItem> cummulativeItems=new ArrayList<>();

    public void setCummulativeItems(List<OrderHistoryFragment.CummulativeItem> cummulativeItems) {
        this.cummulativeItems = cummulativeItems;
    }

    @NonNull
    @Override
    public CummulativeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cummulative_order_history_item,parent,false);
        return new CummulativeHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CummulativeHistoryViewHolder holder, int position) {
            holder.bindData(cummulativeItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cummulativeItems.size();
    }

    public class CummulativeHistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView tvItemName,tvQuantity;
        public CummulativeHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName=itemView.findViewById(R.id.tv_cumm_his_itemName);
            tvQuantity=itemView.findViewById(R.id.tv_cumm_his_quantity);
        }

        public void bindData(OrderHistoryFragment.CummulativeItem cummulativeItem){
            tvItemName.setText(cummulativeItem.itemName);
            tvQuantity.setText(String.valueOf(cummulativeItem.quantity));

        }
    }
}
