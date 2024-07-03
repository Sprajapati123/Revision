package com.example.revision.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.revision.R
import com.example.revision.databinding.ActivityNavigationBinding
import com.example.revision.ui.fragment.CartFragment
import com.example.revision.ui.fragment.CategoryFragment
import com.example.revision.ui.fragment.HomeFragment
import com.example.revision.ui.fragment.ProfileFragment

class NavigationActivity : AppCompatActivity() {
    lateinit var navigationBinding: ActivityNavigationBinding

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        navigationBinding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(navigationBinding.root)

        replaceFragment(HomeFragment())
        navigationBinding.buttomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menuHome -> replaceFragment(HomeFragment())
                R.id.menuCategory -> replaceFragment(CategoryFragment())
                R.id.menuCart -> replaceFragment(CartFragment())
                R.id.menuProfile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}