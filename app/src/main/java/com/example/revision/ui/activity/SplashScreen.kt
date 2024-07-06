package com.example.revision.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.databinding.ActivitySplashScreenBinding
import com.example.revision.repository.auth.AuthRepoImpl
import com.example.revision.viewmodel.AuthViewModel

class SplashScreen : AppCompatActivity() {

    lateinit var splashScreenBinding: ActivitySplashScreenBinding
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        //initializing auth viewmodel
        var repo = AuthRepoImpl()
        authViewModel = AuthViewModel(repo)


        var currentUser = authViewModel.getCurrentUser()

        Handler(Looper.getMainLooper()).postDelayed({
            if(currentUser == null){
                var intent = Intent(this@SplashScreen,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                var intent = Intent(this@SplashScreen,NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }

        },3000)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}