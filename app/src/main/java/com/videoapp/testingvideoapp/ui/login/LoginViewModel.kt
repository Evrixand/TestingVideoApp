package com.videoapp.testingvideoapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.repository.login.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _signInResult = MutableLiveData<Result>()
    val signInResult: LiveData<Result> = _signInResult

    fun signInGoogle() {
        _signInResult.value = loginRepository.signInWithGoogle().get()
    }
}