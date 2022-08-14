package com.nexis.mola.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nexis.mola.R
import com.nexis.mola.databinding.FragmentProfileBinding
import com.nexis.mola.viewmodel.ProfileViewModel

class ProfileFragment(val userId: String) : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var profileBinding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    private fun init(){
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        observeLiveData()
        profileViewModel.getUserData(userId)

        profileBinding.profileFragmentTxtEdit.setOnClickListener(this)
        profileBinding.profileFragmentTxtLogout.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(inflater)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        profileViewModel.userData.observe(viewLifecycleOwner, Observer {
            it?.let {
                profileBinding.user = it
            }
        })

        profileViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.profile_fragment_txtEdit -> println("Edit")
                R.id.profile_fragment_txtLogout -> println("Logout")
            }
        }
    }
}