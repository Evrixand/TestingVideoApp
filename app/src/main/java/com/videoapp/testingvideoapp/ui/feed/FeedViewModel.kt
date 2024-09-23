package com.videoapp.testingvideoapp.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.data.repository.user.UserRepository
import com.videoapp.testingvideoapp.data.repository.video.VideoRepository

class FeedViewModel(
    private val videoRepository: VideoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _videos = MutableLiveData<List<VideoComplete>>()
    val videos: LiveData<List<VideoComplete>> = _videos

    private var _favVideos = MutableLiveData<List<VideoComplete>>()
    val favVideos: LiveData<List<VideoComplete>> = _favVideos

    fun getAllVideos() {
        _videos.value = videoRepository.getAllVideos()
    }
    fun getFavoritesVideos() {
        _favVideos.value = videoRepository.getFavoriteVideos()
    }

    fun getWatchedVideosById(videoId: Int) {

        val list = videoRepository.getWatchedVideos()

        val findVideo = list.find { v -> v.video.videoId == videoId }

        if (findVideo != null) {
            _videos.value = listOf(findVideo)
        } else { _videos.value = emptyList() }
    }

    fun addVideToFavorites(videoId: Int) = videoRepository.addVideoToFav(videoId = videoId)

    fun resetWatchedVideos() = videoRepository.resetWatchedVideos()

    fun removeVideFromFavorites(videoId: Int) = videoRepository.removeVideoFromFav(videoId = videoId)

    fun addVideoToWatched(videoId: Int) = videoRepository.addVideoToWatched(videoId = videoId)

    fun isUserInSystem() :Boolean = userRepository.isUserInSystem()

    fun saveVideoPos(position: Int) = videoRepository.savePositionLastVideo(position = position)

    fun getVideoPos() : Int = videoRepository.getPositionLastVideo()
}