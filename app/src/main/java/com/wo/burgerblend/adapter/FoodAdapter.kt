package com.wo.burgerblend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.food.DetailFoodActivity
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.helper.CartHelper

class FoodAdapter(private var foods: List<Food>) :
    RecyclerView.Adapter<FoodAdapter.PopularFoodViewHolder>() {
    class PopularFoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nameFood: TextView = view.findViewById(R.id.textView_nameFood)
        private var imageFood: ImageView = view.findViewById(R.id.imageView_imageFood)
        private var priceFood: TextView = view.findViewById(R.id.textView_priceFood)
        private var btnAddFood: TextView = view.findViewById(R.id.textView_buttonAddFood)

        fun bind(food: Food) {
            val cartHelper = CartHelper(itemView.context)
            nameFood.text = food.name
            priceFood.text = food.price.toString()

            Glide.with(itemView)
                .load(food.image) // Carga imagen desde la URL
                .placeholder(R.drawable.veggie_burger) // La imagen tarde en cargar
                .error(R.drawable.veggie_burger) // No se pueda cargar la imagen
                .into(imageFood)

            btnAddFood.setOnClickListener {
                val quantity = 1
                food.quantity = quantity
                cartHelper.addToCart(food)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailFoodActivity::class.java)
                intent.putExtra("food", food)
                itemView.context.startActivity(intent)
            }
        }
    }

    fun foods(list: List<Food>) {
        foods = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFoodViewHolder {
        return PopularFoodViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularFoodViewHolder, position: Int) {
        return holder.bind(foods[position])
    }

    override fun getItemCount(): Int {
        return foods.size
    }
}
