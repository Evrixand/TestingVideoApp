package com.videoapp.testingvideoapp.data.repository.user

import android.content.Context
import com.videoapp.testingvideoapp.FAV_VIDEOS_LIST_KEY
import com.videoapp.testingvideoapp.LAST_VIDEO_POSITION_KEY
import com.videoapp.testingvideoapp.SYS_PREF
import com.videoapp.testingvideoapp.USER_DISPLAY_NAME_KEY
import com.videoapp.testingvideoapp.USER_GOOGLE_ID_KEY
import com.videoapp.testingvideoapp.USER_IN_SYSTEM_KEY
import com.videoapp.testingvideoapp.USER_PHOTO_URL_KEY
import com.videoapp.testingvideoapp.WATCHED_VIDEOS_LIST_KEY
import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.model.User
import java.util.concurrent.CompletableFuture

class UserRepositoryImpl(private val context: Context) : UserRepository {

    override fun deleteUserData() : CompletableFuture<Result> {

        val result = CompletableFuture<Result>()

        try {

            val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
            pref.edit().remove(USER_GOOGLE_ID_KEY).apply()
            pref.edit().remove(USER_PHOTO_URL_KEY).apply()
            pref.edit().remove(USER_DISPLAY_NAME_KEY).apply()
            pref.edit().remove(FAV_VIDEOS_LIST_KEY).apply()
            pref.edit().remove(LAST_VIDEO_POSITION_KEY).apply()
            pref.edit().remove(WATCHED_VIDEOS_LIST_KEY).apply()
            pref.edit().remove(USER_IN_SYSTEM_KEY).apply()
            result.complete(Result.Success)

        }catch (e: Exception) {
            result.complete(Result.Failure(e.message))
        }
        return result
    }

    override fun saveUserData(googleId: String, displayName: String, photoUrl: String) : CompletableFuture<Result> {

        val result = CompletableFuture<Result>()
        try {

            val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)

            pref.edit().putString(USER_GOOGLE_ID_KEY, googleId).apply()
            pref.edit().putString(USER_PHOTO_URL_KEY, photoUrl).apply()
            pref.edit().putString(USER_DISPLAY_NAME_KEY, displayName).apply()
            pref.edit().putBoolean(USER_IN_SYSTEM_KEY, true).apply()
            result.complete(Result.Success)

        }catch (e: Exception) {
            result.complete(Result.Failure(e.message))
        }
        return result
    }

    override fun getFakeUsers(): List<User> {
        return listOf(

            User(userId = 1, username = "User First", googleId = "", email = ""),
            User(userId = 2, username = "User Second", googleId = "", email = ""),
            User(userId = 3, username = "User Third", googleId = "", email = ""),
        )
    }

    override fun isUserInSystem(): Boolean {
        val pref = context.getSharedPreferences(SYS_PREF, Context.MODE_PRIVATE)
        return pref.getBoolean(USER_IN_SYSTEM_KEY, false)
    }


}