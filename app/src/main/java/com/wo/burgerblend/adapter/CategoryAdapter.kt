package com.wo.burgerblend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Category

class CategoryAdapter(private var categories: List<Category>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(categoryId: Long)
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nameCategory: TextView = view.findViewById(R.id.textView_nameCategory)
        private var imageCategory: ImageView = view.findViewById(R.id.imageView_imageCategory)

        fun bind(category: Category, itemClickListener: ItemClickListener) {
            nameCategory.text = category.name

            Glide.with(itemView)
                .load(category.image) // Carga imagen desde la URL
                .placeholder(R.drawable.veggie_burger) // La imagen tarde en cargar
                .error(R.drawable.veggie_burger) // No se pueda cargar la imagen
                .into(imageCategory)

            /*itemView.setOnClickListener {
                Toast.makeText(itemView.context, "Categoría: ${category.name}", Toast.LENGTH_SHORT).show()
            }*/

            itemView.setOnClickListener {
                itemClickListener.onItemClick(category.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        return holder.bind(categories[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
