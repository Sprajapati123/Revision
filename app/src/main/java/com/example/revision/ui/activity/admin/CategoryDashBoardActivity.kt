package com.example.revision.ui.activity.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.revision.R
import com.example.revision.adapter.CategoryAdapter
import com.example.revision.databinding.ActivityCategoryDashBoardBinding
import com.example.revision.repository.category.CategoryRepoImpl
import com.example.revision.utils.ImageUtils
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.CategoryViewModel
import java.util.ArrayList

class CategoryDashBoardActivity : AppCompatActivity() {
    lateinit var categoryDashBoardBinding: ActivityCategoryDashBoardBinding
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        categoryDashBoardBinding = ActivityCategoryDashBoardBinding.inflate(layoutInflater)
        setContentView(categoryDashBoardBinding.root)

        var repo = CategoryRepoImpl()
        categoryViewModel = CategoryViewModel(repo)

        categoryViewModel.getAllCategory()

        categoryAdapter = CategoryAdapter(
            this@CategoryDashBoardActivity,
            ArrayList()
        )

        //it is default variable
        categoryViewModel.categoryData.observe(this){category->
            category?.let {
                categoryAdapter.updateData(it)
            }
        }

        categoryViewModel.loadingState.observe(this){loading->
            if(loading){
                categoryDashBoardBinding.progressBarDashCategory.visibility = View.VISIBLE
            }else{
                categoryDashBoardBinding.progressBarDashCategory.visibility = View.GONE

            }
        }
        categoryDashBoardBinding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CategoryDashBoardActivity)
            adapter = categoryAdapter
        }


        categoryDashBoardBinding.categoryFloating.setOnClickListener {
            var intent = Intent(this@CategoryDashBoardActivity, AdmiAddCategoryActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = categoryAdapter.getCategoryId(viewHolder.adapterPosition)
                var imageName = categoryAdapter.getImageName(viewHolder.adapterPosition)

                categoryViewModel.deleteCategory(id){
                    success,message->
                    if(success){
                        categoryViewModel.deleteImage(imageName){
                                s,m->
                            if(s){
                                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }).attachToRecyclerView(categoryDashBoardBinding.categoryRecyclerView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}