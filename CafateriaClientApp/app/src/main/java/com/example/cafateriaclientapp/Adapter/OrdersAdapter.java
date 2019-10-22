package com.example.cafateriaclientapp.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.ActionListener.OrdersActionListener;
import com.example.cafateriaclientapp.Database.Models.DB_Orders;
import com.example.cafateriaclientapp.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<DB_Orders> ordersList;
    private OrdersActionListener ordersActionListener;

    public OrdersAdapter(List<DB_Orders> ordersList, OrdersActionListener ordersActionListener) {
        this.ordersList = ordersList;
        this.ordersActionListener = ordersActionListener;
    }

    public void setOrdersList(List<DB_Orders> ordersList) {
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_sample,parent,false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(ordersList.get(position));

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvItemName,mTvItemPrice,tvDelOrder;
        private TextView etItemQuantity;

        private Button incOrder,decOrder;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvItemName=itemView.findViewById(R.id.tv_order_ItemName);
            mTvItemPrice=itemView.findViewById(R.id.tv_order_item_price);
            etItemQuantity=itemView.findViewById(R.id.et_order_quantity);




            tvDelOrder=itemView.findViewById(R.id.tv_order_del);
            tvDelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        ordersActionListener.delOrder(ordersList.get(getAdapterPosition()));
                    }
            });

            incOrder=itemView.findViewById(R.id.btn_inc_order);
            incOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DB_Orders updatedOrder=ordersList.get(getAdapterPosition());
                    Double currentQuantity= Double.valueOf(etItemQuantity.getText().toString())+1;
                    updatedOrder.setQuantity(currentQuantity);
                    ordersActionListener.changeOrder(updatedOrder);
                }
            });

            decOrder=itemView.findViewById(R.id.btn_dec_order);

            decOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DB_Orders updatedOrder=ordersList.get(getAdapterPosition());
                    Double currentQuantity;
                    if(updatedOrder.getQuantity()>0){
                        currentQuantity=Double.valueOf(etItemQuantity.getText().toString())-1;

                    }else{
                        currentQuantity=0.0;
                    }
                    updatedOrder.setQuantity(currentQuantity);
                    ordersActionListener.changeOrder(updatedOrder);

                }
            });
        }

        public void bindData(DB_Orders orders){
            mTvItemName.setText(orders.getItemName());
            mTvItemPrice.setText(orders.getPrice().toString());
            etItemQuantity.setText(orders.getQuantity().toString());
        }
    }
}
