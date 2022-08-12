package com.nexis.mola.util

import androidx.viewpager2.widget.ViewPager2

class Singleton {
    companion object{
        var mViewPager: ViewPager2? = null

        fun setPage(pIn: Int){
            mViewPager?.let {
                it.currentItem = pIn
            }
        }
    }
}