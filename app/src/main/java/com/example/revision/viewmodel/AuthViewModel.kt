package com.example.revision.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.revision.model.UserModel
import com.example.revision.repository.auth.AuthRepo
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(var repo: AuthRepo) : ViewModel(){

    fun login(email:String,password:String,callback:(Boolean,String?)-> Unit){
        repo.login(email,password,callback)
    }
    fun register(email:String,password:String,callback:(Boolean,String?,String?)-> Unit){
        repo.register(email,password,callback)
    }

    //adding remaining data to real time database
    fun addUserTODatabase(userId:String, userModel: UserModel, callback: (Boolean, String?) -> Unit){
        repo.addUserTODatabase(userId,userModel,callback)
    }

    fun forgetPassword(email:String,callback: (Boolean, String?) -> Unit){
        repo.forgetPassword(email,callback)
    }

    fun getCurrentUser() : FirebaseUser?{
        return repo.getCurrentUser()
    }

    fun logout(callback: (Boolean, String?) -> Unit){
        repo.logout(callback)
    }

    private var _userData = MutableLiveData<UserModel?>()
    var userData = MutableLiveData<UserModel?>()
        get() = _userData

    fun fetchUserData(userId:String){
        repo.getUserFromFirebase(userId){
            userModel, success, message ->
            if(success){
                _userData.value = userModel
            }
        }
    }


    fun uploadImages(imageName: String, imageUri: Uri, callback: (Boolean, String?, String?) -> Unit){
        repo.uploadImages(imageName,imageUri,callback)
    }

    fun updateUser(userID:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit){
        repo.updateUser(userID,data,callback)
    }

}