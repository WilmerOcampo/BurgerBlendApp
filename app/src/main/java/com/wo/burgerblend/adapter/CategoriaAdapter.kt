package com.wo.burgerblend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wo.burgerblend.R
import com.wo.burgerblend.domain.Categoria

class CategoriaAdapter(private var categorias: List<Categoria>) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {
    class CategoriaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var nombreCategoria: TextView = view.findViewById(R.id.textView_nombreCategoria)
        private var imgCategoria: ImageView = view.findViewById(R.id.imageView_imgCategoria)

        fun bind(categoria: Categoria) {
            nombreCategoria.text = categoria.nombre
            var imgUrl = ""
            if(categoria.foto.contains("cat_")) {
                imgUrl = categoria.foto
            }
            val drawableResourceId: Int = itemView.context.resources.getIdentifier(imgUrl, "drawable", itemView.context.packageName)
            imgCategoria.setImageResource(drawableResourceId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        return CategoriaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        return holder.bind(categorias[position])
    }

    override fun getItemCount(): Int {
        return categorias.size
    }
}
