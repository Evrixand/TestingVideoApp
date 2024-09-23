package com.videoapp.testingvideoapp.ui.feed

import com.videoapp.testingvideoapp.data.model.LocationTag
import com.videoapp.testingvideoapp.data.model.Video

interface OnVideoClickListener {
    fun onViewLocation(locationTag: LocationTag?)
    fun onClickLike(videoId: Int, isFavorite: Boolean)
    fun onClickComments(video: Video)
}