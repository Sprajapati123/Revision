package com.example.revision.repository.cart

import com.example.revision.model.CartModel
import com.example.revision.model.CategoryModel

interface CartRepo {
    fun addCart(cartModel: CartModel, callback: (Boolean, String?) -> Unit)

    fun deleteCart(cartID:String,callback: (Boolean, String?) -> Unit)

    fun getCart(callback: (List<CartModel>?,Boolean, String?) -> Unit)



}