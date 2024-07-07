package com.example.revision.ui.activity.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revision.R
import com.example.revision.adapter.CategoryAdapter
import com.example.revision.adapter.ProductAdapter
import com.example.revision.databinding.ActivityProductDashboardBinding
import com.example.revision.repository.category.CategoryRepoImpl
import com.example.revision.repository.products.ProductRepoImpl
import com.example.revision.viewmodel.CategoryViewModel
import com.example.revision.viewmodel.ProductViewModel
import java.util.ArrayList

class ProductDashboardActivity : AppCompatActivity() {
    lateinit var productDashboardBinding: ActivityProductDashboardBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        productDashboardBinding = ActivityProductDashboardBinding.inflate(layoutInflater)
        setContentView(productDashboardBinding.root)

        var repo = ProductRepoImpl()
        productViewModel = ProductViewModel(repo)

        productViewModel.getAllProduct()

        productAdapter = ProductAdapter(
            this@ProductDashboardActivity,
            ArrayList()
        )

        //it is default variable
        productViewModel.productData.observe(this){product->
            product?.let {
                productAdapter.updateData(it)
            }
        }

        productViewModel.loadingState.observe(this){loading->
            if(loading){
                productDashboardBinding.progressBarDashProduct.visibility = View.VISIBLE
            }else{
                productDashboardBinding.progressBarDashProduct.visibility = View.GONE

            }
        }
        productDashboardBinding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductDashboardActivity)
            adapter = productAdapter
        }


        productDashboardBinding.productFloating.setOnClickListener {
            var intent = Intent(this@ProductDashboardActivity,AddProductActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}