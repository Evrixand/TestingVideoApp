package com.videoapp.testingvideoapp.data.repository.login

import com.videoapp.testingvideoapp.infrasctructure.Result
import java.util.concurrent.CompletableFuture

interface LoginRepository {

    fun signInWithGoogle() : CompletableFuture<Result>

    fun signOut() : CompletableFuture<Result>
}