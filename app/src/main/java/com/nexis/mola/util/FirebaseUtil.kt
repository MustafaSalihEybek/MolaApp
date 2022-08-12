package com.nexis.mola.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtil {
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
}