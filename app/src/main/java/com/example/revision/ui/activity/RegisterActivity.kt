package com.example.revision.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.revision.R
import com.example.revision.databinding.ActivityRegisterBinding
import com.example.revision.model.UserModel
import com.example.revision.repository.auth.AuthRepoImpl
import com.example.revision.utils.LoadingUtils
import com.example.revision.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    lateinit var registerBinding: ActivityRegisterBinding
    lateinit var authViewModel: AuthViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        //initializing auth viewmodel
        var repo = AuthRepoImpl()
        authViewModel = AuthViewModel(repo)

        //initializing loading
        loadingUtils = LoadingUtils(this)

        registerBinding.btnRegister.setOnClickListener {
            loadingUtils.showDialog()
            var email: String = registerBinding.editEmail.text.toString()
            var password: String = registerBinding.editPassword.text.toString()
            var name: String = registerBinding.editName.text.toString()
            var address: String = registerBinding.editAddress.text.toString()
            var age: String = registerBinding.editAge.text.toString()


            var userModel = UserModel("",email,name,address,age,"","")

            authViewModel.register(email,password){
                success,message,userId ->
                if(success){
                    addUserToDatabase(userId,userModel)
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                }
            }



        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addUserToDatabase(userId: String?, userModel: UserModel) {
        authViewModel.addUserTODatabase(userId.toString(),userModel){
            success,message->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                loadingUtils.dismiss()
            }
        }
    }
}