package com.videoapp.testingvideoapp.data.repository.video

import android.graphics.Bitmap
import android.net.Uri
import com.videoapp.testingvideoapp.data.model.Video
import com.videoapp.testingvideoapp.infrasctructure.Result
import java.util.concurrent.CompletableFuture
import kotlin.script.dependencies.ScriptContents

interface SupportVideoDataRepository {

    fun saveFavVideosFromPref(videoIds: List<Int>)

    fun getFavVideosFromPref(): List<Int>

    fun getVideoThumbnail(videoUri: Uri): Bitmap?

    fun getTestVideos(): List<Video>

    fun saveWatchedVideosFromPref(videoIds: List<Int>)

    fun getWatchedVideosFromPref(): List<Int>

    fun savePositionLastVideo(position: Int)

    fun getPositionLastVideo() : Int

    fun resetWatchedVideos(): CompletableFuture<Result>
}