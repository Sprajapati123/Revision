package com.example.revision.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.databinding.ActivityProductDetailBinding
import com.example.revision.model.CartModel
import com.example.revision.model.ProductModel
import com.example.revision.repository.auth.AuthRepoImpl
import com.example.revision.repository.cart.CartRepoImpl
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.AuthViewModel
import com.example.revision.viewmodel.CartViewModel
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    lateinit var authViewModel: AuthViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var productDetailBinding: ActivityProductDetailBinding
    var quantity : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        productDetailBinding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(productDetailBinding.root)

        loadingUtils = LoadingUtils(this)
        var cartRepo = CartRepoImpl()
        cartViewModel = CartViewModel(cartRepo)

        var authRepo = AuthRepoImpl()
        authViewModel = AuthViewModel(authRepo)


        setSupportActionBar(productDetailBinding.toolBarDetail)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title  ="Product Detail"

        var products : ProductModel? = intent.getParcelableExtra("products")

        productDetailBinding.productNameDetail.text = products?.productName
        productDetailBinding.descriptionDetail.text = products?.description
        productDetailBinding.productPriceDetail.text = products?.price.toString()

        Picasso.get().load(products?.imageUrl).into(productDetailBinding.imageViewDetail)

        productDetailBinding.btnAdds.setOnClickListener {
            if(quantity<5){
                quantity++
                productDetailBinding.quantityDetail.text = quantity.toString()
            }

        }

        productDetailBinding.btnSubtract.setOnClickListener {
            if(quantity>1){
                quantity--
                productDetailBinding.quantityDetail.text = quantity.toString()

            }
       }

        productDetailBinding.btnAddCart.setOnClickListener {
            addToCart(products)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addToCart(products: ProductModel?) {
        loadingUtils.showDialog()
        var currentUser = authViewModel.getCurrentUser()
        var cartModel = CartModel("",
            products?.id.toString(),
            products?.price.toString().toInt(),
            products?.imageUrl.toString(),
            products?.productName.toString(),
            quantity,
            currentUser?.uid.toString()
            )

        cartViewModel.addCart(cartModel){
            success,message->
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Close this activity and return to the previous one
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}