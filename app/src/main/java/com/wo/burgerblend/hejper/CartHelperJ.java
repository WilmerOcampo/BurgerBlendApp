package com.wo.burgerblend.hejper;

import android.content.Context;
import android.widget.Toast;

import com.wo.burgerblend.domain.Food;

import java.util.ArrayList;
import java.util.Arrays;

public class CartHelperJ {
    private Context context;
    private TinyDB tinyDB;
    public CartHelperJ(Context context) {
        this.context = context;
        tinyDB = new TinyDB(context);
    }
    public void addToCart(Food food) {
        ArrayList<Food> cart = getCart();
        Boolean found = false;
        int index = 0;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getName().equals(food.getName())) {
                found = true;
                index = i;
                break;
            }
        }
        if (found) {
            cart.get(index).setQuantity(food.getQuantity());
        } else {
            cart.add(food);
        }
        tinyDB.putListObject("cart", cart);
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
    }
    public ArrayList<Food> getCart() {
        return tinyDB.getListObject("cart");
    }
    public void removeFromCart(Food food) {

    }
    public void clearCart() {

    }
    public int getCartSize() {
        return getCart().size();
    }
    public double getTotalPrice() {
        return 0;
    }
    public void saveCart() {

    }
    public void loadCart() {

    }
    public void clearData() {

    }
    public void initCart() {

    }



}
