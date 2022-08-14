package com.nexis.mola.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.nexis.mola.R

fun String.show(v: View, msg: String){
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
}

fun ImageView.downloadImageUrl(imageUrl: String?){
    val options = RequestOptions()
        .placeholder(placeHolderProgress(context))
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(imageUrl)
        .into(this)
}

fun placeHolderProgress(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

@BindingAdapter("android:downloadImg")
fun downloadImage(view: ImageView, url: String?){
    view.downloadImageUrl(url)
}

@BindingAdapter("android:setGender")
fun setGender(view: ImageView, gender: String?){
    gender?.let {
        if (it.equals("Erkek"))
            view.setBackgroundResource(R.drawable.male)
        else
            view.setBackgroundResource(R.drawable.female)
    }
}

@BindingAdapter("android:setShopTitle")
fun setShopTitle(view: TextView, minutes: Int){
    view.text = "$minutes Dakika"
}

@BindingAdapter("android:setShopSubTitle")
fun setShopSubTitle(view: TextView, minutes: Int){
    view.text = "$minutes dakika satın alarak konuşma süresini artır"
}