package com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems;

import java.util.List;

public class MenuItems {


    private Integer count;
    private List<MenuItem> menuItems;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
