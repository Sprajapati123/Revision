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
import com.example.revision.ui.activity.admin.UpdateCategoryActivity
import com.squareup.picasso.Picasso

class CategoryAdapter(var context: Context,var data : ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var categoryName: TextView = view.findViewById(R.id.categoryName)
        var categoryDesc: TextView = view.findViewById(R.id.categoryDescription)
        var editLabel: TextView = view.findViewById(R.id.categoryEditLabel)

        var imageView: ImageView = view.findViewById(R.id.imageCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sample_category_admin,parent,false)
        return  CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
      return data.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.categoryName.text = data[position].categoryName
        holder.categoryDesc.text = data[position].categoryDescription

        var image = data[position].categoryImageUrl
        Picasso.get().load(image).into(holder.imageView)


        holder.editLabel.setOnClickListener {
            var intent = Intent(context,UpdateCategoryActivity::class.java)
            intent.putExtra("category",data[position])
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(category: List<CategoryModel>){
        data.clear()
        data.addAll(category)
        notifyDataSetChanged()
    }

    fun getCategoryId(position: Int) : String{
        return data[position].categoryId
    }

    fun getImageName(position: Int): String{
        return data[position].categoryImageName
    }
}