package com.wo.burgerblend.helper;

import android.content.Context;
import android.widget.Toast;

import com.wo.burgerblend.domain.Food;

import java.util.ArrayList;

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

    public void plusQuantity(ArrayList<Food> foods, int position) {
        foods.get(position).setQuantity(foods.get(position).getQuantity() + 1);
        tinyDB.putListObject("cart", foods);
    }

    public void minusQuantity(ArrayList<Food> foods, int position) {
        if (foods.get(position).getQuantity() == 1) {
            foods.remove(position);
        } else {
            foods.get(position).setQuantity(foods.get(position).getQuantity() - 1);
        }
        tinyDB.putListObject("cart", foods);
    }

    public void removeFromCart(Food food) {

    }

    public void clearCart() {

    }

    public int getCartSize() {
        return getCart().size();
    }

    public Double getTotalPrice() {
        ArrayList<Food> cart = getCart();
        double totalPrice = 0.0;
        for (int i = 0; i < cart.size(); i++) {
            //totalPrice += cart.get(i).getPrice() * cart.get(i).getQuantity();
            totalPrice = totalPrice + cart.get(i).getPrice() * cart.get(i).getQuantity();
        }
        return totalPrice;
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
