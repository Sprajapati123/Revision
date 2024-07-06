package com.example.revision.ui.activity.admin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.adapter.CategoryAdapter
import com.example.revision.databinding.ActivityAdmiAddCategoryBinding
import com.example.revision.model.CategoryModel
import com.example.revision.repository.category.CategoryRepoImpl
import com.example.revision.utils.ImageUtils
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.CategoryViewModel
import com.squareup.picasso.Picasso
import java.util.ArrayList
import java.util.UUID

class AdmiAddCategoryActivity : AppCompatActivity() {
    lateinit var imageUtils: ImageUtils
    var imageUri: Uri? = null
    lateinit var loadingUtils: LoadingUtils
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var addBinding: ActivityAdmiAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addBinding = ActivityAdmiAddCategoryBinding.inflate(layoutInflater)
        setContentView(addBinding.root)


        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)

        var repo = CategoryRepoImpl()
        categoryViewModel = CategoryViewModel(repo)


        imageUtils.registerActivity { url ->
            url.let { it ->
                imageUri = it
                Picasso.get().load(it).into(addBinding.imageView)
            }
        }
        addBinding.imageViewCategoryBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        addBinding.btnCategoryAdd.setOnClickListener {
            if(imageUri == null){
                Toast.makeText(applicationContext,"Please select image first",Toast.LENGTH_SHORT).show()
            }else{
                uploadImage()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage() {
        loadingUtils.showDialog()
        var imageName = UUID.randomUUID().toString()
        imageUri.let { uri ->
            categoryViewModel.uploadImages(imageName,uri!!){
                    success,url,message->
                if(success){
                    addCategory(url,imageName)
                }else{
                    Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun addCategory(url: String?,imageName: String?) {
        loadingUtils.showDialog()
        var categoryName : String = addBinding.editTextCategoryName.text.toString()
        var categoryDesc : String = addBinding.editTextCategoryDesc.text.toString()

        var data = CategoryModel("",url.toString(),imageName.toString(),categoryName,categoryDesc)
        categoryViewModel.addCategory(data){
            success,message ->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                finish()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

            }
        }

    }
}