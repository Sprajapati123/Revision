package com.example.revision.viewmodel

import androidx.lifecycle.ViewModel
import com.example.revision.model.CartModel
import com.example.revision.repository.cart.CartRepo

class CartViewModel(var repo: CartRepo) : ViewModel() {

    fun addCart(cartModel: CartModel, callback: (Boolean, String?) -> Unit){
        repo.addCart(cartModel, callback)
    }

    fun deleteCart(cartID:String,callback: (Boolean, String?) -> Unit){
        repo.deleteCart(cartID, callback)
    }

}