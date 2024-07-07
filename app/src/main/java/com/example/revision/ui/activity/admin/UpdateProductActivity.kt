package com.example.revision.ui.activity.admin

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.databinding.ActivityUpdateCategoryBinding
import com.example.revision.databinding.ActivityUpdateProductBinding
import com.example.revision.model.CategoryModel
import com.example.revision.model.ProductModel
import com.example.revision.repository.category.CategoryRepoImpl
import com.example.revision.repository.products.ProductRepoImpl
import com.example.revision.utils.ImageUtils
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.CategoryViewModel
import com.example.revision.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class UpdateProductActivity : AppCompatActivity() {
    lateinit var imageUtils: ImageUtils
    var imageUri: Uri? = null
    lateinit var loadingUtils: LoadingUtils
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel

    var productId = ""
    var imageName = ""
    var category = ""

    lateinit var updateProductBinding: ActivityUpdateProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)
        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)
        var repo = ProductRepoImpl()
        productViewModel = ProductViewModel(repo)

        var categoryRepo = CategoryRepoImpl()
        categoryViewModel = CategoryViewModel(categoryRepo)
        categoryViewModel.getAllCategory()

        imageUtils.registerActivity { url ->
            url.let { it ->
                imageUri = it
                Picasso.get().load(it).into(updateProductBinding.imageViewProductAddUpdate)
            }
        }
        var product : ProductModel? = intent.getParcelableExtra("products")
        productId = product?.id.toString()
        imageName = product?.imageName.toString()
        category = product?.categoryName.toString()

        updateProductBinding.editTextProductNameUpdate.setText(product?.productName)
        updateProductBinding.editTextProductPriceUpdate.setText(product?.price.toString())
        updateProductBinding.editTextProductDescUpdate.setText(product?.description)

        Picasso.get().load(product?.imageUrl).into(updateProductBinding.imageViewProductAddUpdate)

        categoryViewModel.categoryData.observe(this){data->
            var categoryName = data?.map { category->
                category.categoryName
            } ?: emptyList()

            var arrayAdapter = ArrayAdapter(
                this@UpdateProductActivity,android.R.layout.simple_spinner_item,categoryName
            )

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            updateProductBinding.spinnerCategoryUpdate.adapter = arrayAdapter

            val defaultPosition = categoryName.indexOf(category)
            if(defaultPosition >=0){
                updateProductBinding.spinnerCategoryUpdate.setSelection(defaultPosition)
            }
        }


        updateProductBinding.imageViewProductBrowseUpdate.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        updateProductBinding.btnProductUpdate.setOnClickListener {
            if(imageUri == null){
                loadingUtils.showDialog()
                updateProduct(product?.imageUrl.toString())
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
        imageUri.let { uri ->
            productViewModel.uploadImages(imageName,uri!!){
                    success,url,message->
                if(success){

                    updateProduct(url)
                }else{
                    Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun updateProduct(url: String?) {


        var updatedName : String = updateProductBinding.editTextProductNameUpdate.text.toString()
        var updatedDesc : String = updateProductBinding.editTextProductDescUpdate.text.toString()
        var updatedPrice : Int = updateProductBinding.editTextProductPriceUpdate.text.toString().toInt()

        var updatedMap = mutableMapOf<String,Any?>()

        updatedMap["productName"] = updatedName
        updatedMap["description"] = updatedDesc
        updatedMap["price"] = updatedPrice
        updatedMap["imageUrl"] = url.toString()

        productViewModel.updateProduct(productId,updatedMap){
                success,message->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                finish()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
            }
        }
    }

}