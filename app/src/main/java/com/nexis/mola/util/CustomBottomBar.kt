package com.nexis.mola.util

import android.widget.ImageView
import androidx.fragment.app.Fragment

class CustomBottomBar(val selectedList: Array<Int>, val unSelectedList: Array<Int>, val fragmentList: Array<Fragment>, val imageViewList: Array<ImageView>) {
    fun selectBar(bIn: Int) : Fragment {
        lateinit var selectedFragment: Fragment

        for (i in imageViewList.indices){
            if (i == bIn){
                imageViewList.get(i).setImageResource(selectedList.get(i))
                selectedFragment = fragmentList.get(i)
            } else
                imageViewList.get(i).setImageResource(unSelectedList.get(i))
        }

        return selectedFragment
    }
}