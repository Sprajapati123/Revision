package com.example.revision.adapter.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.revision.R
import com.example.revision.model.CartModel
import com.example.revision.model.CategoryModel
import com.example.revision.viewmodel.CartViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class CartAdapter(var context: Context, var data : ArrayList<CartModel>,var cartViewModel: CartViewModel) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var cartId: TextView = view.findViewById(R.id.IdCart)
        var productName: TextView = view.findViewById(R.id.productNameCartSample)
        var price: TextView = view.findViewById(R.id.priceCartSample)
        var quantity: TextView = view.findViewById(R.id.quantitySample)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBarSample)
        var imageView: ImageView = view.findViewById(R.id.imageViewCartSample)
        var removeButton: TextView = view.findViewById(R.id.removeCartSample)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sample_cart,parent,false)
        return  CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.cartId.text = "CID: "+data[position].cartId
        holder.productName.text = data[position].productName
        holder.price.text = "Rs. "+data[position].productPrice.toString()
        holder.quantity.text = "Qty: " + data[position].quantity.toString()

        //get image
        var image = data[position].productImage
        Picasso.get().load(image).into(holder.imageView,object: Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context,e?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        //delete cart

        holder.removeButton.setOnClickListener {
            cartViewModel.deleteCart(data[position].cartId){
                success,message->
              if(success){
                  Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
              }else{
                  Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
              }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(cart: List<CartModel>){
        data.clear()
        data.addAll(cart)
        notifyDataSetChanged()
    }



}