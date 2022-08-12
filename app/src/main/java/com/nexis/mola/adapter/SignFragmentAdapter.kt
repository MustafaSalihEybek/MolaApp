package com.nexis.mola.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SignFragmentAdapter(mFragment: Fragment) : FragmentStateAdapter(mFragment) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList.get(position)

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
    }
}