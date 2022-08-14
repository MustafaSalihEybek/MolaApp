package com.nexis.mola.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.nexis.mola.R
import com.nexis.mola.databinding.ShopItemBinding
import com.nexis.mola.model.ShopItem

class ShopItemsAdapter(
    val shopItemList: ArrayList<ShopItem>,
    var priceList: ArrayList<String>,
    val context: Context,
    val billingClient: BillingClient,
    var flowList: ArrayList<BillingFlowParams>
    ) : RecyclerView.Adapter<ShopItemsAdapter.ShopItemsHolder>() {
    private lateinit var v: ShopItemBinding
    private lateinit var listener: ShopItemClickListener
    private var aPos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemsHolder {
        v = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.shop_item, parent, false)
        return ShopItemsHolder(v)
    }

    override fun onBindViewHolder(holder: ShopItemsHolder, position: Int) {
        holder.sV.shop = shopItemList.get(position)

        if (position < priceList.size)
            holder.txtPrice.text = priceList.get(position)

        holder.itemView.setOnClickListener {
            aPos = holder.adapterPosition

            if (aPos != RecyclerView.NO_POSITION){
                listener.onItemClick(shopItemList.get(aPos))
                billingClient.launchBillingFlow((context as Activity), flowList.get(aPos))
            }
        }
    }

    override fun getItemCount() = shopItemList.size

    inner class ShopItemsHolder(var sV: ShopItemBinding) : RecyclerView.ViewHolder(sV.root) {
        val txtPrice: TextView = sV.root.findViewById(R.id.shop_item_txtPrice)
    }

    interface ShopItemClickListener{
        fun onItemClick(shopItem: ShopItem)
    }

    fun setOnShopItemClickListener(listener: ShopItemClickListener){
        this.listener = listener
    }
}