package com.videoapp.testingvideoapp.infrasctructure

sealed class Result {
    data object Success : Result()
    data class Failure(val exception: String?) : Result()
}