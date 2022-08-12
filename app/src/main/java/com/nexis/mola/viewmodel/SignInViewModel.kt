package com.nexis.mola.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.mola.util.FirebaseUtil
import com.nexis.mola.viewmodel.base.BaseViewModel

class SignInViewModel(application: Application) : BaseViewModel(application) {
    val signedUserId = MutableLiveData<String>()

    fun signInUser(userEmail: String, userPassword: String){
        FirebaseUtil.mAuth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    FirebaseUtil.fUser = it.result.user

                    FirebaseUtil.fUser?.let {
                        if (it.isEmailVerified){
                            successMessage.value = "Başarıyla Giriş Yaptınız"
                            signedUserId.value = it.uid
                        } else
                            errorMessage.value = "Lütfen email adresinize gelen linki onaylayınız"
                    }
                } else
                    errorMessage.value = it.exception?.message
            }
    }

    fun checkSignedUser(){
        FirebaseUtil.fUser = FirebaseUtil.mAuth.currentUser

        FirebaseUtil.fUser?.let {
            if (it.isEmailVerified)
                signedUserId.value = it.uid
            else
                errorMessage.value = "Lütfen email adresinize gelen linki onaylayınız"
        }
    }
}