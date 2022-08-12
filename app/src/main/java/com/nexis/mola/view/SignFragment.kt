package com.nexis.mola.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nexis.mola.R
import com.nexis.mola.adapter.SignFragmentAdapter
import com.nexis.mola.databinding.FragmentSignBinding
import com.nexis.mola.util.Singleton

class SignFragment : Fragment() {
    private lateinit var v: View
    private lateinit var signBinding: FragmentSignBinding

    private lateinit var signAdapter: SignFragmentAdapter

    private fun init(){
        signAdapter = SignFragmentAdapter(this)
        signAdapter.addFragment(SignInFragment())
        signAdapter.addFragment(SignUpFragment())

        signBinding.signFragmentViewPager.adapter = signAdapter
        Singleton.mViewPager = signBinding.signFragmentViewPager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signBinding = FragmentSignBinding.inflate(inflater)
        return signBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }
}