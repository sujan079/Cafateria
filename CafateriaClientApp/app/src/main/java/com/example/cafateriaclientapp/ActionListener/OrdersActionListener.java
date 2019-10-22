package com.example.cafateriaclientapp.ActionListener;

import com.example.cafateriaclientapp.Database.Models.DB_Orders;

public interface OrdersActionListener {

    void delOrder(DB_Orders orders);
    void changeOrder(DB_Orders orders);
}
