package com.videoapp.testingvideoapp.data.model

data class Video (

    var videoId: Int,
    var title: String,
    var description: String,
    var timeAdded: Long,
    var filePath: String,
    val location: LocationTag?,
    var userId: Int
)