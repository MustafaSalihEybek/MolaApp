package com.nexis.mola.util

import androidx.viewpager2.widget.ViewPager2

class Singleton {
    companion object{
        val V_SIZE: Int = 25
        var mViewPager: ViewPager2? = null

        fun setPage(pIn: Int){
            mViewPager?.let {
                it.currentItem = pIn
            }
        }
    }
}