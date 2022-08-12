package com.nexis.mola.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.nexis.mola.R
import com.nexis.mola.databinding.FragmentSignInBinding
import com.nexis.mola.util.Singleton
import com.nexis.mola.view.show
import com.nexis.mola.viewmodel.SignInViewModel

class SignInFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var signInBinding: FragmentSignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var navDirections: NavDirections

    private lateinit var userEmail: String
    private lateinit var userPassword: String

    private fun init(){
        signInBinding.signInFragmentBtnSignUp.setOnClickListener(this)
        signInBinding.signInFragmentBtnSignIn.setOnClickListener(this)

        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        observeLiveData()
        signInViewModel.checkSignedUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signInBinding = FragmentSignInBinding.inflate(inflater)
        return signInBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        signInViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        signInViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        signInViewModel.signedUserId.observe(viewLifecycleOwner, Observer {
            it?.let {
                goToMainPage(it)
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.sign_in_fragment_btnSignIn -> signInUser()
                R.id.sign_in_fragment_btnSignUp -> Singleton.setPage(1)
            }
        }
    }

    private fun signInUser(){
        userEmail = signInBinding.signInFragmentEditEmail.text.toString().trim()
        userPassword = signInBinding.signInFragmentEditPassword.text.toString().trim()

        if (!userEmail.isEmpty()){
            if (!userPassword.isEmpty())
                signInViewModel.signInUser(userEmail, userPassword)
            else
                userPassword.show(v, "Lütfen şifrenizi giriniz")
        } else
            userEmail.show(v, "Lütfen email adresinizi giriniz")
    }

    private fun goToMainPage(userId: String){
        navDirections = SignFragmentDirections.actionSignFragmentToMainFragment(userId)
        Navigation.findNavController(v).navigate(navDirections)
    }
}