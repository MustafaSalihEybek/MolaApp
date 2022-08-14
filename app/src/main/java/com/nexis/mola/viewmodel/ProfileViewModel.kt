package com.nexis.mola.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.nexis.mola.model.User
import com.nexis.mola.util.AppUtil
import com.nexis.mola.util.FirebaseUtil
import com.nexis.mola.viewmodel.base.BaseViewModel

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    val userData = MutableLiveData<User>()

    fun getUserData(userId: String){
        FirebaseUtil.mDocRef = FirebaseUtil.mFireStore.collection("Users").document(userId)
        FirebaseUtil.mDocRef.get()
            .addOnSuccessListener {
                if (it.exists()){
                    AppUtil.userData = it.toObject(User::class.java)!!
                    userData.value = AppUtil.userData
                }
            }.addOnFailureListener {
                errorMessage.value = it.message
            }
    }
}