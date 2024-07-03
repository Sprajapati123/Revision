package com.example.revision.repository

import android.net.Uri
import com.example.revision.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AuthRepoImpl : AuthRepo {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference : DatabaseReference = database.reference.child("users")


    var storage : FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = storage.reference.child("users")

    override fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"Login success")
            }else{
                callback(false,"Unable to login")
            }
        }
    }

    override fun register(email: String, password: String, callback: (Boolean, String?,String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"Register success",auth.currentUser?.uid)
            }else{
                callback(false,"Unable to register","")
            }
        }
    }

    override fun addUserTODatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String?) -> Unit
    ) {
        //setting id in usermodel
        userModel.id = userId

        reference.child(userId).setValue(userModel).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"User registered")
            }else{
                callback(false,"Unable to register user")

            }

        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { res ->
            if (res.isSuccessful) {
                callback(true, "Reset mail sent to $email")
            } else {
                callback(false, "Unable to reset password",)
            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserFromFirebase(
        userId: String,
        callback: (UserModel?, Boolean, String?) -> Unit
    ) {
        reference.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   val userModel = snapshot.getValue(UserModel::class.java)
                   callback(userModel,true,"Fetch success")

               }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,"${error.message}")
            }

        })
    }

    override fun logout(callback: (Boolean, String?) -> Unit) {
        try{
            auth.signOut()
            callback(true,"Logout success")
        }catch (e: Exception){
            callback(false,e.message)
        }
    }

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

    override fun updateUser(
        userID: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String?) -> Unit
    ) {
        data.let { it->
            reference.child(userID).updateChildren(it).addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Successfully updated")
                }else{
                    callback(false,"Unable to update data")

                }
            }
        }
    }
}