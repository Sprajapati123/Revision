package com.example.revision.repository.category

import android.net.Uri
import com.example.revision.model.CategoryModel
import com.example.revision.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CategoryRepoImpl : CategoryRepo{

    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference : DatabaseReference = database.reference.child("category")


    var storage : FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = storage.reference.child("category")

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

    override fun addCategory(categoryModel: CategoryModel, callback: (Boolean, String?) -> Unit) {
        var id = reference.push().key.toString()
        categoryModel.categoryId = id

        reference.child(id).setValue(categoryModel).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"Category added")
            }else{
                callback(false,"Unable to add category")

            }

        }
    }

    override fun updateCategory(
        categoryId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String?) -> Unit
    ) {
        data.let { it->
            reference.child(categoryId).updateChildren(it).addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Successfully updated")
                }else{
                    callback(false,"Unable to update data")

                }
            }
        }
    }

    override fun deleteCategory(
        categoryId: String,
        callback: (Boolean, String?) -> Unit
    ) {
            reference.child(categoryId).removeValue().addOnCompleteListener {
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

    override fun getAllCategory(callback: (List<CategoryModel>?, Boolean, String?) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var categoryList = mutableListOf<CategoryModel>()
                for(eachCategory in snapshot.children){
                    var category = eachCategory.getValue(CategoryModel::class.java)
                    if(category !=null){
                        categoryList.add(category)
                    }
                }
                callback(categoryList,true,"Data fetched success")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,"${error.message}")
            }

        })
    }

}