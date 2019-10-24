package com.example.cafateriaclientapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory.History;
import com.example.cafateriaclientapp.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<History> histories;


    public OrderHistoryAdapter(List<History> histories) {
        this.histories = histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_list_item,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(histories.get(position));
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvItemName,mTvItemPrice,mTvItemQuantity,mTvItemDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvItemName=itemView.findViewById(R.id.tv_orderHis_itemName);
            mTvItemQuantity=itemView.findViewById(R.id.tv_orderHis_quantity);
            mTvItemPrice=itemView.findViewById(R.id.tv_orderHis_itemPrice);
            mTvItemDate=itemView.findViewById(R.id.tv_orderHis_date);


        }

        public void bindData(History history){
            mTvItemQuantity.setText(history.getQuantity().toString());
            mTvItemPrice.setText(String.valueOf(history.getPrice()));
            mTvItemName.setText(history.getItemName());
            mTvItemDate.setText(history.getOrder_date());
        }
    }
}
