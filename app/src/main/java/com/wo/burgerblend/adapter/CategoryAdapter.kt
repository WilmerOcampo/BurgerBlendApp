package com.wo.burgerblend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Category

class CategoryAdapter(private var categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nameCategory: TextView = view.findViewById(R.id.textView_nameCategory)
        private var imageCategory: ImageView = view.findViewById(R.id.imageView_imageCategory)

        fun bind(category: Category) {
            nameCategory.text = category.name
            var imgUrl = ""
            if(category.image.contains("cat_")) {
                imgUrl = category.image
            }
            val drawableResourceId: Int = itemView.context.resources.getIdentifier(imgUrl, "drawable", itemView.context.packageName)
            imageCategory.setImageResource(drawableResourceId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        return holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
