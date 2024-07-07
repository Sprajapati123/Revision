package com.example.revision.repository.products

import android.net.Uri
import com.example.revision.model.CategoryModel
import com.example.revision.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductRepoImpl : ProductRepo {

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference : DatabaseReference = database.reference.child("products")


    var storage : FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = storage.reference.child("products")

    override fun uploadImages(
        imageName: String,
        imageUri: Uri,
        callback: (Boolean, String?, String?) -> Unit
    ) {
        var imageReference = storageRef.child(imageName)
        imageUri.let {url->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {it->
                    var imageUrl = it.toString()
                    callback(true,imageUrl,"Upload success")
                }
            }.addOnFailureListener{
                callback(false,"","unable to upload")
            }
        }
    }

    override fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit) {
        var id = reference.push().key.toString()
        productModel.id = id

        reference.child(id).setValue(productModel).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"Product added")
            }else{
                callback(false,"Unable to add product")

            }

        }
    }

    override fun updateProduct(
        productId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String?) -> Unit
    ) {
        data.let { it->
            reference.child(productId).updateChildren(it).addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Successfully updated")
                }else{
                    callback(false,"Unable to update data")

                }
            }
        }
    }

    override fun deleteProduct(productId: String, callback: (Boolean, String?) -> Unit) {
        reference.child(productId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Successfully deleted")
            }else{
                callback(false,"Unable to delete data")

            }
        }
    }

    override fun deleteImage(imageName: String, callback: (Boolean, String?) -> Unit) {
        storageRef.child(imageName).delete().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Successfully deleted")
            }else{
                callback(false,"Unable to delete image")

            }
        }
    }

    override fun getAllProduct(callback: (List<ProductModel>?, Boolean, String?) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var productList = mutableListOf<ProductModel>()
                for(eachCategory in snapshot.children){
                    var products = eachCategory.getValue(ProductModel::class.java)
                    if(products !=null){
                        productList.add(products)
                    }
                }
                callback(productList,true,"Data fetched success")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,"${error.message}")
            }

        })
    }
}