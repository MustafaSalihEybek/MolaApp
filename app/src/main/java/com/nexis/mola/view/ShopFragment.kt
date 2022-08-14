package com.nexis.mola.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.google.firebase.firestore.FieldValue
import com.nexis.mola.R
import com.nexis.mola.adapter.ShopItemsAdapter
import com.nexis.mola.adapter.decoration.LinearManagerDecoration
import com.nexis.mola.databinding.FragmentShopBinding
import com.nexis.mola.model.ShopItem
import com.nexis.mola.util.Singleton
import com.nexis.mola.viewmodel.ShopViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ShopFragment(val userId: String) : Fragment() {
    private lateinit var v: View
    private lateinit var shopBinding: FragmentShopBinding
    private lateinit var shopViewModel: ShopViewModel

    private lateinit var shopItemsAdapter: ShopItemsAdapter
    private lateinit var shopList: ArrayList<ShopItem>
    private lateinit var billingClient: BillingClient
    private lateinit var mParams: SkuDetailsParams.Builder
    private lateinit var flowParams: BillingFlowParams
    private lateinit var mSkuList : ArrayList<String>
    private lateinit var purchaseUpdateListener: PurchasesUpdatedListener
    private lateinit var mPriceList: ArrayList<String>
    private lateinit var mFlowList : ArrayList<BillingFlowParams>
    private lateinit var selectedShopItem: ShopItem
    private lateinit var purchaseId: String
    private lateinit var mData: HashMap<String, Any>

    private fun init(){
        shopBinding.shopFragmentRecyclerView.setHasFixedSize(true)
        shopBinding.shopFragmentRecyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)

        purchaseUpdateListener = PurchasesUpdatedListener {billingResult, mutableList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && mutableList != null){
                for (purchase in mutableList)
                    handlePurchaseRuby(purchase)
            }
        }

        billingClient = BillingClient.newBuilder(v.context)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()

        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        observeLiveData()
        shopViewModel.getShopItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shopBinding = FragmentShopBinding.inflate(inflater)
        return shopBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        shopViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        shopViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        shopViewModel.shopHistorySaveState.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it)
                    shopViewModel.getUserData(userId)
            }
        })

        shopViewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {
                shopViewModel.updateUserData(mapOf("userSeconds" to (it.userSeconds + (selectedShopItem.shopMinutes * 60))), userId)
            }
        })

        shopViewModel.shopItemList.observe(viewLifecycleOwner, Observer {
            it?.let {
                shopList = it
                mSkuList = ArrayList()

                for (item in it)
                    mSkuList.add(item.shopSkuId)

                setBillingClientSystem(mSkuList, it)
            }
        })
    }

    private fun setBillingClientSystem(skuList: ArrayList<String>, shopList: ArrayList<ShopItem>){
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(p0: BillingResult) {
                if (p0.responseCode == BillingClient.BillingResponseCode.OK){
                    mParams = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

                    billingClient.querySkuDetailsAsync(mParams.build(), object :
                        SkuDetailsResponseListener {
                        override fun onSkuDetailsResponse(
                            p0: BillingResult,
                            p1: MutableList<SkuDetails>?
                        ) {
                            if (p0.responseCode == BillingClient.BillingResponseCode.OK && p1 != null){
                                mPriceList = ArrayList()
                                mFlowList = ArrayList()

                                for (sku in p1.indices){
                                    flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(p1.get(sku))
                                        .build()

                                    mPriceList.add(p1.get(sku).price)
                                    mFlowList.add(flowParams)

                                    if (sku == p1.size -1){
                                        if (shopBinding.shopFragmentRecyclerView.itemDecorationCount > 0)
                                            shopBinding.shopFragmentRecyclerView.removeItemDecorationAt(0)

                                        shopBinding.shopFragmentRecyclerView.addItemDecoration(LinearManagerDecoration(Singleton.V_SIZE, (shopList.size - 1)))
                                        shopItemsAdapter = ShopItemsAdapter(
                                            shopList,
                                            mPriceList,
                                            v.context,
                                            billingClient,
                                            mFlowList
                                        )
                                        shopBinding.shopFragmentRecyclerView.adapter = shopItemsAdapter

                                        shopItemsAdapter.setOnShopItemClickListener(object : ShopItemsAdapter.ShopItemClickListener{
                                            override fun onItemClick(shopItem: ShopItem) {
                                                selectedShopItem = shopItem
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    })
                }
            }

            override fun onBillingServiceDisconnected() {
                "msg".show(v, " Payment System Currently Not Available")
            }
        })
    }

    private fun handlePurchaseRuby(purchase: Purchase?){
        purchase?.let {
            if (it.purchaseState == Purchase.PurchaseState.PURCHASED){
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()

                val consumeResponseListener = ConsumeResponseListener { billingResult, s ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                        purchaseId = UUID.randomUUID().toString()

                        mData = HashMap()
                        mData.put("purchaseId", purchaseId)
                        mData.put("userId", userId)
                        mData.put("purchaseDate", FieldValue.serverTimestamp())
                        mData.put("shopId", selectedShopItem.shopId)

                        shopViewModel.saveHistoryData(mData, userId)
                    }
                }

                billingClient.consumeAsync(consumeParams, consumeResponseListener)
            }
        }
    }
}