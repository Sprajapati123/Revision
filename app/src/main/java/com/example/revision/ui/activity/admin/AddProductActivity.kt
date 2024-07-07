package com.example.revision.ui.activity.admin

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.databinding.ActivityAddProductBinding
import com.example.revision.model.CategoryModel
import com.example.revision.model.ProductModel
import com.example.revision.repository.category.CategoryRepoImpl
import com.example.revision.repository.products.ProductRepoImpl
import com.example.revision.utils.ImageUtils
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.CategoryViewModel
import com.example.revision.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class AddProductActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var addProductBinding: ActivityAddProductBinding
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var productViewModel: ProductViewModel
    var category: String = ""

    lateinit var imageUtils: ImageUtils
    var imageUri: Uri? = null
    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addProductBinding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(addProductBinding.root)

        var repo = CategoryRepoImpl()
        categoryViewModel = CategoryViewModel(repo)
        categoryViewModel.getAllCategory()

        var productRepo = ProductRepoImpl()
        productViewModel = ProductViewModel(productRepo)

        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)


        addProductBinding.spinnerCategory.onItemSelectedListener = this

        imageUtils.registerActivity { url ->
            url.let { it ->
                imageUri = it
                Picasso.get().load(it).into(addProductBinding.imageViewProductAdd)
            }
        }
        addProductBinding.imageViewProductBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        categoryViewModel.categoryData.observe(this){data->
            var categoryName = data?.map { category->
                category.categoryName
            } ?: emptyList()

            var arrayAdapter = ArrayAdapter(
                this@AddProductActivity,android.R.layout.simple_spinner_item,categoryName
            )

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            addProductBinding.spinnerCategory.adapter = arrayAdapter
        }
        addProductBinding.btnProductAdd.setOnClickListener {
            if(imageUri == null){
                Toast.makeText(applicationContext,"Please select image first", Toast.LENGTH_SHORT).show()
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
            productViewModel.uploadImages(imageName,uri!!){
                    success,url,message->
                if(success){
                    addProduct(url,imageName)
                }else{
                    Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun addProduct(url: String?,imageName: String?) {
        loadingUtils.showDialog()
        var productName : String = addProductBinding.editTextProductName.text.toString()
        var productPrice : Int = addProductBinding.editTextProductPrice.text.toString().toInt()
        var productDesc : String = addProductBinding.editTextProductDesc.text.toString()

        var data = ProductModel("",productName,productPrice,productDesc,category,imageName.toString(),url.toString())
        productViewModel.addProduct(data){
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
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent != null){
            category = parent.getItemAtPosition(position).toString()
        }
        Log.d("categoy selected",parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}