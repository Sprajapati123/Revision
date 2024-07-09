package com.example.revision.repository.cart

import com.example.revision.model.CartModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CartRepoImpl : CartRepo {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference : DatabaseReference = database.reference.child("cart")

    override fun addCart(cartModel: CartModel, callback: (Boolean, String?) -> Unit) {
        var id = reference.push().key.toString()
        cartModel.cartId = id

        reference.child(id).setValue(cartModel).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"Added to cart")
            }else{
                callback(false,"Failed adding to cart")

            }

        }
    }

    override fun deleteCart(cartID: String, callback: (Boolean, String?) -> Unit) {
        reference.child(cartID).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Cart Successfully deleted")
            }else{
                callback(false,"Unable to delete cart")

            }
        }

    }
}