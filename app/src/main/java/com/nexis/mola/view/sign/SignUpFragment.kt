package com.nexis.mola.view.sign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nexis.mola.R
import com.nexis.mola.databinding.FragmentSignUpBinding
import com.nexis.mola.model.User
import com.nexis.mola.util.AppUtil
import com.nexis.mola.util.Singleton
import com.nexis.mola.view.show
import com.nexis.mola.viewmodel.SignUpViewModel

class SignUpFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var signUpBinding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var genderAdapter: ArrayAdapter<CharSequence>
    private lateinit var ageAdapter: ArrayAdapter<*>
    private lateinit var ageList: ArrayList<Int>

    private lateinit var userFullName: String
    private lateinit var userNick: String
    private lateinit var userPassword: String
    private lateinit var userEmail: String
    private lateinit var userId: String
    private lateinit var selectedGender: String
    private var selectedAge: Int = -1

    private fun init(){
        ageList = ArrayList()

        for (age in 18..70)
            ageList.add(age)

        ageAdapter = ArrayAdapter(v.context, R.layout.spinner_item, ageList)
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        signUpBinding.signUpFragmentSpinnerAge.adapter = ageAdapter

        signUpBinding.signUpFragmentSpinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedAge = it.getItemAtPosition(p2).toString().toInt()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedAge = it.getItemAtPosition(0).toString().toInt()
                }
            }
        }

        genderAdapter = ArrayAdapter.createFromResource(v.context, R.array.GenderList, R.layout.spinner_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        signUpBinding.signUpFragmentSpinnerGender.adapter = genderAdapter

        signUpBinding.signUpFragmentSpinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.let {
                    selectedGender = it.getItemAtPosition(p2).toString()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                p0?.let {
                    selectedGender = it.getItemAtPosition(0).toString()
                }
            }
        }

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        observeLiveData()

        signUpBinding.signUpFragmentBtnSignUp.setOnClickListener(this)
        signUpBinding.signUpFragmentBtnSignIn.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signUpBinding = FragmentSignUpBinding.inflate(inflater)
        return signUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        signUpViewModel.userId.observe(viewLifecycleOwner, Observer {
            it?.let {
                userId = it

                AppUtil.userData = User(
                    userId,
                    userEmail,
                    userFullName,
                    userNick,
                    selectedGender,
                    selectedAge,
                    ""
                )

                signUpViewModel.saveUserData(AppUtil.userData)
            }
        })

        signUpViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        signUpViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)

                clearAllEdit()
                backToPage()
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.sign_up_fragment_btnSignUp -> signUpUser()
                R.id.sign_up_fragment_btnSignIn -> backToPage()
            }
        }
    }

    private fun signUpUser(){
        userFullName = signUpBinding.signUpFragmentEditFullName.text.toString().trim()
        userNick = signUpBinding.signUpFragmentEditUserName.text.toString().trim()
        userPassword = signUpBinding.signUpFragmentEditPassword.text.toString().trim()
        userEmail = signUpBinding.signUpFragmentEditEmail.text.toString().trim()

        if (!userFullName.isEmpty()){
            if (!userNick.isEmpty()){
                if (!userPassword.isEmpty()){
                    if (!userEmail.isEmpty()){
                        if (!selectedGender.isEmpty()){
                            if (selectedAge != -1)
                                signUpViewModel.signUpUser(userEmail, userPassword)
                            else
                                "message".show(v, "Lütfen listeden yaşınızı seçiniz")
                        } else
                            selectedGender.show(v, "Lütfen listeden cinsiyetinizi seçiniz")
                    } else
                        userEmail.show(v, "Lütfen geçerli bir email adresi giriniz")
                } else
                    userPassword.show(v, "Lütfen geçerli bir şifre belirleyiniz")
            } else
                userNick.show(v, "Lütfen geçerli bir kullanıcı adı belirleyiniz")
        } else
            userFullName.show(v, "Lütfen adınızı ve soyadınızı giriniz")
    }

    private fun backToPage(){
        Singleton.setPage(0)
    }

    private fun clearAllEdit(){
        signUpBinding.signUpFragmentEditFullName.setText("")
        signUpBinding.signUpFragmentEditUserName.setText("")
        signUpBinding.signUpFragmentEditPassword.setText("")
        signUpBinding.signUpFragmentEditEmail.setText("")

        selectedAge = -1
    }
}