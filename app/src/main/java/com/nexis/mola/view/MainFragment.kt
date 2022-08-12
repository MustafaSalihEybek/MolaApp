package com.nexis.mola.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.nexis.mola.R
import com.nexis.mola.databinding.FragmentMainBinding
import com.nexis.mola.util.CustomBottomBar

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var customBottomBar: CustomBottomBar

    private lateinit var fTransaction: FragmentTransaction

    private fun init(){
        customBottomBar = CustomBottomBar(
            arrayOf(R.drawable.profile_selected, R.drawable.call_selected, R.drawable.shop_selected),
            arrayOf(R.drawable.profile_unselected, R.drawable.call_unselected, R.drawable.shop_unselected),
            arrayOf(ProfileFragment(), CallFragment(), ShopFragment()),
            arrayOf(mainBinding.mainFragmentImgProfile, mainBinding.mainFragmentImgCall, mainBinding.mainFragmentImgShop)
        )

        setFragment(customBottomBar.selectBar(0))

        mainBinding.mainFragmentImgProfile.setOnClickListener(this)
        mainBinding.mainFragmentImgCall.setOnClickListener(this)
        mainBinding.mainFragmentImgShop.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainBinding = FragmentMainBinding.inflate(inflater)
        return mainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.main_fragment_imgProfile -> setFragment(customBottomBar.selectBar(0))
                R.id.main_fragment_imgCall -> setFragment(customBottomBar.selectBar(1))
                R.id.main_fragment_imgShop -> setFragment(customBottomBar.selectBar(2))
            }
        }
    }

    private fun setFragment(fragment: Fragment){
        fTransaction = childFragmentManager.beginTransaction()
        fTransaction.replace(R.id.main_fragment_frameLayout, fragment)
        fTransaction.commit()
    }
}