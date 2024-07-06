package com.example.revision.repository.category

import android.net.Uri
import com.example.revision.model.CategoryModel
import com.example.revision.model.UserModel

interface CategoryRepo {

    fun uploadImages(imageName: String, imageUri: Uri, callback: (Boolean, String?, String?) -> Unit)

    fun addCategory(categoryModel: CategoryModel, callback: (Boolean, String?) -> Unit)

    fun updateCategory(categoryId:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit)

    fun deleteCategory(categoryId:String,callback: (Boolean, String?) -> Unit)

    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit)

    fun getAllCategory(callback: (List<CategoryModel>?,Boolean, String?) -> Unit)

}