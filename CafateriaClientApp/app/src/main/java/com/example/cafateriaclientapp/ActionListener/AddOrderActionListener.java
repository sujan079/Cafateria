package com.example.cafateriaclientapp.ActionListener;

import com.example.cafateriaclientapp.Database.Models.DB_Orders;
import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.MenuItem;

public interface AddOrderActionListener {

    void onOrderClick(MenuItem menuItem);
}
