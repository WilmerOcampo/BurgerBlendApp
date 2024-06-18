package com.wo.burgerblend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.helper.CartHelperJ

class CartAdapter(private val foods: List<Food>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nameFood: TextView = view.findViewById(R.id.textView_nameFoodCart)
        private var priceFood: TextView = view.findViewById(R.id.textView_priceFoodCart)
        private var imageFood: ImageView = view.findViewById(R.id.imageView_imageFoodCart)
        private var totalPrice: TextView = view.findViewById(R.id.textView_totalPriceOrderCart)
        private var quantityOrderFood: TextView = view.findViewById(R.id.textView_quantityOrderCart)
        private var buttonPlus: ImageView = view.findViewById(R.id.imageView_buttonPlusOrderCart)
        private var buttonMinus: ImageView = view.findViewById(R.id.imageView_buttonMinusOrderCart)

        fun bind(food: Food) {
            nameFood.text = food.name
            priceFood.text = food.price.toString()
            totalPrice.text = String.format("%.2f", food.quantity * food.price)
            quantityOrderFood.text = food.quantity.toString()

            val drawableResourceId: Int = itemView.context.resources.getIdentifier(food.image, "drawable", itemView.context.packageName)

            imageFood.setImageResource(drawableResourceId)

            val cartHelper = CartHelperJ(itemView.context)

            buttonPlus.setOnClickListener {
                if (quantityOrderFood.text.toString().toInt() > 0) {
                    cartHelper.plusQuantity(cartHelper.getCart(), adapterPosition)
                    quantityOrderFood.text = (quantityOrderFood.text.toString().toInt() + 1).toString()
                    totalPrice.text = String.format("%.2f", food.price * quantityOrderFood.text.toString().toDouble())
                }
            }

            buttonMinus.setOnClickListener {
                if (quantityOrderFood.text.toString().toInt() > 0) {
                    cartHelper.minusQuantity(cartHelper.getCart(), adapterPosition)
                    quantityOrderFood.text = (quantityOrderFood.text.toString().toInt() - 1).toString()
                    totalPrice.text = String.format("%.2f", food.price * quantityOrderFood.text.toString().toDouble())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        return holder.bind(foods[position])
    }

    override fun getItemCount(): Int {
        return foods.size
    }
}