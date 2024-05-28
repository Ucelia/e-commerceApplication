package com.example.ecommerceapplication.dto;

import com.example.ecommerceapplication.model.Orders;
import com.example.ecommerceapplication.model.Product;
import com.example.ecommerceapplication.model.User;

import java.util.*;

public class GlobalData {
    public static List<Product> cart;
    public static User users ;
    public static Orders orders = new Orders();
    static {
        cart = new ArrayList<Product>();
    }


}
