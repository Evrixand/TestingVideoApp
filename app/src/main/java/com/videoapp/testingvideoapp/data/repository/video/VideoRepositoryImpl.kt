package com.videoapp.testingvideoapp.data.repository.video

import android.net.Uri
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.data.repository.user.UserRepository
import java.util.concurrent.CompletableFuture

class VideoRepositoryImpl(
    private val supportVideoDataRepository: SupportVideoDataRepository,
    private val userRepository: UserRepository
) : VideoRepository {

    override fun getAllVideos(): List<VideoComplete> {

        val testVideos = supportVideoDataRepository.getTestVideos()

        val result = mutableListOf<VideoComplete>()

        val fakeUsers = userRepository.getFakeUsers()

        val favList = supportVideoDataRepository.getFavVideosFromPref()

        if (testVideos.isNotEmpty()) {

            testVideos.forEach { video ->

                val videoUri = Uri.parse(video.filePath)

                val isFav = favList.contains(video.videoId )

                val user = fakeUsers.find { i -> i.userId == video.userId }

                if (user != null) {
                    result.add(
                        VideoComplete(
                            video = video,
                            user = user,
                            comments = emptyList(),
                            thumbnail = supportVideoDataRepository.getVideoThumbnail(videoUri = videoUri),
                            isFavourite = isFav
                        )
                    )
                }
            }
        }

        return result
    }

    override fun getFavoriteVideos(): List<VideoComplete> =
        getAllVideos().filter { v -> v.isFavourite }

    override fun getWatchedVideos(): List<VideoComplete> {

        val allVideos = getAllVideos()

        val watchedVideos = supportVideoDataRepository.getWatchedVideosFromPref()

        return allVideos.filter { it.video.videoId in watchedVideos }
    }

    override fun addVideoToFav(videoId: Int): CompletableFuture<Result> {

        val future = CompletableFuture<Result>()

        try {

            val curList = supportVideoDataRepository.getFavVideosFromPref()

            val newList = mutableListOf<Int>()

            newList.addAll(curList)

            val findVideo = newList.find { i -> i == videoId }

            if (findVideo == null) {

                newList.add(videoId)

                supportVideoDataRepository.saveFavVideosFromPref(videoIds = newList)
            }

            future.complete(Result.Success)

        } catch (e: Exception) {

            future.complete(Result.Failure(e.message))
        }

        return future
    }

    override fun removeVideoFromFav(videoId: Int): CompletableFuture<Result> {

        val future = CompletableFuture<Result>()

        try {

            val curList = supportVideoDataRepository.getFavVideosFromPref()

            val newList = mutableListOf<Int>()

            newList.addAll(curList)

            val index = newList.indexOf(videoId)

            if (index != -1) {

                newList.removeAt(index)

                supportVideoDataRepository.saveFavVideosFromPref(videoIds = newList)

                future.complete(Result.Success)
            }

        } catch (e: Exception) {

            future.complete(Result.Failure(e.message))
        }

        return future
    }

    override fun addVideoToWatched(videoId: Int): CompletableFuture<Result> {
        val future = CompletableFuture<Result>()

        try {

            val curList = supportVideoDataRepository.getWatchedVideosFromPref()

            val newList = mutableListOf<Int>()

            newList.addAll(curList)

            val findVideo = newList.find { i -> i == videoId }

            if (findVideo == null) {

                newList.add(videoId)

                supportVideoDataRepository.saveWatchedVideosFromPref(videoIds = newList)
            }

            future.complete(Result.Success)

        } catch (e: Exception) {

            future.complete(Result.Failure(e.message))
        }

        return future
    }

    override fun savePositionLastVideo(position: Int) =
        supportVideoDataRepository.savePositionLastVideo(position = position)

    override fun getPositionLastVideo(): Int =
        supportVideoDataRepository.getPositionLastVideo()

    override fun resetWatchedVideos(): CompletableFuture<Result> =
        supportVideoDataRepository.resetWatchedVideos()
}