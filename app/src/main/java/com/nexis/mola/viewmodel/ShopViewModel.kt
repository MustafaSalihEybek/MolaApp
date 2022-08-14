package com.nexis.mola.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.nexis.mola.model.ShopItem
import com.nexis.mola.model.User
import com.nexis.mola.util.AppUtil
import com.nexis.mola.util.FirebaseUtil
import com.nexis.mola.viewmodel.base.BaseViewModel

class ShopViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var shopItems: ArrayList<ShopItem>

    val shopItemList = MutableLiveData<ArrayList<ShopItem>>()
    val shopHistorySaveState = MutableLiveData<Boolean>()
    val userData = MutableLiveData<User>()

    fun getShopItems(){
        FirebaseUtil.mQuery = FirebaseUtil.mFireStore.collection("ShopItems")
            .orderBy("shopMinutes", Query.Direction.ASCENDING)
        FirebaseUtil.mQuery.get()
            .addOnSuccessListener {
                if (it.documents.size > 0){
                    shopItems = ArrayList()

                    for (snapshot in it.documents.indices){
                        if (it.documents.get(snapshot).exists()){
                            AppUtil.shopItemData = it.documents.get(snapshot).toObject(ShopItem::class.java)!!
                            shopItems.add(AppUtil.shopItemData)

                            if (snapshot == it.documents.size - 1)
                                shopItemList.value = shopItems
                        }
                    }
                }
            }.addOnFailureListener {
                errorMessage.value = it.message
            }
    }

    fun saveHistoryData(purchaseData: HashMap<String, Any>, userId: String){
        FirebaseUtil.mFireStore.collection("Users").document(userId)
            .collection("PurchasesHistory").document(purchaseData.get("purchaseId").toString())
            .set(purchaseData)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    shopHistorySaveState.value = true
                else
                    errorMessage.value = "Something went wrong: ${it.exception?.message}"
            }
    }

    fun getUserData(userId: String){
        FirebaseUtil.getUserDataOneTime(userId, getUserDataOneTimeOnComplete = {dState, user ->
            if (dState){
                user?.let {
                    userData.value = it
                }
            }
        })
    }

    fun updateUserData(data: Map<String, Any>, userId: String){
        FirebaseUtil.mFireStore.collection("Users").document(userId)
            .update(data)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    successMessage.value = "Satın alma işlemi başarıyla gerçekleşti"
                else
                    errorMessage.value = "Bir sorun oluştu: ${it.exception?.message}"
            }
    }
}