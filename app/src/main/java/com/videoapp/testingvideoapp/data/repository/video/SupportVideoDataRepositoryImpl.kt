package com.videoapp.testingvideoapp.data.repository.video

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.videoapp.testingvideoapp.FAV_VIDEOS_LIST_KEY
import com.videoapp.testingvideoapp.LAST_VIDEO_POSITION_KEY
import com.videoapp.testingvideoapp.SYS_PREF
import com.videoapp.testingvideoapp.WATCHED_VIDEOS_LIST_KEY
import com.videoapp.testingvideoapp.data.model.LocationTag
import com.videoapp.testingvideoapp.data.model.Video
import com.videoapp.testingvideoapp.infrasctructure.Result
import java.util.concurrent.CompletableFuture

class SupportVideoDataRepositoryImpl(private val context: Context) : SupportVideoDataRepository {

    override fun saveFavVideosFromPref(videoIds: List<Int>) {
        val gson = Gson()
        val jsonString = gson.toJson(videoIds)

        val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
        pref.edit().putString(FAV_VIDEOS_LIST_KEY, jsonString).apply()
    }

    override fun getFavVideosFromPref(): List<Int> {

        try {
            val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
            val jsonString = pref.getString(FAV_VIDEOS_LIST_KEY, null)

            val gson = Gson()
            val type = object : TypeToken<List<Int>>() {}.type

            return gson.fromJson(jsonString, type)

        } catch(e: Exception) { return emptyList() }
    }

    override fun getVideoThumbnail(videoUri: Uri): Bitmap? {

        val retriever = MediaMetadataRetriever()

        val assetFileDescriptor = context.contentResolver.openAssetFileDescriptor(videoUri, "r")

        return try {

            if (assetFileDescriptor != null) {
                retriever.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                retriever.getFrameAtTime(100000, MediaMetadataRetriever.OPTION_CLOSEST)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            assetFileDescriptor?.close()
            retriever.release()
        }
    }

    override fun getTestVideos(): List<Video> {
        return listOf(
            Video(
                videoId = 1,
                title = "Test_Video_1",
                description = "Description 1",
                timeAdded = 1704067200000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_1",
                location = LocationTag(50.4501, 30.5234),
                userId = 1
            ),
            Video(
                videoId = 2,
                title = "Test_Video_2",
                description = "Description 2",
                timeAdded = 1707961800000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_2",
                location = LocationTag(49.8397, 24.0297),
                userId = 3
            ),
            Video(
                videoId = 3,
                title = "Test_Video_3",
                description = "Description 3",
                timeAdded = 1710236700000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_3",
                location = LocationTag(46.4825, 30.7233),
                userId = 2
            ),
            Video(
                videoId = 4,
                title = "Test_Video_4",
                description = "Description 4",
                timeAdded = 1713605700000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_4",
                location = LocationTag(49.9935, 36.2304),
                userId = 1
            ),
            Video(
                videoId = 5,
                title = "Test_Video_5",
                description = "Description 5",
                timeAdded = 1714884000000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_5",
                location = LocationTag(48.4647, 35.0462),
                userId = 3
            ),
            Video(
                videoId = 6,
                title = "Test_Video_6",
                description = "Description 6",
                timeAdded = 1718071800000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_6",
                location = LocationTag(47.8388, 35.1396),
                userId = 2
            ),
            Video(
                videoId = 7,
                title = "Test_Video_7",
                description = "Description 7",
                timeAdded = 1720519200000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_7",
                location = LocationTag(47.9115, 33.3942),
                userId = 1
            ),
            Video(
                videoId = 8,
                title = "Test_Video_8",
                description = "Description 8",
                timeAdded = 1724075700000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_8",
                location = LocationTag(46.9750, 31.9980),
                userId = 3
            ),
            Video(
                videoId = 9,
                title = "Test_Video_9",
                description = "Description 9",
                timeAdded = 1727973600000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_9",
                location = LocationTag(49.4420, 32.0594),
                userId = 2
            ),
            Video(
                videoId = 10,
                title = "Test_Video_10",
                description = "Description 10",
                timeAdded = 1735730340000L,
                filePath = "android.resource://com.videoapp.testingvideoapp/raw/testvideo_10",
                location = LocationTag(49.5883, 34.5515),
                userId = 1
            )
        )
    }

    override fun resetWatchedVideos(): CompletableFuture<Result> {
        val result = CompletableFuture<Result>()

        try {
            val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
            pref.edit().remove(WATCHED_VIDEOS_LIST_KEY).apply()
            result.complete(Result.Success)

        }catch (e: Exception) {
            result.complete(Result.Failure(e.message))
        }
        return result
    }

    override fun saveWatchedVideosFromPref(videoIds: List<Int>) {
        val gson = Gson()
        val jsonString = gson.toJson(videoIds)

        val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
        pref.edit().putString(WATCHED_VIDEOS_LIST_KEY, jsonString).apply()
    }

    override fun getWatchedVideosFromPref(): List<Int> {
        try {
            val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
            val jsonString = pref.getString(WATCHED_VIDEOS_LIST_KEY, null)

            val gson = Gson()
            val type = object : TypeToken<List<Int>>() {}.type

            return gson.fromJson(jsonString, type)

        } catch(e: Exception) { return emptyList() }
    }

    override fun savePositionLastVideo(position: Int) {
        val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
        pref.edit().putInt(LAST_VIDEO_POSITION_KEY, position).apply()
    }

    override fun getPositionLastVideo() : Int {
        val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
        return pref.getInt(LAST_VIDEO_POSITION_KEY, 0)
    }
}