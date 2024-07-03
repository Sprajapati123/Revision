package com.example.revision.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.databinding.ActivityLoginBinding
import com.example.revision.repository.AuthRepoImpl
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var authViewModel: AuthViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        //initializing auth viewmodel
        var repo = AuthRepoImpl()
        authViewModel = AuthViewModel(repo)

        //initializing loading
        loadingUtils = LoadingUtils(this)

        loginBinding.btnLogin.setOnClickListener {
            loadingUtils.showDialog()
            var email :String = loginBinding.emailLogin.text.toString()
            var password :String = loginBinding.passwordLogin.text.toString()

            authViewModel.login(email,password){
                success,message->
                if(success){
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                    var intent = Intent(this@LoginActivity,NavigationActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()

                }
            }
        }
        loginBinding.createAccount.setOnClickListener {
            var intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)

        }

        loginBinding.forgetPassword.setOnClickListener {
            var intent = Intent(this@LoginActivity,ForgetPasswordActivity::class.java)
            startActivity(intent)

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}