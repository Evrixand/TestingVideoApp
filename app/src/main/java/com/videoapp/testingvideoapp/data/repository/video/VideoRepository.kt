package com.videoapp.testingvideoapp.data.repository.video

import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.model.VideoComplete
import java.util.concurrent.CompletableFuture

interface VideoRepository {

    fun getAllVideos() : List<VideoComplete>

    fun getFavoriteVideos() : List<VideoComplete>

    fun addVideoToFav(videoId: Int) : CompletableFuture<Result>

    fun removeVideoFromFav(videoId: Int) : CompletableFuture<Result>

    fun addVideoToWatched(videoId: Int) : CompletableFuture<Result>

    fun getWatchedVideos() : List<VideoComplete>

    fun savePositionLastVideo(position: Int)

    fun getPositionLastVideo() : Int

    fun resetWatchedVideos() : CompletableFuture<Result>
}