package com.example.revision.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.revision.R
import com.example.revision.model.CategoryModel
import com.example.revision.model.ProductModel
import com.example.revision.ui.activity.admin.UpdateCategoryActivity
import com.example.revision.ui.activity.admin.UpdateProductActivity
import com.squareup.picasso.Picasso


class ProductAdapter(var context: Context, var data : ArrayList<ProductModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var productname: TextView = view.findViewById(R.id.categoryName)
        var productDesc: TextView = view.findViewById(R.id.categoryDescription)
        var editLabel: TextView = view.findViewById(R.id.categoryEditLabel)

        var imageView: ImageView = view.findViewById(R.id.imageCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sample_category_admin,parent,false)
        return  ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.productname.text = data[position].productName
        holder.productDesc.text = data[position].description

        var image = data[position].imageUrl
        Picasso.get().load(image).into(holder.imageView)


        holder.editLabel.setOnClickListener {
            var intent = Intent(context, UpdateProductActivity::class.java)
            intent.putExtra("products",data[position])
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(product: List<ProductModel>){
        data.clear()
        data.addAll(product)
        notifyDataSetChanged()
    }

    fun getProductId(position: Int) : String{
        return data[position].id
    }

    fun getImageName(position: Int): String{
        return data[position].productName
    }
}