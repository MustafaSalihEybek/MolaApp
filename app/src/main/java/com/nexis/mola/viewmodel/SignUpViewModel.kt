package com.nexis.mola.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.mola.model.User
import com.nexis.mola.util.FirebaseUtil
import com.nexis.mola.viewmodel.base.BaseViewModel

class SignUpViewModel(application: Application) : BaseViewModel(application) {
    val userId = MutableLiveData<String>()

    fun signUpUser(userEmail: String, userPassword: String){
        FirebaseUtil.mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    FirebaseUtil.fUser = it.result.user

                    FirebaseUtil.fUser?.let {
                        it.sendEmailVerification()
                            .addOnCompleteListener {
                                if (it.isSuccessful)
                                    userId.value = FirebaseUtil.fUser?.uid
                                else
                                    errorMessage.value = it.exception?.message
                            }
                    }
                } else
                    errorMessage.value = it.exception?.message
            }
    }

    fun saveUserData(userData: User){
        FirebaseUtil.mFireStore.collection("Users").document(userData.userId)
            .set(userData)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    successMessage.value = "Başarıyla Kayıt Oldunuz"
                else
                    errorMessage.value = it.exception?.message
            }
    }
}