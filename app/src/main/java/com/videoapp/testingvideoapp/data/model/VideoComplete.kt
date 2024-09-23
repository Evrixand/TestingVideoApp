package com.videoapp.testingvideoapp.data.model

import android.graphics.Bitmap

data class VideoComplete(
    val video: Video,
    val user: User,
    val comments: List<String>,
    val thumbnail: Bitmap?,
    val isFavourite: Boolean
)
