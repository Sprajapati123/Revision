package com.example.revision.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.revision.R
import com.example.revision.databinding.FragmentProfileBinding
import com.example.revision.repository.AuthRepoImpl
import com.example.revision.ui.activity.EditProfileActivity
import com.example.revision.ui.activity.LoginActivity
import com.example.revision.viewmodel.AuthViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


class ProfileFragment : Fragment() {
    lateinit var profileBinding: FragmentProfileBinding
    lateinit var authViewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(inflater,container,false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing auth viewmodel
        var repo = AuthRepoImpl()
        authViewModel = AuthViewModel(repo)

        var currentUser = authViewModel.getCurrentUser()
        currentUser.let {
            authViewModel.fetchUserData(currentUser?.uid.toString())
        }

        profileBinding.editProfileCard.setOnClickListener {
            var intent = Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("userData",authViewModel.userData.value)
            startActivity(intent)
        }

        authViewModel.userData.observe(requireActivity()){users->
            users.let {
                if(users?.imageUrl == null || users.imageUrl.isEmpty()){
                    profileBinding.innerImage.setImageResource(R.drawable.profile)
                    profileBinding.progressBarImage.visibility = View.GONE
                }else{
                    Picasso.get().load(users.imageUrl).into(profileBinding.innerImage,object : Callback{
                        override fun onSuccess() {

                            profileBinding.progressBarImage.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                           Toast.makeText(requireContext(),e?.message,Toast.LENGTH_LONG).show()
                        }

                    })
                }
                profileBinding.profileEmail.text = users?.email.toString()
                profileBinding.profileFullName.text = users?.name.toString()
            }
        }
        profileBinding.editLogout.setOnClickListener {
            authViewModel.logout{
                success,message->
                if(success){
                    var intent = Intent(requireContext(),LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }else{
                    Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}