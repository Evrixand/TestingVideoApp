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

    override fun getAllVideos(): List<VideoComplete>  {

        val videos = supportVideoDataRepository.getTestVideos()

        val list = mutableListOf<VideoComplete>()
        val fakeUsers = userRepository.getFakeUsers()
        val favoriteVideos = getFavoriteVideos()

        if (videos.isNotEmpty()){

            videos.forEach {

                val findVideo = favoriteVideos.find { i -> i.video.videoId == it.videoId }

                val isFav = findVideo != null

                val user = fakeUsers.find { i -> i.userId == it.userId }

                if (user != null) {
                    list.add(VideoComplete(video = it, user = user, comments = emptyList(), thumbnail = null, isFavourite = isFav))
                }
            }
        }

        return list
    }

    override fun getFavoriteVideos(): List<VideoComplete> {

        val list = supportVideoDataRepository.getTestVideos()

        val favList = supportVideoDataRepository.getFavVideosFromPref()

        val users = userRepository.getFakeUsers()

        val favoriteVideos = mutableListOf<VideoComplete>()

        favList.forEach { vId ->

            val findVideo = list.find { v -> v.videoId == vId }

            if (findVideo != null) {
                val videoUri = Uri.parse(findVideo.filePath)

                val findUser = users.find { i -> i.userId == findVideo.userId }

                favoriteVideos.add(
                    VideoComplete(
                        video = findVideo,
                        thumbnail = supportVideoDataRepository.getVideoThumbnail(videoUri =  videoUri),
                        user = findUser!!,
                        comments = emptyList(),
                        isFavourite = true
                    )
                )
            }
        }
        return favoriteVideos
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

            newList.remove(videoId)

            supportVideoDataRepository.saveFavVideosFromPref(videoIds = newList)

            future.complete(Result.Success)

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

    override fun getWatchedVideos(): List<VideoComplete> {

        val list = supportVideoDataRepository.getTestVideos()

        val watchedVideos = supportVideoDataRepository.getWatchedVideosFromPref()

        val users = userRepository.getFakeUsers()

        val favoriteVideos = mutableListOf<VideoComplete>()

        watchedVideos.forEach { vId ->

            val findVideo = list.find { v -> v.videoId == vId }

            if (findVideo != null) {
                val videoUri = Uri.parse(findVideo.filePath)

                val findUser = users.find { i -> i.userId == findVideo.userId }

                favoriteVideos.add(
                    VideoComplete(
                        video = findVideo,
                        thumbnail = supportVideoDataRepository.getVideoThumbnail(videoUri =  videoUri),
                        user = findUser!!,
                        comments = emptyList(),
                        isFavourite = true
                    )
                )
            }
        }
        return favoriteVideos
    }

    override fun savePositionLastVideo(position: Int) {
        supportVideoDataRepository.savePositionLastVideo(position = position)
    }
    override fun getPositionLastVideo(): Int {
        return supportVideoDataRepository.getPositionLastVideo()
    }

    override fun resetWatchedVideos(): CompletableFuture<Result> {
        return supportVideoDataRepository.resetWatchedVideos()
    }

}