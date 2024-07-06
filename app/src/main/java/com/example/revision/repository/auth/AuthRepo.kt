package com.example.revision.repository.auth

import android.net.Uri
import com.example.revision.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {

//    {
//        "success":true,
//        "status":200,
//        "message":"Login succesful",
//        "token":"eybjnfkbdjkaf"
//          "id":"sasasas",
    //      "users":{
//}
//      }
    //authentication
    fun login(email:String,password:String,callback:(Boolean,String?)-> Unit)
    fun register(email:String,password:String,callback:(Boolean,String?,String?)-> Unit)

    //adding remaining data to real time database
    fun addUserTODatabase(userId:String,userModel: UserModel,callback: (Boolean, String?) -> Unit)

    fun forgetPassword(email:String,callback: (Boolean, String?) -> Unit)

    fun getCurrentUser() : FirebaseUser?

    fun getUserFromFirebase(userId:String,callback: (UserModel?,Boolean, String?) -> Unit)

    fun logout(callback: (Boolean, String?) -> Unit)

    fun uploadImages(imageName: String,imageUri: Uri,callback: (Boolean, String?, String?) -> Unit)

    fun updateUser(userID:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit)

}