package com.videoapp.testingvideoapp.data.repository.user

import com.videoapp.testingvideoapp.infrasctructure.Result
import com.videoapp.testingvideoapp.data.model.User
import java.util.concurrent.CompletableFuture

interface UserRepository {

    fun deleteUserData() : CompletableFuture<Result>

    fun saveUserData(googleId: String, displayName: String, photoUrl: String) : CompletableFuture<Result>

    fun getFakeUsers(): List<User>

    fun isUserInSystem() : Boolean


}