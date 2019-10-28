package com.example.cafateriaadminapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.History;
import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.OrderHistory;
import com.example.cafateriaadminapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    private List<History> histories=new ArrayList<>();

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_list,parent,false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bindData(histories.get(position));
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvItemName,mTvItemPrice,mTvItemQuantity,mTvOrderBy,mTvOrderDate;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemName=itemView.findViewById(R.id.tv_order_his_ItemName);
            mTvItemPrice=itemView.findViewById(R.id.tv_order_his_Price);
            mTvItemQuantity=itemView.findViewById(R.id.tv_order_his_quantity);
            mTvOrderBy=itemView.findViewById(R.id.tv_order_his_orderBy);
            mTvOrderDate=itemView.findViewById(R.id.tv_order_his_order_date);
        }

        public void bindData(History history){

            mTvItemName.setText(history.getItemName());
            mTvItemQuantity.setText(String.valueOf(history.getQuantity()));
            mTvOrderDate.setText(history.getOrder_date());
            mTvItemPrice.setText(String.valueOf(history.getPrice()));
            mTvOrderBy.setText(history.getOrderBy());

        }
    }
}
