package com.videoapp.testingvideoapp.ui.feed

import androidx.recyclerview.widget.DiffUtil
import com.videoapp.testingvideoapp.data.model.VideoComplete

class VideoDiffCallback(
    private val oldList: List<VideoComplete>,
    private val newList: List<VideoComplete>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].video == newList[newItemPosition].video
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        if (oldList[oldItemPosition].isFavourite != newList[newItemPosition].isFavourite) return newList[newItemPosition].isFavourite

        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}