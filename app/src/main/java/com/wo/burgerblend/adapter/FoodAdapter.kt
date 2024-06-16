package com.wo.burgerblend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Food

class FoodAdapter(private var foods: List<Food>) : RecyclerView.Adapter<FoodAdapter.PopularFoodViewHolder>() {
    class PopularFoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nameFood: TextView = view.findViewById(R.id.textView_nameFood)
        private var imageFood: ImageView = view.findViewById(R.id.imageView_imageFood)
        private var priceFood: TextView = view.findViewById(R.id.textView_priceFood)
        private var btnAddFood: TextView = view.findViewById(R.id.textView_buttonAddFood)

        fun bind(food: Food) {
            nameFood.text = food.name
            priceFood.text = food.price.toString()
            val drawableResourceId: Int = itemView.context.resources.getIdentifier(food.image, "drawable", itemView.context.packageName)
            imageFood.setImageResource(drawableResourceId)
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
