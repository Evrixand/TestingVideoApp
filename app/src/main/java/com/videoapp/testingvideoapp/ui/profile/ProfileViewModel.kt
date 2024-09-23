package com.videoapp.testingvideoapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.data.repository.login.LoginRepository
import com.videoapp.testingvideoapp.data.repository.video.VideoRepository

class ProfileViewModel(
    private val loginRepository: LoginRepository,
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _signOutResult = MutableLiveData<Result>()
    val signOutResult: LiveData<Result> = _signOutResult

    private val _favVideos = MutableLiveData<List<VideoComplete>>()
    val favVideos: LiveData<List<VideoComplete>> = _favVideos

    fun signOut() {
        _signOutResult.value = loginRepository.signOut().get()
    }
    fun getFavVideos() {
        _favVideos.value = videoRepository.getFavoriteVideos()
    }
}