package com.example.revision.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.revision.model.CategoryModel
import com.example.revision.model.UserModel
import com.example.revision.repository.category.CategoryRepo

class CategoryViewModel(val repo : CategoryRepo) : ViewModel() {

    fun uploadImages(imageName: String, imageUri: Uri, callback: (Boolean, String?, String?) -> Unit){
        repo.uploadImages(imageName,imageUri,callback)
    }

    fun addCategory(categoryModel: CategoryModel, callback: (Boolean, String?) -> Unit){
        repo.addCategory(categoryModel,callback)
    }

    fun updateCategory(categoryId:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit){
        repo.updateCategory(categoryId,data,callback)
    }

    fun deleteCategory(categoryId:String,callback: (Boolean, String?) -> Unit){
        repo.deleteCategory(categoryId,callback)
    }

    private var _categoryData = MutableLiveData<List<CategoryModel>?>()
    var categoryData = MutableLiveData<List<CategoryModel>?>()
        get() = _categoryData


    private var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun getAllCategory(){
        _loadingState.value = true
        repo.getAllCategory{
            categoryList,success,message->
            if(categoryList!=null){
                _loadingState.value = false
                _categoryData.value = categoryList
            }
        }
    }


    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit){
        repo.deleteImage(imageName,callback)
    }

}