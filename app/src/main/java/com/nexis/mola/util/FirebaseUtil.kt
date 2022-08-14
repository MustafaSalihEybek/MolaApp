package com.nexis.mola.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nexis.mola.model.User

object FirebaseUtil {
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var fUser: FirebaseUser? = null
    lateinit var mQuery: Query
    lateinit var mDocRef: DocumentReference

    fun getUserDataOneTime(userId: String, getUserDataOneTimeOnComplete: (dState: Boolean, user: User?) -> Unit){
        mDocRef = mFireStore.collection("Users").document(userId)
        mDocRef.get()
            .addOnSuccessListener {
                if (it.exists()){
                    AppUtil.userData = it.toObject(User::class.java)!!
                    getUserDataOneTimeOnComplete(true, AppUtil.userData)
                }else
                    getUserDataOneTimeOnComplete(false, null)
            }.addOnFailureListener {
                getUserDataOneTimeOnComplete(false, null)
            }
    }
}