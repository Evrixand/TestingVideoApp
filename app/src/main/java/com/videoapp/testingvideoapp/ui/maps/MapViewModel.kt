package com.videoapp.testingvideoapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.data.repository.video.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(private val videoRepository: VideoRepository) : ViewModel() {

    private var _videos = MutableLiveData<List<VideoComplete>>()
    val videos: LiveData<List<VideoComplete>> = _videos

    fun getAllVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            _videos.postValue(videoRepository.getWatchedVideos())
        }
    }

    fun getWatchedVideoById(videoId: Int?) : VideoComplete? {

        val list = videoRepository.getWatchedVideos()

        val findVideo = list.find { v -> v.video.videoId == videoId }

        return findVideo
    }
}