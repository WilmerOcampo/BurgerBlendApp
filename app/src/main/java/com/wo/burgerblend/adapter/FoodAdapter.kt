package com.wo.burgerblend.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.activity.DetailFoodActivity
import com.wo.burgerblend.domain.Food
import com.wo.burgerblend.helper.CartHelper

class FoodAdapter(private var foods: List<Food>) : RecyclerView.Adapter<FoodAdapter.PopularFoodViewHolder>() {
    class PopularFoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nameFood: TextView = view.findViewById(R.id.textView_nameFood)
        private var imageFood: ImageView = view.findViewById(R.id.imageView_imageFood)
        private var priceFood: TextView = view.findViewById(R.id.textView_priceFood)
        private var btnAddFood: TextView = view.findViewById(R.id.textView_buttonAddFood)
        private var itemFood: ConstraintLayout = view.findViewById(R.id.constraintLayout_itemFood)

        fun bind(food: Food) {
            var cartHelper = CartHelper(itemView.context)
            nameFood.text = food.name
            priceFood.text = food.price.toString()
            val drawableResourceId: Int = itemView.context.resources.getIdentifier(food.image, "drawable", itemView.context.packageName)
            imageFood.setImageResource(drawableResourceId)

            btnAddFood.setOnClickListener {
                val quantity = 1
                food.quantity = quantity
                cartHelper.addToCart(food)
            }
            itemFood.setOnClickListener {
                val intent = Intent(itemView.context, DetailFoodActivity::class.java)
                intent.putExtra("food", food)
                itemView.context.startActivity(intent)
            }
        }
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
