package com.videoapp.testingvideoapp.ui.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.videoapp.testingvideoapp.data.model.VideoComplete
import com.videoapp.testingvideoapp.databinding.FavVideoItemBinding

class FavVideoAdapter(private val listener: OnClickFavVideoListener) : RecyclerView.Adapter<FavVideoAdapter.FavVideoViewHolder>() {

    private var videos = emptyList<VideoComplete>()

    class FavVideoViewHolder(private val binding: FavVideoItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(video: VideoComplete, listener: OnClickFavVideoListener, position: Int) = with(binding){

            videoThumbnail.setImageBitmap(video.thumbnail)

            itemView.setOnClickListener {

                listener.onClickFavVideo(position)
            }
        }
        companion object {
            fun create(parent: ViewGroup): FavVideoViewHolder {
                return FavVideoViewHolder(FavVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavVideoViewHolder {
        return FavVideoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FavVideoViewHolder, position: Int) {
        holder.bind(videos[position], listener = listener, position = position)
    }

    override fun getItemCount(): Int = videos.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<VideoComplete>) {
        videos = list
        notifyDataSetChanged()
    }
}