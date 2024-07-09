package com.example.revision.repository.cart

import com.example.revision.model.CartModel
import com.example.revision.model.CategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartRepoImpl : CartRepo {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference : DatabaseReference = database.reference.child("cart")
    var auth : FirebaseAuth = FirebaseAuth.getInstance()

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

    override fun getCart(callback: (List<CartModel>?, Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid.toString()

        val query = reference.orderByChild("userId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var cartList = mutableListOf<CartModel>()
                for(eachCart in snapshot.children){
                    var category = eachCart.getValue(CartModel::class.java)
                    if(category !=null){
                        cartList.add(category)
                    }
                }
                callback(cartList,true,"Data fetched success")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,"${error.message}")
            }

        })
    }


}